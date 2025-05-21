package Backend.DAO.interfaces.usuarios;

import Backend.Entidades.DirectorCarrera;
import Backend.Exceptions.RegisterExceptions;
import Backend.Exceptions.UserException;

import java.util.List;

public interface DirectorCarreraDAO {
    void create(DirectorCarrera director) throws RegisterExceptions;

    List<DirectorCarrera> obtenerDirectores() throws  UserException;

    DirectorCarrera buscarByUsername(String username) throws  UserException;

    DirectorCarrera buscarById(int id) throws  UserException;
}
