package Backend.DAO.dom.elementos;

import Backend.DAO.DBAcces;
import Backend.DAO.interfaces.elementos.ACTIVIDADDAO;
import Backend.Entidades.Actividad;
import Backend.Exceptions.CreateException;
import Backend.Exceptions.DeleteException;
import Backend.Exceptions.ReadException;

import java.util.List;

public class ActividadDAODB extends DBAcces implements ACTIVIDADDAO {
    @Override
    public void create(Actividad actividad) throws CreateException {

    }

    @Override
    public void delete(int id) throws DeleteException {

    }

    @Override
    public Actividad buscarByID(int id) throws ReadException {
        return null;
    }

    @Override
    public List<Actividad> obtenerActividades() throws ReadException {
        return List.of();
    }

    @Override
    public boolean validarTituloUnico(String titulo) throws ReadException {
        return false;
    }

    @Override
    public Actividad buscarByTitulo(String titulo) throws ReadException {
        return null;
    }
}
