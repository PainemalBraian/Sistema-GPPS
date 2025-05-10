package Backend.Entidades;

import Backend.Exceptions.EmptyException;

import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.isNull;

public class Proyecto extends Item{
    private String areaDeInteres;
    private String ubicación;
    private String objetivos;
    private String requisitos;
    private TutorExterno encargado;

    public Proyecto() {
    }

    public Proyecto(int id, String titulo, String descripcion, String areaDeInteres, String ubicación, String objetivos, String requisitos, TutorExterno encargado) throws EmptyException {
        super(id, titulo, descripcion);
        if (isNull(areaDeInteres) || areaDeInteres.isEmpty()) {
            throw new EmptyException("El área de interés no puede estar vacío.");
        }
        if (isNull(ubicación) || ubicación.isEmpty()) {
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
        this.ubicación = ubicación;
        this.objetivos = objetivos;
        this.requisitos = requisitos;
        this.encargado = encargado;
    }

    public String getAreaDeInteres() {
        return areaDeInteres;
    }

    public String getUbicación() {
        return ubicación;
    }

    public String getObjetivos() {
        return objetivos;
    }

    public String getRequisitos() {
        return requisitos;
    }

    public TutorExterno getEncargado() {
        return encargado;
    }
}
