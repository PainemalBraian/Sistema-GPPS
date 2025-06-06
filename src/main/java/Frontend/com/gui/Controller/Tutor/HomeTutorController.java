package Frontend.com.gui.Controller.Tutor;

import Backend.API.API;
<<<<<<< HEAD
=======
import Backend.Exceptions.UserException;
>>>>>>> 6c4b88f60d8f438e5a20427d61cee662601a4be7
import Frontend.com.gui.Controller.IngresoController;
import Frontend.com.gui.Controller.MensajesController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HomeTutorController {

    private static final Logger LOGGER = Logger.getLogger(HomeTutorController.class.getName());

    private API api;
    private ResourceBundle bundle;

    @FXML private Label lblBienvenida;
    @FXML private Label lblEmpresa;
    @FXML private Label lblProyectos;
    @FXML private Label lblSeguimiento;
    @FXML private Label lblEvaluacion;
    @FXML private Label lblMensajes;

    @FXML private Button btnRegistrarProyecto;
    @FXML private Button btnActualizarEmpresa;
    @FXML private Button btnSeguimiento;
    @FXML private Button btnEvaluarInformesEstudiante;
    @FXML private Button btnMensajes;
    @FXML private Button btnCerrarSesion;

    public void setPersistenceAPI(API persistenceAPI) throws Exception {
        this.api = persistenceAPI;
<<<<<<< HEAD
        //actualizarIdioma();
    }

    private void actualizarIdioma() {
        bundle = api.obtenerIdioma();

        lblBienvenida.setText(bundle.getString("label.bienvenida.tutor"));
=======
        lblBienvenida.setText(api.obtenerSesionDeUsuario().getNombre());
        //actualizarIdioma();
    }

    private void actualizarIdioma() throws UserException {
        bundle = api.obtenerIdioma();

        lblBienvenida.setText(bundle.getString("label.bienvenida.tutor")+ api.obtenerSesionDeUsuario().getNombre());
>>>>>>> 6c4b88f60d8f438e5a20427d61cee662601a4be7
        lblEmpresa.setText(bundle.getString("label.infoEmpresa"));
        lblProyectos.setText(bundle.getString("label.proyectosDisponibles"));
        lblSeguimiento.setText(bundle.getString("label.seguimientoEstudiantes"));
        lblEvaluacion.setText(bundle.getString("label.evaluarEstudiantes"));
        lblMensajes.setText(bundle.getString("label.mensajes"));

        btnRegistrarProyecto.setText(bundle.getString("button.registrarProyecto"));
        btnActualizarEmpresa.setText(bundle.getString("button.actualizarEmpresa"));
        btnSeguimiento.setText(bundle.getString("button.verSeguimiento"));
        btnEvaluarInformesEstudiante.setText(bundle.getString("button.evaluarEstudiante"));
        btnMensajes.setText(bundle.getString("button.mensajes"));
        btnCerrarSesion.setText(bundle.getString("button.cerrarSesion"));
    }

    @FXML
    public void registrarProyecto(ActionEvent event) {
        navegar("/Frontend/vistas/Tutor/registrarProyecto.fxml", "Registrar Proyecto", event);
    }

    @FXML
    public void actualizarEmpresa(ActionEvent event) {
        navegar("/Frontend/vistas/Tutor/actualizarEmpresa.fxml", "Actualizar Empresa", event);
    }

    @FXML
    public void verSeguimiento(ActionEvent event) {
        navegar("/Frontend/vistas/Tutor/seguimientoEstudiante.fxml", "Seguimiento", event);
    }

    @FXML
    public void evaluarInformeEstudiante(ActionEvent event) {
        navegar("/Frontend/vistas/Tutor/evaluarInformes.fxml", "Evaluar Informes", event);
    }

    @FXML
    public void verMensajes(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Frontend/vistas/mensajes.fxml"));
            Parent root = loader.load();

            MensajesController controller = loader.getController();
            controller.setPersistenceAPI(api);

            Stage stage = new Stage();
            stage.setTitle("Mensajes");
            stage.setScene(new Scene(root));
            stage.show();

            ((Stage) ((Node) event.getSource()).getScene().getWindow()).close();

        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error al abrir mensajes", e);
            mostrarAlerta("Error", "No se pudo abrir la pantalla de mensajes");
        }
    }

    @FXML
    public void cerrarSesion(ActionEvent event) {
        try {
            // Volver a la pantalla de login
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Frontend/vistas/Ingreso.fxml"));
            Parent root = loader.load();

            // Obtener el controlador y pasarle la API
            IngresoController controller = loader.getController();
            controller.setPersistenceAPI(api);

            // Mostrar la ventana de login
            Stage stage = new Stage();
            stage.setTitle("Login - GPPS");
            stage.setScene(new Scene(root));
            stage.show();

            // Cerrar la ventana actual
            ((Stage) ((Button) event.getSource()).getScene().getWindow()).close();

        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, "Error al cerrar sesión", ex);
            mostrarAlerta("Error", "No se pudo cerrar la sesión");
        }
    }

    private void navegar(String rutaFXML, String tituloVentana, ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(rutaFXML));
            Parent root = loader.load();

            // Podrías pasar API si los controladores destino la requieren

            Stage stage = new Stage();
            stage.setTitle(tituloVentana);
            stage.setScene(new Scene(root));
            stage.show();

            ((Stage) ((Node) event.getSource()).getScene().getWindow()).close();

        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error al navegar a " + tituloVentana, e);
            mostrarAlerta("Error", "No se pudo abrir " + tituloVentana);
        }
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
