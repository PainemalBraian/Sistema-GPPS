package Backend.DAO.dom.elementos;

import Backend.DAO.DBAcces;
import Backend.DAO.interfaces.elementos.INFORMEDAO;
import Backend.Entidades.Informe;
import Backend.Exceptions.*;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static java.sql.DriverManager.getConnection;

public class InformeDAODB extends DBAcces implements INFORMEDAO {
    @Override
    public void create(Informe informe) throws CreateException {
        try (Connection conn = connect();
             PreparedStatement statement = conn.prepareStatement(
                     "INSERT INTO Informes(titulo, descripcion, archivo_pdf, fecha, idActividad, porcentajeAvance, calificacionDocente, calificacionTutor) VALUES (?, ?, ?, ?, ?, ?, ?, ?)",
                     Statement.RETURN_GENERATED_KEYS
             )) {

            statement.setString(1, informe.getTitulo());
            statement.setString(2, informe.getDescripcion());
            statement.setBytes(3, informe.getArchivoPDF());
            statement.setDate(4, Date.valueOf(informe.getFecha()));
            int idActividad = new ActividadDAODB().buscarByTitulo(informe.getTituloActividad()).getID();
            statement.setInt(5, idActividad);
            statement.setInt(6, informe.getPorcentajeAvance());

            statement.setInt(7, informe.getCalificacionDocente());
            statement.setInt(8, informe.getCalificacionTutor());

            // Ejecutar el INSERT
            statement.executeUpdate();

            // Obtener ID generado
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int idGenerado = generatedKeys.getInt(1);
                    informe.setID(idGenerado);

                    // Insertar actividad relacionada si hay
                    agregarRelacionInforme(conn, idActividad, idGenerado);
                } else {
                    throw new CreateException("No se pudo obtener el ID generado del informe");
                }
            }

        } catch (ConnectionException | ReadException e) {
            throw new CreateException(e.getMessage());
        } catch (SQLException e) {
            throw new CreateException("Error al crear el informe: " + e.getMessage());
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
    public List<Informe> obtenerInformes() throws ReadException {
        List<Informe> informes = new ArrayList<>();

        try (Connection conn = connect();
             PreparedStatement statement = conn.prepareStatement("SELECT * FROM Informes");
             ResultSet result = statement.executeQuery()) {

            while (result.next()) {
                Informe informe = new Informe(
                        result.getInt("idInforme"),
                        result.getString("titulo"),
                        result.getString("descripcion"),
                        result.getBytes("archivo_pdf")
                );
                informe.setCalificacionDocente(result.getInt("calificacionDocente"));
                informe.setCalificacionTutor(result.getInt("calificacionTutor"));
                informe.setFecha(result.getDate("fecha").toLocalDate());
                informe.setPorcentajeAvance(result.getInt("porcentajeAvance"));
                informes.add(informe);
            }

            return informes;
        } catch (SQLException e) {
            throw new ReadException("Error al obtener los datos de los informes." + e.getMessage());
        } catch (ConnectionException e) {
            throw new ReadException(e.getMessage());
        } catch (EmptyException e) {
            throw new ReadException(e.getMessage());
        }
    }


    @Override
    public Informe buscarByID(int id) throws ReadException {
        try (Connection conn = connect();
             PreparedStatement statement = conn.prepareStatement("SELECT * FROM Informes WHERE idInforme = ?")) {

            statement.setInt(1, id);
            ResultSet result = statement.executeQuery();

            if (result.next()) {
                int idInforme = result.getInt("idInforme");
                String titulo = result.getString("titulo");
                String descripcion = result.getString("descripcion");
                byte[] archivo = result.getBytes("archivo_pdf");
                LocalDate fecha = result.getDate("fecha").toLocalDate();

                Informe informe = new Informe(idInforme, titulo, descripcion, archivo);
                informe.setCalificacionDocente(result.getInt("calificacionDocente"));
                informe.setCalificacionTutor(result.getInt("calificacionTutor"));
                informe.setFecha(fecha);
                informe.setPorcentajeAvance(result.getInt("porcentajeAvance"));
                result.close();
                return informe;
            } else {
                throw new ReadException("No se encontró el informe en la base de datos");
            }

        } catch (ConnectionException e) {
            throw new ReadException( e.getMessage());
        } catch (SQLException e) {
            throw new ReadException("Error al leer el informe: " + e.getMessage());
        } catch (EmptyException e) {
            throw new ReadException(e.getMessage());
        }
    }

    @Override
    public Informe buscarByTitulo(String titulo) throws ReadException {
        try (Connection conn = connect();
             PreparedStatement statement = conn.prepareStatement("SELECT * FROM Informes WHERE titulo = ?")) {

            statement.setString(1, titulo);
            ResultSet result = statement.executeQuery();

            if (result.next()) {
                int idInforme = result.getInt("idInforme");
                String descripcion = result.getString("descripcion");
                byte[] archivo = result.getBytes("archivo_pdf");
                LocalDate fecha = result.getDate("fecha").toLocalDate();

                Informe informe = new Informe(idInforme, titulo, descripcion, archivo);

                informe.setCalificacionDocente(result.getInt("calificacionDocente"));
                informe.setCalificacionTutor(result.getInt("calificacionTutor"));
                informe.setFecha(fecha);
                informe.setPorcentajeAvance(result.getInt("porcentajeAvance"));
                result.close();
                return informe;
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
    public boolean validarTituloUnico(String titulo) throws ReadException {
        try (Connection conn = connect()) {
            String sql = "SELECT COUNT(*) AS total FROM Informes WHERE LOWER(titulo) = LOWER(?)"; //Lower para indistinto a Mayúsculas o minúsculas
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, titulo);
            ResultSet result = statement.executeQuery();

            if (result.next() && result.getInt("total") != 0) {
                throw new ReadException("Informe con titulo insertado existente en el sistema.");
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

    public void update(Informe informe) throws CreateException {
        if (informe == null) {
            throw new CreateException("El informe no puede ser null");
        }

        if (informe.getTitulo() == null || informe.getTitulo().trim().isEmpty()) {
            throw new CreateException("El título del informe no puede estar vacío");
        }

        // SQL que actualiza ambos campos de calificación
        String sql = "UPDATE Informes SET calificacionDocente = ?, calificacionTutor = ? WHERE titulo = ?";

        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, informe.getCalificacionDocente());
            stmt.setInt(2, informe.getCalificacionTutor());
            stmt.setString(3, informe.getTitulo().trim());

            int filasAfectadas = stmt.executeUpdate();

            if (filasAfectadas == 0) {
                throw new CreateException("No se encontró el informe en la base de datos para actualizar");
            }

        } catch (SQLException e) {
            throw new CreateException("Error de base de datos al actualizar el informe: " + e.getMessage());
        } catch (ConnectionException e) {
            throw new RuntimeException(e);
        }
    }

}
