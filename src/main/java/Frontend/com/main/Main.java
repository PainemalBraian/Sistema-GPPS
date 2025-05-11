package Frontend.com.main;

import Backend.API.API;
import Backend.API.PersistanceAPI;
import Frontend.com.gui.Controller.IngresoController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage pantalla) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Frontend/vistas/ingreso.fxml"));
            Parent root = loader.load();

            // Obtener el controlador
            IngresoController controller = loader.getController();

            // Crear y pasar la instancia de PersistenceAPI
            API api = new PersistanceAPI() {};
            controller.setPersistenceAPI(api);

            Scene login = new Scene(root);
            pantalla.setResizable(false);
            pantalla.setScene(login);
            pantalla.show();

        } catch (IOException e) {
            System.err.println("Error al cargar la ventana: " + e.getMessage());
            e.printStackTrace();
        }
    }


}
