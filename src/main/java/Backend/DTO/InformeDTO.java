package Backend.DTO;

import java.time.LocalDate;

public class InformeDTO extends ItemDTO{
    private byte[] archivo;
    private LocalDate fecha;
    private int porcentajeAvance;
    private int calificacionDocente;
    private int calificacionTutor;

    public InformeDTO(int id, String titulo, String descripcion, byte[] archivo,LocalDate fecha, int porcentajeAvance) {
        super(id, titulo, descripcion);
        this.archivo = archivo;
        this.fecha = fecha;
        this.porcentajeAvance = porcentajeAvance;
    }

    public InformeDTO(int id, String titulo, String descripcion, byte[] archivo,LocalDate fecha) {
        super(id, titulo, descripcion);
        this.archivo = archivo;
        this.fecha = fecha;
    }

    public InformeDTO(int id, String titulo, String descripcion, byte[] archivo,LocalDate fecha, int porcentajeAvance, int calificacionDocente, int calificacionTutor) {
        super(id, titulo, descripcion);
        this.archivo = archivo;
        this.fecha = fecha;
        this.porcentajeAvance = porcentajeAvance;
        this.calificacionDocente = calificacionDocente;
        this.calificacionTutor = calificacionTutor;
    }

    public InformeDTO() {

    }

    public void setPorcentajeAvance(int porcentajeAvance) {
        this.porcentajeAvance = porcentajeAvance;
    }

    public int getPorcentajeAvance() {
        return porcentajeAvance;
    }

    public void setArchivo(byte[] archivo) {
        this.archivo = archivo;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public void setCalificacionTutor(int calificacionTutor) {
        this.calificacionTutor = calificacionTutor;
    }

    public void setCalificacionDocente(int calificacionDocente) {
        this.calificacionDocente = calificacionDocente;
    }

    public byte[] getArchivo() {
        return archivo;
    }

    public int getCalificacionDocente() {
        return calificacionDocente;
    }

    public int getCalificacionTutor() {
        return calificacionTutor;
    }

    public LocalDate getFecha() {
        return fecha;
    }
}
