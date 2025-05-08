package Backend.Entidades;

import Backend.Exceptions.UserExceptions;

import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.isNull;

public class TutorExterno extends Usuario{
    private EntidadColaborativa empresa;
    private List<Proyecto> proyectosAsignados = new ArrayList<>();

    public TutorExterno() {
    }

    public TutorExterno(Usuario user, EntidadColaborativa empresa, List<Proyecto> proyectosAsignados) throws UserExceptions {
        super(user.getUsername(), user.getContrasena(), user.getNombre(), user.getEmail(), user.getRol());
        if (isNull(empresa)) {
            throw new UserExceptions("La entidad de colaboración debe existir.");
        }
        this.proyectosAsignados = proyectosAsignados;
        this.empresa = empresa;
    }

    //metodos
    public void supervisarEstudiante(Estudiante estudiante){
    }
    public void evaluarDesempeno(PPS pps){
    }
    public void validarActividad(){
    }
}
