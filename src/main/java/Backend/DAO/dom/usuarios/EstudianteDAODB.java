package Backend.DAO.dom.usuarios;

import Backend.DAO.DBAcces;
import Backend.DAO.UsuarioDAODB;
import Backend.DAO.interfaces.usuarios.ESTUDIANTEDAO;
import Backend.Entidades.Estudiante;
import Backend.Entidades.Usuario;
import Backend.Exceptions.ConnectionException;
import Backend.Exceptions.RegisterExceptions;
import Backend.Exceptions.UserException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EstudianteDAODB extends DBAcces implements ESTUDIANTEDAO {
    UsuarioDAODB UsuarioDAODB=new UsuarioDAODB();

    @Override
    public void create(Estudiante estudiante) throws RegisterExceptions {
        try {
            Connection conn = connect();
            PreparedStatement statement = conn.prepareStatement(
                    "INSERT INTO Estudiantes(idUsuario, matricula, carrera) " +
                            "VALUES (?, ?, ?)"
            );
            statement.setInt(1, UsuarioDAODB.create(estudiante));
            statement.setString(2, estudiante.getMatricula());
            statement.setString(3, estudiante.getCarrera());

            statement.executeUpdate();
            statement.close();
            disconnect();
        } catch (SQLException e) {
            throw new RegisterExceptions("Error al crear y guardar el estudiante: " + e.getMessage());
        }catch(ConnectionException e){
            throw new RegisterExceptions(e.getMessage());
        }
    }

    @Override
    public Estudiante buscarByID(int id) throws UserException {
        try {
            Connection conn = connect();
            PreparedStatement statement = conn.prepareStatement(
                    "SELECT * FROM Estudiantes E " +
                            "JOIN Usuarios U ON E.idUsuario = U.idUsuario " +
                            "WHERE E.idEstudiante = ?"
            );
            statement.setInt(1, id);

            ResultSet result = statement.executeQuery();

            if (result.next()) {
                Usuario usuario = UsuarioDAODB.buscarByID(result.getInt("idUsuario"));
                Estudiante estudiante = new Estudiante(usuario, result.getString("matricula"),result.getString("carrera") );
                disconnect();
                statement.close();
                result.close();
                return estudiante;
            } else {
                disconnect();
                statement.close();
                result.close();
                throw new UserException("Estudiante no encontrado.");
            }
        }
        catch (SQLException e) {
            throw new UserException("Error al buscar el estudiante en la base de datos: " + e.getMessage());
        }
        catch(ConnectionException e){
            throw new UserException(e.getMessage());
        }
    }


    @Override
    public List<Estudiante> obtenerEstudiantes() throws UserException {
        try (Connection conn = connect();
             PreparedStatement statement = conn.prepareStatement("SELECT * FROM Estudiantes E JOIN Usuarios U ON U.idUsuario = E.idUsuario");
             ResultSet result = statement.executeQuery()) {

            List<Estudiante> estudiantes = new ArrayList<>();
            UsuarioDAODB usuarioDAODB = new UsuarioDAODB();
            while (result.next()) {
                Estudiante estudiante = new Estudiante(usuarioDAODB.buscarByID(result.getInt("idUsuario")), result.getString("matricula"),result.getString("carrera"));
                estudiantes.add(estudiante);
            }

            return estudiantes;
        } catch (SQLException e) {
            throw new UserException("Error al leer en la base de datos: " + e);
        } catch (UserException e) {
            throw new UserException(e.getMessage());
        }catch(ConnectionException e){
            throw new UserException(e.getMessage());
        }
    }

    @Override
    public Estudiante buscarByUsername(String username) throws UserException {
        try {
            Connection conn = connect();
            PreparedStatement statement = conn.prepareStatement(
                    "SELECT * FROM Estudiantes E " +
                            "JOIN Usuarios U ON E.idUsuario = U.idUsuario " +
                            "WHERE U.username = ?"
            );
            statement.setString(1, username);

            ResultSet result = statement.executeQuery();

            if (result.next()) {
                Usuario usuario = UsuarioDAODB.buscarByID(result.getInt("idUsuario"));
                Estudiante estudiante = new Estudiante(usuario, result.getString("matricula"),result.getString("carrera") );
                disconnect();
                statement.close();
                result.close();
                return estudiante;
            } else {
                disconnect();
                statement.close();
                result.close();
                throw new UserException("Estudiante no encontrado.");
            }
        }
        catch (SQLException e) {
            throw new UserException("Error al buscar el estudiante en la base de datos: " + e.getMessage());
        }
        catch(ConnectionException e){
            throw new UserException(e.getMessage());
        }
    }

    @Override
    public boolean validarMatriculaUnica(String matricula) throws UserException {
        try (Connection conn = connect()) {
            String sql = "SELECT COUNT(*) AS total FROM Estudiantes WHERE matricula = ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, matricula);

            ResultSet result = statement.executeQuery();

            if (result.next() && result.getInt("total") > 0) {
                throw new UserException("Matricula existente.");
            }
            statement.close();
            result.close();
            return true;
        }
        catch (SQLException e) {
            throw new UserException("Error al validar: " + e.getMessage());
        }
        catch (ConnectionException e) {
            throw new UserException(e.getMessage());
        }
    }
}
