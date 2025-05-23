package Backend.DAO.dom.elementos;

import Backend.DAO.DBAcces;
import Backend.DAO.dom.usuarios.TutorExternoDAODB;
import Backend.DAO.interfaces.elementos.INFORMEDAO;
import Backend.Entidades.Informe;
import Backend.Entidades.Proyecto;
import Backend.Exceptions.*;

import java.sql.*;
import java.util.ArrayList;
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
        List<Informe> informes = new ArrayList<>();

        try (Connection conn = connect();
             PreparedStatement statement = conn.prepareStatement("SELECT * FROM Informes");
             ResultSet result = statement.executeQuery()) {

            while (result.next()) {
                Informe informe = new Informe(
                        result.getInt("idInforme"),
                        result.getString("titulo"),
                        result.getString("descripcion"),
                        result.getString("contenido")
                );
                informe.setFecha(result.getDate("fecha").toLocalDate());
//                informe.setpdfresult.getDate("archivo_pdf")?
                informes.add(informe);
            }

            return informes;
        } catch (SQLException e) {
            throw new ReadException("Error al obtener los datos de los informes.");
        } catch (ConnectionException e) {
            throw new ReadException(e.getMessage());
        } catch (EmptyException e) {
            throw new ReadException(e.getMessage());
        }
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
