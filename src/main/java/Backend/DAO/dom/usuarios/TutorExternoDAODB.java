package Backend.DAO.dom.usuarios;

import Backend.DAO.DBAcces;
import Backend.DAO.UsuarioDAODB;
import Backend.DAO.interfaces.usuarios.TUTOREXTERNODAO;
import Backend.Entidades.Estudiante;
import Backend.Exceptions.ConnectionException;
import Backend.Exceptions.RegisterExceptions;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class TutorExternoDAODB extends DBAcces implements TUTOREXTERNODAO {
    UsuarioDAODB UsuarioDAODB=new UsuarioDAODB();

    @Override
    public void create(Object tutor) throws RegisterExceptions {
        Estudiante estudiante = (Estudiante) tutor;
        try {
            Connection conn = connect();
            PreparedStatement statement = conn.prepareStatement(
                    "INSERT INTO Estudiantes(idUsuario, matricula, carrera) " +
                            "VALUES (?, ?, ?)"
            );
            // Guardar en la base de datos
            statement.setInt(1, UsuarioDAODB.create(estudiante));
            statement.setString(2, estudiante.getMatricula());
            statement.setString(3, estudiante.getCarrera());

            statement.executeUpdate();  // Cambié executeQuery() por executeUpdate()
            statement.close();
            disconnect();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RegisterExceptions("Error al crear y guardar el estudiante: " + e.getMessage());
        }catch(ConnectionException e){
            throw new RegisterExceptions(e.getMessage());
        }
    }
}
