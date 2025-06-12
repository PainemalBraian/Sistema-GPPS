package Backend.DTO;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ActividadDTO extends ItemDTO{
    private List<InformeDTO> informes = new ArrayList<>();
    private int duracion;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private int porcentajeAvance;
    private Boolean calificacion; // null por defecto, indicando "no cargada".

    public ActividadDTO(int id, String titulo, String descripcion, LocalDate fechaFin, int duracion,LocalDate fechaInicio) {
        super(id, titulo, descripcion);
        this.fechaFin = fechaFin;
        this.duracion = duracion;
        this.fechaInicio=fechaInicio;
    }
    public ActividadDTO(int id, String titulo, String descripcion, LocalDate fechaFin, int duracion,LocalDate fechaInicio, int porcentajeAvance) {
        super(id, titulo, descripcion);
        this.fechaFin = fechaFin;
        this.duracion = duracion;
        this.fechaInicio=fechaInicio;
        this.porcentajeAvance = porcentajeAvance;
    }

    public List<InformeDTO> getInformes() {
        return informes;
    }

    public void setInformes(List<InformeDTO> informes) {
        this.informes = informes;
    }

    public int getDuracion() {
        return duracion;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setDuracion(int duracion) {
        this.duracion = duracion;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }

    public Boolean getCalificacion() {
        return calificacion;
    }

    public void setCalificacion(Boolean calificacion) {
        this.calificacion = calificacion;
    }

    public void addInforme(InformeDTO informe) {
        informes.add(informe);
    }

    public int getPorcentajeAvance() {
        return porcentajeAvance;
    }

    public void setPorcentajeAvance(int porcentajeAvance) {
        this.porcentajeAvance = porcentajeAvance;
    }
}
