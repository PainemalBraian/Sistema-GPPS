package DAO;

import Exceptions.ConnectionException;
import Exceptions.DeleteException;
import Exceptions.UpdateException;

public interface  DAO {
    void delete(int id) throws DeleteException;
    void update(Object objeto) throws UpdateException, ConnectionException;
}
