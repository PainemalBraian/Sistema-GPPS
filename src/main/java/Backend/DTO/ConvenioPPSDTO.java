package Backend.DTO;

import java.util.ArrayList;
import java.util.List;

public class ConvenioPPSDTO extends ItemDTO{
    private DirectorCarreraDTO director;
    private boolean habilitado;
    private EntidadColaborativaDTO entidad;
    private ProyectoDTO proyecto;
    private DocenteDTO docente;
    private EstudianteDTO estudiante;
    private List<ActividadDTO> actividades = new ArrayList<>();

    public ConvenioPPSDTO() {
    }

    public ConvenioPPSDTO(int id, String titulo, String descripcion, ProyectoDTO proyecto, DocenteDTO docente, EstudianteDTO estudiante,
                       DirectorCarreraDTO director, EntidadColaborativaDTO entidad, List<ActividadDTO> actividades) {
        super(id, titulo, descripcion);
        this.proyecto = proyecto;
        this.docente = docente;
        this.estudiante = estudiante;
        this.director = director;
        this.entidad = entidad;
        this.actividades = actividades;
    }

    public DirectorCarreraDTO getDirector() {
        return director;
    }

    public void setDirector(DirectorCarreraDTO director) {
        this.director = director;
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

    public List<ActividadDTO> getActividades() {
        return actividades;
    }

    public void setActividades(List<ActividadDTO> actividades) {
        this.actividades = actividades;
    }
}
