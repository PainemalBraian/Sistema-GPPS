package Backend.DAO.interfaces.elementos;

import Backend.Entidades.Actividad;
import Backend.Exceptions.CreateException;
import Backend.Exceptions.DeleteException;
import Backend.Exceptions.ReadException;

import java.util.List;

public interface ACTIVIDADDAO {
    void create(Actividad actividad) throws CreateException;

    void delete(int id) throws DeleteException;

    Actividad buscarByID(int id) throws ReadException;

    List<Actividad> obtenerActividades() throws ReadException;

    boolean validarTituloUnico(String titulo) throws ReadException;

    Actividad buscarByTitulo(String titulo) throws ReadException;

}
