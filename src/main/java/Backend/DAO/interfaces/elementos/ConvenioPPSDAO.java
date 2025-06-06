package Backend.DAO.interfaces.elementos;

import Backend.Entidades.ConvenioPPS;
import Backend.Exceptions.CreateException;
import Backend.Exceptions.DeleteException;
import Backend.Exceptions.ReadException;

import java.util.List;

public interface ConvenioPPSDAO {

    void create(ConvenioPPS pps) throws CreateException;

    void delete(int id) throws DeleteException;

<<<<<<< HEAD
=======
    void update(ConvenioPPS convenio) throws CreateException;

>>>>>>> 6c4b88f60d8f438e5a20427d61cee662601a4be7
    ConvenioPPS buscarByID(int id) throws ReadException;

    List<ConvenioPPS> obtenerConvenios() throws ReadException;

    boolean validarTituloUnico(String titulo) throws CreateException;

    ConvenioPPS buscarByTitulo(String titulo) throws ReadException;

}
