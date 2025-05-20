package Backend.DAO.dom.elementos;

import Backend.DAO.DBAcces;
import Backend.DAO.interfaces.elementos.ConvenioPPSDAO;
import Backend.Entidades.ConvenioPPS;
import Backend.Exceptions.CreateException;
import Backend.Exceptions.DeleteException;
import Backend.Exceptions.ReadException;

import java.util.List;

public class ConvenioPPSDAODB extends DBAcces implements ConvenioPPSDAO {
    @Override
    public void create(ConvenioPPS pps) throws CreateException {

    }

    @Override
    public void delete(int id) throws DeleteException {

    }

    @Override
    public ConvenioPPS buscarByID(int id) throws ReadException {
        return null;
    }

    @Override
    public List<ConvenioPPS> obtenerConvenios() throws ReadException {
        return List.of();
    }

    @Override
    public boolean validarTituloUnico(String titulo) throws ReadException {
        return false;
    }

    @Override
    public ConvenioPPS buscarByTitulo(String titulo) throws ReadException {
        return null;
    }
}
