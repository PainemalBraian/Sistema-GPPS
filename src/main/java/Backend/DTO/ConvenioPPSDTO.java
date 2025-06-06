package Backend.DTO;

<<<<<<< HEAD
import java.util.List;

=======
>>>>>>> 6c4b88f60d8f438e5a20427d61cee662601a4be7
public class ConvenioPPSDTO extends ItemDTO{
    private boolean habilitado;
    private EntidadColaborativaDTO entidad;
    private ProyectoDTO proyecto;
    private EstudianteDTO estudiante;
    private PlanDeTrabajoDTO plan;

    public ConvenioPPSDTO() {
    }

    // Carga/Creacion
    public ConvenioPPSDTO(int id,String titulo, String descripcion, ProyectoDTO proyecto, EstudianteDTO estudiante,
                       EntidadColaborativaDTO entidad, PlanDeTrabajoDTO plan) {
        super(id,titulo, descripcion);

        this.proyecto = proyecto;
        this.estudiante = estudiante;
        this.entidad = entidad;
        this.plan = plan;
    }

    public boolean isHabilitado() {
        return habilitado;
    }

    public void setHabilitado(boolean habilitado) {
        this.habilitado = habilitado;
    }

    public EntidadColaborativaDTO getEntidad() {
        return entidad;
    }

    public void setEntidad(EntidadColaborativaDTO entidad) {
        this.entidad = entidad;
    }

    public ProyectoDTO getProyecto() {
        return proyecto;
    }

    public void setProyecto(ProyectoDTO proyecto) {
        this.proyecto = proyecto;
    }

    public EstudianteDTO getEstudiante() {
        return estudiante;
    }

    public void setEstudiante(EstudianteDTO estudiante) {
        this.estudiante = estudiante;
    }

    public DocenteDTO getDocente(){
        return this.plan.getDocente();
    }

    public TutorExternoDTO getTutor(){
        return this.plan.getTutor();
    }

    public PlanDeTrabajoDTO getPlan() {
        return plan;
    }

    public void setPlan(PlanDeTrabajoDTO plan) {
        this.plan = plan;
    }
}