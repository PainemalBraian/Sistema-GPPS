package Backend.DTO;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ActividadDTO extends ItemDTO{
    private List<InformeDTO> informes = new ArrayList<>();
    private float duracion;
    private LocalDate fechaFin;
    private Boolean calificacion; // null por defecto, indicando "no cargada".

    public ActividadDTO(int id, String titulo, String descripcion, LocalDate fechaFin, float duracion) {
        super(id, titulo, descripcion);
        this.fechaFin = fechaFin;
        this.duracion = duracion;
    }

    public List<InformeDTO> getInformes() {
        return informes;
    }

    public void setInformes(List<InformeDTO> informes) {
        this.informes = informes;
    }

    public float getDuracion() {
        return duracion;
    }

    public void setDuracion(float duracion) {
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
}
