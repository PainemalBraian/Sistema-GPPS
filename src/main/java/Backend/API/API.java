package Backend.API;

import Backend.DTO.*;
import Backend.Exceptions.*;
import java.util.List;
import java.util.ResourceBundle;

public interface API {

    ResourceBundle obtenerIdioma();

    void cambiarIdioma(String idioma);

    void registrarUsuario(String username, String password, String email, String nombre, int rol) throws RegisterExceptions, UserExceptions, Exception; // Registra el usuario en la bd

    UsuarioDTO obtenerUsuario(String username) throws UserExceptions; // Devuelve el usuario según username

    void eliminarUsuario(int id) throws UserExceptions, DeleteException; // Eliminar un usuario por username de la bd

    List<RolDTO> obtenerRoles() throws Exception; // Devuelve los roles en bd

    List<RolDTO> obtenerRolesActivos() throws ReadException; // Devuelve los roles activos

    void guardarRol(int id, String descripcion, boolean estado) throws UpdateException; // crear el objeto de dominio �Rol�

    RolDTO obtenerRolPorId(int id) throws Exception; // Recuperar el rol almacenado

    void activarRol(int id) throws  ReadException; // Recuperar el objeto Rol, implementar el comportamiento de estado.

    void desactivarRol(int id) throws ReadException, UpdateException; // Recuperar el objeto Rol, imp

    List<UsuarioDTO> obtenerUsuarios() throws UserExceptions; // Recuperar todos los usuarios

    void activarUsuario(String username) throws  UserExceptions, UpdateException; // Recuperar el objeto Usuario, implementar el comportamiento de estado.

    void desactivarUsuario(String username) throws UserExceptions, UpdateException; // Recuperar el objeto Usuario, implementar el comportamiento de estado.

    UsuarioDTO obtenerUsuarioByEmail(String email) throws UserExceptions; // Devuelve el usuario si fue encontrado por email

    UsuarioDTO login(String username, String password) throws LoginException; // Loguea el usuario y si todo es correcto devuelve el Usuario, sino null

    UsuarioDTO obtenerSesionDeUsuario(); // Devuelve el usuario que esta logueado

    void cerrarSesion(); // Cierra la sesion del usuario

    RolDTO obtenerRolUsuarioId(int id) throws Exception; // Obtiene el rol de un usuario mediante su ID

}
