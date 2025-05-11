package Backend.DAO.dom.usuarios;

import Backend.DAO.DBAcces;
import Backend.DAO.UsuarioDAODB;
import Backend.DAO.interfaces.usuarios.ENTIDADCOLABORATIVADAO;
import Backend.Entidades.EntidadColaborativa;
import Backend.Entidades.Usuario;
import Backend.Exceptions.ConnectionException;
import Backend.Exceptions.RegisterExceptions;
import Backend.Exceptions.UserException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class EntidadColaborativaDAODB extends DBAcces implements ENTIDADCOLABORATIVADAO {
    UsuarioDAODB UsuarioDAODB=new UsuarioDAODB();

    @Override
    public void create(EntidadColaborativa entidad) throws RegisterExceptions {
        try {
            Connection conn = connect();
            PreparedStatement statement = conn.prepareStatement(
                    "INSERT INTO EntidadesColaborativas(idUsuario, nombreEntidad, cuit, direccionEntidad) " +
                            "VALUES (?, ?, ?, ?)"
            );
            // Guardar en la base de datos
            statement.setInt(1, UsuarioDAODB.create(entidad));
            statement.setString(2, entidad.getNombreEntidad());
            statement.setString(3, entidad.getCuit());
            statement.setString(4, entidad.getDireccionEntidad()); //Falta relacion con colección Proyectos

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
    public boolean validarDatosUnicos(String nombreEntidad, String cuit, String direccionEntidad) throws UserException {
        try (Connection conn = connect()) {
            // Verificar nombreEntidad
            String sqlNombre = "SELECT COUNT(*) AS total FROM EntidadesColaborativas WHERE nombreEntidad = ?";
            PreparedStatement statementNombre = conn.prepareStatement(sqlNombre);
            statementNombre.setString(1, nombreEntidad);
            ResultSet resultNombre = statementNombre.executeQuery();
            if (resultNombre.next() && resultNombre.getInt("total") > 0) {
                throw new UserException("El nombre de la entidad ya está registrado.");
            }

            // Verificar cuit
            String sqlCuit = "SELECT COUNT(*) AS total FROM EntidadesColaborativas WHERE cuit = ?";
            PreparedStatement statementCuit = conn.prepareStatement(sqlCuit);
            statementCuit.setString(1, cuit);
            ResultSet resultCuit = statementCuit.executeQuery();
            if (resultCuit.next() && resultCuit.getInt("total") > 0) {
                throw new UserException("El CUIT ya está registrado.");
            }

            // Verificar direccionEntidad
            String sqlDireccion = "SELECT COUNT(*) AS total FROM EntidadesColaborativas WHERE direccionEntidad = ?";
            PreparedStatement statementDireccion = conn.prepareStatement(sqlDireccion);
            statementDireccion.setString(1, direccionEntidad);
            ResultSet resultDireccion = statementDireccion.executeQuery();
            if (resultDireccion.next() && resultDireccion.getInt("total") > 0) {
                throw new UserException("La dirección de la entidad ya está registrada.");
            }

            return true;
        } catch (ConnectionException e) {
            throw new UserException("Error al conectar con la base de datos: " + e.getMessage());
        } catch (SQLException e) {
            throw new UserException("Error al validar: " + e.getMessage());
        }
    }

}
