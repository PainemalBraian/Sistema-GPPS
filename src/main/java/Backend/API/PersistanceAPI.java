package Backend.API;

import Backend.DAO.*;
import Backend.DAO.dom.elementos.*;
import Backend.DAO.dom.usuarios.*;
import Backend.DTO.*;
import Backend.Entidades.*;
import Backend.Exceptions.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class PersistanceAPI implements API {
    Usuario userSession = new Usuario();
    ResourceBundle labels = ResourceBundle.getBundle("Backend.labels", Locale.getDefault()); //Busca el labels.properties con el Tipo de idioma Local (SO)

    UsuarioDAODB UsuarioDAODB = new UsuarioDAODB();
    RolDAODB RolDAODB = new RolDAODB();
    EstudianteDAODB EstudianteDAODB= new EstudianteDAODB();
    DocenteDAODB DocenteDAODB = new DocenteDAODB();
    TutorExternoDAODB TutorExternoDAODB = new TutorExternoDAODB();
    EntidadColaborativaDAODB EntidadColaborativaDAODB = new EntidadColaborativaDAODB();

    ProyectoDAODB ProyectoDAODB = new ProyectoDAODB();
    InformeDAODB InformeDAODB = new InformeDAODB();
    PPSDAODB PPSDAODB = new PPSDAODB();

    @Override
    public ResourceBundle obtenerIdioma() {
        return labels;
    }

    @Override
    public void cambiarIdioma(String idioma) {
        labels = ResourceBundle.getBundle("Backend.labels", new Locale(idioma));
    }

    @Override
    public void activarUsuarioByUsername(String username) throws UserException, UpdateException {
        Usuario user = UsuarioDAODB.buscarByUsername(username);
        user.activar();
        UsuarioDAODB.update(user);
    }

    @Override
    public void desactivarUsuarioByUsername(String username) throws UserException, UpdateException{
        Usuario user = UsuarioDAODB.buscarByUsername(username);
        user.desactivar();
        UsuarioDAODB.update(user);
    }

    @Override
    public void cerrarSesion() {
        userSession = null;
    }

    @Override
    public void activarRol(int idRol) throws ReadException, UpdateException {
        Rol rol = RolDAODB.buscarByID(idRol);
        rol.activar();
        RolDAODB.update(rol);
    }

    @Override
    public void desactivarRol(int id) throws ReadException, UpdateException {
        Rol rol = RolDAODB.buscarByID(id);
        rol.desactivar();
        RolDAODB.update(rol);
    }

    @Override
    public void eliminarUsuario(int id) throws DeleteException {
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
            Usuario user = UsuarioDAODB.buscarByUsername(username);
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

    private RolDTO convertirARolDTO(Rol rol) throws UserException {
        if (rol == null)
            throw new UserException("El rol que se intenta convertir no existe.");
        return new RolDTO(rol.getId(), rol.getNombre(), rol.isActivo());
    }

    @Override
    public List<RolDTO> obtenerRoles() throws Exception {
        List<RolDTO> rolesDTO = new ArrayList<>();
        try {
            List<Rol> roles = RolDAODB.buscarRoles();  // Seria conveniente implementar un metodo en el Dao par que filtre ahi mismo por campo "activo", acá se hace el bucle 2 veces
            for (Rol rol : roles) {
                rolesDTO.add(new RolDTO(rol.getId(), rol.getNombre(), rol.isActivo()));
            }
            return rolesDTO;
        } catch (Exception e) {
            //e.getStackTrace();
            throw new Exception("Error al obtener los roles");
        }
    }

    @Override
    public List<RolDTO> obtenerRolesActivos() throws ReadException {
        List<RolDTO> rolesDTO = new ArrayList<>();
        try {
            List<Rol> roles = RolDAODB.buscarRoles();
            for (Rol rol : roles) {
                if (rol.isActivo()) {
                    rolesDTO.add(new RolDTO(rol.getId(), rol.getNombre(), rol.isActivo()));
                }
            }
            return rolesDTO;
        } catch(Exception e){
            throw new ReadException(e.getMessage());
        }
    }

    @Override
    public UsuarioDTO obtenerSesionDeUsuario() throws UserException {
        RolDTO rol = convertirARolDTO(userSession.getRol());
        UsuarioDTO user = new UsuarioDTO(userSession.getIdUsuario(), userSession.getUsername(), userSession.getContrasena(), userSession.getNombre(),
                userSession.getEmail(), rol, userSession.isActivo());
        return user;
    }

    @Override
    public UsuarioDTO obtenerUsuarioByUsername(String username) throws UserException {
        Usuario usuario = UsuarioDAODB.buscarByUsername(username);
        RolDTO rol = convertirARolDTO(usuario.getRol());
        UsuarioDTO usuarioDTO = new UsuarioDTO(usuario.getIdUsuario(), usuario.getUsername(),
                usuario.getContrasena(), usuario.getNombre(),
                usuario.getEmail(), rol, usuario.isActivo());
        return usuarioDTO;
    }
    @Override
    public String obtenerNombreUsuario() throws UserExceptions {
        try {
            return userSession.getNombre();
        }catch (Exception e){
            throw new UserExceptions("No hay un usuario en la sesión");}
    }

//    @Override
//    public UsuarioDTO obtenerUsuarioByEmail(String email) throws UserException {
//        return null;
//    }

    @Override
    public List<UsuarioDTO> obtenerUsuarios() throws UserException {
        try {
            List<UsuarioDTO> usuariosDTO = new ArrayList<>();
            List<Usuario> usuarios = UsuarioDAODB.read();

            for (Usuario usuario : usuarios) {
                RolDTO rol = convertirARolDTO(usuario.getRol());
                usuariosDTO.add(new UsuarioDTO(usuario.getIdUsuario(), usuario.getUsername(), usuario.getContrasena(),
                        usuario.getNombre(), usuario.getEmail(), rol, usuario.isActivo()));
            }
            return usuariosDTO;
        } catch (Exception e) {
            throw new UserException("Error al obtener los usuarios" + e.getMessage());
        }
    }

    @Override
    public void registrarUsuario(String username, String password, String email, String nombre, int rolId, String matricula, String carrera, String legajo, String nombreEntidad, String cuit, String direccionEntidad
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

        Rol rol = RolDAODB.buscarByID(id);

        return new RolDTO(id, rol.getNombre(), rol.isActivo());
    }

    @Override
    public RolDTO obtenerRolByUsuarioId(int id) throws Exception{
        try {
            Usuario usuario = UsuarioDAODB.buscarById(id);
            Rol rolUsuario = usuario.getRol();
            return new RolDTO(rolUsuario.getId(), rolUsuario.getNombre(), rolUsuario.isActivo());
        } catch (Exception e) {
            throw new Exception("Error al obtener el rol del usuario");
        }
    }

///////////////////////////////////////////////////////////////////////////////////////////////////
//    MÉTODOS DE ELEMENTOS
///////////////////////////////////////////////////////////////////////////////////////////////////

    @Override //probar
    public void cargarProyecto(String titulo, String descripcion, String areaDeInteres, String ubicacion, String objetivos, String requisitos, String tutorEncargado) throws CreateException {
        try {
            // Validar que la matricula no esté en uso
            ProyectoDAODB.validarTituloUnico(titulo);
            TutorExterno encargado = TutorExternoDAODB.buscarByUsername(tutorEncargado);
            // Guardar en la base de datos
            Proyecto proyecto = new Proyecto(titulo, descripcion, areaDeInteres, ubicacion, objetivos, requisitos, encargado);
            ProyectoDAODB.create(proyecto);
        } catch (Exception e) {
            throw new CreateException(e.getMessage());
        }
    }

    @Override //probar
    public ProyectoDTO obtenerProyectoByTitulo(String titulo) throws ReadException {
        try {
            Proyecto proyecto = ProyectoDAODB.buscarByTitulo(titulo);
            TutorExternoDTO tutor = null;
            tutor = convertirATutorDTO(proyecto.getTutorEncargado());
            ProyectoDTO proyectoDTO = new ProyectoDTO(proyecto.getId(), proyecto.getTitulo(),
                    proyecto.getDescripcion(), proyecto.getAreaDeInteres(),
                    proyecto.getUbicacion(), proyecto.getObjetivos(), proyecto.getRequisitos(),tutor);
            return proyectoDTO;
        } catch (UserException e) {
            throw new ReadException("Error al obtener el proyecto: "+e.getMessage());
        }
    }

    private TutorExternoDTO convertirATutorDTO(TutorExterno tutor) throws UserException {
        if (tutor == null)
            throw new UserException("El rol que se intenta convertir no existe.");
        RolDTO rolDTO = convertirARolDTO(tutor.getRol());
        UsuarioDTO usuarioDTO = new UsuarioDTO(tutor.getIdUsuario(), tutor.getUsername(), tutor.getContrasena(), tutor.getNombre(), tutor.getEmail(),rolDTO,tutor.isActivo());
        return new TutorExternoDTO(usuarioDTO, tutor.getNombreEntidadColaborativa());
    }

}
