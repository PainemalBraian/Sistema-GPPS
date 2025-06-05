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
import Backend.Exceptions.UserException;
import Backend.Entidades.Rol;
import Backend.Entidades.Usuario;

public class UsuarioDAODB extends DBAcces implements USUARIODAO {

    @Override
    public int create(Usuario usuario) throws RegisterExceptions {
        try {
            Connection conn = connect();
            PreparedStatement statement = conn.prepareStatement(
                    "INSERT INTO Usuarios(nombreCompleto, email, username, password, idRol, activo) " +
                            "VALUES (?, ?, ?, ?, ?, ?)",
                    PreparedStatement.RETURN_GENERATED_KEYS //Retorna el id AA generado por la db
            );

            statement.setString(1, usuario.getNombre());
            statement.setString(2, usuario.getEmail());
            statement.setString(3, usuario.getUsername());
            statement.setString(4, usuario.getContrasena());
            statement.setInt(5, usuario.getRol().getId());
            statement.setBoolean(6, usuario.isActivo());

            statement.executeUpdate();

            // Obtener el ID generado
            ResultSet result = statement.getGeneratedKeys();
            int idGenerado = 0;
            if (result.next()) {
                idGenerado = result.getInt(1);
            }
            result.close();
            statement.close();
            disconnect();
            return idGenerado;
        } catch (SQLException e) {
            throw new RegisterExceptions("Error al crear y guardar el usuario: " + e.getMessage());
        }catch(ConnectionException e){
            throw new RegisterExceptions( e.getMessage());
        }
    }

    @Override
    public List<Usuario> obtenerUsuarios() throws UserException {
        List<Usuario> usuarios = new ArrayList<>();
        Usuario user = null;
        try (Connection conn = connect();
             PreparedStatement statement = conn.prepareStatement("SELECT * FROM Usuarios U JOIN Roles R ON U.idRol = R.idRol");
             ResultSet result = statement.executeQuery()) {

            while (result.next()) {
                Rol rol = new Rol();
                rol.setId(result.getInt("R.idRol"));

                rol.setNombre(result.getString("R.nombre"));

                rol.setActivo(result.getBoolean("R.activo"));

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
        } catch (SQLException e) {
            throw new UserException("Error al leer en la base de datos: " + e);
        } catch (UserException e) {
            throw new UserException(e.getMessage());
        }catch(ConnectionException e){
            throw new UserException(e.getMessage());
        }

        return usuarios;
    }

    @Override
    public void update(Object objeto) throws UpdateException {
        if (!(objeto instanceof Usuario)) {
            throw new IllegalArgumentException("El objeto no es de tipo Usuario");
        }

        Usuario user = (Usuario) objeto;
        String sql = "UPDATE Usuarios SET nombreCompleto = ?, password = ?, email = ?, activo = ?, idRol = ?, username = ? WHERE idUsuario = ?";
        try {
            Connection conn = connect();
            PreparedStatement statement = conn.prepareStatement(sql);

            statement.setString(1, user.getNombre());
            statement.setString(2, user.getContrasena());
            statement.setString(3, user.getEmail());
            statement.setBoolean(4, user.isActivo());
            statement.setInt(5, user.getRol().getId());
            statement.setString(6, user.getUsername());
            statement.setInt(7, user.getIdUsuario());

            int rowsAffected = statement.executeUpdate();
            if (rowsAffected < 1) {
                throw new UpdateException("No se han podido actualizar los datos.");
            }
        } catch(ConnectionException e){
            throw new UpdateException("Error al conectar con la base de datos: " + e.getMessage());
        }catch(SQLException e){
            throw new UpdateException("Error al actualizar" + e.getMessage());
        }

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
            throw new DeleteException("Error al eliminar: "+e.getMessage());
        }
    }

//    public void delete(String username) throws DeleteException {
//        String sql = "DELETE FROM Usuarios WHERE idUsuario = ?";
//
//        try (Connection conn = connect();
//             PreparedStatement statement = conn.prepareStatement(sql)) {
//
//            statement.setString(1, username);
//            int rowsAffected = statement.executeUpdate();
//
//            if (rowsAffected < 1) {
//                throw new DeleteException("No se ha podido eliminar el usuario");
//            }
//        }catch(ConnectionException e){
//          throw new DeleteException("Error al conectar con la base de datos: " + e.getMessage());
//        } catch (SQLException e) {
//            throw new DeleteException("No se ha podido eliminar el usuario: " + e.getMessage());
//        }
//    }

    @Override
    public Usuario buscarByUsername(String username) throws UserException {
        try {
            Connection conn = connect();
            PreparedStatement statement = conn.prepareStatement("SELECT * FROM Usuarios u JOIN Roles R ON u.idRol = R.idRol WHERE u.username = ?");
            statement.setString(1, username);

            ResultSet result = statement.executeQuery();

            if (result.next()) {

                Rol rol = new Rol(result.getInt("R.idRol"), result.getString("R.nombre"));

                rol.setActivo(result.getBoolean("R.activo"));

                Usuario usuario = new Usuario(result.getInt("idUsuario"), result.getString("username"),
                        result.getString("password"), result.getString("nombreCompleto"),
                        result.getString("email"), rol);

                usuario.setActivo(result.getBoolean("activo"));
                disconnect();
                statement.close();
                result.close();
                return usuario;
            } else {
                disconnect();
                statement.close();
                result.close();
                throw new UserException("Usuario no encontrado.");
            }
        } catch(ConnectionException e){
            throw new UserException(e.getMessage());
        } catch(SQLException e){
            throw new UserException("Error al buscar el usuario en la base de datos: " + e.getMessage());
        }
    }

    @Override
    public Usuario buscarByID(int id) throws UserException {
        try {
            Connection conn = connect();
            PreparedStatement statement = conn.prepareStatement(
                    "SELECT * FROM Usuarios u " +
                    "JOIN Roles r ON u.idRol = r.idRol " +
                    "WHERE u.idUsuario = ?");
            statement.setInt(1, id);

            ResultSet result = statement.executeQuery();

            if (result.next()) {

                Rol rol = new Rol(result.getInt("idRol"), result.getString("r.nombre"));
                rol.setActivo(result.getBoolean("activo"));

                Usuario usuario = new Usuario(result.getInt("idUsuario"), result.getString("username"),
                        result.getString("password"), result.getString("nombreCompleto"),
                        result.getString("email"), rol);
                usuario.activar();

                disconnect();
                statement.close();
                result.close();
                return usuario;
            } else {
                throw new UserException("Usuario no encontrado.");
            }
        }
        catch (SQLException e) {
            throw new UserException("Error al buscar el usuario en la base de datos: " + e.getMessage());
        }
        catch(ConnectionException e){
            throw new UserException(e.getMessage());
        }
    }

    public boolean validarUsernameYEmailUnicos(String username, String email) throws UserException {
        try (Connection conn = connect()) {
            String sql = "SELECT COUNT(*) AS total FROM Usuarios WHERE username = ? OR email = ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, username);
            statement.setString(2, email);

            ResultSet result = statement.executeQuery();

            if (result.next() && result.getInt("total") > 0) {
                throw new UserException("El username o el email ya están registrados.");
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
