package Frontend.com.main;

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
    private TextField departamentoField;

    @FXML
    private VBox camposEntidad_o_Tutor;
    @FXML
    private TextField nombreEntidadField;
    @FXML
    private TextField cuitField;
    @FXML
    private TextField nombreContactoField;

    public void initialize() {
        rolComboBox.setOnAction((ActionEvent event) -> {
            String selectedRol = rolComboBox.getValue();
            camposEstudiante.setVisible(selectedRol != null && selectedRol.equals("Estudiante"));
            camposDocente.setVisible(selectedRol != null && selectedRol.equals("Docente"));
            boolean esEntidadOTutor = "Representante de Entidad Colaboradora".equals(selectedRol) ||
                    "Tutor externo".equals(selectedRol);
            camposEntidad_o_Tutor.setVisible(esEntidadOTutor);
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
            ((Stage)((Button)event.getSource()).getScene().getWindow()).close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void registrarse() {
        String nombre = nombreField.getText();
        String correo = correoField.getText();
        String contrasena = contrasenaField.getText();
        String confirmarContrasena = confirmarContrasenaField.getText();
        String rol = rolComboBox.getValue();

        String matricula = camposEstudiante.isVisible() ? matriculaField.getText() : "";
        String carrera = camposEstudiante.isVisible() ? carreraField.getText() : "";
        String legajo = camposDocente.isVisible() ? legajoField.getText() : "";
        String departamento = camposDocente.isVisible() ? departamentoField.getText() : "";
        String nombreEntidad = camposEntidad_o_Tutor.isVisible() ? nombreEntidadField.getText() : "";
        String cuit = camposEntidad_o_Tutor.isVisible() ? cuitField.getText() : "";
        String nombreContacto = camposEntidad_o_Tutor.isVisible() ? nombreContactoField.getText() : "";

        System.out.println("Registrando a: " + nombre + ", Correo: " + correo + ", Rol: " + rol);
        if (!matricula.isEmpty()) System.out.println("Matrícula: " + matricula + ", Carrera: " + carrera);
        if (!legajo.isEmpty()) System.out.println("Legajo: " + legajo + ", Departamento: " + departamento);
        if (!nombreEntidad.isEmpty()) System.out.println("Entidad: " + nombreEntidad + ", CUIT: " + cuit + ", Contacto: " + nombreContacto);

        //ógica para guardar los datos del usuario
    }
}