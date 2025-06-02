package Backend.DAO.interfaces.usuarios;

import Backend.Entidades.Estudiante;
import Backend.Exceptions.RegisterExceptions;
import Backend.Exceptions.UserException;

import java.util.List;

public interface ESTUDIANTEDAO {
    void create(Estudiante estudiante) throws RegisterExceptions;

    Estudiante buscarByID(int id) throws UserException;

    List<Estudiante> obtenerEstudiantes() throws  UserException;

    Estudiante buscarByUsername(String username) throws  UserException;

    boolean validarMatriculaUnica(String matricula) throws  UserException;


//    Estudiante buscarById(int id) throws  UserExceptions;
}
