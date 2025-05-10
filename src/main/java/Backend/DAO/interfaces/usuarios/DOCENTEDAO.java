package Backend.DAO.interfaces.usuarios;

import Backend.Entidades.Docente;
import Backend.Entidades.Usuario;
import Backend.Exceptions.RegisterExceptions;
import Backend.Exceptions.UserException;

import java.util.List;

public interface DOCENTEDAO {
    void create(Docente docente) throws RegisterExceptions;

    boolean validarLegajoUnico(String legajo) throws UserException;

    List<Usuario> read() throws  UserException;

    Usuario findByUsername(String username) throws  UserException;

    Usuario findById(int id) throws  UserException;

}
