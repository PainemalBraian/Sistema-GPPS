package Backend.DAO.dom.usuarios;

import Backend.DAO.DBAcces;
import Backend.DAO.UsuarioDAODB;
import Backend.DAO.interfaces.usuarios.TUTOREXTERNODAO;
import Backend.Entidades.Proyecto;
import Backend.Entidades.TutorExterno;
import Backend.Entidades.Usuario;
import Backend.Exceptions.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TutorExternoDAODB extends DBAcces implements TUTOREXTERNODAO {
    UsuarioDAODB UsuarioDAODB=new UsuarioDAODB();

    @Override
    public void create(TutorExterno tutor) throws RegisterExceptions {
        try {
            Connection conn = connect();
            PreparedStatement statement = conn.prepareStatement(
                    "INSERT INTO TutoresExternos(idUsuario, nombreEntidadColaborativa) " +
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
    public List<TutorExterno> obtenerTutores() throws ReadException {
        List<TutorExterno> tutores = new ArrayList<>();

        try (Connection conn = connect();
             PreparedStatement statement = conn.prepareStatement("SELECT * FROM TutoresExternos");
             ResultSet result = statement.executeQuery()) {

            while (result.next()) {
                TutorExterno tutor = new TutorExterno(
                        UsuarioDAODB.buscarById(result.getInt("idUsuario")), // Obtiene el usuario
                        result.getString("requisitos")
                );
//                tutor.setProyectosAsignados(buscarProyectosAsignados()); Implementar usando tabla de relacion_Tutores_Proyectos.
                tutores.add(tutor);
            }

            return tutores;
        } catch (SQLException e) {
            throw new ReadException("Error al obtener los datos de los proyectos.");
        } catch (ConnectionException e) {
            throw new ReadException(e.getMessage());
        } catch (UserException e) {
            throw new ReadException("Error al construir el proyecto: " + e.getMessage());
        }
    }

    @Override
    public TutorExterno buscarByUsername(String username) throws UserException {
        try {
            Connection conn = connect();
            PreparedStatement statement = conn.prepareStatement(
                    "SELECT * FROM TutoresExternos TE " +
                            "JOIN Usuarios U ON TE.idUsuario = U.idUsuario " +
                            "WHERE U.username = ?");
            statement.setString(1, username);

            ResultSet tutor = statement.executeQuery();

            if (tutor.next()) {
                Usuario usuario = UsuarioDAODB.buscarById(tutor.getInt("idUsuario"));
                TutorExterno tutorExt = new TutorExterno(usuario, tutor.getString("nombreEntidadColaborativa") );
                tutorExt.setIdTutor(tutor.getInt("idTutor"));
                return tutorExt;
            } else {
                throw new UserException("Tutor no encontrado.");
            }
        }
        catch (SQLException e) {
            throw new UserException("Error al buscar el usuario en la base de datos: " + e.getMessage());
        }
        catch(ConnectionException e){
            throw new UserException("Error al conectar con la base de datos: " + e.getMessage());
        }
    }

    @Override
    public TutorExterno buscarByID(int id) throws UserException {
        try {
            Connection conn = connect();
            PreparedStatement statement = conn.prepareStatement(
                    "SELECT * FROM TutoresExternos TE " +
                            "JOIN Usuarios U ON TE.idUsuario = U.idUsuario " +
                            "WHERE TE.idTutor = ?"
            );
            statement.setInt(1, id);

            ResultSet tutor = statement.executeQuery();

            if (tutor.next()) {
                Usuario usuario = UsuarioDAODB.buscarById(tutor.getInt("idUsuario"));
                TutorExterno tutorExt = new TutorExterno(usuario, tutor.getString("nombreEntidadColaborativa") );
                return tutorExt;
            } else {
                throw new UserException("Tutor no encontrado.");
            }
        }
        catch (SQLException e) {
            throw new UserException("Error al buscar el usuario en la base de datos: " + e.getMessage());
        }
        catch(ConnectionException e){
            throw new UserException("Error al conectar con la base de datos: " + e.getMessage());
        }
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
