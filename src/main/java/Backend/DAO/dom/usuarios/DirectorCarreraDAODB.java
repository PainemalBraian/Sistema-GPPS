package Backend.DAO.dom.usuarios;

import Backend.DAO.DBAcces;
import Backend.DAO.UsuarioDAODB;
import Backend.DAO.interfaces.usuarios.DirectorCarreraDAO;
import Backend.Entidades.DirectorCarrera;
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

public class DirectorCarreraDAODB extends DBAcces implements DirectorCarreraDAO {
    @Override
    public void create(DirectorCarrera director) throws RegisterExceptions {
        try {
            UsuarioDAODB userdaoDB= new UsuarioDAODB(); // manera simplificada, ya que  director no tiene datos adicionales
            userdaoDB.create(director.getUsuario());
        }
        catch (UserException e) {
            throw new RegisterExceptions(e.getMessage());
        }
    }

    @Override
    public List<DirectorCarrera> obtenerDirectores() throws UserException {
        try (Connection conn = connect();
             PreparedStatement statement = conn.prepareStatement("SELECT * FROM DirectoresCarrera DC JOIN Usuarios U ON U.idUsuario = DC.idUsuario");
             ResultSet result = statement.executeQuery()) {

            List<DirectorCarrera> directores = new ArrayList<>();
            UsuarioDAODB usuarioDAODB = new UsuarioDAODB();
            while (result.next()) {
                DirectorCarrera director = new DirectorCarrera(usuarioDAODB.buscarById(result.getInt("idUsuario")));
                directores.add(director);
            }
            disconnect();
            return directores;
        } catch (SQLException e) {
            throw new UserException("Error al leer en la base de datos: " + e);
        } catch (UserException e) {
            throw new UserException(e.getMessage());
        }catch(ConnectionException e){
            throw new UserException("Error al conectar con la base de datos: " + e.getMessage());
        }
    }

    @Override
    public DirectorCarrera buscarByUsername(String username) throws UserException {
        try {
            Connection conn = connect();
            PreparedStatement statement = conn.prepareStatement(
                    "SELECT * FROM DirectoresCarrera DC " +
                            "JOIN Usuarios u ON u.idUsuairo = DC.idUsuario " +
                            "WHERE u.username = ?");
            statement.setString(1, username);

            ResultSet result = statement.executeQuery();

            if (result.next()) {
                UsuarioDAODB userDAO = new UsuarioDAODB();
                Usuario usuario = userDAO.buscarById(result.getInt("idUsuario"));

                return new DirectorCarrera(usuario);
            } else {
                throw new UserException("Director no encontrado.");
            }
        }
        catch (SQLException e) {
            throw new UserException("Error al buscar el director en la base de datos: " + e.getMessage());
        }
        catch(ConnectionException e){
            throw new UserException("Error al conectar con la base de datos: " + e.getMessage());
        }
    }

    @Override
    public DirectorCarrera buscarById(int id) throws UserException {
        try {
            Connection conn = connect();
            PreparedStatement statement = conn.prepareStatement(
                    "SELECT * FROM DirectoresCarrera DC " +
                            "JOIN Usuarios u ON u.idUsuario = DC.idUsuario " +
                            "WHERE DC.idDirector = ?");
            statement.setInt(1, id);

            ResultSet result = statement.executeQuery();

            if (result.next()) {
                UsuarioDAODB userDAO = new UsuarioDAODB();
                Usuario usuario = userDAO.buscarById(result.getInt("idUsuario"));

                return new DirectorCarrera(usuario);
            } else {
                throw new UserException("Director no encontrado.");
            }
        }
        catch (SQLException e) {
            throw new UserException("Error al buscar el director en la base de datos: " + e.getMessage());
        }
        catch(ConnectionException e){
            throw new UserException("Error al conectar con la base de datos: " + e.getMessage());
        }
    }
}
