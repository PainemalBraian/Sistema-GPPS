package Frontend.com.main;

import Backend.API.PersistanceAPI;
import Backend.Exceptions.RegisterExceptions;
import Backend.Exceptions.UserExceptions;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class RegistroController {

    @FXML
    private TextField nombreField;
    @FXML
    private TextField correoField;
    @FXML
    private PasswordField contrasenaField;
    @FXML
    private PasswordField confirmarContrasenaField;
    @FXML
    private ComboBox<String> rolComboBox;

    @FXML
    private VBox camposEstudiante;
    @FXML
    private TextField matriculaField;
    @FXML
    private TextField carreraField;

    @FXML
    private VBox camposDocente;
    @FXML
    private TextField legajoField;

    @FXML
    private VBox camposEntidad;
    @FXML
    private TextField nombreEntidadField;
    @FXML
    private TextField cuitField;
    @FXML
    private TextField direccionEntidadField;

    public void initialize() {
        rolComboBox.setOnAction((ActionEvent event) -> {
            String selectedRol = rolComboBox.getValue();
            camposEstudiante.setVisible(selectedRol != null && selectedRol.equals("Estudiante"));
            camposDocente.setVisible(selectedRol != null && selectedRol.equals("Docente"));
            boolean esEntidadOTutor = "Representante de Entidad Colaboradora".equals(selectedRol) ||
                    "Tutor externo".equals(selectedRol);
            camposEntidad.setVisible(esEntidadOTutor);
        });
    }

    @FXML
    public void volverLogin(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Frontend/vistas/Ingreso.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle(" Login - GPPS");
            stage.setScene(new Scene(root));
            stage.show();

            // Opcional: cerrar la ventana actual
            ((Stage) ((Button) event.getSource()).getScene().getWindow()).close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void registrarse() {
        try {
            PersistanceAPI api = new PersistanceAPI();
            api.registrarUsuario(
                    nombreField.getText(),                    // username
                    contrasenaField.getText(),                // password
                    correoField.getText(),                    // email
                    nombreField.getText(),                    // nombre
                    Integer.parseInt(rolComboBox.getValue()), // rolId
                    matriculaField.getText(),
                    carreraField.getText(),
                    legajoField.getText(),
                    nombreEntidadField.getText(),
                    cuitField.getText(),
                    direccionEntidadField.getText()
            );
            System.out.println("Usuario registrado exitosamente");
        } catch (RegisterExceptions | UserExceptions e) {
            System.err.println(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}