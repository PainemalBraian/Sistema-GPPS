package Backend.DTO;

import java.util.ArrayList;
import java.util.List;

public class PPSDTO extends ItemDTO{
    private ProyectoDTO proyecto;
    private DocenteDTO docente;
    private EstudianteDTO estudiante;
    private List<InformeDTO> informes = new ArrayList<>();

    public PPSDTO() {
    }

    public PPSDTO(int id, String titulo, String descripcion, ProyectoDTO proyecto, DocenteDTO docente, EstudianteDTO estudiante){
        super(id, titulo, descripcion);
        this.proyecto = proyecto;
        this.docente = docente;
        this.estudiante = estudiante;
    }

    public PPSDTO(int id, String titulo, String descripcion, ProyectoDTO proyecto, DocenteDTO docente, EstudianteDTO estudiante,List<InformeDTO> informes){
        super(id, titulo, descripcion);
        this.proyecto = proyecto;
        this.docente = docente;
        this.estudiante = estudiante;
        this.informes = informes;
    }

    public ProyectoDTO getProyecto() {
        return proyecto;
    }

    public void setProyecto(ProyectoDTO proyecto) {
        this.proyecto = proyecto;
    }

    public DocenteDTO getDocente() {
        return docente;
    }

    public void setDocente(DocenteDTO docente) {
        this.docente = docente;
    }

    public EstudianteDTO getEstudiante() {
        return estudiante;
    }

    public void setEstudiante(EstudianteDTO estudiante) {
        this.estudiante = estudiante;
    }

    public List<InformeDTO> getInformes() {
        return informes;
    }

    public void setInformes(List<InformeDTO> informes) {
        this.informes = informes;
    }
}
