package Backend.DAO.interfaces;

import java.sql.SQLException;
import java.util.List;
import Backend.Exceptions.RegisterExceptions;
import Backend.Exceptions.UserException;
import Backend.Entidades.Usuario;

public interface USUARIODAO extends DAO {

    int create(Object objeto) throws RegisterExceptions;

    List<Usuario> read() throws  UserException;

    Usuario findByUsername(String username) throws  UserException;

    Usuario findById(int id) throws  UserException;

    boolean validarUsernameYEmailUnicos(String username, String email) throws  UserException;

}

