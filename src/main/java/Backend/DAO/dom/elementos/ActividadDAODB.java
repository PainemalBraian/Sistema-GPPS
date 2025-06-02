package Backend.DAO.dom.elementos;

import Backend.DAO.DBAcces;
import Backend.DAO.interfaces.elementos.ACTIVIDADDAO;
import Backend.Entidades.Actividad;
import Backend.Exceptions.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ActividadDAODB extends DBAcces implements ACTIVIDADDAO {
    @Override
    public void create(Actividad actividad) throws CreateException {
        try (Connection conn = connect();
             PreparedStatement statement = conn.prepareStatement(
                     "INSERT INTO Informes(titulo, descripcion, duracion, fechaFin, fechaInicio) VALUES (?, ?, ?, ?, ?)"
             )) {

            statement.setString(1, actividad.getTitulo());
            statement.setString(2, actividad.getDescripcion());
            statement.setInt(3, actividad.getDuracion());
            statement.setDate(4, Date.valueOf(actividad.getFechaFin()));
            statement.setDate(5, Date.valueOf(actividad.getFechaInicio()));

            statement.executeUpdate();

        }catch(ConnectionException e){
            throw new CreateException( e.getMessage());
        }catch(SQLException e){
            throw new CreateException("Error al crear la Actividad: " + e.getMessage());
        }
    }

    @Override
    public void delete(int id) throws DeleteException {

    }

    @Override
    public Actividad buscarByID(int id) throws ReadException {
        try (Connection conn = connect();
             PreparedStatement statement = conn.prepareStatement("SELECT * FROM Actividades WHERE idActividad = ?")) {

            statement.setInt(1, id);
            ResultSet result = statement.executeQuery();

            if (result.next()) {
                Actividad actividad = new Actividad(
                        result.getInt("idActividad"),
                        result.getString("titulo"),
                        result.getString("descripcion"),
                        result.getDate("fechaFin").toLocalDate(),
                        result.getInt("duracion"),
                        result.getDate("fechaInicio").toLocalDate());

                result.close();
                return actividad;
            } else {
                throw new ReadException("No se encontró el informe en la base de datos");
            }
        } catch (ConnectionException e) {
            throw new ReadException(e.getMessage());
        } catch (SQLException e) {
            throw new ReadException("Error al leer el informe: " + e.getMessage());
        } catch (EmptyException e) {
            throw new ReadException(e.getMessage());
        }
    }

    @Override
    public List<Actividad> obtenerActividades() throws ReadException {
        List<Actividad> actividades = new ArrayList<>();

        try (Connection conn = connect();
             PreparedStatement statement = conn.prepareStatement("SELECT * FROM Informes");
             ResultSet result = statement.executeQuery()) {

            while (result.next()) {
                Actividad actividad = new Actividad(
                        result.getInt("idActividad"),
                        result.getString("titulo"),
                        result.getString("descripcion"),
                        result.getDate("fechaFin").toLocalDate(),
                        result.getInt("duracion"),
                        result.getDate("fechaInicio").toLocalDate());

                actividades.add(actividad);
            }

            return actividades;
        } catch (SQLException e) {
            throw new ReadException("Error al obtener los datos de los informes." + e.getMessage());
        } catch (ConnectionException e) {
            throw new ReadException(e.getMessage());
        } catch (EmptyException e) {
            throw new ReadException(e.getMessage());
        }
    }

    @Override
    public boolean validarTituloUnico(String titulo) throws ReadException {
        try (Connection conn = connect()) {
            String sql = "SELECT COUNT(*) AS total FROM Actividades WHERE LOWER(titulo) = LOWER(?)"; //Lower para indistinto a Mayúsculas o minúsculas
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, titulo);
            ResultSet result = statement.executeQuery();

            if (result.next() && result.getInt("total") != 0) {
                throw new ReadException("Actividad con titulo insertado existente en el sistema.");
            }
            statement.close();
            result.close();
            return true;
        }
        catch (SQLException e) {
            throw new ReadException("Error al validar: " + e.getMessage());
        }
        catch (ConnectionException e) {
            throw new ReadException(e.getMessage());
        }
    }

    @Override
    public Actividad buscarByTitulo(String titulo) throws ReadException {
        try (Connection conn = connect();
             PreparedStatement statement = conn.prepareStatement("SELECT * FROM Actividades WHERE titulo = ?")) {

            statement.setString(1, titulo);
            ResultSet result = statement.executeQuery();

            if (result.next()) {
                Actividad actividad = new Actividad(
                        result.getInt("idActividad"),
                        titulo,
                        result.getString("descripcion"),
                        result.getDate("fechaFin").toLocalDate(),
                        result.getInt("duracion"),
                        result.getDate("fechaInicio").toLocalDate());

                result.close();
                return actividad;
            } else {
                throw new ReadException("No se encontró el informe en la base de datos");
            }
        } catch (ConnectionException e) {
            throw new ReadException(e.getMessage());
        } catch (SQLException e) {
            throw new ReadException("Error al leer el informe: " + e.getMessage());
        } catch (EmptyException e) {
            throw new ReadException(e.getMessage());
        }
    }

    public List<Actividad> obtenerActividadesHabilitadas() throws ReadException {
        List<Actividad> actividades = new ArrayList<>();

        try (Connection conn = connect();
             PreparedStatement statement = conn.prepareStatement("SELECT * FROM Informes WHERE habilitado = TRUE");
             ResultSet result = statement.executeQuery()) {

            while (result.next()) {
                Actividad actividad = new Actividad(
                        result.getInt("idActividad"),
                        result.getString("titulo"),
                        result.getString("descripcion"),
                        result.getDate("fechaFin").toLocalDate(),
                        result.getInt("duracion"),
                        result.getDate("fechaInicio").toLocalDate());

                actividades.add(actividad);
            }

            return actividades;
        } catch (SQLException e) {
            throw new ReadException("Error al obtener los datos de los informes." + e.getMessage());
        } catch (ConnectionException e) {
            throw new ReadException(e.getMessage());
        } catch (EmptyException e) {
            throw new ReadException(e.getMessage());
        }
    }
}
