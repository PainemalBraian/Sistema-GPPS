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
            statement.setInt(7, proyecto.getTutorEncargado().getIdTutor());
            statement.setBoolean(8, proyecto.isHabilitado());

            statement.executeUpdate();

        }catch(ConnectionException e){
            throw new CreateException(e.getMessage());
        }catch(SQLException e){
            throw new CreateException("Error al crear el proyecto: " + e.getMessage());
        }
    }

    public void createPropuesta(Proyecto proyecto) throws CreateException {
        try (Connection conn = connect();
             PreparedStatement statement = conn.prepareStatement(
                     "INSERT INTO Proyectos(titulo, descripcion, areaDeInteres, ubicacion, objetivos, requisitos, habilitado) VALUES (?, ?, ?, ?, ?, ?, ?)"
             )) {

            statement.setString(1, proyecto.getTitulo());
            statement.setString(2, proyecto.getDescripcion());
            statement.setString(3, proyecto.getAreaDeInteres());
            statement.setString(4, proyecto.getUbicacion());
            statement.setString(5, proyecto.getObjetivos());
            statement.setString(6, proyecto.getRequisitos());
            statement.setBoolean(7, proyecto.isHabilitado());

            statement.executeUpdate();

            statement.close();

        }catch(ConnectionException e){
            throw new CreateException( e.getMessage());
        }catch(SQLException e){
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
                if (idTutor == 0)
                    idTutor =-10;
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
                proyecto.setHabilitado(result.getBoolean("habilitado"));

                proyectos.add(proyecto);
            }

            return proyectos;
        } catch (SQLException e) {
            throw new ReadException("Error al obtener los datos de los proyectos.");
        } catch (ConnectionException e) {
            throw new ReadException(e.getMessage());
        } catch (UserException e) {
            throw new ReadException("Error al obtener el proyecto: " + e.getMessage());
        } catch (EmptyException e) {
            throw new RuntimeException(e.getMessage());
        }

    }

    public List<Proyecto> obtenerProyectosHabilitados() throws ReadException {
        List<Proyecto> proyectos = new ArrayList<>();

        try (Connection conn = connect();
             PreparedStatement statement = conn.prepareStatement("SELECT * FROM Proyectos WHERE habilitado = true ");
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
                proyecto.setHabilitado(result.getBoolean("habilitado"));

                proyectos.add(proyecto);
            }

            return proyectos;
        } catch (SQLException e) {
            throw new ReadException("Error al obtener los datos de los proyectos.");
        } catch (ConnectionException e) {
            throw new ReadException(e.getMessage());
        } catch (UserException e) {
            throw new ReadException("Error al obtener el proyecto: " + e.getMessage());
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
                int idTutor = -10;
                 if (result.getInt("idTutor") != 0){
                     idTutor = result.getInt("idTutor");
                 }
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
                proyecto.setHabilitado(result.getBoolean("habilitado"));
            }
            result.close();
            return proyecto;
        }
        catch (EmptyException e) {
            throw new ReadException(e.getMessage());}
        catch (UserException e) {
            throw new ReadException("Error al obtener el tutor: " + e.getMessage());}
        catch (SQLException e) {
            throw new ReadException("Error al obtener el Proyecto: " + e.getMessage());}
        catch (ConnectionException e) {
            throw new ReadException(e.getMessage());
        }
    }

    @Override
    public boolean validarTituloUnico(String titulo) throws CreateException{
        try (Connection conn = connect();
             PreparedStatement statement = conn.prepareStatement("SELECT COUNT(*) AS total FROM Proyectos WHERE LOWER(titulo) = LOWER(?)");
             ) {

            statement.setString(1, titulo);
            ResultSet result = statement.executeQuery();

            if (result.next() && result.getInt("total") != 0) {
                throw new CreateException("Proyecto con titulo insertado existente en el sistema.");
            }
            result.close();
            return true;
        }
        catch (SQLException e) {
            throw new CreateException("Error al validar: " + e.getMessage());
        }
        catch (ConnectionException e) {
            throw new CreateException(e.getMessage());
        }
    }

    @Override
    public Proyecto buscarByTitulo(String titulo) throws ReadException {
        try (Connection conn = connect();
             PreparedStatement statement = conn.prepareStatement("SELECT * FROM Proyectos WHERE titulo = ?");
             ) {

            statement.setString(1, titulo);
            ResultSet result = statement.executeQuery();

            Proyecto proyecto = null;
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
                proyecto.setHabilitado(result.getBoolean("habilitado"));
                result.close();
            }

            return proyecto;
        } catch (SQLException e) {
            throw new ReadException("Error al buscar el proyecto: " + e.getMessage());
        } catch (ConnectionException e) {
            throw new ReadException(e.getMessage());
        } catch (EmptyException e) {
            throw new ReadException(e.getMessage());
        } catch (UserException e) {
            throw new ReadException(e.getMessage());
        }
    }


    public void update(Proyecto proyecto) throws CreateException {
        try (Connection conn = connect();
             PreparedStatement statement = conn.prepareStatement(
                     "UPDATE Proyectos SET titulo = ?, descripcion = ?, habilitado = ?, areaDeInteres = ?, ubicacion = ?, objetivos = ?, requisitos = ?, idTutor = ? WHERE idProyecto = ?"
             )) {

            statement.setString(1, proyecto.getTitulo());
            statement.setString(2, proyecto.getDescripcion());
            statement.setBoolean(3, proyecto.isHabilitado());
            statement.setString(4, proyecto.getAreaDeInteres());
            statement.setString(5, proyecto.getUbicacion());
            statement.setString(6, proyecto.getObjetivos());
            statement.setString(7, proyecto.getRequisitos());

            if (proyecto.getTutorEncargado() != null) {
                statement.setInt(8, proyecto.getTutorEncargado().getIdUsuario());
            }
            else {
                statement.setNull(8,java.sql.Types.INTEGER);
            }


            statement.setInt(9, proyecto.getID()); // Identificador del proyecto a actualizar

            int filasActualizadas = statement.executeUpdate();

            // Verificar si se actualizó al menos una fila
            if (filasActualizadas == 0) {
                throw new CreateException("No se encontró el proyecto con ID: " + proyecto.getID());
            }

        } catch (ConnectionException e) {
            throw new CreateException(e.getMessage());
        } catch (SQLException e) {
            throw new CreateException("Error al actualizar el proyecto: " + e.getMessage());
        }
    }
}
