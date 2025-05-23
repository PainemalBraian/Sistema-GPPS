package Backend.Entidades;

import Backend.Exceptions.EmptyException;

import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.isNull;

public class ConvenioPPS extends Item {
    private DirectorCarrera director;
    private boolean habilitado = false;
    private EntidadColaborativa entidad;
    private Proyecto proyecto;
    private Docente docente;
    private Estudiante estudiante;
    private List <Actividad> actividades = new ArrayList<>();
    private PlanDeTrabajo plan; // Conlleva docente, estudiante y actividades

    public ConvenioPPS() {
    }

    public ConvenioPPS(String titulo, String descripcion, Proyecto proyecto, Docente docente, Estudiante estudiante,
                       DirectorCarrera director, EntidadColaborativa entidad, List<Actividad> actividades) throws EmptyException {
        super(titulo, descripcion);
        if (isNull(proyecto)) { throw new EmptyException("El proyecto debe existir."); }
        if (isNull(docente)) { throw new EmptyException("El docente debe existir."); }
        if (isNull(estudiante)) { throw new EmptyException("El estudiante debe existir."); }
        if (isNull(director)) { throw new EmptyException("El director debe existir."); }
        if (isNull(entidad)) { throw new EmptyException("La entidad debe existir."); }
//        if (isNull(actividades)) { throw new EmptyException("La lista de actividades debe existir."); }
//        if (calcularHorasActividades(actividades) != 100 ) { throw new EmptyException("Las horas de todas las actividades deben sumar 100."); }
//          LLevar esas dos validaciones en un metodo de instancia que se utilice especificamente para cargar las actividades, inicialmente puede estar vacio

        this.proyecto = proyecto;
        this.docente = docente;
        this.estudiante = estudiante;
        this.director = director;
        this.entidad = entidad;
        if (actividades != null)
            setActividades(actividades);
    }

    public ConvenioPPS(int id, String titulo, String descripcion, Proyecto proyecto, Docente docente, Estudiante estudiante,
                       DirectorCarrera director, EntidadColaborativa entidad, List<Actividad> actividades) throws EmptyException {
        super(id, titulo, descripcion);
        if (isNull(proyecto)) { throw new EmptyException("El proyecto debe existir."); }
        if (isNull(docente)) { throw new EmptyException("El docente debe existir."); }
        if (isNull(estudiante)) { throw new EmptyException("El estudiante debe existir."); }
        if (isNull(director)) { throw new EmptyException("El director debe existir."); }
        if (isNull(entidad)) { throw new EmptyException("La entidad debe existir."); }
//        if (isNull(actividades)) { throw new EmptyException("La lista de actividades debe existir."); }
//        if (calcularHorasActividades(actividades) != 100 ) { throw new EmptyException("Las horas de todas las actividades deben sumar 100."); }
//          LLevar esas dos validaciones en un metodo de instancia que se utilice especificamente para cargar las actividades, inicialmente puede estar vacio

        this.proyecto = proyecto;
        this.docente = docente;
        this.estudiante = estudiante;
        this.director = director;
        this.entidad = entidad;
        this.actividades = actividades;
    }

    private int calcularHorasActividades(List<Actividad> actividades) {
        int horasTotales = 0;
        for (Actividad a : actividades){
            horasTotales += a.getDuracion();
        }
        return horasTotales;
    }

//////////////////////////////METHODs///////////////////////////////////////////////////

    public void cargarInforme(Actividad actividad) {
        actividades.add(actividad);
    }
    public void eliminarInforme(Actividad actividad) {
        actividades.add(actividad);
    }
    // Adaptar al uso de Director de carrera, con habilitación mediante DAO
    public void habilitarConvenio() throws EmptyException {
        if (isNull(actividades)) {
            throw new EmptyException("La lista de actividades deben existir."); }
        if (calcularHorasActividades(actividades) != 100 ) {
            throw new EmptyException("Las horas de todas las actividades deben sumar 100."); }
        this.habilitado=true;
        proyecto.setHabilitado(true);
    } // Si está habilitado este convenio, automaticamente se habilita el proyecto.



////////////////////////// GETTERS ////////////////////////////////////////////////////////////////////////
    public DirectorCarrera getDirector() { return director; }

    public Proyecto getProyecto() { return proyecto; }

    public Docente getDocente() { return docente; }

    public Estudiante getEstudiante() { return estudiante; }

    public List<Actividad> getActividades() { return actividades; }

    public boolean isHabilitado() { return habilitado; }

    public EntidadColaborativa getEntidad() {
        return entidad;
    }

    //////////////////////// SETTERS ////////////////////////////////////////////////////////////////
    public void setDirector(DirectorCarrera director) throws EmptyException {
        if (isNull(director)) { throw new EmptyException("El director debe existir."); }
        this.director = director;
    }

    public void setProyecto(Proyecto proyecto) throws EmptyException {
        if (isNull(proyecto)) { throw new EmptyException("El proyecto debe existir."); }
        this.proyecto = proyecto;
    }

    public void setDocente(Docente docente) throws EmptyException {
        if (isNull(docente)) { throw new EmptyException("El docente debe existir."); }
        this.docente = docente;
    }

    public void setEstudiante(Estudiante estudiante) throws EmptyException {
        if (isNull(estudiante)) {
            throw new EmptyException("El estudiante debe existir.");
        }
        this.estudiante = estudiante;
    }

    public void setActividades(List<Actividad> actividades) throws EmptyException {
        if (isNull(actividades)) {
            throw new EmptyException("La lista de actividades debe existir.");
        }
        if (calcularHorasActividades(actividades) > 100) {
            throw new EmptyException("Las horas totales de actividades acordadas son: 100hs");
        }

        this.actividades = actividades;
    }

    public void setHabilitado(boolean habilitado) {
        this.habilitado = habilitado;
    }

    public void setEntidad(EntidadColaborativa entidad) {
        this.entidad = entidad;
    }

}