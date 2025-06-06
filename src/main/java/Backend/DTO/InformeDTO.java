package Backend.DTO;

import java.time.LocalDate;

public class InformeDTO extends ItemDTO{
    private byte[] archivo;
    private LocalDate fecha;
<<<<<<< HEAD


    public InformeDTO(int id, String titulo, String descripcion, byte[] archivo,LocalDate fecha) {
        super(id, titulo, descripcion);
        this.archivo = archivo;
        this.fecha = fecha;
=======
    private int porcentajeAvance;

    public InformeDTO(int id, String titulo, String descripcion, byte[] archivo,LocalDate fecha, int porcentajeAvance) {
        super(id, titulo, descripcion);
        this.archivo = archivo;
        this.fecha = fecha;
        this.porcentajeAvance = porcentajeAvance;
>>>>>>> 6c4b88f60d8f438e5a20427d61cee662601a4be7
    }

    public InformeDTO(String titulo, String descripcion, byte[] archivo,LocalDate fecha) {
        super(titulo, descripcion);
        this.archivo = archivo;
        this.fecha = fecha;
    }

    public InformeDTO() {

    }

<<<<<<< HEAD
=======
    public void setPorcentajeAvance(int porcentajeAvance) {
        this.porcentajeAvance = porcentajeAvance;
    }

    public int getPorcentajeAvance() {
        return porcentajeAvance;
    }

>>>>>>> 6c4b88f60d8f438e5a20427d61cee662601a4be7
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
