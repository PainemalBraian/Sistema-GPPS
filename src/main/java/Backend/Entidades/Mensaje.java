package Backend.Entidades;

import Backend.Exceptions.EmptyException;

import static java.util.Objects.isNull;

public class Mensaje extends Item{
    Usuario emisor;
    Usuario receptor;
    String contenido;

    public Mensaje(String titulo, String descripcion, Usuario emisor, Usuario receptor, String contenido) throws EmptyException {
        super(titulo, descripcion);
        if (isNull(emisor))
            throw new EmptyException("El emisor no puede estar vacio.");
        if (isNull(receptor))
            throw new EmptyException("El receptor no puede estar vacio.");
        if (isNull(contenido) || contenido.isEmpty())
            throw new EmptyException("El contenido del mensaje no puede estar vacio.");
        this.emisor = emisor;
        this.receptor = receptor;
        this.contenido = contenido;
    }

    public Mensaje() {
    }

    public Mensaje(int id, String titulo, String descripcion, Usuario emisor, Usuario receptor, String contenido) throws EmptyException {
        super(id, titulo, descripcion);
        this.emisor = emisor;
        this.receptor = receptor;
        this.contenido = contenido;
    }

    public String getEmisor() {
        return emisor.getUsername();
    }

    public String getReceptor() {
        return receptor.getUsername();
    }

    public String getContenido() {
        return contenido;
    }
}
