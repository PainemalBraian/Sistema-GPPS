package Frontend.com.main;

import Backend.API.API;
import Backend.API.PersistanceAPI;
import Backend.DAO.dom.elementos.InformeDAODB;
import Backend.DAO.dom.elementos.PlanDeTrabajoDAODB;
import Backend.DTO.*;
import Backend.Entidades.Informe;
import Backend.Entidades.Rol;
import Backend.Exceptions.CreateException;
import Backend.Exceptions.EmptyException;
import Backend.Exceptions.ReadException;
import Frontend.com.gui.Controller.IngresoController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Main extends Application {

    @Override
    public void start(Stage pantalla) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Frontend/vistas/Ingreso.fxml"));
            Parent root = loader.load();

            // Obtener el controlador
            IngresoController controller = loader.getController();

            // Crear y pasar la instancia de PersistenceAPI
<<<<<<< HEAD
            API controlador = new PersistanceAPI() {};

            controller.setPersistenceAPI(controlador);
=======
            API api = new PersistanceAPI() {};

            controller.setPersistenceAPI(api);
>>>>>>> 6c4b88f60d8f438e5a20427d61cee662601a4be7
            Scene login = new Scene(root);
            pantalla.setResizable(false);
            pantalla.setScene(login);
            pantalla.show();
        }
        catch (Exception e) {
<<<<<<< HEAD
=======
            e.printStackTrace();
>>>>>>> 6c4b88f60d8f438e5a20427d61cee662601a4be7
            System.out.println(e.getMessage());
        }
    }
}
