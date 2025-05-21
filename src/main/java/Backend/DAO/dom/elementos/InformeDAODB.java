package Backend.DAO.dom.elementos;

import Backend.DAO.DBAcces;
import Backend.DAO.interfaces.elementos.INFORMEDAO;
import Backend.Entidades.Informe;
import Backend.Exceptions.ConnectionException;
import Backend.Exceptions.CreateException;
import Backend.Exceptions.DeleteException;
import Backend.Exceptions.ReadException;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class InformeDAODB extends DBAcces implements INFORMEDAO {
    @Override
    public void create(Informe informe) throws CreateException {
        try (Connection conn = connect();
             PreparedStatement statement = conn.prepareStatement(
                     "INSERT INTO Informes(titulo, descripcion, contenido,fecha) VALUES (?, ?, ?,?)"
             )) {

            statement.setString(1, informe.getTitulo());
            statement.setString(2, informe.getDescripcion());
            statement.setString(3, informe.getContenido());
            statement.setDate(4, Date.valueOf(informe.getFecha()));

            statement.executeUpdate();

        }catch(ConnectionException e){
            throw new CreateException("Error al conectar con la base de datos: " + e.getMessage());
        }catch(SQLException e){
            throw new CreateException("Error al crear el proyecto: " + e.getMessage());
        }
    }

    @Override
    public void delete(int id) throws DeleteException {

    }

    @Override
    public List<Informe> obtenerInformes() throws ReadException {
        return List.of();
    }

    @Override
    public Informe buscarByID(int id) throws ReadException {
        return null;
    }

    @Override
    public Informe buscarByTitulo(String titulo) throws ReadException {
        return null;
    }

    @Override
    public boolean validarTituloUnico(String titulo) throws ReadException {
        return false;
    }

}
