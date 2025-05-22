package Backend.API;

import Backend.DTO.*;
import Backend.Entidades.Actividad;
import Backend.Entidades.Informe;
import Backend.Exceptions.*;
import java.util.List;
import java.util.ResourceBundle;

public interface API {

    ResourceBundle obtenerIdioma(); // Retorna el ResourceBundle del idioma que esté seteado en esta instancia.

    void cambiarIdioma(String idioma); //Modifica el resourceBundle para que busque en el label del idioma enviado.

    void registrarUsuario(String username, String password, String email, String nombre, int rolId, String matricula, String carrera, String legajo, String nombreEntidad, String cuit, String direccionEntidad) throws RegisterExceptions, UserException, Exception;

    UsuarioDTO obtenerUsuarioByUsername(String username) throws UserException; // Devuelve el usuario según username

    void eliminarUsuario(int id) throws UserException, DeleteException; // Eliminar un usuario por username de la bd

    List<RolDTO> obtenerRoles() throws Exception; // Devuelve los roles en bd

    List<RolDTO> obtenerRolesActivos() throws ReadException; // Devuelve los roles activos

    void guardarRol(int id, String descripcion, boolean estado) throws UpdateException; // crear el objeto de dominio �Rol�

    RolDTO obtenerRolPorId(int id) throws Exception; // Recuperar el rol almacenado

    void activarRol(int id) throws ReadException, UpdateException; // Recuperar el objeto Rol, implementar el comportamiento de estado.

    void desactivarRol(int id) throws ReadException, UpdateException; // Recuperar el objeto Rol, imp

    List<UsuarioDTO> obtenerUsuarios() throws UserException; // Recuperar todos los usuarios

    void activarUsuarioByUsername(String username) throws UserException, UpdateException; // Recuperar el objeto Usuario, implementar el comportamiento de estado.

    void desactivarUsuarioByUsername(String username) throws UserException, UpdateException; // Recuperar el objeto Usuario, implementar el comportamiento de estado.

//    UsuarioDTO obtenerUsuarioByEmail(String email) throws UserException; // Devuelve el usuario si fue encontrado por email

    String obtenerNombreUsuario() throws UserException; //Devuelve el nombre del usuario logeado

    UsuarioDTO login(String username, String password) throws LoginException; // Loguea el usuario y si todo es correcto devuelve el Usuario, sino null

    UsuarioDTO obtenerSesionDeUsuario() throws UserException; // Devuelve el usuario que esta logueado

    void cerrarSesion(); // Cierra la sesion del usuario

    RolDTO obtenerRolByUsuarioId(int id) throws Exception; // Obtiene el rol de un usuario mediante su ID

    void cargarConvenio()throws CreateException;

    void cargarActividad()throws CreateException;

    void cargarInforme()throws CreateException;

    void cargarProyecto(String titulo, String descripcion, String areaDeInteres, String ubicacion, String objetivos, String requisitos, String tutorEncargado) throws CreateException; // Carga un proyecto, con los datos solicitados por parametro

    Informe obtenerInformeByTitulo(String titulo) throws EmptyException;

    Actividad obtenerActividadByTitulo(String titulo) throws EmptyException;

    //probar
    ProyectoDTO obtenerProyectoByTitulo(String titulo) throws ReadException; // Devuelve un proyecto de la DB que es buscado dado un titulo

    List<Actividad> obtenerActividadesHabilitadas() throws EmptyException;

    //probar
    List <ProyectoDTO> obtenerProyectosHabilitados() throws ReadException; // Devuelve de la DB todos los proyectos que esten habilitados

    //probar
    ConvenioPPSDTO obtenerConvenioPPSByTitulo(String titulo) throws ReadException, EmptyException, UserException; // Devuelve Un convenio de PPs de la DB dado su titulo

    List<Informe> obtenerInformesByConvenioTitulo(String tituloConvenio) throws EmptyException;
}
