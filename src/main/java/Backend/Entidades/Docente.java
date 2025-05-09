package Backend.Entidades;

import Backend.Exceptions.UserExceptions;

import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.isNull;

public class Docente extends Usuario{
    private String legajo;
    private List <Estudiante> estudiantesAsignados = new ArrayList<>();

    public Docente() {}

    public Docente(Usuario user, String legajo, List<Estudiante> estudiantesAsignados) throws UserExceptions {
        super(user.getUsername(), user.getContrasena(), user.getNombre(), user.getEmail(), user.getRol());
        if (isNull(legajo)) {
            throw new UserExceptions("El legajo debe existir.");
        }
        // Verificar la cantidad de dígitos
        int cantidadDigitos = legajo.length();
        if (cantidadDigitos != 6) {
            throw new UserExceptions("El legajo debe contener 6 dígitos.");
        }
        this.legajo = legajo;
        this.estudiantesAsignados = estudiantesAsignados;
    }

    //metodos
    public String getLegajo() {
        return legajo;
    }
    public void setLegajo(String legajo) {
        this.legajo = legajo;
    }
    public void asignarEstudiante(Estudiante estudiante) {
        estudiantesAsignados.add(estudiante);
    }
    public List<Estudiante> getEstudiantesAsignados() {
        return estudiantesAsignados;
    }
    public void monitorearProgreso(PPS pps){
    }
    public void evaluarInforme(Informe informe){
    }

}
