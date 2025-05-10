package Backend.API;

import Backend.DAO.*;
import Backend.DAO.dom.usuarios.DocenteDAODB;
import Backend.DAO.dom.usuarios.EntidadColaborativaDAODB;
import Backend.DAO.dom.usuarios.EstudianteDAODB;
import Backend.DAO.dom.usuarios.TutorExternoDAODB;
import Backend.DTO.*;
import Backend.Entidades.*;
import Backend.Exceptions.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class PersistanceAPI implements API {
    Usuario userSession = new Usuario();
    UsuarioDAODB UsuarioDAODB = new UsuarioDAODB();
    RolDAODB RolDAODB = new RolDAODB();
    EstudianteDAODB EstudianteDAODB= new EstudianteDAODB();
    DocenteDAODB DocenteDAODB = new DocenteDAODB();
    TutorExternoDAODB TutorExternoDAODB = new TutorExternoDAODB();
    EntidadColaborativaDAODB EntidadColaborativaDAODB = new EntidadColaborativaDAODB();

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
    public void activarUsuario(String username) throws UserException, UpdateException {
        try {
            Usuario user = UsuarioDAODB.findByUsername(username);
            user.activar();
            UsuarioDAODB.update(user);
        } catch (UserException e) {
            throw new UserException(e.getMessage());
        }catch(Exception e){
            throw new UserException(e.getMessage());
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
    public void desactivarUsuario(String username) throws UserException, UpdateException{
        Usuario user = UsuarioDAODB.findByUsername(username);
        Usuario userDB = new Usuario(user.getIdUsuario(), user.getUsername(), user.getContrasena(),
                user.getNombre(), user.getEmail(), user.getRol());
        userDB.desactivar();
        UsuarioDAODB.update(userDB);
    }

    @Override
    public void eliminarUsuario(int id) throws UserException, DeleteException {
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
            if (user == null || !user.getContrasena().equals(password)) {throw new LoginException("Usuario o contraseña inválidos");}
            if (!user.isActivo())                                       {throw new LoginException("El usuario está inactivo");}
            RolDTO rol = convertirARolDTO(user.getRol());
            userSession = user;

            return new UsuarioDTO(
                    user.getIdUsuario(),
                    user.getUsername(),
                    user.getContrasena(),
                    user.getNombre(),
                    user.getEmail(),
                    rol,
                    user.isActivo()
            );

        } catch (UserException e) {
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
        UsuarioDTO user = new UsuarioDTO(userSession.getIdUsuario(), userSession.getUsername(), userSession.getContrasena(), userSession.getNombre(),
                userSession.getEmail(), rol, userSession.isActivo());
        return user;
    }

    @Override
    public UsuarioDTO obtenerUsuario(String username) throws UserException {
        UsuarioDTO usuarioDTO=null;
        Usuario usuario = UsuarioDAODB.findByUsername(username);
        RolDTO rol = convertirARolDTO(usuario.getRol());
        usuarioDTO = new UsuarioDTO(usuario.getIdUsuario(), usuario.getUsername(),
                usuario.getContrasena(), usuario.getNombre(),
                usuario.getEmail(), rol, usuario.isActivo());
        return usuarioDTO;
    }

    @Override
    public UsuarioDTO obtenerUsuarioByEmail(String email) throws UserException {
        return null;
    }

    @Override
    public List<UsuarioDTO> obtenerUsuarios() throws UserException {
        try {
            List<UsuarioDTO> usuarioDTOs = new ArrayList<>();
            List<Usuario> usuarios = UsuarioDAODB.read();

            for (Usuario usuario : usuarios) {
                RolDTO rol = convertirARolDTO(usuario.getRol());
                usuarioDTOs.add(new UsuarioDTO(usuario.getIdUsuario(), usuario.getUsername(), usuario.getContrasena(),
                        usuario.getNombre(), usuario.getEmail(), rol, usuario.isActivo()));
            }
            return usuarioDTOs;
        } catch (Exception e) {
            throw new UserException("Error al obtener los usuarios");
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
    ) throws RegisterExceptions, UserException,ReadException {

        // Validar que el username y email no estén en uso
        UsuarioDAODB.validarUsernameYEmailUnicos(username, email);

        // Obtener Rol desde ID
        RolDTO rolDTO = obtenerRolPorId(rolId);
        Rol rol = new Rol(rolDTO.getId(), rolDTO.getNombre());
        rol.setActivo(rolDTO.isActivo());

        // Crear el objeto Usuario
        Usuario usuario = new Usuario(username, password, nombre, email, rol);

        if (rol.getNombre().equals("Estudiante")){
            // Validar que la matricula no esté en uso
            EstudianteDAODB.validarMatriculaUnica(matricula);
            // Guardar en la base de datos
            Estudiante estudiante=new Estudiante(usuario,matricula,carrera);
            EstudianteDAODB.create(estudiante);
        }
        if (rol.getNombre().equals("Docente")){
            // Validar que la matricula no esté en uso
            DocenteDAODB.validarLegajoUnico(legajo);
            // Guardar en la base de datos
            Docente docente=new Docente(usuario,legajo);
            DocenteDAODB.create(docente);
        }
        if (rol.getNombre().equals("Representante de Entidad Colaboradora")){
            // Validar que los datos de la Entidad no esté en uso
            EntidadColaborativaDAODB.validarDatosUnicos(nombreEntidad,cuit,direccionEntidad);
            // Guardar en la base de datos
            EntidadColaborativa entidad = new EntidadColaborativa(usuario,nombreEntidad,cuit,direccionEntidad);
            EntidadColaborativaDAODB.create(entidad);
        }
        if (rol.getNombre().equals("Tutor externo")){
            // Validar que los datos de la Entidad no esté en uso
            TutorExternoDAODB.validarExistenciaEntidad(nombreEntidad);
            // Guardar en la base de datos
            TutorExterno tutorExterno = new TutorExterno(usuario,nombreEntidad);
            TutorExternoDAODB.create(tutorExterno);
        }
        if (rol.getNombre().equals("Director de Carrera")){
            DirectorCarrera director = new DirectorCarrera();
            UsuarioDAODB.create(director);
        }
        if (rol.getNombre().equals("Administrador")){
            Administrador administrador = new Administrador();
            UsuarioDAODB.create(administrador);
        }
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
