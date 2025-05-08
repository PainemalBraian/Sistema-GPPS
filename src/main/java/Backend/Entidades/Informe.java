package Backend.Entidades;

import Backend.Exceptions.EmptyException;

public class Informe extends Item{
    String contenido;
    private int calificacion = -1;

    public Informe() {
    }

    public Informe(int id, String titulo, String descripcion, String contenido) throws EmptyException {
        super(id, titulo, descripcion);
        this.contenido = contenido;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public int getCalificacion() {
        return calificacion;
    }

    public void evaluarInforme(int calificacion) {
        if (calificacion < 0 || calificacion > 10)
            throw new RuntimeException("La calificación debe ser entre 0 y 10");
        this.calificacion = calificacion;
    }
}
