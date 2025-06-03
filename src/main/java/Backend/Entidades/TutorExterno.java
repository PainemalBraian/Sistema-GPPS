package Backend.Entidades;

import Backend.Exceptions.UserException;

import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.isNull;

public class TutorExterno extends Usuario{
    private String nombreEntidadColaborativa;
    private List<Proyecto> proyectosAsignados = new ArrayList<>();

    public TutorExterno() {
    }

    public TutorExterno(Usuario user, String nombreEntidadColaborativa) throws UserException {
        super(user.getIdUsuario(),user.getUsername(), user.getContrasena(), user.getNombre(), user.getEmail(), user.getRol());
        if (isNull(nombreEntidadColaborativa)||nombreEntidadColaborativa.isEmpty()) {
            throw new UserException("El nombre de la entidad no debe estar vacío.");
        }
        this.nombreEntidadColaborativa = nombreEntidadColaborativa;
    }

    public String getNombreEntidadColaborativa() {
        return nombreEntidadColaborativa;
    }

    public void setNombreEntidadColaborativa(String nombreEntidadColaborativa) {
        this.nombreEntidadColaborativa = nombreEntidadColaborativa;
    }

    public List<Proyecto> getProyectosAsignados() {
        return proyectosAsignados;
    }

    public void setProyectosAsignados(List<Proyecto> proyectosAsignados) {
        this.proyectosAsignados = proyectosAsignados;
    }

    //metodos
    public void supervisarEstudiante(Estudiante estudiante){
    }
    public void evaluarDesempeno(ConvenioPPS convenioPps){
    }
    public void validarActividad(){
    }

    public int getIdTutor() {
        return this.getIdUsuario();
    }

}
