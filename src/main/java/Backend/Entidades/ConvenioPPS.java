package Backend.Entidades;

import Backend.Exceptions.EmptyException;

import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.isNull;

public class ConvenioPPS extends Item {
<<<<<<< HEAD
    private boolean habilitado = false;
=======
    private boolean habilitado;
>>>>>>> 6c4b88f60d8f438e5a20427d61cee662601a4be7
    private Proyecto proyecto;
    private Estudiante estudiante;
    private EntidadColaborativa entidad;
    private PlanDeTrabajo plan; // Conlleva docente, tutor y actividades

    public ConvenioPPS() {
    }

    // Carga/Creacion
    public ConvenioPPS(String titulo, String descripcion, Proyecto proyecto, Estudiante estudiante,
                       EntidadColaborativa entidad,PlanDeTrabajo plan) throws EmptyException {
        super(titulo, descripcion);
        if (isNull(proyecto)) { throw new EmptyException("El proyecto debe existir."); }
        if (isNull(estudiante)) { throw new EmptyException("El estudiante debe existir."); }
        if (isNull(entidad)) { throw new EmptyException("La entidad debe existir."); }
<<<<<<< HEAD
        if (isNull(plan)) { throw new EmptyException("El plan de trabajo debe existir."); }
=======
//        if (isNull(plan)) { throw new EmptyException("El plan de trabajo debe existir."); }
>>>>>>> 6c4b88f60d8f438e5a20427d61cee662601a4be7

        this.proyecto = proyecto;
        this.estudiante = estudiante;
        this.entidad = entidad;
        this.plan = plan;
    }

    // Busquedas
    public ConvenioPPS(int id, String titulo, String descripcion, Proyecto proyecto, Estudiante estudiante,
                       EntidadColaborativa entidad, PlanDeTrabajo plan) throws EmptyException {
        super(id, titulo, descripcion);
        if (isNull(proyecto)) { throw new EmptyException("El proyecto debe existir."); }
        if (isNull(estudiante)) { throw new EmptyException("El estudiante debe existir."); }
        if (isNull(entidad)) { throw new EmptyException("La entidad debe existir."); }

        this.proyecto = proyecto;
        this.estudiante = estudiante;
        this.entidad = entidad;
        this.plan = plan;
    }

//////////////////////////////METHODs///////////////////////////////////////////////////

    public void cargarInforme() {}

    public void eliminarInforme() {}

    // Adaptar al uso de Director de carrera, con habilitación mediante DAO
    public void habilitarConvenio() throws EmptyException {
        this.habilitado=true;
        this.proyecto.setHabilitado(true);
        this.plan.setHabilitado(true);
    } // Si está habilitado este convenio, automaticamente se habilita el proyecto.

    public void setActividades(List<Actividad> actividades) throws EmptyException {
        this.plan.setActividades(actividades);
    }

////////////////////////// GETTERS ////////////////////////////////////////////////////////////////////////
    public Proyecto getProyecto() { return proyecto; }

    public Docente getDocente() { return plan.getDocente(); }

    public Estudiante getEstudiante() { return estudiante; }

    public boolean isHabilitado() { return habilitado; }

    public EntidadColaborativa getEntidad() { return entidad; }

    public PlanDeTrabajo getPlan() {
        return plan;
    }

    //////////////////////// SETTERS ////////////////////////////////////////////////////////////////

    public void setProyecto(Proyecto proyecto) throws EmptyException {
        if (isNull(proyecto)) { throw new EmptyException("El proyecto debe existir."); }
        this.proyecto = proyecto;
    }

    public void setEstudiante(Estudiante estudiante) throws EmptyException {
        if (isNull(estudiante)) {throw new EmptyException("El estudiante debe existir.");}
        this.estudiante = estudiante;
    }

    public void setEntidad(EntidadColaborativa entidad) throws EmptyException {
        if (isNull(entidad)) { throw new EmptyException("La entidad debe existir."); }
        this.entidad = entidad;
    }

<<<<<<< HEAD
=======
    public void setPlan(PlanDeTrabajo plan) throws EmptyException {
        if (isNull(plan)) { throw new EmptyException("El plan de trabajo debe existir."); }
        this.plan = plan;
    }

>>>>>>> 6c4b88f60d8f438e5a20427d61cee662601a4be7
    public void setHabilitado(boolean habilitado) {
        this.habilitado = habilitado;
        this.plan.setHabilitado(habilitado);
    }

<<<<<<< HEAD
    public void setPlan(PlanDeTrabajo plan) {
        this.plan = plan;
    }
=======
>>>>>>> 6c4b88f60d8f438e5a20427d61cee662601a4be7
}