package Backend.DAO.interfaces.usuarios;

import Backend.Entidades.TutorExterno;
import Backend.Exceptions.ReadException;
import Backend.Exceptions.RegisterExceptions;
import Backend.Exceptions.UserException;

import java.util.List;

public interface TUTOREXTERNODAO {
    void create(TutorExterno tutor) throws RegisterExceptions;

    List<TutorExterno> obtenerTutores() throws ReadException;

    TutorExterno buscarByUsername(String username) throws  UserException;

    TutorExterno buscarByID(int id) throws  UserException;

    boolean validarExistenciaEntidad(String nombreEntidad) throws  UserException;
}
