package Backend.DAO;

import Backend.DAO.interfaces.ESTUDIANTEDAO;
import Backend.Entidades.Estudiante;
import Backend.Exceptions.ConnectionException;
import Backend.Exceptions.RegisterExceptions;
import Backend.Exceptions.UserExceptions;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class EstudianteDAODB extends DBAcces implements ESTUDIANTEDAO {

    @Override
    public void create(Object object) throws RegisterExceptions {
        Estudiante estudiante = (Estudiante) object;
        try {
            Connection conn = connect();
            PreparedStatement statement = conn.prepareStatement(
                    "INSERT INTO Estudiantes(idEstudiante, matricula, carrera) " +
                            "VALUES (?, ?, ?, ?)"
            );
            statement.setInt(1, estudiante.getIdUsuario());
            statement.setString(2, String.valueOf(estudiante.getMatricula()));
            statement.setString(3, estudiante.getCarrera());

            statement.executeUpdate();  // Cambié executeQuery() por executeUpdate()
            statement.close();
            disconnect();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RegisterExceptions("Error al crear y guardar el estudiante: " + e.getMessage());
        }catch(ConnectionException e){
            throw new RegisterExceptions(e.getMessage());
        }
    }

    @Override
    public Estudiante buscarEstudiante(int id) throws UserExceptions, SQLException {
        return null;
    }

    @Override
    public List<Estudiante> read() throws SQLException, UserExceptions {
        return List.of();
    }

    @Override
    public Estudiante buscarByUsername(String username) throws SQLException, UserExceptions {
        return null;
    }
}
