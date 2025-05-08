package Backend.DAO.interfaces;

import java.sql.SQLException;
import java.util.List;
import Backend.Exceptions.RegisterExceptions;
import Backend.Exceptions.UserExceptions;
import Backend.Entidades.Usuario;

public interface USUARIODAO extends DAO {

    void create(Object objeto) throws RegisterExceptions;

    Usuario findOne(int id) throws UserExceptions, SQLException;

    List<Usuario> read() throws SQLException, UserExceptions;

    Usuario findByUsername(String username) throws SQLException, UserExceptions;

    Usuario findById(int id) throws SQLException, UserExceptions;

    boolean validarUsernameYEmailUnicos(String username, String email) throws SQLException, UserExceptions;

}

