package Frontend.com.gui;

import Backend.API.API;
import Backend.DTO.UsuarioDTO;
import Backend.Exceptions.LoginException;
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
    public ImageView IconLogin;
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
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void loginUsuario(ActionEvent event) {
        String username = TextFieldUsuario.getText();
        String password = PasswordFieldClave.getText();

        try {
            UsuarioDTO usuario = api.login(username, password);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Frontend/vistas/home.fxml"));
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
            // Mostrar alerta si el login falla
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error de inicio de sesión");
            alert.setHeaderText("Credenciales inválidas");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

}
