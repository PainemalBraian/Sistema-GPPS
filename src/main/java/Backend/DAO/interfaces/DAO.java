package Backend.DAO.interfaces;

import Backend.Exceptions.ConnectionException;
import Backend.Exceptions.DeleteException;
import Backend.Exceptions.UpdateException;

public interface  DAO {
    void delete(int id) throws DeleteException;
    void update(Object objeto) throws UpdateException, ConnectionException;
}
