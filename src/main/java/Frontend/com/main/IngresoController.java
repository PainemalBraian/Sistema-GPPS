package Frontend.com.main;

import Backend.API.API;
import Backend.API.PersistanceAPI;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import java.util.ResourceBundle;

public class IngresoController {
    API api = new PersistanceAPI() {};
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

    @FXML
    public void initialize() {
        actualizarIdioma();
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

    private void actualizarIdioma() {
        ResourceBundle bundle = api.obtenerIdioma();
        LabelUsuario.setText(bundle.getString("label.usuario"));
        LabelContrasena.setText(bundle.getString("label.contrasena"));
        LabelIngreso.setText(bundle.getString("label.ingreso"));
        ButtonIngresar.setText(bundle.getString("button.ingresar"));
        ButtonRegistro.setText(bundle.getString("button.registro"));
        PasswordFieldClave.setPromptText(bundle.getString("field.clave"));
        TextFieldUsuario.setPromptText(bundle.getString("field.usuario"));
    }


// Por ahora no hace nada, pero podés agregar métodos y @FXML si conectás botones o eventos
}
