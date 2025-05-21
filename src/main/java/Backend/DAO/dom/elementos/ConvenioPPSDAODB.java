package Backend.DAO.dom.elementos;

import Backend.DAO.DBAcces;
import Backend.DAO.dom.usuarios.*;
import Backend.DAO.interfaces.elementos.ConvenioPPSDAO;
import Backend.Entidades.*;
import Backend.Exceptions.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ConvenioPPSDAODB extends DBAcces implements ConvenioPPSDAO {
    @Override
    public void create(ConvenioPPS pps) throws CreateException {

    }

    @Override
    public void delete(int id) throws DeleteException {

    }

    @Override
    public ConvenioPPS buscarByID(int id) throws ReadException {
        return null;
    }

    @Override
    public List<ConvenioPPS> obtenerConvenios() throws ReadException {
        return List.of();
    }

    @Override
    public boolean validarTituloUnico(String titulo) throws ReadException {
        return false;
    }

    @Override
    public ConvenioPPS buscarByTitulo(String titulo) throws ReadException {
        try (Connection conn = connect();
             PreparedStatement statement = conn.prepareStatement("SELECT * FROM ConveniosPPS WHERE titulo = ?")) {

            statement.setString(1, titulo);
            ResultSet result = statement.executeQuery();

            if (result.next()) {
                int idConvenio = result.getInt("idConvenio");
                String descripcion = result.getString("descripcion");
                boolean habilitado = result.getBoolean("habilitado");

                // Obtener objetos relacionados
                Proyecto proyecto = new ProyectoDAODB().buscarByID(result.getInt("idProyecto"));
                Docente docente = new DocenteDAODB().buscarById(result.getInt("idDocente"));
                Estudiante estudiante = new EstudianteDAODB().buscarById(result.getInt("idEstudiante"));
                DirectorCarrera director = new DirectorCarreraDAODB().buscarById(result.getInt("idDirector"));
                EntidadColaborativa entidad = new EntidadColaborativaDAODB().buscarById(result.getInt("idEntidad"));

                // Si no hay actividades cargadas en la base, se envía una lista vacía por ahora
                List<Actividad> actividades = new ArrayList<>();

                ConvenioPPS convenio = new ConvenioPPS(
                        idConvenio,
                        titulo,
                        descripcion,
                        proyecto,
                        docente,
                        estudiante,
                        director,
                        entidad,
                        actividades
                );
                convenio.setHabilitado(habilitado);
                return convenio;
            } else {
                throw new ReadException("No se encontró un convenio con el título proporcionado.");
            }
        } catch (SQLException e) {
            throw new ReadException("Error SQL al buscar el convenio: " + e.getMessage());
        } catch (ConnectionException e) {
            throw new ReadException("Error de conexión: " + e.getMessage());
        } catch (UserException | EmptyException e) {
            throw new ReadException("Error al cargar datos relacionados: " + e.getMessage());
        }
    }

}
