package Backend.DAO.interfaces.usuarios;

import Backend.Entidades.TutorExterno;
import Backend.Entidades.Usuario;
import Backend.Exceptions.RegisterExceptions;
import Backend.Exceptions.UserException;

import java.util.List;

public interface TUTOREXTERNODAO {
    void create(TutorExterno tutor) throws RegisterExceptions;

    List<Usuario> read() throws UserException;

    Usuario findByUsername(String username) throws  UserException;

    Usuario findById(int id) throws  UserException;

    boolean validarExistenciaEntidad(String nombreEntidad) throws  UserException;
}
