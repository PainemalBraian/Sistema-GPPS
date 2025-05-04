package Frontend.com.main;

import Backend.API.API;
import Backend.API.PersistanceAPI;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.util.ResourceBundle;

public class RegistroController {

    API api = new PersistanceAPI() {};

    @FXML public TextField nombreField;
    @FXML public TextField correoField;
    @FXML public TextField matriculaField;
    @FXML public PasswordField contrasenaField;
    @FXML public PasswordField confirmarContrasenaField;
    @FXML public ComboBox<String> rolComboBox;
    @FXML public Button registrarseButton;

    @FXML public Label labelTitulo;

    @FXML
    public void initialize() {
        actualizarIdioma();
    }

    @FXML
    public void registrarse(ActionEvent event) {
        String nombre = nombreField.getText();
        String correo = correoField.getText();
        String matricula = matriculaField.getText();
        String contrasena = contrasenaField.getText();
        String confirmar = confirmarContrasenaField.getText();
        String rol = rolComboBox.getValue();
/*
        if (nombre.isEmpty() || correo.isEmpty() || matricula.isEmpty() ||
                contrasena.isEmpty() || confirmar.isEmpty() || rol == null) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "Todos los campos son obligatorios.");
            return;
        }

        if (!contrasena.equals(confirmar)) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "Las contraseñas no coinciden.");
            return;
        } */

        // Aquí podrías llamar a tu backend para registrar al usuario
        //boolean exito = api.registrarUsuario(nombre, correo, matricula, contrasena, rol); // Suponiendo que tengas esto
/*
        if (exito) {
            mostrarAlerta(Alert.AlertType.INFORMATION, "Registro exitoso", "Usuario registrado correctamente.");
            limpiarCampos();
        } else {
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se pudo registrar el usuario.");
        } */
    }

    private void actualizarIdioma() {
        ResourceBundle bundle = api.obtenerIdioma();

        if (labelTitulo != null) labelTitulo.setText(bundle.getString("label.registroTitulo"));
        if (nombreField != null) nombreField.setPromptText(bundle.getString("field.nombre"));
        if (correoField != null) correoField.setPromptText(bundle.getString("field.correo"));
        if (matriculaField != null) matriculaField.setPromptText(bundle.getString("field.matricula"));
        if (contrasenaField != null) contrasenaField.setPromptText(bundle.getString("field.contrasena"));
        if (confirmarContrasenaField != null) confirmarContrasenaField.setPromptText(bundle.getString("field.confirmar"));
        if (rolComboBox != null) rolComboBox.setPromptText(bundle.getString("field.rol"));
        if (registrarseButton != null) registrarseButton.setText(bundle.getString("button.registrarse"));
    }
/*
    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    } */

    private void limpiarCampos() {
        nombreField.clear();
        correoField.clear();
        matriculaField.clear();
        contrasenaField.clear();
        confirmarContrasenaField.clear();
        rolComboBox.setValue(null);
    }
}
