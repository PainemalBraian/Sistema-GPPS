package Backend.Entidades;

import Backend.Exceptions.UserExceptions;

public class DirectorCarrera extends Usuario{

    public DirectorCarrera() {
    }

    public DirectorCarrera(Usuario user) throws UserExceptions {
        super(user.getUsername(), user.getContrasena(), user.getNombre(), user.getEmail(), user.getRol());
    }

    //metodos
    public void calificarPlanDeTrabajo(){

    }
    public void calificarInformeFinal(){

    }


}
