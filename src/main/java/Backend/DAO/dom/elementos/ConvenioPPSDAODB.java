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
    public void create(ConvenioPPS convenio) throws CreateException {
        try (Connection conn = connect();
             PreparedStatement statement = conn.prepareStatement(
                     "INSERT INTO ConveniosPPS(titulo, descripcion, habilitado, idProyecto, idDocente, idEstudiante,idDirector, idEntidad) VALUES (?, ?, ?, ?, ?, ?, ?,?)"
             )) {

            statement.setString(1, convenio.getTitulo());
            statement.setString(2, convenio.getDescripcion());
            statement.setBoolean(3, convenio.isHabilitado());
            statement.setInt(4, convenio.getProyecto().getId());
            statement.setInt(5, convenio.getDocente().getIdUsuario());
            statement.setInt(6, convenio.getEstudiante().getIdUsuario());
            statement.setInt(7, convenio.getDirector().getIdUsuario());
            statement.setInt(8, convenio.getEntidad().getIdUsuario());

            statement.executeUpdate();

        }catch(ConnectionException e){
            throw new CreateException("Error al conectar con la base de datos: " + e.getMessage());
        }catch(SQLException e){
            throw new CreateException("Error al crear el convenio: " + e.getMessage());
        }
    }

    @Override
    public void delete(int id) throws DeleteException {

    }

    @Override
    public ConvenioPPS buscarByID(int id) throws ReadException {
        try (Connection conn = connect();
             PreparedStatement statement = conn.prepareStatement("SELECT * FROM ConveniosPPS WHERE idConvenio = ?")) {

            statement.setInt(1, id);
            ResultSet result = statement.executeQuery();

            if (result.next()) {
                int idConvenio = result.getInt("idConvenio");
                String titulo = result.getString("titulo");
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
            throw new ReadException("Error al buscar el convenio: " + e.getMessage());
        }

    }

    @Override // Probar
    public List<ConvenioPPS> obtenerConvenios() throws ReadException {
        List<ConvenioPPS> convenios = new ArrayList<>();

        try (Connection conn = connect();
             PreparedStatement statement = conn.prepareStatement("SELECT * FROM ConveniosPPS");
             ResultSet result = statement.executeQuery()) {

            while (result.next()) {
                int idConvenio = result.getInt("idConvenio");
                String titulo = result.getString("titulo");
                String descripcion = result.getString("descripcion");
                boolean habilitado = result.getBoolean("habilitado");

                // Obtener objetos relacionados
                Proyecto proyecto = new ProyectoDAODB().buscarByID(result.getInt("idProyecto"));
                Docente docente = new DocenteDAODB().buscarById(result.getInt("idDocente"));
                Estudiante estudiante = new EstudianteDAODB().buscarById(result.getInt("idEstudiante"));
                DirectorCarrera director = new DirectorCarreraDAODB().buscarById(result.getInt("idDirector"));
                EntidadColaborativa entidad = new EntidadColaborativaDAODB().buscarById(result.getInt("idEntidad"));

                // Lista de actividades vacía por el momento
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

                convenios.add(convenio);
            }

            return convenios;

        } catch (SQLException e) {
            throw new ReadException("Error SQL al obtener los convenios: " + e.getMessage());
        } catch (ConnectionException e) {
            throw new ReadException("Error de conexión: " + e.getMessage());
        } catch (UserException | EmptyException e) {
            throw new ReadException("Error al construir los convenios: " + e.getMessage());
        }
    }

    @Override
    public boolean validarTituloUnico(String titulo) throws CreateException {
        try (Connection conn = connect()) {
            String sql = "SELECT COUNT(*) AS total FROM ConveniosPPS WHERE LOWER(titulo) = LOWER(?)"; //Lower para indistinto a Mayúsculas o minúsculas
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, titulo);
            ResultSet result = statement.executeQuery();

            if (result.next() && result.getInt("total") != 0) {
                throw new CreateException("Convenio con titulo insertado existente en el sistema.");
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
            throw new ReadException("Error al buscar el convenio: " + e.getMessage());
        }
    }

}
