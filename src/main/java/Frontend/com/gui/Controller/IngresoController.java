package Frontend.com.gui.Controller;

import Backend.API.API;
import Backend.DTO.UsuarioDTO;
import Backend.Exceptions.LoginException;
import Frontend.com.gui.Controller.Docente.HomeDocenteController;
import Frontend.com.gui.Controller.Estudiante.HomeController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ResourceBundle;

public class IngresoController {
    API api;
    @FXML public Button ButtonRegistro;
    @FXML public Pane PaneIMG;
    @FXML public AnchorPane PaneBase;
    @FXML public TextField TextFieldUsuario;
    @FXML public PasswordField PasswordFieldClave;
    @FXML public Label LabelUsuario;
    @FXML public Label LabelContrasena;
    @FXML public Label LabelIngreso;
    @FXML public Button ButtonIngresar;
    @FXML public ImageView IMGIngreso;
    @FXML public MenuButton MenuIdiomas;
    @FXML public Label newUser;
    public ImageView IconLogin;
    @FXML public Button ButtonIngresarEstudiante;
    @FXML public Button ButtonIngresarDocente;
    public Label LabelGPPS;

    @FXML
    public void initialize() {
     //   actualizarIdioma();
    }

    public void idiomaEN(ActionEvent actionEvent) {
        api.cambiarIdioma("en");
        actualizarIdioma();
    }
    public void idiomaES(ActionEvent actionEvent) {
        api.cambiarIdioma("es");
        actualizarIdioma();
    }
    public void idiomaPT(ActionEvent actionEvent) {
        api.cambiarIdioma("pt");
        actualizarIdioma();
    }
    public void idiomaIT(ActionEvent actionEvent) {
        api.cambiarIdioma("it");
        actualizarIdioma();
    }

    public void setPersistenceAPI(API persistenceAPI) {
        this.api = persistenceAPI;
        actualizarIdioma();
    }

    private void actualizarIdioma() {
        ResourceBundle bundle = api.obtenerIdioma();
        LabelUsuario.setText(bundle.getString("label.usuario"));
        LabelContrasena.setText(bundle.getString("label.contrasena"));
        LabelIngreso.setText(bundle.getString("label.ingreso"));
        ButtonIngresar.setText(bundle.getString("button.ingresar"));
        ButtonRegistro.setText(bundle.getString("button.registro"));
        PasswordFieldClave.setPromptText(bundle.getString("field.clave"));
        TextFieldUsuario.setPromptText(bundle.getString("field.usuario"));
        newUser.setText(bundle.getString("field.newUser"));
        MenuIdiomas.setText(bundle.getString("button.idioma"));
        ButtonIngresarEstudiante.setText(bundle.getString("button.IngresarEstudiante"));
        ButtonIngresarDocente.setText(bundle.getString("button.IngresarDocente"));
        LabelGPPS.setText(bundle.getString("label.GPPS"));
    }

    @FXML
    public void abrirRegistro(ActionEvent event) {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Frontend/vistas/registro.fxml"));
            Parent root = loader.load();

            // Obtener el controlador
            RegistroController controller = loader.getController();

            // Crear y pasar la instancia de PersistenceAPI
            controller.setPersistenceAPI(api);

            Stage stage = new Stage();
            stage.setTitle("Registro - GPPS");
            stage.setScene(new Scene(root));
            stage.show();
            // Opcional: cerrar la ventana actual
            ((Stage)((Button)event.getSource()).getScene().getWindow()).close();

        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta("Error I/O", "" + e.getMessage());

        } catch (Exception e) {
            mostrarAlerta("Error", "" + e.getMessage());
        }
    }

    @FXML
    public void loginUsuario(ActionEvent event) {
        String username = TextFieldUsuario.getText();
        String password = PasswordFieldClave.getText();

        try {
            UsuarioDTO usuario = api.login(username, password);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Frontend/vistas/Estudiante/home.fxml"));
            Parent root = loader.load();

            // Obtener el controlador de la vista principal
            HomeController controllerHome = loader.getController();
            controllerHome.setPersistenceAPI(api);

            Stage stage = new Stage();
            stage.setTitle("Principal - GPPS");
            stage.setScene(new Scene(root));
            stage.show();

            // Cerrar la ventana actual de ingreso
            ((Stage)((Button)event.getSource()).getScene().getWindow()).close();

        } catch (LoginException e) {
            mostrarAlerta("Error", "" + e.getMessage());


        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta("Error I/O", "" + e.getMessage());

        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Error", "" + e.getMessage());
        }
    }

    public void IngresarComoEstudiante(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Frontend/vistas/Estudiante/home.fxml"));
            Parent root = loader.load();

            // Obtener el controlador de la vista principal
            HomeController controllerHome = loader.getController();
            controllerHome.setPersistenceAPI(api);

            Stage stage = new Stage();
            stage.setTitle("Principal - GPPS");
            stage.setScene(new Scene(root));
            stage.show();

            // Cerrar la ventana actual de ingreso
            ((Stage)((Button)actionEvent.getSource()).getScene().getWindow()).close();

        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Error", "" + e.getMessage());
        }
    }
    public void IngresarComoDocente(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Frontend/vistas/Docente/homeDocente.fxml"));
            Parent root = loader.load();

            // Obtener el controlador de la vista principal
            HomeDocenteController controllerHome = loader.getController();
            controllerHome.setPersistenceAPI(api);

            Stage stage = new Stage();
            stage.setTitle("Principal - GPPS");
            stage.setScene(new Scene(root));
            stage.show();

            // Cerrar la ventana actual de ingreso
            ((Stage)((Button)actionEvent.getSource()).getScene().getWindow()).close();

        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Error", "" + e.getMessage());
        }
    }


    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }


}
