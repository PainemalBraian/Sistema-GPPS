package Backend.DAO.interfaces.usuarios;

import Backend.Entidades.Docente;
import Backend.Exceptions.RegisterExceptions;
import Backend.Exceptions.UserException;

import java.util.List;

public interface DOCENTEDAO {
    void create(Docente docente) throws RegisterExceptions;

    boolean validarLegajoUnico(String legajo) throws UserException;

    List<Docente> obtenerDocentes() throws  UserException;

    Docente buscarByUsername(String username) throws  UserException;

    Docente buscarById(int id) throws  UserException;

}
