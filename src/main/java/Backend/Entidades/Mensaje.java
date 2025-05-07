package Backend.Entidades;

import Backend.Exceptions.EmptyException;

public class Mensaje extends Item{
    Usuario emisor;
    Usuario receptor;
    String contenido;

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
