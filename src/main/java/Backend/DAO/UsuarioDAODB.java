package Backend.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import Backend.DAO.interfaces.USUARIODAO;
import Backend.Exceptions.ConnectionException;
import Backend.Exceptions.DeleteException;
import Backend.Exceptions.RegisterExceptions;
import Backend.Exceptions.UpdateException;
import Backend.Exceptions.UserExceptions;
import Backend.Entidades.Rol;
import Backend.Entidades.Usuario;

public class UsuarioDAODB extends DBAcces implements USUARIODAO {

    @Override
    public void create(Object usuario) throws RegisterExceptions {
        Usuario user = (Usuario) usuario;
        try {
            Connection conn = connect();

            PreparedStatement statement = conn.prepareStatement(
                    "INSERT INTO Usuarios(nombre, apellido, email, username, password, idRol, activo, " +
                            "matricula, carrera, legajo, nombreEntidad, cuit, direccionEntidad) " +
                            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"
            );

            statement.setString(1, user.getNombre());
            statement.setString(2, "-"); // Apellido por defecto
            statement.setString(3, user.getEmail());
            statement.setString(4, user.getUsername());
            statement.setString(5, user.getContrasena());
            statement.setInt(6, user.getRol().getId());
            statement.setBoolean(7, user.isActivo());

            // Extras por tipo de usuario
            statement.setString(8, "user.getMatricula()");         // Estudiante
            statement.setString(9, "user.getCarrera()");           // Estudiante
            statement.setString(10, user.getLegajo());           // Docente
            statement.setString(11, user.getNombreEntidad());    // Entidad
            statement.setString(12, user.getCuit());             // Entidad
            statement.setString(13, user.getDireccionEntidad()); // Entidad


            statement.executeUpdate();  // Cambié executeQuery() por executeUpdate()
            statement.close();
            disconnect();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RegisterExceptions("Error al crear y guardar el usuario: " + e.getMessage());
        }catch(ConnectionException e){
            throw new RegisterExceptions(e.getMessage());
        }
    }


    @Override
    public List<Usuario> read() throws SQLException, UserExceptions{
        List<Usuario> usuarios = new ArrayList<>();
        Usuario user = null;
        try (Connection conn = connect();
             PreparedStatement statement = conn.prepareStatement("SELECT * FROM Usuarios U JOIN Roles R ON U.idRol = R.idRol");
             ResultSet result = statement.executeQuery()) {

            while (result.next()) {
                Rol rol = new Rol();
                rol.setId(result.getInt("idRol"));

                rol.setNombre(result.getString("R.nombre"));

                rol.setActivo(result.getBoolean("activo"));

                user = new Usuario(
                        result.getInt("idUsuario"),
                        result.getString("username"),
                        result.getString("password"),
                        result.getString("nombre"),
                        result.getString("email"),
                        rol
                );
                user.setActivo(result.getBoolean("activo"));
                usuarios.add(user);
            }
            disconnect();
        } catch (SQLException e) {
            throw new SQLException("Error: " + e);
        } catch (UserExceptions e) {
            throw new UserExceptions(e.getMessage());
        }catch(ConnectionException e){
            throw new UserExceptions(e.getMessage());
        }

        return usuarios;
    }

    @Override
    public void update(Object objeto) throws UpdateException {
        if (!(objeto instanceof Usuario)) {
            throw new IllegalArgumentException("El objeto no es de tipo Usuario");
        }

        Usuario user = (Usuario) objeto;
        String sql = "UPDATE Usuarios SET nombre = ?, password = ?, email = ?, activo = ?, idRol = ? WHERE idUsuario = ?";
        try {
            Connection conn = connect();
            PreparedStatement statement = conn.prepareStatement(sql);

            statement.setString(1, user.getNombre());
            statement.setString(2, user.getContrasena());
            statement.setString(3, user.getEmail());
            statement.setBoolean(4, user.isActivo());
            statement.setInt(5, user.getRol().getId());
            statement.setInt(6, user.getIdUsuario());

            int rowsAffected = statement.executeUpdate();
            if (rowsAffected < 1) {
                throw new UpdateException("No se han podido actualizar los datos.");
            }

        } catch(ConnectionException e){
            throw new UpdateException(e.getMessage());
        }catch(SQLException e){
            throw new UpdateException("Error al actualizar");
        }

    }




