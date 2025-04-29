package DAO;

import java.util.List;

import Exceptions.CreateException;
import Exceptions.ReadException;
import Entidades.Rol;

public interface ROLDAO extends DAO {
    Rol findOne(int id) throws ReadException;
    void create(Rol rol) throws CreateException;
    List<Rol> read() throws ReadException;
}

