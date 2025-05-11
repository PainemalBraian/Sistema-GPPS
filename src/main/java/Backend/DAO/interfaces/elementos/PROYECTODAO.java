package Backend.DAO.interfaces.elementos;

import Backend.Entidades.Proyecto;
import Backend.Exceptions.CreateException;
import Backend.Exceptions.DeleteException;
import Backend.Exceptions.ReadException;

import java.util.List;

public interface PROYECTODAO {

    void create(Proyecto proyecto) throws CreateException;

    void delete(int id) throws DeleteException;

    Proyecto buscarByID(int id) throws ReadException;

    List<Proyecto> obtenerProyectos() throws ReadException;

    boolean validarTituloUnico(String titulo) throws CreateException;

    Proyecto buscarByTitulo(String titulo) throws ReadException;
}
