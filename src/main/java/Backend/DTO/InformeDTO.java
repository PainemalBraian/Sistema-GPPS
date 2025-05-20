package Backend.DTO;

import Backend.Exceptions.EmptyException;

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
}
