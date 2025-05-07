package Frontend.com.main;

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
