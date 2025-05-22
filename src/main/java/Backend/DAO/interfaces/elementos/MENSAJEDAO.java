package Backend.DAO.interfaces.elementos;

import Backend.Entidades.Mensaje;
import Backend.Exceptions.CreateException;
import Backend.Exceptions.DeleteException;
import Backend.Exceptions.ReadException;

import java.util.List;

public interface MENSAJEDAO {
    void create(Mensaje mensaje) throws CreateException;

    void delete(int id) throws DeleteException;

    Mensaje buscarByID(int id) throws ReadException;

    List<Mensaje> obtenerMensajes() throws ReadException;

    Mensaje buscarByTitulo(String titulo) throws ReadException;

}
