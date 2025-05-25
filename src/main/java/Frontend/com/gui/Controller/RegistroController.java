package Frontend.com.gui.Controller;

import Backend.API.API;
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
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import static java.util.Objects.isNull;

public class RegistroController {
    API api;
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
    @FXML private TextField direccionEntidadField;

    public void initialize() throws Exception {
        cargarComboBoxRoles();
        setearLogicaDeCampos();
    }


    private void cargarComboBoxRoles() {
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

    private void setearLogicaDeCampos() {
        // Agregar un ChangeListener para permitir solo números y máximo de 6 dígitos
        matriculaField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d{0,6}")) {
                matriculaField.setText(oldValue);
            }
        });
        legajoField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d{0,6}")) {
                legajoField.setText(oldValue);
            }
        });
        cuitField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d{0,11}")) {
                cuitField.setText(oldValue);
            }
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
            mostrarAlerta("Error", "Ocurrió un error al intentar volver\n" + e.getMessage());
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
        direccionEntidadField.setPromptText(bundle.getString("label.direccionEntidad"));
        cuitField.setPromptText(bundle.getString("label.cuit"));

        // Botones
        BottonRegistrarse.setText(bundle.getString("button.registrarse"));
        volver_login.setText(bundle.getString("button.volver"));

        rolComboBox.setPromptText(bundle.getString("combo.rol"));

        rolComboBox.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(RolDTO rol, boolean empty) {
                super.updateItem(rol, empty);
                if (empty || rol == null) {
                    setText(null);
                } else {
                    setText(obtenerNombreRolTraducido(rol, bundle));
                }
            }
        });

        rolComboBox.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(RolDTO rol, boolean empty) {
                super.updateItem(rol, empty);
                if (empty || rol == null) {
                    setText(null);
                } else {
                    setText(obtenerNombreRolTraducido(rol, bundle));
                }
            }
        });
    }

    private void inicializarRoles() throws Exception {
        rolComboBox.getItems().clear();
        List<RolDTO> roles = api.obtenerRoles();

        List<RolDTO> rolesElegibles = roles.stream()
                .filter(rol -> !rol.getNombre().equalsIgnoreCase("Administrador"))
                .filter(rol -> !rol.getNombre().equalsIgnoreCase("Director de carrera"))
                .filter(rol -> !rol.getNombre().equalsIgnoreCase("Tutor externo"))
                .toList();

        rolComboBox.getItems().addAll(rolesElegibles);

        ResourceBundle bundle = api.obtenerIdioma();

        rolComboBox.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(RolDTO rol, boolean empty) {
                super.updateItem(rol, empty);
                if (empty || rol == null) {
                    setText(null);
                } else {
                    setText(obtenerNombreRolTraducido(rol, bundle));
                }
            }
        });

        rolComboBox.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(RolDTO rol, boolean empty) {
                super.updateItem(rol, empty);
                if (empty || rol == null) {
                    setText(null);
                } else {
                    setText(obtenerNombreRolTraducido(rol, bundle));
                }
            }
        });
    }


    private String obtenerNombreRolTraducido(RolDTO rol, ResourceBundle bundle) {
        try {
            // Normalizar el nombre del rol para la clave
            String nombreNormalizado = rol.getNombre().trim();
            String clave = "rol." + nombreNormalizado;
            return bundle.getString(clave);
        } catch (MissingResourceException e) {
            return rol.getNombre(); // fallback si no se encuentra la clave
        }
    }

    @FXML
    private void registrarse(ActionEvent actionEvent) {
        String nombre = nombreField.getText();
        String correo = correoField.getText();
        String contrasena = contrasenaField.getText();
        String confirmarContrasena = confirmarContrasenaField.getText();
        String matriculaText = camposEstudiante.isVisible() ? matriculaField.getText() : "";

        String carrera = camposEstudiante.isVisible() ? carreraField.getText() : "";
        String legajo = camposDocente.isVisible() ? legajoField.getText() : "";

        String nombreEntidad = camposEntidad.isVisible() ? nombreEntidadField.getText() : "";
        String cuit = camposEntidad.isVisible() ? cuitField.getText() : "";

        RolDTO rol = rolComboBox.getValue();

        try {
            if (isNull(rol))
                throw new RuntimeException("Debes seleccionar un rol");
            if (!contrasena.equals(confirmarContrasena))
                throw new RuntimeException("Las contraseñas deben coincidir");
            api.registrarUsuario(
                    nombre,
                    contrasena,
                    correo,
                    nombre,
                    rol.getId(),
                    matriculaText,
                    carreraField.getText(),
                    legajoField.getText(),
                    nombreEntidadField.getText(),
                    cuitField.getText(),
                    direccionEntidadField.getText()
            );
            mostrarAlerta("Registro Exitoso", "Usuario registrado exitosamente.");
        } catch (Exception e) {
            mostrarAlerta("Error de Registro", "Ocurrió un error al registrar el usuario:\n" + e.getMessage());
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
