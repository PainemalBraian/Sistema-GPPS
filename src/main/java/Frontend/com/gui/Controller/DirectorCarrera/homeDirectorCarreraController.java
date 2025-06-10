package Frontend.com.gui.Controller.DirectorCarrera; // Paquete ajustado

import Backend.API.API;
import Frontend.com.gui.Controller.IngresoController;
import Frontend.com.gui.Controller.MensajesController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class homeDirectorCarreraController { // Nombre de clase cambiado

    private static final Logger LOGGER = Logger.getLogger(homeDirectorCarreraController.class.getName());

    private API api;
    private ResourceBundle bundle;

    @FXML private Label lblBienvenida;
    // @FXML private Label lblGestion; // Etiqueta agrupador opcional
    // @FXML private Label lblValidaciones; // Etiqueta agrupador opcional
    @FXML private Label lblMensajes;

    // Botones del menú lateral
    @FXML private Button btnGestionarConvenios;     // Nuevo
    @FXML private Button btnValidarPlanesTrabajo;   // Nuevo
    @FXML private Button btnValidarInformesFinales; // Nuevo
    @FXML private Button btnMensajes;
    @FXML private Button btnCerrarSesion;

    // Labels del panel de resumen (adaptados para Director de Carrera)
    @FXML private Label lblTituloConveniosDashboard;
    @FXML private Label lblConveniosActivosDashboard;
    @FXML private Label lblConveniosPorRevisarDashboard;

    @FXML private Label lblTituloValidacionesDashboard;
    @FXML private Label lblPlanesPorValidarDashboard;
    @FXML private Label lblInformesPorValidarDashboard;


    public void setPersistenceAPI(API persistenceAPI) throws Exception {
        this.api = persistenceAPI;
        //actualizarIdioma();
        cargarDatosResumen();
    }

    private void actualizarIdioma() {
        if (api == null) {
            LOGGER.log(Level.WARNING, "API no inicializada al intentar actualizar idioma.");
            // Considerar cargar un bundle por defecto o manejar el error de forma más robusta
            // bundle = ResourceBundle.getBundle("Frontend.com.gui.Idioma.messages"); // Ejemplo
            return;
        }
        bundle = api.obtenerIdioma();
        if (bundle == null) {
            LOGGER.log(Level.SEVERE, "No se pudo cargar el ResourceBundle.");
            // Aquí también, manejar el caso en que el bundle no se pueda cargar
            return;
        }

        lblBienvenida.setText(bundle.getString("label.bienvenida.director")); // Ajustado
        lblMensajes.setText(bundle.getString("label.mensajes"));

        btnGestionarConvenios.setText(bundle.getString("button.gestionarConvenios"));
        btnValidarPlanesTrabajo.setText(bundle.getString("button.validarPlanesTrabajo"));
        btnValidarInformesFinales.setText(bundle.getString("button.validarInformesFinales"));
        btnMensajes.setText(bundle.getString("button.mensajes"));
        btnCerrarSesion.setText(bundle.getString("button.cerrarSesion"));

        // Actualizar textos de los paneles de resumen
        if (lblTituloConveniosDashboard != null) lblTituloConveniosDashboard.setText(bundle.getString("panel.titulo.conveniosDashboard"));
        if (lblTituloValidacionesDashboard != null) lblTituloValidacionesDashboard.setText(bundle.getString("panel.titulo.validacionesDashboard"));
        // ... y así para las demás etiquetas de los paneles (e.g., "Activos:", "Pendientes:")
    }

    private void cargarDatosResumen() {
        // Lógica para cargar datos desde la API y actualizar los Labels del dashboard
        // Ejemplo:
         if (lblConveniosActivosDashboard != null)
             lblConveniosActivosDashboard.setText(/*bundle.getString("panel.convenios.activos") + */ api.director_getConveniosActivosCount());
        if (lblConveniosPorRevisarDashboard != null)
            lblConveniosPorRevisarDashboard.setText(/*bundle.getString("panel.convenios.porRevisar") +*/ "Por Revisar/Renovar: "+api.director_getConveniosPorRevisarCount());
//         if (lblPlanesPorValidarDashboard != null)
////             lblPlanesPorValidarDashboard.setText(/*bundle.getString("panel.planes.porValidar") +*/ api.director_getPlanesPorValidarCount());
//         if (lblInformesPorValidarDashboard != null)
////             lblInformesPorValidarDashboard.setText(/*bundle.getString("panel.informes.porValidar"*/) + api.director_getInformesPorValidarCount());
    }

    @FXML
    public void gestionarConvenios(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Frontend/vistas/DirectorCarrera/GestionDeConvenios.fxml"));
            Parent root = loader.load();

            GestionConveniosController controller = loader.getController();
            controller.setPersistenceAPI(api);

            Stage stage = new Stage();
            stage.setTitle("Gestión de Convenios - GPPS");
            stage.setScene(new Scene(root));
            stage.show();

            ((Stage) ((Button) event.getSource()).getScene().getWindow()).close();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error al abrir la gestión de convenios", e);
            mostrarAlerta("Error", "No se pudo abrir la gestión de convenios");
        }
    }

    @FXML
    public void validarPlanesTrabajo(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Frontend/vistas/DirectorCarrera/GestionDePlanesTrabajos.fxml"));
            Parent root = loader.load();

            GestionPlanesTrabajosController controller = loader.getController();
            controller.setPersistenceAPI(api);

            Stage stage = new Stage();
            stage.setTitle("Gestión de Planes de Trabajos - GPPS");
            stage.setScene(new Scene(root));
            stage.show();

            ((Stage) ((Button) event.getSource()).getScene().getWindow()).close();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error al abrir la gestión de planes de trabajos", e);
            mostrarAlerta("Error", "No se pudo abrir la gestión de planes de trabajos.");
        }
    }

    @FXML
    public void validarInformesFinales(ActionEvent event) {
//        try {
//            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Frontend/vistas/DirectorCarrera/GestionInformesFinales.fxml"));
//            Parent root = loader.load();
//
//            GestionValidarInformesFinalesController controller = loader.getController();
//            controller.setPersistenceAPI(api);
//
//            Stage stage = new Stage();
//            stage.setTitle("Gestión de Informes Finales - GPPS");
//            stage.setScene(new Scene(root));
//            stage.show();
//
//            ((Stage) ((Button) event.getSource()).getScene().getWindow()).close();
//        } catch (Exception e) {
//            LOGGER.log(Level.SEVERE, "Error al abrir la gestión de Informes Finales", e);
//            mostrarAlerta("Error", "No se pudo abrir la gestion de Informes Finales");
//        }
    }

    @FXML
    public void validarPropuestasProyectos(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Frontend/vistas/DirectorCarrera/GestionPropuestasProyectos.fxml"));
            Parent root = loader.load();

            GestionProyectosController controller = loader.getController();
            controller.setPersistenceAPI(api);

            Stage stage = new Stage();
            stage.setTitle("Gestión de Propuestas de Proyectos - GPPS");
            stage.setScene(new Scene(root));
            stage.show();

            ((Stage) ((Button) event.getSource()).getScene().getWindow()).close();
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Error", "No se pudo abrir la gestión de proyectos");
        }
    }

    @FXML
    public void verMensajes(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Frontend/vistas/mensajes.fxml"), bundle);
            Parent root = loader.load();

            MensajesController controller = loader.getController();
            if (this.api != null) { // Chequeo para evitar NPE si la API no se inyectó
                controller.setPersistenceAPI(api);
            } else {
                LOGGER.log(Level.WARNING, "API no disponible para MensajesController.");
            }


            Stage stage = new Stage();
            stage.setTitle(bundle.getString("title.mensajes"));
            stage.setScene(new Scene(root));
            stage.show();

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error al abrir mensajes", e);
            mostrarAlerta(bundle.getString("alert.error.titulo"), bundle.getString("alert.error.abrirMensajes"));
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource(rutaFXML), bundle);
            Parent root = loader.load();

            // Inyectar API si el controlador destino la necesita
            Object loadedController = loader.getController();
            if (loadedController instanceof ControllerNecesitaAPI) { // Asumiendo una interfaz común
                ((ControllerNecesitaAPI) loadedController).setPersistenceAPI(api);
            }


            Stage stage = new Stage();
            stage.setTitle(tituloVentana);
            stage.setScene(new Scene(root));
            stage.show();

            // Opcional: Cerrar ventana actual. Considera si es la mejor UX.
            // ((Stage) ((Node) event.getSource()).getScene().getWindow()).close();

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error al navegar a " + tituloVentana, e);
            mostrarAlerta(bundle.getString("alert.error.titulo"), bundle.getString("alert.error.navegacion") + ": " + tituloVentana);
        }
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }



    // Interfaz opcional para controladores que necesitan la API
    public interface ControllerNecesitaAPI {
        void setPersistenceAPI(API api) throws Exception;
    }
}