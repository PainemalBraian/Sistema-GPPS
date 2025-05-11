package Backend.Entidades;

import Backend.Exceptions.EmptyException;

import static java.util.Objects.isNull;

public class Proyecto extends Item{
    private String areaDeInteres;
    private String ubicacion;
    private String objetivos;
    private String requisitos;
    private TutorExterno tutorEncargado;

    public Proyecto() {
    }

    public Proyecto(int id, String titulo, String descripcion, String areaDeInteres, String ubicacion, String objetivos, String requisitos, TutorExterno encargado) throws EmptyException {
        super(id, titulo, descripcion);
        if (isNull(areaDeInteres) || areaDeInteres.isEmpty()) {
            throw new EmptyException("El área de interés no puede estar vacío.");
        }
        if (isNull(ubicacion) || ubicacion.isEmpty()) {
            throw new EmptyException("La ubicación no puede estar vacía.");
        }
        if (isNull(objetivos) || objetivos.isEmpty()) {
            throw new EmptyException("Los objetivos no pueden estar vacios.");
        }
        if (isNull(requisitos) || requisitos.isEmpty()) {
            throw new EmptyException("Los requisitos no pueden estar vacios.");
        }
        if (isNull(encargado)) {
            throw new EmptyException("El encargado debe existir.");
        }
        this.areaDeInteres = areaDeInteres;
        this.ubicacion = ubicacion;
        this.objetivos = objetivos;
        this.requisitos = requisitos;
        this.tutorEncargado = encargado;
    }

    //Sin id para manejo de creacion en bd, ya que lo genera AutoIncrement
    public Proyecto(String titulo, String descripcion, String areaDeInteres, String ubicacion, String objetivos, String requisitos, TutorExterno encargado) throws EmptyException {
        super(titulo, descripcion);
        if (isNull(areaDeInteres) || areaDeInteres.isEmpty()) {
            throw new EmptyException("El área de interés no puede estar vacío.");
        }
        if (isNull(ubicacion) || ubicacion.isEmpty()) {
            throw new EmptyException("La ubicación no puede estar vacía.");
        }
        if (isNull(objetivos) || objetivos.isEmpty()) {
            throw new EmptyException("Los objetivos no pueden estar vacios.");
        }
        if (isNull(requisitos) || requisitos.isEmpty()) {
            throw new EmptyException("Los requisitos no pueden estar vacios.");
        }
        if (isNull(encargado)) {
            throw new EmptyException("El encargado debe existir.");
        }
        this.areaDeInteres = areaDeInteres;
        this.ubicacion = ubicacion;
        this.objetivos = objetivos;
        this.requisitos = requisitos;
        this.tutorEncargado = encargado;
    }

    public String getAreaDeInteres() {
        return areaDeInteres;
    }

    public void setAreaDeInteres(String areaDeInteres) {
        this.areaDeInteres = areaDeInteres;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public String getObjetivos() {
        return objetivos;
    }

    public void setObjetivos(String objetivos) {
        this.objetivos = objetivos;
    }

    public String getRequisitos() {
        return requisitos;
    }

    public void setRequisitos(String requisitos) {
        this.requisitos = requisitos;
    }

    public TutorExterno getTutorEncargado() {
        return tutorEncargado;
    }

    public void setTutorEncargado(TutorExterno tutorEncargado) {
        this.tutorEncargado = tutorEncargado;
    }
}
