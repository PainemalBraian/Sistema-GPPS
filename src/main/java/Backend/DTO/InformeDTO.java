package Backend.DTO;

import java.time.LocalDate;

public class InformeDTO extends ItemDTO{
    private String contenido;
    private LocalDate fecha;


    public InformeDTO(int id, String titulo, String descripcion, String contenido,LocalDate fecha) {
        super(id, titulo, descripcion);
        this.contenido = contenido;
        this.fecha = fecha;
    }

    public InformeDTO(String titulo, String descripcion, String contenido,LocalDate fecha) {
        super(titulo, descripcion);
        this.contenido = contenido;
        this.fecha = fecha;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public String getContenido() {
        return contenido;
    }

    public LocalDate getFecha() {
        return fecha;
    }
}
