package Frontend.com.main;


import Backend.API.API;
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

import java.util.ResourceBundle;

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

    API api;
  
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
            // Obtener el controlador
            IngresoController controller = loader.getController();

            // Crear y pasar la instancia de PersistenceAPI
            controller.setPersistenceAPI(api);

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

    public void setPersistenceAPI(API persistenceAPI) {
        this.api = persistenceAPI;
        actualizarIdioma();
    }
    private void actualizarIdioma() {
        ResourceBundle bundle = api.obtenerIdioma();

        // Labels
        nombreField.setPromptText(bundle.getString("label.nombre"));
        correoField.setPromptText(bundle.getString("label.correo"));
        contrasenaField.setPromptText(bundle.getString("label.contrasena"));
        confirmarContrasenaField.setPromptText(bundle.getString("label.confirmarContrasena"));
        matriculaField.setPromptText(bundle.getString("label.matricula"));
        carreraField.setPromptText(bundle.getString("label.carrera"));
        legajoField.setPromptText(bundle.getString("label.legajo"));
        nombreEntidadField.setPromptText(bundle.getString("label.nombreEntidad"));
        cuitField.setPromptText(bundle.getString("label.cuit"));
        // ComboBox
        rolComboBox.setPromptText(bundle.getString("combo.rol"));
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
    private void registrarse(ActionEvent actionEvent) {

        String nombre = nombreField.getText();
        String correo = correoField.getText();
        String contrasena = contrasenaField.getText();
        String confirmarContrasena = confirmarContrasenaField.getText();


        String matricula = camposEstudiante.isVisible() ? matriculaField.getText() : "";
        String carrera = camposEstudiante.isVisible() ? carreraField.getText() : "";
        String legajo = camposDocente.isVisible() ? legajoField.getText() : "";
 
        String nombreEntidad = camposEntidad.isVisible() ? nombreEntidadField.getText() : "";
        String cuit = camposEntidad.isVisible() ? cuitField.getText() : "";


        RolDTO rol = rolComboBox.getValue();
        System.out.println(rol.getNombre() + rol.getId());
        try {
            PersistanceAPI api = new PersistanceAPI();
            api.registrarUsuario(
                    nombreField.getText(),
                    contrasenaField.getText(),
                    correoField.getText(),
                    nombreField.getText(),
                    rol.getId(),
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
