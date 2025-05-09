package Backend.DAO.interfaces;

import Backend.Entidades.Estudiante;
import Backend.Exceptions.RegisterExceptions;
import Backend.Exceptions.UserExceptions;

import java.sql.SQLException;
import java.util.List;

public interface ESTUDIANTEDAO {
    void create(Object objeto) throws RegisterExceptions;

    Estudiante buscarEstudiante(int id) throws UserExceptions, SQLException;

    List<Estudiante> read() throws SQLException, UserExceptions;

    Estudiante buscarByUsername(String username) throws SQLException, UserExceptions;

//    Estudiante buscarById(int id) throws SQLException, UserExceptions;
}
