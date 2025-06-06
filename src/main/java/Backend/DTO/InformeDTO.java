package Backend.DTO;

import java.time.LocalDate;

public class InformeDTO extends ItemDTO{
    private byte[] archivo;
    private LocalDate fecha;
    private int porcentajeAvance;

    public InformeDTO(int id, String titulo, String descripcion, byte[] archivo,LocalDate fecha, int porcentajeAvance) {
        super(id, titulo, descripcion);
        this.archivo = archivo;
        this.fecha = fecha;
        this.porcentajeAvance = porcentajeAvance;
    }

    public InformeDTO(String titulo, String descripcion, byte[] archivo,LocalDate fecha) {
        super(titulo, descripcion);
        this.archivo = archivo;
        this.fecha = fecha;
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

    public byte[] getArchivo() {
        return archivo;
    }

    public LocalDate getFecha() {
        return fecha;
    }
}
