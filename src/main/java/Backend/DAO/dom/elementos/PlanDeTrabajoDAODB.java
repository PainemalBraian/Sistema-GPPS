package Backend.DAO.dom.elementos;

import Backend.DAO.DBAcces;
import Backend.DAO.dom.usuarios.DocenteDAODB;
import Backend.DAO.dom.usuarios.TutorExternoDAODB;
import Backend.DAO.interfaces.elementos.PLANDETRABAJODAO;
import Backend.Entidades.*;
import Backend.Exceptions.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PlanDeTrabajoDAODB extends DBAcces implements PLANDETRABAJODAO{

    @Override
    public void create(PlanDeTrabajo plan) throws CreateException {
        String sql = "INSERT INTO PlanesDeTrabajos (titulo, descripcion, idDocente, idTutor, idInformeFinal, habilitado) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = connect();
             PreparedStatement statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, plan.getTitulo());
            statement.setString(2, plan.getDescripcion());
            statement.setInt(3, plan.getDocente().getIdUsuario());
            statement.setInt(4, plan.getTutor().getIdUsuario());

            if (plan.getInformeFinal() != null) {
                statement.setInt(5, plan.getInformeFinal().getID());
            } else {
                statement.setNull(5, Types.INTEGER);
            }

            statement.setBoolean(6, plan.isHabilitado());

            int rowsInserted = statement.executeUpdate();
            if (rowsInserted == 0) {
                throw new CreateException("No se pudo insertar el plan de trabajo");
            }

            // Obtener ID generado
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int idGenerado = generatedKeys.getInt(1);
                    plan.setID(idGenerado);
                } else {
                    throw new CreateException("No se pudo obtener el ID generado del plan");
                }
            }

            // Insertar actividades relacionadas si hay
            if (plan.getActividades() != null && !plan.getActividades().isEmpty()) {
                for (Actividad act : plan.getActividades()) {
                    agregarRelacionActividad(conn, plan.getID(), act.getID());
                }
            }

        } catch (SQLException e) {
            throw new CreateException("Error SQL al crear el plan de trabajo: " + e.getMessage());
        } catch (ConnectionException | EmptyException e) {
            throw new CreateException(e.getMessage());
        }
    }
    private void agregarRelacionActividad(Connection conn, int idPlan, int idActividad) throws SQLException {
        String sql = "INSERT INTO Relacion_PlanDeTrabajo_Actividades (idPlan, idActividad) VALUES (?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idPlan);
            stmt.setInt(2, idActividad);
            stmt.executeUpdate();
        }
    }


    @Override
    public void delete(int id) throws DeleteException {

    }

    @Override
    public PlanDeTrabajo buscarByID(int id) throws ReadException {
        String sql = "SELECT * FROM PlanesDeTrabajos WHERE idPlanDeTrabajo = ?";

        try (Connection conn = connect();
             PreparedStatement statement = conn.prepareStatement(sql)) {

            statement.setInt(1, id);
            ResultSet result = statement.executeQuery();

            if (result.next()) {
                int idPlan = result.getInt("idPlanDeTrabajo");
                String titulo = result.getString("titulo");
                String descripcion = result.getString("descripcion");
                int idDocente = -10;
                if(result.getInt("idDocente") != 0) {
                    idDocente = result.getInt("idDocente");
                }
                int idTutor = -10;
                if(result.getInt("idTutor") != 0) {
                    idTutor = result.getInt("idTutor");
                }

                int idInforme = result.getInt("idInformeFinal");

                Docente docente = new DocenteDAODB().buscarByID(idDocente);
                TutorExterno tutor = new TutorExternoDAODB().buscarByID(idTutor);

                PlanDeTrabajo plan = new PlanDeTrabajo(idPlan, titulo, descripcion, docente, tutor);

                // Buscar informe final si existe
                if (idInforme != 0) {
                    Informe informeFinal = new InformeDAODB().buscarByID(idInforme);
                    plan.setInformeFinal(informeFinal);
                }

                // Cargar actividades relacionadas
                List<Actividad> actividades = buscarActividades(idPlan);
                plan.setActividades(actividades);

                plan.setHabilitado(result.getBoolean("habilitado"));

                result.close();
                return plan;

            } else {
                throw new ReadException("No se encontró el plan de trabajo en la base de datos");
            }

        } catch (ConnectionException e) {
            throw new ReadException(e.getMessage());
        } catch (SQLException e) {
            throw new ReadException("Error al leer el plan de trabajo: " + e.getMessage());
        } catch (EmptyException | UserException e) {
            e.printStackTrace();
            throw new ReadException("Error al construir el plan de trabajo: " + e.getMessage());
        }
    }

    @Override
    public List<PlanDeTrabajo> obtenerPlanes() throws ReadException {
        List<PlanDeTrabajo> planes = new ArrayList<>();
        String sql = "SELECT * FROM PlanesDeTrabajos";

        try (Connection conn = connect();
             PreparedStatement statement = conn.prepareStatement(sql);
             ResultSet result = statement.executeQuery()) {

            while (result.next()) {
                int idPlan = result.getInt("idPlanDeTrabajo");
                String titulo = result.getString("titulo");
                String descripcion = result.getString("descripcion");
                int idDocente = result.getInt("idDocente");
                int idTutor = result.getInt("idTutor");
                int idInforme = result.getInt("idInformeFinal");

                Docente docente = new DocenteDAODB().buscarByID(idDocente);
                TutorExterno tutor = new TutorExternoDAODB().buscarByID(idTutor);

                PlanDeTrabajo plan = new PlanDeTrabajo(idPlan, titulo, descripcion, docente, tutor);

                // Buscar informe final si existe
                if (idInforme != 0) {
                    Informe informeFinal = new InformeDAODB().buscarByID(idInforme);
                    plan.setInformeFinal(informeFinal);
                }

                // Cargar actividades relacionadas
                List<Actividad> actividades = buscarActividades(idPlan);
                plan.setActividades(actividades);

                plan.setHabilitado(result.getBoolean("habilitado"));

                planes.add(plan);
            }

            return planes;

        } catch (ConnectionException e) {
            throw new ReadException(e.getMessage());
        } catch (SQLException e) {
            throw new ReadException("Error al leer los planes de trabajo: " + e.getMessage());
        } catch (EmptyException | UserException e) {
            throw new ReadException("Error al construir el plan de trabajo: " + e.getMessage());
        }
    }


    @Override
    public boolean validarTituloUnico(String titulo) throws ReadException {
        String sql = "SELECT COUNT(*) AS total FROM PlanesDeTrabajos WHERE titulo = ?";

        try (Connection conn = connect();
             PreparedStatement statement = conn.prepareStatement(sql)) {

            statement.setString(1, titulo);
            ResultSet result = statement.executeQuery();

            if (result.next() && result.getInt("total") != 0) {
                throw new ReadException("Plan de trabajo con titulo insertado existente en el sistema.");
            }
            statement.close();
            result.close();
            return true;
        } catch (SQLException e) {
            throw new ReadException("Error SQL al validar título único: " + e.getMessage());
        } catch (ConnectionException e) {
            throw new ReadException(e.getMessage());
        }
    }


    @Override
    public PlanDeTrabajo buscarByTitulo(String titulo) throws ReadException {
        String sql = "SELECT * FROM PlanesDeTrabajos WHERE titulo = ?";

        try (Connection conn = connect();
             PreparedStatement statement = conn.prepareStatement(sql)) {

            statement.setString(1, titulo);
            ResultSet result = statement.executeQuery();

            if (result.next()) {
                int idPlan = result.getInt("idPlanDeTrabajo");
                String descripcion = result.getString("descripcion");
                int idDocente = result.getInt("idDocente");
                int idTutor = result.getInt("idTutor");
                int idInforme = result.getInt("idInformeFinal");

                Docente docente = new DocenteDAODB().buscarByID(idDocente);
                TutorExterno tutor = new TutorExternoDAODB().buscarByID(idTutor);

                PlanDeTrabajo plan = new PlanDeTrabajo(idPlan, titulo, descripcion, docente, tutor);

                // Buscar informe final si existe
                if (idInforme != 0) {
                    Informe informeFinal = new InformeDAODB().buscarByID(idInforme);
                    plan.setInformeFinal(informeFinal);
                }

                // Cargar actividades relacionadas
                List<Actividad> actividades = buscarActividades(idPlan);
                plan.setActividades(actividades);

                plan.setHabilitado(result.getBoolean("habilitado"));

                result.close();
                return plan;

            } else {
                throw new ReadException("No se encontró el plan de trabajo en la base de datos");
            }

        } catch (ConnectionException e) {
            throw new ReadException(e.getMessage());
        } catch (SQLException e) {
            throw new ReadException("Error al leer el plan de trabajo: " + e.getMessage());
        } catch (EmptyException | UserException e) {
            throw new ReadException("Error al construir el plan de trabajo: " + e.getMessage());
        }
    }


    public List<Actividad> buscarActividades(int idPlan) throws ReadException {
        List<Actividad> actividades = new ArrayList<>();

        String sql = "SELECT idActividad FROM Relacion_PlanDeTrabajo_Actividades WHERE idPlan = ?";

        try (Connection conn = connect();
             PreparedStatement statement = conn.prepareStatement(sql)) {

            statement.setInt(1, idPlan);

            try (ResultSet result = statement.executeQuery()) {
                ActividadDAODB actividadDAO = new ActividadDAODB();

                while (result.next()) {
                    int idActividad = result.getInt("idActividad");
                    Actividad actividad = actividadDAO.buscarByID(idActividad);
                    actividades.add(actividad);
                }
            }

            return actividades;

        } catch (SQLException e) {
            throw new ReadException("Error SQL al obtener las actividades del plan." + e.getMessage());
        } catch (ConnectionException e) {
            throw new ReadException(e.getMessage());
        }
    }

    public void update(PlanDeTrabajo plan) throws CreateException {
        String sql = "UPDATE PlanesDeTrabajos SET titulo = ?, descripcion = ?, idDocente = ?, idTutor = ?, " +
                "idInformeFinal = ?, habilitado = ? WHERE idPlanDeTrabajo = ?";

        try (Connection conn = connect();
             PreparedStatement statement = conn.prepareStatement(sql)) {

            statement.setString(1, plan.getTitulo());
            statement.setString(2, plan.getDescripcion());
            statement.setInt(3, plan.getDocente().getIdUsuario());
            statement.setInt(4, plan.getTutor().getIdUsuario());

            if (plan.getInformeFinal().getID() == 0) {
                statement.setNull(5, Types.INTEGER);
            }
            else if (plan.getInformeFinal() != null) {
                statement.setInt(5, plan.getInformeFinal().getID());
            }
            else {
                statement.setNull(5, Types.INTEGER);
            }

            statement.setBoolean(6, plan.isHabilitado());
            statement.setInt(7, plan.getID());

            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated == 0) {
                throw new CreateException("No se encontró el plan de trabajo con ID: " + plan.getID());
            }

            // Antes del bucle de inserción
            eliminarRelacionesDelPlan(conn, plan.getID());

            // Luego insertás nuevamente todas las actividades como hacías
            for (Actividad act : plan.getActividades()) {
                actualizarRelacionActividad(conn, plan.getID(), act.getID());
            }


        } catch (SQLException e) {
            throw new CreateException("Error SQL al actualizar el plan de trabajo: " + e.getMessage());
        } catch (ConnectionException | EmptyException e) {
            throw new CreateException(e.getMessage());
        }
    }

    private void eliminarRelacionesDelPlan(Connection conn, int idPlan) throws SQLException {
        String sql = "DELETE FROM relacion_plandetrabajo_actividades WHERE idPlan = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idPlan);
            stmt.executeUpdate();
        }
    }


    private void actualizarRelacionActividad(Connection conn, int idPlan, int idActividad) throws SQLException {
        String sql = "INSERT INTO relacion_plandetrabajo_actividades (idPlan, idActividad) VALUES (?, ?) " +
                "ON DUPLICATE KEY UPDATE idActividad = idActividad"; // no hace nada si ya existe
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idPlan);
            stmt.setInt(2, idActividad);
            stmt.executeUpdate();
        }
    }


}
