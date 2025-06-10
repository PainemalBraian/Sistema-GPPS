package Backend.DTO;

import java.util.ArrayList;
import java.util.List;

public class PlanDeTrabajoDTO extends ItemDTO{

    private DocenteDTO docente;
    private TutorExternoDTO tutor;
    private List<ActividadDTO> actividades = new ArrayList<>();
    private InformeDTO informeFinal;
    private boolean habilitado;

    public PlanDeTrabajoDTO(int id, String titulo, String descripcion, DocenteDTO docente, TutorExternoDTO tutor, List<ActividadDTO> actividades, InformeDTO informeFinal, boolean habilitado) {
        super(id, titulo, descripcion);
        this.docente = docente;
        this.tutor = tutor;
        this.actividades = actividades;
        this.informeFinal = informeFinal;
        this.habilitado = habilitado;
    }

    public PlanDeTrabajoDTO() {

    }

    public DocenteDTO getDocente() {
        return docente;
    }

    public TutorExternoDTO getTutor() {
        return tutor;
    }

    public List<ActividadDTO> getActividades() {
        return actividades;
    }

    public InformeDTO getInformeFinal() {
        return informeFinal;
    }

    public boolean isHabilitado() {
        return habilitado;
    }

    public void setDocente(DocenteDTO docente) {
        this.docente = docente;
    }

    public void setTutor(TutorExternoDTO tutor) {
        this.tutor = tutor;
    }

    public void setActividades(List<ActividadDTO> actividades) {
        this.actividades = actividades;
    }

    public void setInformeFinal(InformeDTO informeFinal) {
        this.informeFinal = informeFinal;
    }

    public void setHabilitado(boolean habilitado) {
        this.habilitado = habilitado;
    }
}