    @Override
    public Usuario findOne(int id) throws UserExceptions, SQLException {
        Usuario user = null;
        try (Connection conn = connect();
             PreparedStatement statement = conn.prepareStatement(
                     "SELECT * FROM Usuarios U JOIN Roles R ON U.idRol = R.idRol WHERE U.idUsuario = ?"
             )) {
            statement.setInt(1, id);
            ResultSet result = statement.executeQuery();

            if (result.next()) {
                // Crear instancia de Usuario
                user = new Usuario(result.getInt("idUsuario"),
                        result.getString("username"),
                        result.getString("password"),
                        result.getString("nombre"),
                        result.getString("email"),
                        null
                );
                Rol rol = new Rol();
                rol.setId(result.getInt("idRol"));
                rol.setNombre(result.getString("R.nombre"));
                user.setRol(rol);
            }
            disconnect();
        } catch (SQLException e) {
            throw new UserExceptions(e.getMessage());
        }catch(UserExceptions e){
            throw new UserExceptions(e.getMessage());
        }catch(ConnectionException e){
            throw new UserExceptions(e.getMessage());
        }
        return user;
    }

    @Override
    public void delete(int id) throws DeleteException {
        String sql = "DELETE FROM Usuarios WHERE idUsuario = ?";

        try (Connection conn = connect();
             PreparedStatement statement = conn.prepareStatement(sql)) {

            statement.setInt(1, id);
            int rowsAffected = statement.executeUpdate();

            if (rowsAffected < 1) {
                throw new DeleteException("No se ha podido eliminar el usuario");
            }
        }catch(ConnectionException e){
            throw new DeleteException(e.getMessage());
        }catch(SQLException e){
            throw new DeleteException("Error al eliminar"+e.getMessage());
        }
    }

    public void delete(String username) throws SQLException {
        String sql = "DELETE FROM Usuarios WHERE idUsuario = ?";

        try (Connection conn = connect();
             PreparedStatement statement = conn.prepareStatement(sql)) {

            statement.setString(1, username);
            int rowsAffected = statement.executeUpdate();

            if (rowsAffected < 1) {
                throw new SQLException("No se ha podido eliminar el usuario");
            }
        }catch(ConnectionException e){
            throw new SQLException(e.getMessage());
        }
    }
    @Override
    public Usuario findByUsername(String username) throws  UserExceptions {
        try {
            Connection conn = connect();
            PreparedStatement statement = conn.prepareStatement("SELECT * FROM Usuarios u JOIN Roles ON u.idRol = Roles.idRol WHERE username = ?");
            statement.setString(1, username);

            ResultSet result = statement.executeQuery();

            if (result.next()) {

                Rol rol = new Rol(result.getInt("idRol"), result.getString("Roles.nombre"));

                rol.setActivo(result.getBoolean("activo"));

                Usuario usuario = new Usuario(result.getInt("idUsuario"), result.getString("username"),
                        result.getString("password"), result.getString("nombre"),
                        result.getString("email"), rol);

                usuario.setActivo(result.getBoolean("activo"));
                return usuario;
            } else {
                throw new UserExceptions("Usuario no encontrado.");
            }

        } catch(ConnectionException e){
            throw new UserExceptions(e.getMessage());
        } catch(SQLException e){
            throw new UserExceptions(e.getMessage());
        }
    }

    @Override
    public Usuario findById(int id) throws SQLException, UserExceptions {
        try {
            Connection conn = connect();
            PreparedStatement statement = conn.prepareStatement("SELECT * FROM Usuarios u JOIN Roles ON u.idRol = Roles.idRol WHERE idUsuario = ?");
            statement.setInt(1, id);

            ResultSet result = statement.executeQuery();

            if (result.next()) {

                Rol rol = new Rol(result.getInt("idRol"), result.getString("Roles.nombre"));

                rol.setActivo(result.getBoolean("activo"));

                Usuario usuario = new Usuario(result.getInt("idUsuario"), result.getString("username"),
                        result.getString("password"), result.getString("nombre"),
                        result.getString("email"), rol);
                usuario.activar();
                return usuario;
            } else {
                throw new UserExceptions("Usuario no encontrado.");
            }

        } catch(ConnectionException e){
            throw new UserExceptions(e.getMessage());
        }
    }
    public boolean validarUsernameYEmailUnicos(String username, String email) throws SQLException, UserExceptions {
        try (Connection conn = connect()) {
            String sql = "SELECT COUNT(*) AS total FROM Usuarios WHERE username = ? OR email = ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, username);
            statement.setString(2, email);

            ResultSet result = statement.executeQuery();

            if (result.next() && result.getInt("total") > 0) {
                throw new UserExceptions("El username o el email ya están registrados.");
            }
            return true;
        } catch (ConnectionException e) {
            e.printStackTrace();
            throw new UserExceptions("Error al conectar con la base de datos: " + e.getMessage());
        }
    }
}
