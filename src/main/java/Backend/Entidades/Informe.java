package Backend.Entidades;

import Backend.Exceptions.EmptyException;

import java.time.LocalDate;

import static java.util.Objects.isNull;

public class Informe extends Item{
    private String contenido;
    private LocalDate fecha = LocalDate.now();

    public Informe(String contenido) {
        this.contenido = contenido;
        fecha = LocalDate.now();
    }

    public Informe(int id, String titulo, String descripcion, String contenido) throws EmptyException {
        super(id, titulo, descripcion);
        this.contenido = contenido;
        fecha = LocalDate.now();
    }

    public Informe(String titulo, String descripcion, String contenido) throws EmptyException {
        super(titulo, descripcion);
        if (isNull(contenido) || contenido.isEmpty())
            throw new EmptyException("El contenido del informe no puede estar vacio.");
        this.contenido = contenido;
        fecha = LocalDate.now();
    }


    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void actualizarFecha(LocalDate fecha) throws EmptyException {
        if (isNull(fecha))
            throw new EmptyException("La fecha no puede estar vacia.");
        this.fecha = fecha;
    }
}
