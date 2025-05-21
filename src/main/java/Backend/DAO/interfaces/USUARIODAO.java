package Backend.DAO.interfaces;

import java.util.List;
import Backend.Exceptions.RegisterExceptions;
import Backend.Exceptions.UserException;
import Backend.Entidades.Usuario;

public interface USUARIODAO extends DAO {

    int create(Usuario usuario) throws RegisterExceptions;

    List<Usuario> obtenerUsuarios() throws  UserException;

    Usuario buscarByUsername(String username) throws  UserException;

    Usuario buscarById(int id) throws  UserException;

    boolean validarUsernameYEmailUnicos(String username, String email) throws  UserException;

}

