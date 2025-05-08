package Backend.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import Backend.Exceptions.ConnectionException;
import Backend.Exceptions.CreateException;
import Backend.Exceptions.DeleteException;
import Backend.Exceptions.ReadException;
import Backend.Exceptions.UpdateException;
import Backend.Entidades.Rol;

public class RolDAODB extends DBAcces implements ROLDAO {

    @Override
    public void create(Rol rol) throws CreateException {

        String sql = "INSERT INTO Roles (nombre,  activoRol) VALUES (?,  ?)";
        try {
            Connection conn = connect();

            if (conn == null) {
                throw new CreateException("No se pudo establecer conexión con la base de datos.");
            }

            PreparedStatement statement = conn.prepareStatement(sql);

            if (statement == null) {
                throw new CreateException("No se pudo preparar la sentencia SQL.");
            }

            statement.setString(1, rol.getNombre());
            statement.setBoolean(2, rol.isActivo());

            int rowsAffected = statement.executeUpdate();
            if (rowsAffected < 1) {
                throw new CreateException("No se ha podido insertar el rol.");
            }

            statement.close();
            disconnect();

        } catch (ConnectionException e) {
            throw new CreateException(e.getMessage());
        } catch(SQLException e){
            throw new CreateException("Error al crear el rol");
        }

    }

    @Override
    public void delete(int id) throws DeleteException {
        try{
            Connection conn = connect();
            PreparedStatement statement = conn.prepareStatement("DELETE FROM Roles WHERE idRol = ?");
            statement.setInt(1, id);
            statement.executeUpdate();
            disconnect();
        }catch(SQLException e){
            throw new DeleteException("Existen registros con este rol: " + e.getMessage());
        }catch (ConnectionException e) {
            throw new DeleteException(e.getMessage());
        }
    }

    @Override
    public List<Rol> read() throws ReadException {
        List<Rol> roles = new ArrayList<>();
        try {
            Connection conn = connect();
            String sql = "SELECT * FROM Roles";

            PreparedStatement statement = conn.prepareStatement(sql);

            ResultSet results = statement.executeQuery();

            while (results.next()) {
                Rol rol = new Rol(results.getInt("id"),
                        results.getString("nombre")
                );
                rol.setActivo(results.getBoolean("activoRol"));
                roles.add(rol);
            }

            disconnect();

            return roles;

        } catch(ConnectionException e){
            throw new ReadException(e.getMessage());
        }catch(SQLException e){
            throw new ReadException(e.getMessage());
        }
    }

    @Override
    public void update(Object objeto) throws UpdateException {
        try {

            if (!(objeto instanceof Rol)) {
                throw new UpdateException("El objeto debe ser de tipo Rol");
            }

            Rol rol = (Rol) objeto;
            Connection conn = connect();
            if (conn == null) {
                throw new UpdateException("No se pudo establecer la conexión a la base de datos.");
            }

            String sql = "UPDATE Roles SET nombre = ?, activoRol = ? WHERE idRol = ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            if (statement == null) {
                throw new UpdateException("No se pudo preparar la sentencia SQL.");
            }

            statement.setString(1, rol.getNombre());
            statement.setBoolean(3, rol.isActivo());
            statement.setInt(4, rol.getId());

            int rowsAffected = statement.executeUpdate();
            if (rowsAffected < 1) {
                throw new UpdateException("No se ha podido actualizar el rol.");
            }
            disconnect();
        } catch (ConnectionException e) {
            throw new UpdateException(e.getMessage());
        } catch(SQLException e){
            throw new UpdateException("Error al actualizar");
        }
    }

    @Override
    public Rol findOne(int id) throws ReadException {
        try {
            System.out.println(id);
            Rol rol = null;
            Connection conn = connect();

            if (conn == null) {
                throw new ReadException("No se pudo establecer la conexión.");
            }

            PreparedStatement statement = conn.prepareStatement("SELECT * FROM Roles WHERE id = ?");
            if (statement == null) {
                throw new ReadException("No se pudo preparar la consulta.");
            }

            statement.setInt(1, id);
            ResultSet result = statement.executeQuery();

            if (result.next()) {
                rol = new Rol();
                rol.setId(result.getInt("id"));
                rol.setNombre(result.getString("nombre"));
                rol.setActivo(result.getBoolean("activoRol"));
            }
            disconnect();

            return rol;
        } catch(ConnectionException e){
            throw new ReadException(e.getMessage());
        } catch(SQLException e){
            e.printStackTrace();
            throw new ReadException("Error al obtener el rol");
        }


    }
}

