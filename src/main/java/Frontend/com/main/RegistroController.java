package Frontend.com.main;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

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
    private VBox camposEntidad;
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
            camposEntidad.setVisible(selectedRol != null && selectedRol.equals("Representante de Entidad Colaboradora"));
        });
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
        String nombreEntidad = camposEntidad.isVisible() ? nombreEntidadField.getText() : "";
        String cuit = camposEntidad.isVisible() ? cuitField.getText() : "";
        String nombreContacto = camposEntidad.isVisible() ? nombreContactoField.getText() : "";

        System.out.println("Registrando a: " + nombre + ", Correo: " + correo + ", Rol: " + rol);
        if (!matricula.isEmpty()) System.out.println("Matrícula: " + matricula + ", Carrera: " + carrera);
        if (!legajo.isEmpty()) System.out.println("Legajo: " + legajo + ", Departamento: " + departamento);
        if (!nombreEntidad.isEmpty()) System.out.println("Entidad: " + nombreEntidad + ", CUIT: " + cuit + ", Contacto: " + nombreContacto);

        //ógica para guardar los datos del usuario
    }
}