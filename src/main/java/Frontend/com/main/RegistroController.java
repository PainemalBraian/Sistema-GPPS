package Frontend.com.main;

import Backend.API.PersistanceAPI;
import Backend.DTO.RolDTO;
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
import java.util.List;

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
    private ComboBox<RolDTO> rolComboBox;


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
        inicializarRoles(); // ✔ primero cargar los roles

        rolComboBox.setOnAction((ActionEvent event) -> {
            RolDTO selectedRol = rolComboBox.getValue(); // ✔ ya es un RolDTO
            String nombreRol = selectedRol != null ? selectedRol.getNombre() : "";

            camposEstudiante.setVisible(nombreRol.equals("Estudiante"));
            camposDocente.setVisible(nombreRol.equals("Docente"));

            boolean esEntidadOTutor = nombreRol.equals("Representante de Entidad Colaboradora") ||
                    nombreRol.equals("Tutor externo");
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


    private void inicializarRoles() {
        rolComboBox.getItems().clear(); // Evita duplicados si se llama varias veces

        rolComboBox.getItems().addAll(
                new RolDTO(1, "Estudiante", true),
                new RolDTO(2, "Docente", true),
                new RolDTO(3, "Representante de Entidad Colaboradora", true),
                new RolDTO(4, "Tutor externo", true)
        );
    }



    @FXML
    private void registrarse() {
        RolDTO rol = rolComboBox.getValue();

        try {
            PersistanceAPI api = new PersistanceAPI();
            api.registrarUsuario(
                    nombreField.getText(),
                    contrasenaField.getText(),
                    correoField.getText(),
                    nombreField.getText(),
                    rol.getId(), // ✅ aquí usás el ID directamente
                    matriculaField.getText(),
                    carreraField.getText(),
                    legajoField.getText(),
                    nombreEntidadField.getText(),
                    cuitField.getText(),
                    direccionEntidadField.getText()
            );
            System.out.println("Usuario registrado exitosamente");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}