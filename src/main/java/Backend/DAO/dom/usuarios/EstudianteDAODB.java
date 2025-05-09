package Backend.DAO.dom.usuarios;

import Backend.DAO.DBAcces;
import Backend.DAO.UsuarioDAODB;
import Backend.DAO.interfaces.usuarios.ESTUDIANTEDAO;
import Backend.Entidades.Estudiante;
import Backend.Exceptions.ConnectionException;
import Backend.Exceptions.RegisterExceptions;
import Backend.Exceptions.UserExceptions;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class EstudianteDAODB extends DBAcces implements ESTUDIANTEDAO {
    UsuarioDAODB UsuarioDAODB=new UsuarioDAODB();

    @Override
    public void create(Object object) throws RegisterExceptions {
        Estudiante estudiante = (Estudiante) object;
        try {
            Connection conn = connect();
            PreparedStatement statement = conn.prepareStatement(
                    "INSERT INTO Estudiantes(idUsuario, matricula, carrera) " +
                            "VALUES (?, ?, ?)"
            );
            // Guardar en la base de datos
            statement.setInt(1, UsuarioDAODB.create(estudiante));
            statement.setString(2, estudiante.getMatricula());
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

    @Override
    public boolean validarMatriculaUnica(String matricula) throws SQLException, UserExceptions {
        try (Connection conn = connect()) {
            String sql = "SELECT COUNT(*) AS total FROM Estudiantes WHERE matricula = ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, matricula);

            ResultSet result = statement.executeQuery();

            if (result.next() && result.getInt("total") > 0) {
                throw new UserExceptions("Matricula existente.");
            }
            return true;
        } catch (ConnectionException e) {
            e.printStackTrace();
            throw new UserExceptions("Error al conectar con la base de datos: " + e.getMessage());
        }
    }
}
