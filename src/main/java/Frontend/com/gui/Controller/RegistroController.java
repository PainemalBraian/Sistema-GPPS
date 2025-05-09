package Frontend.com.gui.Controller;


import Backend.API.API;
import Backend.API.PersistanceAPI;
import Backend.DTO.RolDTO;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

import java.util.ResourceBundle;

public class RegistroController {

    @FXML public Label registroField;
    @FXML public Button BottonRegistrarse;
    @FXML private TextField nombreField;
    @FXML private TextField correoField;
    @FXML private PasswordField contrasenaField;
    @FXML private PasswordField confirmarContrasenaField;
    @FXML private ComboBox<RolDTO> rolComboBox;
    @FXML private VBox camposEstudiante;
    @FXML private TextField matriculaField;
    @FXML private TextField carreraField;
    @FXML private VBox camposDocente;
    @FXML private TextField legajoField;
    @FXML private VBox camposEntidad;
    @FXML private TextField nombreEntidadField;
    @FXML private TextField cuitField;
    @FXML private TextField dni;
    @FXML private Button volver_login;
    API api;
  
    @FXML
    private TextField direccionEntidadField;

    public void initialize() throws Exception {
        rolComboBox.setOnAction((ActionEvent event) -> {
            RolDTO selectedRol = rolComboBox.getValue();
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

    public void setPersistenceAPI(API persistenceAPI) throws Exception {
        this.api = persistenceAPI;
        actualizarIdioma();
        inicializarRoles();
    }
    private void actualizarIdioma() {
        ResourceBundle bundle = api.obtenerIdioma();

        // Labels
        registroField.setText(bundle.getString("label.registro"));
        dni.setPromptText(bundle.getString("label.dni"));
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
        //Botones
        BottonRegistrarse.setText(bundle.getString("button.registrarse"));
        volver_login.setText(bundle.getString("button.volver"));

    }
    private void inicializarRoles() throws Exception {
        rolComboBox.getItems().clear(); // Evita duplicados si se llama varias veces
        List<RolDTO> roles = api.obtenerRoles();
        rolComboBox.getItems().addAll(roles);

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

        try {
            PersistanceAPI api = new PersistanceAPI();
            api.registrarUsuario(
                    nombre,
                    contrasena,
                    correo,
                    nombre,
                    rol.getId(),
                    matricula,
                    carrera,
                    legajo,
                    nombreEntidad,
                    cuit,
                    direccionEntidadField.getText()
            );
            volverLogin(actionEvent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




}
