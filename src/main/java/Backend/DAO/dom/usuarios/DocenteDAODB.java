package Backend.DAO.dom.usuarios;

import Backend.DAO.DBAcces;
import Backend.DAO.UsuarioDAODB;
import Backend.DAO.interfaces.usuarios.DOCENTEDAO;
import Backend.Entidades.Docente;
import Backend.Entidades.Usuario;
import Backend.Exceptions.ConnectionException;
import Backend.Exceptions.RegisterExceptions;
import Backend.Exceptions.UserException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class DocenteDAODB extends DBAcces implements DOCENTEDAO {
    UsuarioDAODB UsuarioDAODB=new UsuarioDAODB();

    @Override
    public void create(Docente docente) throws RegisterExceptions {
        try {
            Connection conn = connect();
            PreparedStatement statement = conn.prepareStatement(
                    "INSERT INTO Docentes(idUsuario, legajo) " +
                            "VALUES (?, ?)"
            );
            // Guardar en la base de datos
            statement.setInt(1, UsuarioDAODB.create(docente));
            statement.setString(2, docente.getLegajo()); //Falta relacion con colección estudiantes

            statement.executeUpdate();
            statement.close();
            disconnect();
        } catch (SQLException e) {
            throw new RegisterExceptions("Error al crear y guardar el estudiante: " + e.getMessage());
        }catch(ConnectionException e){
            throw new RegisterExceptions("Error al conectar con la base de datos: " + e.getMessage());
        }
    }

    @Override
    public boolean validarLegajoUnico(String legajo) throws UserException {
        try (Connection conn = connect()) {
            String sql = "SELECT COUNT(*) AS total FROM Docentes WHERE legajo = ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, legajo);

            ResultSet result = statement.executeQuery();

            if (result.next() && result.getInt("total") > 0) {
                throw new UserException("Legajo existente.");
            }
            return true;
        } catch (ConnectionException e) {
            e.printStackTrace();
            throw new UserException("Error al conectar con la base de datos: " + e.getMessage());
        } catch (SQLException e) {
            throw new UserException("Error al validar: " + e.getMessage());
        }
    }

    @Override
    public List<Usuario> read() throws UserException {
        return List.of();
    }

    @Override
    public Usuario findByUsername(String username) throws UserException {
        return null;
    }

    @Override
    public Usuario findById(int id) throws UserException {
        return null;
    }
}

