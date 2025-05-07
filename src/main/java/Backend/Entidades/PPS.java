package Backend.Entidades;

import Backend.Exceptions.EmptyException;

import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.isNull;

public class PPS extends Item {
    private Proyecto proyecto;
    private Docente docente;
    private Estudiante estudiante;
    private List <Informe> informes = new ArrayList<>();

    public PPS() {
    }

    public PPS(int id, String titulo, String descripcion, Proyecto proyecto, Docente docente, Estudiante estudiante) throws EmptyException {
        super(id, titulo, descripcion);
        if (isNull(proyecto)) {
            throw new EmptyException("El proyecto debe existir.");
        }
        if (isNull(docente)) {
            throw new EmptyException("El docente debe existir.");
        }
        if (isNull(estudiante)) {
            throw new EmptyException("El estudiante debe existir.");
        }
        this.proyecto = proyecto;
        this.docente = docente;
        this.estudiante = estudiante;
    }
    //metodos
    public void cargarInforme(Informe informe) {
        informes.add(informe);
    }
    public void eliminarInforme(Informe informe) {
        informes.add(informe);
    }
}
