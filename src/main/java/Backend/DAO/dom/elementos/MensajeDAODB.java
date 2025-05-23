package Backend.DAO.dom.elementos;

import Backend.DAO.DBAcces;
import Backend.DAO.interfaces.elementos.MENSAJEDAO;
import Backend.Entidades.Mensaje;
import Backend.Exceptions.CreateException;
import Backend.Exceptions.DeleteException;
import Backend.Exceptions.ReadException;

import java.util.List;

public class MensajeDAODB extends DBAcces implements MENSAJEDAO {
    @Override
    public void create(Mensaje mensaje) throws CreateException {

    }

    @Override
    public void delete(int id) throws DeleteException {

    }

    @Override
    public Mensaje buscarByID(int id) throws ReadException {
        return null;
    }

    @Override
    public List<Mensaje> obtenerMensajes() throws ReadException {
        return List.of();
    }

    @Override
    public Mensaje buscarByTitulo(String titulo) throws ReadException {
        return null;
    }
}
