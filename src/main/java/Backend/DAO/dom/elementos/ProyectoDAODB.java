package Backend.DAO.dom.elementos;

import Backend.DAO.DBAcces;
import Backend.DAO.dom.usuarios.TutorExternoDAODB;
import Backend.DAO.interfaces.elementos.PROYECTODAO;
import Backend.Entidades.Proyecto;
import Backend.Entidades.TutorExterno;
import Backend.Exceptions.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProyectoDAODB extends DBAcces implements PROYECTODAO {
    TutorExternoDAODB tutorExternoDAODB = new TutorExternoDAODB();
    @Override
    public void create(Proyecto proyecto) throws CreateException {
        try (Connection conn = connect();
             PreparedStatement statement = conn.prepareStatement(
                     "INSERT INTO Proyectos(titulo, descripcion, areaDeInteres, ubicacion, objetivos, requisitos,idTutor, habilitado) VALUES (?, ?, ?, ?, ?, ?, ?,?)"
             )) {

            statement.setString(1, proyecto.getTitulo());
            statement.setString(2, proyecto.getDescripcion());
            statement.setString(3, proyecto.getAreaDeInteres());
            statement.setString(4, proyecto.getUbicacion());
            statement.setString(5, proyecto.getObjetivos());
            statement.setString(6, proyecto.getRequisitos());
            statement.setInt(7, proyecto.getTutorEncargado().getIdTutor())/*,
            statement.setBoolean(8, /*proyecto.getHabilitado())*/;
            statement.setBoolean(8, true);

            statement.executeUpdate();

        }catch(ConnectionException e){
            throw new CreateException("Error al conectar con la base de datos: " + e.getMessage());
        }catch(SQLException e){
            System.out.println(e);
            throw new CreateException("Error al crear el proyecto: " + e.getMessage());
        }
    }

    @Override   //chequear
    public List<Proyecto> obtenerProyectos() throws ReadException {
        List<Proyecto> proyectos = new ArrayList<>();

        try (Connection conn = connect();
             PreparedStatement statement = conn.prepareStatement("SELECT * FROM Proyectos");
             ResultSet result = statement.executeQuery()) {

            while (result.next()) {
                TutorExternoDAODB tutorExternoDAO = new TutorExternoDAODB();
                int idTutor = result.getInt("idTutor");

                Proyecto proyecto = new Proyecto(
                        result.getInt("idProyecto"),
                        result.getString("titulo"),
                        result.getString("descripcion"),
                        result.getString("areaDeInteres"),
                        result.getString("ubicacion"),
                        result.getString("objetivos"),
                        result.getString("requisitos"),
                        tutorExternoDAO.buscarByID(idTutor) // Obtiene el tutor asociado
                );

                proyectos.add(proyecto);
            }

            return proyectos;
        } catch (SQLException e) {
            throw new ReadException("Error al obtener los datos de los proyectos.");
        } catch (ConnectionException e) {
            throw new ReadException(e.getMessage());
        } catch (UserException e) {
            throw new ReadException("Error al construir el proyecto: " + e.getMessage());
        } catch (EmptyException e) {
            throw new RuntimeException(e.getMessage());
        }

    }

    @Override   //chequear
    public void delete(int id) throws DeleteException {
        try (Connection conn = connect()) {
            conn.setAutoCommit(false);

            // Eliminar relaciones en la tabla que vincula el proyecto con otros datos
            try (PreparedStatement deleteRelaciones = conn.prepareStatement(
                    "DELETE FROM Relacion_Proyectos_Otros WHERE idProyecto = ?")) {
                deleteRelaciones.setInt(1, id);
                deleteRelaciones.executeUpdate();
            }

            // Eliminar el proyecto principal
            try (PreparedStatement deleteProyecto = conn.prepareStatement(
                    "DELETE FROM proyectos WHERE idProyecto = ?")) {
                deleteProyecto.setInt(1, id);
                if (deleteProyecto.executeUpdate() < 1) {
                    throw new SQLException("No se pudo eliminar el proyecto, no existe o ya fue eliminado.");
                }
            }

            conn.commit(); // Confirma la operacion si todo fue exitoso
        } catch (SQLException e) {
            try (Connection conn = connect()) {
                conn.rollback(); // Revierte la operacion en caso de error
            } catch (SQLException rollbackEx) {
                throw new DeleteException("Error al realizar rollback: " + rollbackEx.getMessage());
            } catch (ConnectionException e1) {
                throw new DeleteException(e1.getMessage());
            }
            throw new DeleteException("Error al eliminar el proyecto: " + e.getMessage());
        } catch (ConnectionException e) {
            throw new DeleteException(e.getMessage());
        }
    }

    @Override
    public Proyecto buscarByID(int id) throws ReadException {
        TutorExternoDAODB TutorExternoDAODB = new TutorExternoDAODB();
        Proyecto proyecto = null;

        try (Connection conn = connect();
             PreparedStatement statement = conn.prepareStatement("SELECT * FROM Proyectos WHERE idProyecto = ?")) {

            statement.setInt(1, id);

            ResultSet result = statement.executeQuery();
            if (result.next()) {
                int idTutor = result.getInt("idTutor");
                TutorExterno tutor = new TutorExternoDAODB().buscarByID(idTutor);
                proyecto = new Proyecto(
                        result.getInt("idProyecto"),
                        result.getString("titulo"),
                        result.getString("descripcion"),
                        result.getString("areaDeInteres"),
                        result.getString("ubicacion"),
                        result.getString("objetivos"),
                        result.getString("requisitos"),
                        tutor
                );
            }

            return proyecto;
        }
        catch (EmptyException e) {
            throw new ReadException(e.getMessage());}
        catch (UserException e) {
            throw new ReadException("Error al obtener el tutor: " + e.getMessage());}
        catch (SQLException e) {
            throw new ReadException("Error al obtener el Proyecto: " + e.getMessage());}
        catch (ConnectionException e) {
            throw new ReadException("Error al conectar con la base de datos: " + e.getMessage());
        }
    }

    @Override
    public boolean validarTituloUnico(String titulo) throws CreateException{
        try (Connection conn = connect()) {
            String sql = "SELECT COUNT(*) AS total FROM Proyectos WHERE LOWER(titulo) = LOWER(?)"; //Lower para indistinto a Mayúsculas o minúsculas
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, titulo);
            ResultSet result = statement.executeQuery();

            if (result.next() && result.getInt("total") != 0) {
                throw new CreateException("Proyecto con titulo insertado existente en el sistema.");
            }
            return true;
        }
        catch (SQLException e) {
            throw new CreateException("Error al validar: " + e.getMessage());
        }
        catch (ConnectionException e) {
            throw new CreateException("Error al conectar con la base de datos: " + e.getMessage());
        }
    }

    @Override
    public Proyecto buscarByTitulo(String titulo) throws ReadException {
        TutorExternoDAODB TutorExternoDAODB = new TutorExternoDAODB();
        Proyecto proyecto = null;

        try (Connection conn = connect();
             PreparedStatement statement = conn.prepareStatement("SELECT * FROM Proyectos WHERE titulo = ?")) {

            statement.setString(1, titulo);

            ResultSet result = statement.executeQuery();
            if (result.next()) {
                int idTutor = result.getInt("idTutor");
                System.out.println("Id Tutor : "+ idTutor);
                TutorExterno tutor = new TutorExternoDAODB().buscarByID(idTutor);
                proyecto = new Proyecto(
                        result.getInt("idProyecto"),
                        result.getString("titulo"),
                        result.getString("descripcion"),
                        result.getString("areaDeInteres"),
                        result.getString("ubicacion"),
                        result.getString("objetivos"),
                        result.getString("requisitos"),
                        tutor
                );
            }

            return proyecto;
    } catch (SQLException e) {
            throw new ReadException("Error al buscar el proyecto: " + e.getMessage());
        } catch (ConnectionException e) {
            throw new ReadException("Error al conectar con la base de datos: " + e.getMessage());
        } catch (EmptyException e) {
            throw new ReadException(e.getMessage());
        } catch (UserException e) {
            throw new ReadException(e.getMessage());
        }
    }


}
