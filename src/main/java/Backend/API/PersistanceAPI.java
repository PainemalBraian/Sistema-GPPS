package Backend.API;

import Backend.DAO.*;
import Backend.DTO.*;
import Backend.Entidades.*;
import Backend.Exceptions.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class PersistanceAPI implements API {
    Usuario userSession;
    UsuarioDAODB UsuarioDAODB = new UsuarioDAODB();
    RolDAODB RolDAODB = new RolDAODB();


    ResourceBundle labels = ResourceBundle.getBundle("Backend.labels", Locale.getDefault());


    @Override
    public ResourceBundle obtenerIdioma() {
        return labels;
    }

    @Override
    public void cambiarIdioma(String idioma) {
        labels = ResourceBundle.getBundle("Backend.labels", new Locale(idioma));
    }


    @Override
    public void activarUsuario(String username) throws UserExceptions, UpdateException {
        try {
            Usuario user = UsuarioDAODB.findByUsername(username);
            user.activar();
            UsuarioDAODB.update(user);
        } catch (UserExceptions e) {
            throw new UserExceptions(e.getMessage());
        }catch(Exception e){
            throw new UserExceptions(e.getMessage());
        }
    }

    @Override
    public void cerrarSesion() {
        userSession = null;
    }

    @Override
    public void activarRol(int idRol) throws ReadException {
        Rol rol = RolDAODB.findOne(idRol);
        rol.activar();
    }

    @Override
    public void desactivarRol(int id) throws ReadException, UpdateException {

        Rol rol = RolDAODB.findOne(id);
        rol.desactivar();
        RolDAODB.update(rol);


    }

    @Override
    public void desactivarUsuario(String username) throws UserExceptions, UpdateException{
        Usuario user = UsuarioDAODB.findByUsername(username);

        Usuario userDB = new Usuario(user.getId(), user.getUsername(), user.getContrasena(),
                user.getNombre(), user.getEmail(), user.getRol());

        userDB.desactivar();
        UsuarioDAODB.update(userDB);
    }

    @Override
    public void eliminarUsuario(int id) throws UserExceptions, DeleteException {
        UsuarioDAODB.delete(id);
    }

    @Override
    public void guardarRol(int id, String nombre, boolean estado) throws UpdateException {
        Rol rol = new Rol(id, nombre);
        rol.setActivo(estado);
        RolDAODB.update(rol);
    }

    @Override
    public UsuarioDTO login(String username, String password) throws LoginException {
        if (username == null || username.trim().isEmpty()) {
            throw new LoginException("Ingrese un usuario");
        }

        if (password == null || password.trim().isEmpty()) {
            throw new LoginException("Ingrese una contraseña");
        }

        try {
            Usuario user = UsuarioDAODB.findByUsername(username);

            if (user == null || !user.getContrasena().equals(password)) {
                throw new LoginException("Usuario o contraseña inválidos");
            }

            if (!user.isActivo()) {
                throw new LoginException("El usuario está inactivo");
            }

            RolDTO rol = convertirARolDTO(user.getRol());
            userSession = user;

            return new UsuarioDTO(
                    user.getId(),
                    user.getUsername(),
                    user.getContrasena(),
                    user.getNombre(),
                    user.getEmail(),
                    rol,
                    user.isActivo()
            );

        } catch (UserExceptions e) {
            throw new LoginException(e.getMessage());
        }
    }

    private RolDTO convertirARolDTO(Rol rol) {
        if (rol == null) return null;
        return new RolDTO(rol.getId(), rol.getNombre(), rol.isActivo());
    }

    @Override
    public List<RolDTO> obtenerRoles() throws Exception {
        List<RolDTO> rolDTOs = new ArrayList<>();
        try {
            List<Rol> roles = RolDAODB.read();
            for (Rol rol : roles) {
                rolDTOs.add(new RolDTO(rol.getId(), rol.getNombre(), rol.isActivo()));
            }
            return rolDTOs;
        } catch (Exception e) {
            //e.getStackTrace();
            throw new Exception("Error al obtener los roles");
        }
    }


    @Override
    public List<RolDTO> obtenerRolesActivos() throws ReadException {
        List<RolDTO> rolDTOs = new ArrayList<>();
        try {
            List<Rol> roles = RolDAODB.read();
            for (Rol rol : roles) {
                if (rol.isActivo()) {
                    rolDTOs.add(new RolDTO(rol.getId(), rol.getNombre(), rol.isActivo()));
                }

            }
            return rolDTOs;
        } catch(Exception e){
            throw new ReadException(e.getMessage());
        }
    }

    @Override
    public UsuarioDTO obtenerSesionDeUsuario() {
        RolDTO rol = convertirARolDTO(userSession.getRol());
        UsuarioDTO user = new UsuarioDTO(userSession.getId(), userSession.getUsername(), userSession.getContrasena(), userSession.getNombre(),
                userSession.getEmail(), rol, userSession.isActivo());
        return user;
    }

    @Override
    public UsuarioDTO obtenerUsuario(String username) throws UserExceptions {
        UsuarioDTO usuarioDTO=null;
        Usuario usuario = UsuarioDAODB.findByUsername(username);
        RolDTO rol = convertirARolDTO(usuario.getRol());
        usuarioDTO = new UsuarioDTO(usuario.getId(), usuario.getUsername(),
                usuario.getContrasena(), usuario.getNombre(),
                usuario.getEmail(), rol, usuario.isActivo());
        return usuarioDTO;
    }

    @Override
    public UsuarioDTO obtenerUsuarioByEmail(String email) throws UserExceptions {
        return null;
    }

    @Override
    public List<UsuarioDTO> obtenerUsuarios() throws UserExceptions {
        try {
            List<UsuarioDTO> usuarioDTOs = new ArrayList<>();
            List<Usuario> usuarios = UsuarioDAODB.read();

            for (Usuario usuario : usuarios) {
                RolDTO rol = convertirARolDTO(usuario.getRol());
                usuarioDTOs.add(new UsuarioDTO(usuario.getId(), usuario.getUsername(), usuario.getContrasena(),
                        usuario.getNombre(), usuario.getEmail(), rol, usuario.isActivo()));
            }
            return usuarioDTOs;
        } catch (Exception e) {
            throw new UserExceptions("Error al obtener los usuarios");
        }
    }

    @Override
    public void registrarUsuario(
            String username,
            String password,
            String email,
            String nombre,
            int rolId,
            String matricula,
            String carrera,
            String legajo,
            String nombreEntidad,
            String cuit,
            String direccionEntidad
    ) throws RegisterExceptions, UserExceptions, Exception {

        // Validar que el username y email no estén en uso
        UsuarioDAODB.validarUsernameYEmailUnicos(username, email);

        // Obtener Rol desde ID
        RolDTO rolDTO = obtenerRolPorId(rolId);
        Rol rol = new Rol(rolDTO.getId(), rolDTO.getNombre());
        rol.setActivo(rolDTO.isActivo());

        // Crear el objeto Usuario
        Usuario usuario = new Usuario(username, password, nombre, email, rol);

        // Asignar atributos adicionales si aplican
        usuario.setMatricula(matricula);
        usuario.setCarrera(carrera);
        usuario.setLegajo(legajo);
        usuario.setNombreEntidad(nombreEntidad);
        usuario.setCuit(cuit);
        usuario.setDireccionEntidad(direccionEntidad);
        usuario.setActivo(true);

        // Guardar en la base de datos
        UsuarioDAODB.create(usuario);
    }


    @Override
    public RolDTO obtenerRolPorId(int id) throws ReadException{

        Rol rol = RolDAODB.findOne(id);

        return new RolDTO(id, rol.getNombre(), rol.isActivo());
    }

    @Override
    public RolDTO obtenerRolUsuarioId(int id) throws Exception{
        try {
            Usuario usuario = UsuarioDAODB.findById(id);
            Rol rolUsuario = usuario.getRol();
            return new RolDTO(rolUsuario.getId(), rolUsuario.getNombre(), rolUsuario.isActivo());
        } catch (Exception e) {
            throw new Exception("Error al obtener el rol del usuario");
        }
    }

}
