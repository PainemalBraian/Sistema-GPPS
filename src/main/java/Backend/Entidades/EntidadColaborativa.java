package Backend.Entidades;

import Backend.Exceptions.UserExceptions;

import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.isNull;

public class EntidadColaborativa extends Usuario{
    private Usuario responsable;
    private List<Proyecto> proyectos = new ArrayList();

    public EntidadColaborativa() {
    }

    public EntidadColaborativa(Usuario user, Usuario responsable) throws UserExceptions {
        super(user.getUsername(), user.getContrasena(), user.getNombre(), user.getEmail(), user.getRol());
        if (isNull(responsable)) {
            throw new UserExceptions("El responsable debe existir.");
        }
        this.responsable = responsable;
    }

    // metodos
    public void cargarProyecto(Proyecto proyecto){
    }
    public void borrarProyecto(Proyecto proyecto){
    }

}
