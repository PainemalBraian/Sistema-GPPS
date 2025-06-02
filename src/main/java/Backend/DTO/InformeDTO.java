package Backend.DTO;

import java.time.LocalDate;

public class InformeDTO extends ItemDTO{
    private byte[] archivo;
    private LocalDate fecha;


    public InformeDTO(int id, String titulo, String descripcion, byte[] archivo,LocalDate fecha) {
        super(id, titulo, descripcion);
        this.archivo = archivo;
        this.fecha = fecha;
    }

    public InformeDTO(String titulo, String descripcion, byte[] archivo,LocalDate fecha) {
        super(titulo, descripcion);
        this.archivo = archivo;
        this.fecha = fecha;
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
