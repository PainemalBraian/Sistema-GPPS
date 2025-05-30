package Backend.Entidades;

import Backend.Exceptions.EmptyException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.isNull;

public class Actividad extends Item{
    private List<Informe> informes = new ArrayList<>();
    private int duracion;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private Boolean calificacion; // null por defecto, indicando "no cargada".

    public Actividad() {
    }

    public Actividad(String titulo, String descripcion, LocalDate fechaFin, int duracion, LocalDate fechaInicio) throws EmptyException {
        super(titulo, descripcion);
        if (isNull(fechaFin)) {throw new EmptyException("Se debe establecer una fecha de fin.");}
        if (isNull(duracion)) {throw new EmptyException("La actividad debe contener una duración estimada.");}
        if (duracion < 0) {throw new EmptyException("La duración debe ser positiva.");}

        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.duracion = duracion;
    }

    public Actividad(int id, String titulo, String descripcion, LocalDate fechaFin, int duracion, LocalDate fechaInicio) throws EmptyException {
        super(id, titulo, descripcion);
        if (isNull(fechaFin)) {throw new EmptyException("Se debe establecer una fecha de fin.");}
        if (isNull(duracion)) {throw new EmptyException("La actividad debe contener una duración estimada.");}
        if (duracion < 0) {throw new EmptyException("La duración debe ser positiva.");}

        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.duracion = duracion;
    }


//////////////// METHODS //////////////////////////////////////////////////////
    public void cargarInforme(Informe informe) throws EmptyException {
        if (isNull(informe)) {throw new EmptyException("El informe debe existir.");}

        informes.add(informe);
    }

    public String estadoCalificacion() throws EmptyException {
        if (calificacion == null) {throw new EmptyException("La calificación aún no está cargada.");}
        return calificacion ? "Aprobado" : "Desaprobado";
    }

//////////////// GETTERS //////////////////////////////////////////////////////
    public Boolean getCalificacion() throws EmptyException {
        if (calificacion == null) {throw new EmptyException("Calificación no cargada");}

        return calificacion;
    }

    public List<Informe> getInformes() throws EmptyException {
        if (informes == null) {throw new EmptyException("No existen informes cargados.");}

        return this.informes;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public int getDuracion() {
        return duracion;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    //////////////// SETTERS //////////////////////////////////////////////////////
    public void setInformes(List <Informe> informes) throws EmptyException {
        if (informes == null) {throw new EmptyException("La lista de informes no debe estar vacia.");}
        this.informes = informes;
    }

    public void setFechaFin(LocalDate fechaFin) throws EmptyException {
        if (fechaFin == null) {throw new EmptyException("La fecha no se estableció correctamente.");}

        this.fechaFin = fechaFin;
    }

    public void setCalificacion(Boolean calificacion) throws EmptyException {
        this.calificacion = calificacion;
    }

    public void setDuracion(int duracion) throws EmptyException {
        if (duracion < 0) {throw new EmptyException("La duración debe ser positiva.");}
        this.duracion = duracion;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

}
