package Backend.DAO.dom.elementos;

import Backend.DAO.DBAcces;
import Backend.DAO.interfaces.elementos.ACTIVIDADDAO;
import Backend.Entidades.Actividad;
import Backend.Entidades.Informe;
import Backend.Exceptions.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ActividadDAODB extends DBAcces implements ACTIVIDADDAO {
    @Override
    public void create(Actividad actividad) throws CreateException {
        try (Connection conn = connect();
             PreparedStatement statement = conn.prepareStatement(
                     "INSERT INTO actividades(titulo, descripcion, duracion, fechaFin, fechaInicio) VALUES (?, ?, ?, ?, ?)",
                     Statement.RETURN_GENERATED_KEYS
             )) {

            statement.setString(1, actividad.getTitulo());
            statement.setString(2, actividad.getDescripcion());
            statement.setInt(3, actividad.getDuracion());
            statement.setDate(4, Date.valueOf(actividad.getFechaFin()));
            statement.setDate(5, Date.valueOf(actividad.getFechaInicio()));

            int rowsInserted = statement.executeUpdate();
            if (rowsInserted == 0) {
                throw new CreateException("No se pudo insertar la Actividad.");
            }

            // Obtener ID generado
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int idGenerado = generatedKeys.getInt(1);
                    actividad.setID(idGenerado);
                } else {
                    throw new CreateException("No se pudo obtener el ID generado de la Actividad");
                }
            }

            // Insertar actividades relacionadas si hay
            if (actividad.getInformes() != null && !actividad.getInformes().isEmpty()) {
                for (Informe informe : actividad.getInformes()) {
                    agregarRelacionInforme(conn, actividad.getID(), informe.getID());
                }
            }

        }catch(ConnectionException e){
            throw new CreateException( e.getMessage());
        }catch(SQLException e){
            throw new CreateException("Error al crear la Actividad: " + e.getMessage());
        }
    }
    private void agregarRelacionInforme(Connection conn, int idActividad, int idInforme) throws SQLException {
        String sql = "INSERT INTO Relacion_Actividad_Informes (idActividad, idInforme) VALUES (?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idActividad);
            stmt.setInt(2, idInforme);
            stmt.executeUpdate();
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

                // Cargar informes relacionados
                List<Informe> informes = buscarInformes(id);
                actividad.setInformes(informes);

                result.close();
                return actividad;
            } else {
                throw new ReadException("No se encontró la actividad en la base de datos");
            }
        } catch (ConnectionException | EmptyException e) {
            throw new ReadException(e.getMessage());
        } catch (SQLException e) {
            throw new ReadException("Error al leer el informe: " + e.getMessage());
        }
    }

    @Override
    public List<Actividad> obtenerActividades() throws ReadException {
        List<Actividad> actividades = new ArrayList<>();

        try (Connection conn = connect();
             PreparedStatement statement = conn.prepareStatement("SELECT * FROM actividades");
             ResultSet result = statement.executeQuery()) {

            while (result.next()) {
                Actividad actividad = new Actividad(
                        result.getInt("idActividad"),
                        result.getString("titulo"),
                        result.getString("descripcion"),
                        result.getDate("fechaFin").toLocalDate(),
                        result.getInt("duracion"),
                        result.getDate("fechaInicio").toLocalDate());

                // Cargar informes relacionados
                List<Informe> informes = buscarInformes(result.getInt("idActividad"));
                actividad.setInformes(informes);

                actividades.add(actividad);
            }

            return actividades;
        } catch (SQLException e) {
            throw new ReadException("Error al obtener los datos de las actividades." + e.getMessage());
        } catch (ConnectionException | EmptyException e) {
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

                // Cargar informes relacionados
                List<Informe> informes = buscarInformes(result.getInt("idActividad"));
                actividad.setInformes(informes);

                result.close();
                return actividad;
            } else {
                throw new ReadException("No se encontró el informe en la base de datos");
            }
        } catch (ConnectionException | EmptyException e) {
            throw new ReadException(e.getMessage());
        } catch (SQLException e) {
            throw new ReadException("Error al leer el informe: " + e.getMessage());
        }
    }

    public List<Informe> buscarInformes(int idActividad) throws ReadException {
        List<Informe> informes = new ArrayList<>();

        String sql = "SELECT idInforme FROM Relacion_Actividad_Informes WHERE idActividad = ?";

        try (Connection conn = connect();
             PreparedStatement statement = conn.prepareStatement(sql)) {

            statement.setInt(1, idActividad);

            try (ResultSet result = statement.executeQuery()) {
                InformeDAODB informeDAO = new InformeDAODB();

                while (result.next()) {
                    int idInforme = result.getInt("idInforme");
                    Informe informe = informeDAO.buscarByID(idInforme);
                    informes.add(informe);
                }
            }

            return informes;

        } catch (SQLException e) {
            throw new ReadException("Error SQL al obtener los informes de la actividad." + e.getMessage());
        } catch (ConnectionException e) {
            throw new ReadException(e.getMessage());
        }
    }
}
