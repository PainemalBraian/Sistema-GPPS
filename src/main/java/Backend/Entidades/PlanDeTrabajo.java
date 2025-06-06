package Backend.Entidades;

import Backend.Exceptions.EmptyException;

import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.isNull;

public class PlanDeTrabajo extends Item{
    private Docente docente;
    private TutorExterno tutor;
    private List<Actividad> actividades = new ArrayList<>();
    private Informe informeFinal;
    private boolean habilitado = true; // Direccion y Entidad deben aprobarlo. Pero se preestablece true para probar funcionamiento

    public PlanDeTrabajo(String titulo, String descripcion,Docente docente, TutorExterno tutor) throws EmptyException {
        super(titulo,descripcion);
        if (isNull(docente)) { throw new EmptyException("El docente debe existir."); }
        if (isNull(tutor)) { throw new EmptyException("El tutor debe existir."); }
        this.docente = docente;
        this.tutor = tutor;
    }

    public PlanDeTrabajo(int id, String titulo, String descripcion,Docente docente, TutorExterno tutor) throws EmptyException {
        super(id,titulo,descripcion);
        if (isNull(docente)) { throw new EmptyException("El docente debe existir."); }
        if (isNull(tutor)) { throw new EmptyException("El tutor debe existir."); }
        this.docente = docente;
        this.tutor = tutor;
    }

<<<<<<< HEAD
//////////////////////////////METHODs///////////////////////////////////////////////////
=======
    public PlanDeTrabajo() {

    }

    //////////////////////////////METHODs///////////////////////////////////////////////////
>>>>>>> 6c4b88f60d8f438e5a20427d61cee662601a4be7

    private int calcularHorasActividades(List<Actividad> actividades) {
        int horasTotales = 0;
        for (Actividad a : actividades){
            horasTotales += a.getDuracion();
        }
        return horasTotales;
    }

////////////////////////// GETTERS ////////////////////////////////////////////////////////////////////////
    public Docente getDocente() {
        return docente;
    }

    public TutorExterno getTutor() {
        return tutor;
    }

    public List<Actividad> getActividades() throws EmptyException {
        if (isNull(actividades))
            throw new EmptyException("La lista de actividades aún no existe.");
        return actividades;
    }

    public Informe getInformeFinal() throws EmptyException {
//        if (isNull(informeFinal))
//            throw new EmptyException("El informe final aún no fué cargado.");
        return informeFinal;
    }

    public boolean isHabilitado() {
        return habilitado;
    }

//////////////////////// SETTERS ////////////////////////////////////////////////////////////////

    public void setActividades(List<Actividad> actividades) throws EmptyException {
        if (isNull(actividades))
            throw new EmptyException("La lista de actividades debe existir.");
        if (calcularHorasActividades(actividades) > 100 )
            throw new EmptyException("Las horas de todas las actividades deben ser como máximo 100.");

        this.actividades = actividades;
    }

    public void setInformeFinal(Informe informeFinal) throws EmptyException {
        if (isNull(informeFinal))
            throw new EmptyException("El informe final debe existir.");
        this.informeFinal = informeFinal;
    }

    public void setHabilitado(boolean habilitado) {
        this.habilitado = habilitado;
    }

    public void setDocente(Docente docente) throws EmptyException {
        if (isNull(docente)) { throw new EmptyException("El docente debe existir."); }
        this.docente = docente;
    }

    public void setTutor(TutorExterno tutor) throws EmptyException {
        if (isNull(tutor)) { throw new EmptyException("El tutor debe existir."); }
        this.tutor = tutor;
    }
}
