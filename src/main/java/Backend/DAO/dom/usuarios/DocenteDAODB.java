package Backend.DAO.dom.usuarios;

import Backend.DAO.DBAcces;
import Backend.DAO.UsuarioDAODB;

import Backend.DAO.interfaces.usuarios.DOCENTEDAO;
import Backend.Entidades.Docente;
import Backend.Entidades.Estudiante;
import Backend.Entidades.Usuario;
import Backend.Exceptions.ConnectionException;
import Backend.Exceptions.ReadException;
import Backend.Exceptions.RegisterExceptions;
import Backend.Exceptions.UserException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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
            statement.setInt(1, UsuarioDAODB.create(docente.getUsuario()));
            statement.setString(2, docente.getLegajo()); //Falta relacion con colección estudiantes

            statement.executeUpdate();
            statement.close();
            disconnect();
        } catch (SQLException e) {
            throw new RegisterExceptions("Error al crear y guardar el estudiante: " + e.getMessage());
        }catch(ConnectionException e){
            throw new RegisterExceptions(e.getMessage());
        } catch (UserException e) {
            throw new RegisterExceptions(e.getMessage());
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
            statement.close();
            result.close();
            return true;
        } catch (ConnectionException e) {
            throw new UserException(e.getMessage());
        } catch (SQLException e) {
            throw new UserException("Error al validar: " + e.getMessage());
        }
    }

    @Override
    public List<Docente> obtenerDocentes() throws UserException {
        try (Connection conn = connect();
             PreparedStatement statement = conn.prepareStatement("SELECT * FROM Docentes D JOIN Usuarios U ON U.idUsuario = D.idUsuario");
             ResultSet result = statement.executeQuery()) {

            List<Docente> docentes = new ArrayList<>();
            UsuarioDAODB usuarioDAODB = new UsuarioDAODB();
            while (result.next()) {
                Docente docente = new Docente(usuarioDAODB.buscarByID(result.getInt("idUsuario")),result.getString("legajo"));
                // Cargar informes relacionados
                List<Estudiante> estudiantes = buscarEstudiantes(result.getInt("idUsuario"));
                docente.setEstudiantesAsignados(estudiantes);
                docentes.add(docente);
            }

            return docentes;
        } catch (SQLException e) {
            throw new UserException("Error al leer en la base de datos: " + e);
        } catch (UserException | ReadException | ConnectionException e) {
            throw new UserException(e.getMessage());
        }
    }

    @Override
    public Docente buscarByUsername(String username) throws UserException {
        try {
            Connection conn = connect();
            PreparedStatement statement = conn.prepareStatement(
                    "SELECT * FROM Docentes D " +
                            "JOIN Usuarios U ON D.idUsuario = U.idUsuario " +
                            "WHERE U.username = ?"
            );
            statement.setString(1, username);

            ResultSet result = statement.executeQuery();

            if (result.next()) {
                Usuario usuario = UsuarioDAODB.buscarByID(result.getInt("idUsuario"));
                Docente docente = new Docente(usuario, result.getString("legajo"));
                // Cargar informes relacionados
                List<Estudiante> estudiantes = buscarEstudiantes(result.getInt("idUsuario"));
                docente.setEstudiantesAsignados(estudiantes);

                statement.close();
                result.close();
                disconnect();
                return docente;
            } else {
                statement.close();
                result.close();
                disconnect();
                throw new UserException("Docente no encontrado.");
            }
        }
        catch (SQLException e) {
            throw new UserException("Error al buscar el docente en la base de datos: " + e.getMessage());
        }
        catch(ConnectionException | ReadException e){
            throw new UserException(e.getMessage());
        }
    }

    @Override
    public Docente buscarByID(int id) throws UserException {
        try {
            Connection conn = connect();
            PreparedStatement statement = conn.prepareStatement(
                    "SELECT * FROM Docentes D " +
                            "JOIN Usuarios U ON D.idUsuario = U.idUsuario " +
                            "WHERE D.idDocente = ?"
            );
            statement.setInt(1, id);

            ResultSet result = statement.executeQuery();
            if (result.next()) {
                Usuario usuario = UsuarioDAODB.buscarByID(result.getInt("idUsuario"));
                Docente docente = new Docente(usuario, result.getString("legajo"));

                // Cargar informes relacionados
                List<Estudiante> estudiantes = buscarEstudiantes(id);
                docente.setEstudiantesAsignados(estudiantes);


                statement.close();
                result.close();
                disconnect();
                return docente;
            } else {
                statement.close();
                result.close();
                disconnect();
                throw new UserException("docente no encontrado.");
            }
        }
        catch (UserException e) {
            throw new UserException("Error al buscar el docente: " + e.getMessage());
        }
        catch (SQLException e) {
            throw new UserException("Error al buscar el docente en la base de datos: " + e.getMessage());
        }
        catch(ConnectionException|ReadException e){
            throw new UserException(e.getMessage());
        }
    }

    public List<Estudiante> buscarEstudiantes(int idDocente) throws ReadException {
        List<Estudiante> estudiantes = new ArrayList<>();

        String sql = "SELECT idEstudiante FROM Relacion_Docente_Estudiantes WHERE idDocente = ?";

        try (Connection conn = connect();
             PreparedStatement statement = conn.prepareStatement(sql)) {

            statement.setInt(1, idDocente);

            try (ResultSet result = statement.executeQuery()) {
                EstudianteDAODB estudianteDAO = new EstudianteDAODB();

                while (result.next()) {
                    int idEstudiante = result.getInt("idEstudiante");
                    Estudiante estudiante = estudianteDAO.buscarByID(idEstudiante);
                    estudiantes.add(estudiante);
                }
            } catch (UserException e) {
                throw new ReadException(e.getMessage());
            }

            return estudiantes;

        } catch (SQLException e) {
            throw new ReadException("Error SQL al obtener los estudiantes del Docente." + e.getMessage());
        } catch (ConnectionException e) {
            throw new ReadException(e.getMessage());
        }
    }

    public Docente buscarByNombre(String nombre) throws UserException {
        try {
            Connection conn = connect();
            PreparedStatement statement = conn.prepareStatement(
                    "SELECT * FROM Docentes D " +
                            "JOIN Usuarios U ON D.idUsuario = U.idUsuario " +
                            "WHERE U.nombreCompleto = ?"
            );
            statement.setString(1, nombre);

            ResultSet result = statement.executeQuery();

            if (result.next()) {
                Usuario usuario = UsuarioDAODB.buscarByID(result.getInt("idUsuario"));
                Docente docente = new Docente(usuario, result.getString("legajo"));
                // Cargar informes relacionados
                List<Estudiante> estudiantes = buscarEstudiantes(result.getInt("idUsuario"));
                docente.setEstudiantesAsignados(estudiantes);

                statement.close();
                result.close();
                disconnect();
                return docente;
            } else {
                statement.close();
                result.close();
                disconnect();
                throw new UserException("Docente no encontrado.");
            }
        }
        catch (SQLException e) {
            throw new UserException("Error al buscar el docente en la base de datos: " + e.getMessage());
        }
        catch(ConnectionException | ReadException e){
            throw new UserException(e.getMessage());
        }
    }
}

