package Backend.API;

import Backend.DAO.*;
import Backend.DAO.dom.elementos.*;
import Backend.DAO.dom.usuarios.*;
import Backend.DTO.*;
import Backend.Entidades.*;
import Backend.Exceptions.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
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
    DirectorCarreraDAODB DirectorCarreraDAODB = new DirectorCarreraDAODB();

    ActividadDAODB ActividadDAODB = new ActividadDAODB();
    InformeDAODB InformeDAODB = new InformeDAODB();
    ProyectoDAODB ProyectoDAODB = new ProyectoDAODB();
    ConvenioPPSDAODB ConvenioPPSDAODB = new ConvenioPPSDAODB();
    PlanDeTrabajoDAODB PlanDeTrabajoDAODB = new PlanDeTrabajoDAODB();
    MensajeDAODB MensajeDAODB = new MensajeDAODB();

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

    @Override
    public List<RolDTO> obtenerRoles() throws Exception {
        List<RolDTO> rolesDTO = new ArrayList<>();
        try {
            List<Rol> roles = RolDAODB.buscarRoles();
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
            List<Rol> roles = RolDAODB.buscarRoles();// Seria conveniente implementar un metodo en el Dao par que filtre ahi mismo por campo "activo", acá se hace el bucle 2 veces
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
    public String obtenerNombreUsuario() throws UserException {
        try {
            return userSession.getNombre();
        }catch (Exception e){
            throw new UserException("No hay un usuario en la sesión");}
    }

//    @Override
//    public UsuarioDTO obtenerUsuarioByEmail(String email) throws UserException {
//        return null;
//    }

    @Override
    public List<UsuarioDTO> obtenerUsuarios() throws UserException {
        try {
            List<UsuarioDTO> usuariosDTO = new ArrayList<>();
            List<Usuario> usuarios = UsuarioDAODB.obtenerUsuarios();

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
            DirectorCarrera director = new DirectorCarrera(usuario);
            UsuarioDAODB.create(director);
        }
        if (rol.getNombre().equals("Administrador")){
            Administrador administrador = new Administrador(usuario);
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
    @Override
    public void descargarArchivoPDF(String tituloInforme) throws IOException, ReadException {
        // Se escribe el archivo en la carpeta de descargas default del sistema
        Informe informe = InformeDAODB.buscarByTitulo(tituloInforme);
        byte[] datosPDF = informe.getArchivoPDF();
        Path downloadPath = Paths.get(System.getProperty("user.home"), "Downloads");
        // Crear la carpeta si no existe
        File carpeta = new File(String.valueOf(downloadPath) + "/.GPPS_Downloads");
        if (!carpeta.exists()) {
            boolean creada = carpeta.mkdirs();
            if (!creada) {
                throw new IOException("No se pudo crear la carpeta destino: " + String.valueOf(downloadPath));
            }
        }

        // Crear archivo temporal dentro de la carpeta
        Path archivoTemporal = Files.createTempFile(carpeta.toPath(), "informe_", ".pdf");

        // Escribir el contenido del PDF
        try (FileOutputStream fos = new FileOutputStream(archivoTemporal.toFile())) {
            fos.write(datosPDF);
        }
    }

/////////////////////////////////////////////////////
//    MÉTODOS DE CARGAS

    @Override
    public void cargarConvenio(String tituloConvenio, String descripcionConvenio, ProyectoDTO proyectoDTO, EstudianteDTO estudianteDTO,
                               EntidadColaborativaDTO entidadDTO, String tituloPlan)throws CreateException {
        try {
            ConvenioPPSDAODB.validarTituloUnico(tituloConvenio);

            PlanDeTrabajo plan = PlanDeTrabajoDAODB.buscarByTitulo(tituloPlan);

            TutorExterno tutor = convertirATutor(proyectoDTO.getTutorEncargado());
            Proyecto proyecto = convertirAProyecto(proyectoDTO);
            Estudiante estudiante = convertirAEstudiante(estudianteDTO);
            EntidadColaborativa entidad = convertirAEntidad(entidadDTO);
            ConvenioPPS convenio = new ConvenioPPS(tituloConvenio, descripcionConvenio, proyecto, estudiante, entidad,plan);
            convenio.setHabilitado(true);

            ConvenioPPSDAODB.create(convenio);
        } catch (Exception e) {
            throw new CreateException("Error al crear el convenio: " + e.getMessage());
        }
    }

    @Override //probar
    public void cargarActividad(String titulo, String descripcion, LocalDate fechaFin, int duracion, LocalDate fechaInicio)throws CreateException {
        try {
            Actividad actividad = new Actividad(titulo, descripcion,fechaFin,duracion,fechaInicio);

            ActividadDAODB.create(actividad);
        } catch (Exception e) {
            throw new CreateException("Error al crear la Actividad: " + e.getMessage());
        }
    }

    @Override
    public void cargarInforme(String titulo, String descripcion, byte[] archivo, LocalDate fecha)throws CreateException {
        try {
            InformeDAODB.validarTituloUnico(titulo);
            Informe informe = new Informe(titulo, descripcion, archivo);
//            informe.setFecha(fecha); // Por defecto al crear el Informe se hace un LocalDate.now

            InformeDAODB.create(informe);
        } catch (Exception e) {
            throw new CreateException("Error al crear el informe: " + e.getMessage());
        }
    }

    @Override
    public void cargarPropuestaPropia(String titulo, String descripcion, String areaDeInteres, String ubicacion, String objetivos, String requisitos) throws CreateException {
        try {
            ProyectoDAODB.validarTituloUnico(titulo);
            Proyecto proyecto = new Proyecto(titulo, descripcion, areaDeInteres, ubicacion, objetivos, requisitos);
            ProyectoDAODB.createPropuesta(proyecto);
        } catch (Exception e) {
            throw new CreateException("Error al crear el proyecto: " + e.getMessage());
        }
    }

    @Override
    public void cargarProyecto(String titulo, String descripcion, String areaDeInteres, String ubicacion, String objetivos, String requisitos, String tutorEncargado) throws CreateException {
        try {
            ProyectoDAODB.validarTituloUnico(titulo);
            Proyecto proyecto = new Proyecto(titulo, descripcion, areaDeInteres, ubicacion, objetivos, requisitos);
            TutorExterno encargado = TutorExternoDAODB.buscarByUsername(tutorEncargado);
            proyecto.setTutorEncargado(encargado);
            // Guardar en la base de datos

//            proyecto.setHabilitado(false);  Por defecto es false. pero para que se entienda que existe este campo. Solo el director debe poder habilitar o admin
            ProyectoDAODB.create(proyecto);
        } catch (Exception e) {
            throw new CreateException("Error al crear el proyecto: " + e.getMessage());
        }
    }


/////////////////////////////////////////////////////
//    MÉTODOS DE BUSQUEDA

    @Override
    public InformeDTO obtenerInformeByTitulo(String titulo) throws ReadException {
        try {
            Informe informe = InformeDAODB.buscarByTitulo(titulo);

            InformeDTO informeDTO = new InformeDTO(informe.getId(), informe.getTitulo(),
                    informe.getDescripcion(), informe.getArchivoPDF(), informe.getFecha());

            return informeDTO;
        } catch (ReadException e) {
            throw new ReadException("Error al obtener la actividad: " + e.getMessage());
        }
    }

    @Override
    public ActividadDTO obtenerActividadByTitulo(String titulo) throws ReadException {

        try {
            Actividad actividad = ActividadDAODB.buscarByTitulo(titulo);

            ActividadDTO actividadDTO = new ActividadDTO(actividad.getId(), actividad.getTitulo(),
                    actividad.getDescripcion(), actividad.getFechaFin(), actividad.getDuracion(),
                    actividad.getFechaInicio());

            if (actividad.getInformes()!=null){
                List<InformeDTO> informes = new ArrayList<>();
                actividadDTO.setInformes(informes);}

            return actividadDTO;
        } catch (ReadException e) {
            throw new ReadException("Error al obtener la actividad: " + e.getMessage());
        }
//        catch (EmptyException e) {
//            throw new ReadException(e.getMessage());
//        }
    }

    @Override
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

    @Override
    public List<ActividadDTO> obtenerActividadesHabilitadas() throws ReadException {
        try {
            List<ActividadDTO> actividadesDTO = convertirAListaActividadesDTO(ActividadDAODB.obtenerActividadesHabilitadas());
            return actividadesDTO;
        } catch (EmptyException e) {
            throw new ReadException("Error al obtener los proyectos: "+e.getMessage());
        }
    }

    @Override //probar
    public List <ProyectoDTO> obtenerProyectosHabilitados() throws ReadException {
        try {
            List<Proyecto> proyectos = ProyectoDAODB.obtenerProyectosHabilitados();
            List<ProyectoDTO> proyectosDTO = convertirAListaProyectosDTO(proyectos);
            return proyectosDTO;
        } catch (UserException e) {
            throw new ReadException("Error al obtener los proyectos: "+e.getMessage());
        }
    }

    @Override //Convenio por nombre
    public ConvenioPPSDTO obtenerConvenioPPSByTitulo(String titulo) throws ReadException {
        try {
            ConvenioPPS convenio = ConvenioPPSDAODB.buscarByTitulo(titulo);
            ConvenioPPSDTO convenioDTO = convertirAConvenioDTO(convenio);

            return convenioDTO;
        } catch (ReadException | UserException | EmptyException e) {
            throw new ReadException("Error al obtener el convenio: " + e.getMessage());
        }
    }

    @Override
    public PlanDeTrabajoDTO obtenerPlanByConvenioTitulo(String titulo) throws ReadException {
        try {
            return obtenerConvenioPPSByTitulo(titulo).getPlan();
        } catch (ReadException e) {
            throw new ReadException("Error al obtener las actividades: "+ titulo + ". " + e.getMessage());
        }
    }

    @Override //Informes de una actividad
    public List<ActividadDTO> obtenerActividadesByConvenioTitulo(String titulo) throws ReadException {
        try {
            List <ActividadDTO> actividades = obtenerConvenioPPSByTitulo(titulo).getActividades();
            return actividades;
        } catch (ReadException e) {
            throw new ReadException("Error al obtener las actividades: "+ titulo + ". " + e.getMessage());
        }
    }

    @Override //Informes de un convenio
    public List<InformeDTO> obtenerInformesByConvenioTitulo(String titulo) throws ReadException {
        try {
            List <InformeDTO> informes = new ArrayList<>();

            for (ActividadDTO a : (obtenerConvenioPPSByTitulo(titulo).getActividades())){
                for (InformeDTO i : a.getInformes()){
                    informes.add(i);
                }
            }

            return informes;
        } catch (ReadException e) {
            throw new ReadException("Error al obtener los informes del convenio: "+ titulo +". " + e.getMessage());
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //  CONVERSIONES DE TIPOS
    ////////////////////////////////////////////////////////////////////////////////////////////////
    private RolDTO convertirARolDTO(Rol rol) throws UserException {
        if (rol == null)
            throw new UserException("El rol que se intenta convertir no existe.");
        return new RolDTO(rol.getId(), rol.getNombre(), rol.isActivo());
    }

    private UsuarioDTO convertirAUsuarioDTO(Usuario usuario) throws UserException {
        if (usuario == null)
            throw new UserException("El usuario que se intenta convertir no existe.");
        RolDTO rolDTO = convertirARolDTO(usuario.getRol());
        return new UsuarioDTO(usuario.getIdUsuario(), usuario.getUsername(), usuario.getContrasena(), usuario.getNombre(), usuario.getEmail(),rolDTO,usuario.isActivo());
    }

    private TutorExternoDTO convertirATutorDTO(TutorExterno tutor) throws UserException {
        if (tutor == null)
            throw new UserException("El rol que se intenta convertir no existe.");
//        RolDTO rolDTO = convertirARolDTO(tutor.getRol());
//        UsuarioDTO usuarioDTO = new UsuarioDTO(tutor.getIdUsuario(), tutor.getUsername(), tutor.getContrasena(), tutor.getNombre(), tutor.getEmail(),rolDTO,tutor.isActivo());
        UsuarioDTO usuarioDTO = convertirAUsuarioDTO(tutor.getUsuario());
        return new TutorExternoDTO(usuarioDTO, tutor.getNombreEntidadColaborativa());
    }

    private DocenteDTO convertirADocenteDTO(Docente docente) throws UserException {
        if (docente == null)
            throw new UserException("El docente que se intenta convertir no existe.");
//        RolDTO rolDTO = convertirARolDTO(docente.getRol());
//        UsuarioDTO usuarioDTO = new UsuarioDTO(docente.getIdUsuario(), docente.getUsername(), docente.getContrasena(), docente.getNombre(), docente.getEmail(),rolDTO,docente.isActivo());
        UsuarioDTO usuarioDTO = convertirAUsuarioDTO(docente.getUsuario());
        return new DocenteDTO(usuarioDTO, docente.getLegajo());
    }

    private EntidadColaborativaDTO convertirAEntidadDTO(EntidadColaborativa entidad) throws UserException {
        if (entidad == null)
            throw new UserException("La entidad que se intenta convertir no existe.");
//        RolDTO rolDTO = convertirARolDTO(entidad.getRol());
//        UsuarioDTO usuarioDTO = new UsuarioDTO(entidad.getIdUsuario(), entidad.getUsername(), entidad.getContrasena(), entidad.getNombre(), entidad.getEmail(),rolDTO,entidad.isActivo());
        UsuarioDTO usuarioDTO = convertirAUsuarioDTO(entidad.getUsuario());
        List <ProyectoDTO> proyectosDTO = convertirAListaProyectosDTO(entidad.getProyectos());
        return new EntidadColaborativaDTO(usuarioDTO, entidad.getNombreEntidad(),entidad.getCuit(),entidad.getDireccionEntidad(),proyectosDTO);
    }

    private EstudianteDTO convertirAEstudianteDTO(Estudiante estudiante) throws UserException {
        if (estudiante == null)
            throw new UserException("El estudiante que se intenta convertir no existe.");
//        RolDTO rolDTO = convertirARolDTO(estudiante.getRol());
//        UsuarioDTO usuarioDTO = new UsuarioDTO(estudiante.getIdUsuario(), estudiante.getUsername(), estudiante.getContrasena(), estudiante.getNombre(), estudiante.getEmail(),rolDTO,estudiante.isActivo());
        UsuarioDTO usuarioDTO = convertirAUsuarioDTO(estudiante.getUsuario());
        return new EstudianteDTO(usuarioDTO, estudiante.getMatricula(),estudiante.getCarrera());
    }

    private List<ProyectoDTO> convertirAListaProyectosDTO(List<Proyecto> proyectos) throws UserException {
//        if (proyectos == null){
//            List <ProyectoDTO> proyectosDTO = new ArrayList<>();
//            return proyectosDTO;}
//            //throw new UserException("La lista de proyectos que se intenta convertir no existe.");
        List <ProyectoDTO> proyectosDTO = new ArrayList<>();
        for (Proyecto proyecto: proyectos){
            proyectosDTO.add(convertirAProyectoDTO(proyecto));
        }
        return proyectosDTO;
    }

    private ProyectoDTO convertirAProyectoDTO(Proyecto proyecto) throws UserException {
        if (proyecto == null) { // Forzado a creación vacia para que se permitan cargas de "proyectos vacios en espera de asignación" en caso de estudiante, o decente o tutor
            return new ProyectoDTO();
            //throw new UserException("El proyecto que se intenta convertir no existe.");
        }
        TutorExternoDTO tutorDTO = convertirATutorDTO(proyecto.getTutorEncargado());
        ProyectoDTO proyectoDTO = new ProyectoDTO(proyecto.getId(), proyecto.getTitulo(), proyecto.getDescripcion(), proyecto.getAreaDeInteres(), proyecto.getUbicacion(),
                proyecto.getObjetivos(), proyecto.getRequisitos(),tutorDTO);
        proyectoDTO.setHabilitado(proyecto.isHabilitado());

        return proyectoDTO;
    }

    private ConvenioPPSDTO convertirAConvenioDTO(ConvenioPPS convenio) throws UserException, EmptyException, ReadException {
        if (convenio == null) {
            throw new ReadException("El convenio que se intenta convertir no existe.");
        }
        ConvenioPPSDTO convenioDTO = null;
        try {
            ProyectoDTO proyectoDTO = convertirAProyectoDTO(ProyectoDAODB.buscarByID(convenio.getProyecto().getId()));
            DocenteDTO docenteDTO = new DocenteDTO(convertirADocenteDTO(convenio.getDocente()),convenio.getDocente().getLegajo());
            // Consultar por Estudiantes enlazados?
            EstudianteDTO estudianteDTO = new EstudianteDTO(convertirAEstudianteDTO(convenio.getEstudiante()),convenio.getEstudiante().getMatricula(),convenio.getEstudiante().getCarrera());
            EntidadColaborativaDTO entidadDTO = new EntidadColaborativaDTO(convertirAEntidadDTO(convenio.getEntidad()),convenio.getEntidad().getNombreEntidad(),convenio.getEntidad().getCuit(),convenio.getEntidad().getDireccionEntidad());
            // consultar proyectos de entidad ?
            List<ActividadDTO> actividadesDTO = new ArrayList<>(convertirAListaActividadesDTO(convenio.getActividades()));
            InformeDTO informeDTO = convertirAInformeDTO(convenio.getPlan().getInformeFinal());
            PlanDeTrabajoDTO planDTO = new PlanDeTrabajoDTO(convenio.getPlan().getId(),convenio.getPlan().getTitulo(),convenio.getPlan().getDescripcion(),docenteDTO,proyectoDTO.getTutorEncargado(),actividadesDTO,informeDTO,true);

            convenioDTO = new ConvenioPPSDTO(convenio.getId(), convenio.getTitulo(), convenio.getDescripcion(),proyectoDTO,estudianteDTO,entidadDTO,planDTO);
            proyectoDTO.setHabilitado(convenio.isHabilitado());

        }
        catch (Exception e) {
            e.printStackTrace();
            throw new ReadException("Error al convertir el convenio: " + e.getMessage());
        }
        return convenioDTO;
    }

    private DirectorCarreraDTO convertirADirectorDTO(DirectorCarrera directorCarrera) throws UserException {
        if (directorCarrera == null)
            throw new UserException("El director que se intenta convertir no existe.");
        return new DirectorCarreraDTO(convertirAUsuarioDTO(directorCarrera.getUsuario()));
    }

    private List<ActividadDTO> convertirAListaActividadesDTO(List<Actividad> actividades) throws EmptyException {
        List <ActividadDTO> actividadesDTO = new ArrayList<>();
        for (Actividad actividad: actividades){
            actividadesDTO.add(convertirAActividadDTO(actividad));
        }
        return actividadesDTO;
    }

    private ActividadDTO convertirAActividadDTO(Actividad actividad) throws EmptyException {
        if (actividad == null)
            throw new EmptyException("La actividad que se intenta convertir no existe.");
        return new ActividadDTO(actividad.getId(),actividad.getTitulo(),actividad.getDescripcion(),actividad.getFechaFin(),actividad.getDuracion(),actividad.getFechaInicio());
    }

    private InformeDTO convertirAInformeDTO(Informe informe) throws UserException {
        if (informe == null)
            throw new UserException("El informe que se intenta convertir no existe.");
        return new InformeDTO(informe.getId(),informe.getTitulo(),informe.getDescripcion(),informe.getArchivoPDF(),informe.getFecha());
    }


/////////////////////////////////////////////////////////////////////////////////////////////////

    private Rol convertirARol(RolDTO rolDTO) throws UserException {
        if (rolDTO == null)
            throw new UserException("El rolDTO que se intenta convertir no existe.");
        return new Rol(rolDTO.getId(), rolDTO.getNombre());
    }

    private Usuario convertirAUsuario(UsuarioDTO usuarioDTO) throws UserException {
        if (usuarioDTO == null)
            throw new UserException("El usuarioDTO que se intenta convertir no existe.");
        Rol rol = convertirARol(usuarioDTO.getRol());
        return new Usuario(usuarioDTO.getUsername(), usuarioDTO.getPassword(), usuarioDTO.getNombre(), usuarioDTO.getEmail(), rol);
    }

    private TutorExterno convertirATutor(TutorExternoDTO tutorDTO) throws UserException {
        if (tutorDTO == null)
            throw new UserException("El tutorDTO que se intenta convertir no existe.");
        Usuario usuario = convertirAUsuario(tutorDTO.getUsuario());
        return new TutorExterno(usuario, tutorDTO.getNombreEntidadColaborativa());
    }

    private Docente convertirADocente(DocenteDTO docenteDTO) throws UserException {
        if (docenteDTO == null)
            throw new UserException("El docenteDTO que se intenta convertir no existe.");
        Usuario usuario = convertirAUsuario(docenteDTO.getUsuario());
        return new Docente(usuario, docenteDTO.getLegajo());
    }

    private EntidadColaborativa convertirAEntidad(EntidadColaborativaDTO entidadDTO) throws UserException, EmptyException {
        if (entidadDTO == null)
            throw new UserException("La entidadDTO que se intenta convertir no existe.");
        Usuario usuario = convertirAUsuario(entidadDTO.getUsuario());
        List<Proyecto> proyectos = convertirAListaProyectos(entidadDTO.getProyectos());
        return new EntidadColaborativa(usuario, entidadDTO.getNombreEntidad(), entidadDTO.getCuit(), entidadDTO.getDireccionEntidad());
    }

    private Estudiante convertirAEstudiante(EstudianteDTO estudianteDTO) throws UserException {
        if (estudianteDTO == null)
            throw new UserException("El estudianteDTO que se intenta convertir no existe.");
        Usuario usuario = convertirAUsuario(estudianteDTO.getUsuario());
        return new Estudiante(usuario, estudianteDTO.getMatricula(), estudianteDTO.getCarrera());
    }

    private DirectorCarrera convertirADirector(DirectorCarreraDTO directorDTO) throws UserException {
        if (directorDTO == null)
            throw new UserException("El directorDTO que se intenta convertir no existe.");
        Usuario usuario = convertirAUsuario(directorDTO.getUsuario());
        return new DirectorCarrera(usuario);
    }

    private Proyecto convertirAProyecto(ProyectoDTO proyectoDTO) throws EmptyException, UserException {
        if (proyectoDTO == null)
            return null; // o new Proyecto() según lógica de negocio
        TutorExterno tutor = convertirATutor(proyectoDTO.getTutorEncargado());
        Proyecto proyecto = new Proyecto(proyectoDTO.getTitulo(), proyectoDTO.getDescripcion(), proyectoDTO.getAreaDeInteres(),
                proyectoDTO.getUbicacion(), proyectoDTO.getObjetivos(), proyectoDTO.getRequisitos(), tutor);
        proyecto.setHabilitado(proyectoDTO.isHabilitado());
        return proyecto;
    }

    private List<Proyecto> convertirAListaProyectos(List<ProyectoDTO> proyectosDTO) throws UserException, EmptyException {
        List<Proyecto> proyectos = new ArrayList<>();
        for (ProyectoDTO proyectoDTO : proyectosDTO) {
            proyectos.add(convertirAProyecto(proyectoDTO));
        }
        return proyectos;
    }

    private Actividad convertirAActividad(ActividadDTO actividadDTO) throws EmptyException {
        if (actividadDTO == null)
            throw new EmptyException("La actividadDTO que se intenta convertir no existe.");
        Actividad actividad = new Actividad(actividadDTO.getTitulo(), actividadDTO.getDescripcion(), actividadDTO.getFechaFin(), actividadDTO.getDuracion(),actividadDTO.getFechaInicio());
        actividad.setCalificacion(actividadDTO.getCalificacion());
        return actividad;
    }

    private List<Actividad> convertirAListaActividades(List<ActividadDTO> actividadesDTO) throws EmptyException {
        List<Actividad> actividades = new ArrayList<>();
        for (ActividadDTO actividadDTO : actividadesDTO) {
            actividades.add(convertirAActividad(actividadDTO));
        }
        return actividades;
    }

    private Informe convertirAInforme(InformeDTO informeDTO) throws UserException, EmptyException {
        if (informeDTO == null)
            throw new UserException("El informeDTO que se intenta convertir no existe.");
        Informe informe = new Informe(informeDTO.getTitulo(), informeDTO.getDescripcion(), informeDTO.getArchivo());
        informe.setFecha(informeDTO.getFecha());
        return informe;
    }

    private ConvenioPPS convertirAConvenio(ConvenioPPSDTO convenioDTO) throws UserException, EmptyException, ReadException {
        if (convenioDTO == null)
            throw new ReadException("El convenioDTO que se intenta convertir no existe.");

        Proyecto proyecto = convertirAProyecto(convenioDTO.getProyecto());
        Docente docente = convertirADocente(convenioDTO.getDocente());
        TutorExterno tutor = convertirATutor(convenioDTO.getTutor());
        Estudiante estudiante = convertirAEstudiante(convenioDTO.getEstudiante());
        EntidadColaborativa entidad = convertirAEntidad(convenioDTO.getEntidad());
        List<Actividad> actividades = convertirAListaActividades(convenioDTO.getActividades());
        PlanDeTrabajo plan = new PlanDeTrabajo(convenioDTO.getPlan().getTitulo(),convenioDTO.getDescripcion(),docente,tutor);

        ConvenioPPS convenio = new ConvenioPPS(convenioDTO.getTitulo(), convenioDTO.getDescripcion(),
                proyecto, estudiante, entidad, plan);
        convenio.setHabilitado(convenioDTO.isHabilitado());
        return convenio;
    }


}
