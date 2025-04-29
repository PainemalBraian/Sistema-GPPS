package Backend.DAO;

import java.util.List;

import Backend.Exceptions.CreateException;
import Backend.Exceptions.ReadException;
import Backend.Entidades.Rol;

public interface ROLDAO extends DAO {
    Rol findOne(int id) throws ReadException;
    void create(Rol rol) throws CreateException;
    List<Rol> read() throws ReadException;
}

