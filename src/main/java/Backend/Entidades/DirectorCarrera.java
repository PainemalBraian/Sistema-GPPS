package Backend.Entidades;

import Backend.Exceptions.UserException;

public class DirectorCarrera extends Usuario{

    public DirectorCarrera() {
    }

    public DirectorCarrera(Usuario user) throws UserException {
        super(user.getIdUsuario(),user.getUsername(), user.getContrasena(), user.getNombre(), user.getEmail(), user.getRol());
        super.setActivo(user.isActivo());
    }

    //metodos
    public void calificarPlanDeTrabajo(){

    }
    public void calificarInformeFinal(){

    }

//////////////////////// GETTERS ////////////////////////////////////////////////

//////////////////////// SETTERS ////////////////////////////////////////////////

}
