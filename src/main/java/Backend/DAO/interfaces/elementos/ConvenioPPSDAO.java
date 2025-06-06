package Backend.DAO.interfaces.elementos;

import Backend.Entidades.ConvenioPPS;
import Backend.Exceptions.CreateException;
import Backend.Exceptions.DeleteException;
import Backend.Exceptions.ReadException;

import java.util.List;

public interface ConvenioPPSDAO {

    void create(ConvenioPPS pps) throws CreateException;

    void delete(int id) throws DeleteException;

    void update(ConvenioPPS convenio) throws CreateException;

    ConvenioPPS buscarByID(int id) throws ReadException;

    List<ConvenioPPS> obtenerConvenios() throws ReadException;

    boolean validarTituloUnico(String titulo) throws CreateException;

    ConvenioPPS buscarByTitulo(String titulo) throws ReadException;

}
