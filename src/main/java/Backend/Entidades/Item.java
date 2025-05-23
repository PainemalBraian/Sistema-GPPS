package Backend.Entidades;

import Backend.Exceptions.EmptyException;

import static java.util.Objects.isNull;

public class Item {
    private int id;
    private String titulo;
    private String descripcion;

    public Item() {
    }

    public Item(int id, String titulo, String descripcion) throws EmptyException {
        if (isNull(titulo) || titulo.isEmpty()) {
            throw new EmptyException("El titulo no puede estar vacío.");
        }
        if (isNull(descripcion) || descripcion.isEmpty()) {
            throw new EmptyException("La descripcion no puede estar vacía.");
        }
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
    }

    //Sin id para manejo de creacion en bd, ya que lo genera AutoIncrement
    public Item(String titulo, String descripcion) throws EmptyException {
        if (isNull(titulo) || titulo.isEmpty()) {
            throw new EmptyException("El titulo no puede estar vacío.");
        }
        if (isNull(descripcion) || descripcion.isEmpty()) {
            throw new EmptyException("La descripcion no puede estar vacía.");
        }
        this.titulo = titulo;
        this.descripcion = descripcion;
    }

    public int getId() {
        return id;
    }

    public boolean isId(int id) {
        return id == this.id;
    }

//    public void setId(int id) {
//        this.id = id;
//    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
