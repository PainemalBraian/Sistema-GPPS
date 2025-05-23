package Backend.DAO.dom.elementos;

import Backend.DAO.DBAcces;
import Backend.DAO.interfaces.elementos.ACTIVIDADDAO;
import Backend.Entidades.Actividad;
import Backend.Exceptions.ConnectionException;
import Backend.Exceptions.CreateException;
import Backend.Exceptions.DeleteException;
import Backend.Exceptions.ReadException;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
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
            throw new CreateException("Error al conectar con la base de datos: " + e.getMessage());
        }catch(SQLException e){
            throw new CreateException("Error al crear la Actividad: " + e.getMessage());
        }
    }

    @Override
    public void delete(int id) throws DeleteException {

    }

    @Override
    public Actividad buscarByID(int id) throws ReadException {
        return null;
    }

    @Override
    public List<Actividad> obtenerActividades() throws ReadException {
        return List.of();
    }

    @Override
    public boolean validarTituloUnico(String titulo) throws ReadException {
        return false;
    }

    @Override
    public Actividad buscarByTitulo(String titulo) throws ReadException {
        return null;
    }

    public List<Actividad> obtenerActividadesHabilitadas() {
        return null;
    }
}
