package Backend.Entidades;

import Backend.Exceptions.UserExceptions;

public class Administrador extends Usuario{

    public Administrador() {
    }

    public Administrador(Usuario user) throws UserExceptions {
        super(user.getUsername(), user.getContrasena(), user.getNombre(), user.getEmail(), user.getRol());
    }


    // Metodos
    public void cargarUsuario(Usuario user){

    }
    public void bajarUsuario(Usuario user){

    }
    public void activarUsuario(Usuario user){

    }
    public void desactivarUsuario(Usuario user){

    }

}
