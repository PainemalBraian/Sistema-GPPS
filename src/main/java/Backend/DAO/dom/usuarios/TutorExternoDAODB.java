package Backend.DAO.dom.usuarios;

import Backend.DAO.DBAcces;
import Backend.DAO.UsuarioDAODB;
import Backend.DAO.interfaces.usuarios.TUTOREXTERNODAO;
import Backend.Entidades.TutorExterno;
import Backend.Entidades.Usuario;
import Backend.Exceptions.ConnectionException;
import Backend.Exceptions.RegisterExceptions;
import Backend.Exceptions.UserException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class TutorExternoDAODB extends DBAcces implements TUTOREXTERNODAO {
    UsuarioDAODB UsuarioDAODB=new UsuarioDAODB();

    @Override
    public void create(TutorExterno tutor) throws RegisterExceptions {
        try {
            Connection conn = connect();
            PreparedStatement statement = conn.prepareStatement(
                    "INSERT INTO TutoresExternos(idTutor, nombreEntidadColaborativa) " +
                            "VALUES (?, ?)"
            );
            // Guardar en la base de datos
            statement.setInt(1, UsuarioDAODB.create(tutor));
            statement.setString(2, tutor.getNombreEntidadColaborativa());

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

    @Override
    public boolean validarExistenciaEntidad(String nombreEntidad) throws UserException {
        try (Connection conn = connect()) {
            String sql = "SELECT COUNT(*) AS total FROM EntidadesColaborativas WHERE LOWER(nombreEntidad) = LOWER(?)"; //Lower para indistinto a Mayúsculas o minúsculas
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, nombreEntidad);

            ResultSet result = statement.executeQuery();

            if (result.next() && result.getInt("total") == 0) {
                throw new UserException("La entidad insertada no existe en el sistema.");
            }
            return true;
        }
        catch (SQLException e) {
            throw new UserException("Error al validar: " + e.getMessage());
        }
        catch (ConnectionException e) {
            throw new UserException("Error al conectar con la base de datos: " + e.getMessage());
        }
    }
}
