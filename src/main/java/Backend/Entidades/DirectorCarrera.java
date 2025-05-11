package Backend.Entidades;

import Backend.Exceptions.UserException;

public class DirectorCarrera extends Usuario{

    public DirectorCarrera() {
    }

    public DirectorCarrera(Usuario user) throws UserException {
        super(user.getUsername(), user.getContrasena(), user.getNombre(), user.getEmail(), user.getRol());
    }

    //metodos
    public void calificarPlanDeTrabajo(){

    }
    public void calificarInformeFinal(){

    }


}
