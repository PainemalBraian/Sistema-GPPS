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

    public PPSDTO(int i, String desarrolloAppGestiónAcadémica, String proyectoDeCátedra, String ingenieríaDeSoftwareIi, String s, String s1, String s2, String s3, int i1, boolean b) {
    }

    public PPSDTO(String text, String text1, String text2, String text3, String text4, String text5, String text6) {
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
