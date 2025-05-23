package Frontend.com.gui.Controller;

import Backend.API.API;
import Backend.DTO.ProyectoDTO;
import Backend.Exceptions.CreateException;
import Backend.Exceptions.ReadException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;



public class PropuestaPropiaController {

    @FXML private TextField tituloField;
    @FXML private TextArea descripcionArea;
    @FXML private TextField areaInteresField;
    @FXML private TextField ubicacionField;
    @FXML private TextArea objetivosArea;
    @FXML private TextArea requisitosArea;
    @FXML private Button btnRegistrarPropuesta;

    private API api;

    public void setApi(API api) {
        this.api = api;
    }

    @FXML
    public void initialize() throws Exception {
        setPersistenceAPI(api);
    }
    public void setPersistenceAPI(API persistenceAPI) throws Exception {
        this.api = persistenceAPI;
    }
    @FXML
    private void handleRegistrarPropuesta() {
        try {
            // Obtener y validar datos del formulario
            String titulo = tituloField.getText().trim();
            String descripcion = descripcionArea.getText().trim();
            String areaDeInteres = areaInteresField.getText().trim();
            String ubicacion = ubicacionField.getText().trim();
            String objetivos = objetivosArea.getText().trim();
            String requisitos = requisitosArea.getText().trim();

            // metodo de la persistencia que crea el proyecto
            this.api.cargarPropuestaPropia(titulo, descripcion, areaDeInteres, ubicacion, objetivos, requisitos);

            // Mostrar éxito
            mostrarAlerta("Registro Exitoso", "La propuesta fue registrada correctamente.", Alert.AlertType.INFORMATION);
            limpiarCampos();

        } catch (CreateException e) {
            mostrarAlerta("Error al Registrar", "No se pudo registrar el proyecto: " + e.getMessage(), Alert.AlertType.ERROR);
        } catch (Exception e) {
            mostrarAlerta("Error Inesperado", "Ocurrió un error inesperado: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void limpiarCampos() {
        tituloField.clear();
        descripcionArea.clear();
        areaInteresField.clear();
        ubicacionField.clear();
        objetivosArea.clear();
        requisitosArea.clear();
        // if (tutorEntidadColaborativaField != null) tutorEntidadColaborativaField.clear();
    }

    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    @FXML
    public void VolverHome(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Frontend/vistas/home.fxml"));
            Parent root = loader.load();
            HomeController controller = loader.getController();

            // Pasar la instancia de API (asumiendo que la tienes disponible aquí)
            controller.setPersistenceAPI(this.api); // Asegúrate que 'api' esté inicializada

            Stage stage = new Stage();
            stage.setTitle("Home - GPPS");
            stage.setScene(new Scene(root));
            stage.show();

            ((Stage) ((Button) event.getSource()).getScene().getWindow()).close();

        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta("Error de Carga", "No se pudo cargar la vista principal.", Alert.AlertType.ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Error Inesperado", "Ocurrió un error al intentar volver al home: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }
}