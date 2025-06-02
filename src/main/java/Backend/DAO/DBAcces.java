package Backend.DAO;

import Backend.Exceptions.ConnectionException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.ResourceBundle;

public class DBAcces {
    protected Connection conn = null;
    protected static Properties prop = null;

    private static Properties getProperties() throws ConnectionException {
        Properties prop = new Properties();
        try {
            ResourceBundle infoDataBase = ResourceBundle.getBundle("Backend.database");
            prop.setProperty("connection", infoDataBase.getString("db.url"));
            prop.setProperty("username", infoDataBase.getString("db.user"));
            prop.setProperty("password", infoDataBase.getString("db.password"));
        } catch (Exception e) {
            throw new ConnectionException("Ocurrio un error al leer la configuracion desde el archivo");
        }
        return prop;
    }

    protected Connection connect() throws ConnectionException {
        try {
            Properties prop = getProperties();
            Connection conn = DriverManager.getConnection(
                    prop.getProperty("connection"),
                    prop.getProperty("username"),
                    prop.getProperty("password")
            );

            conn.setAutoCommit(true);

            return conn;
        } catch (SQLException e) {
            throw new ConnectionException("Error al conectar con base de datos");
        }
    }

    public void disconnect() throws ConnectionException {
        if (conn != null) {
            try {
                conn.close();
                conn = null;
            } catch (SQLException e) {
                throw new ConnectionException("Error al cerrar la sesion");
            }
        }
    }
}
