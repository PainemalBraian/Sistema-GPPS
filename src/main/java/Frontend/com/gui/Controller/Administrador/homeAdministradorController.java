package Frontend.com.gui.Controller.Administrador; // Paquete ajustado

import Backend.API.API;

import Backend.Exceptions.UserException;
import Frontend.com.gui.Controller.DirectorCarrera.GestionConveniosController;
import Frontend.com.gui.Controller.IngresoController;
import Frontend.com.gui.Controller.MensajesController; // Asumiendo que el admin también puede tener mensajes o notificaciones
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

public class homeAdministradorController {

    private static final Logger LOGGER = Logger.getLogger(homeAdministradorController.class.getName());

    private API api;
    private ResourceBundle bundle;

    @FXML private Label lblBienvenida;
    @FXML private Label lblMensajes; // Para notificaciones del sistema o mensajes directos

    // Botones del menú lateral
    @FXML private Button btnGestionarUsuarios;
    @FXML private Button btnGestionarPeriodos;
    @FXML private Button btnGestionarCarreras;
    @FXML private Button btnConfiguracionSistema;
    @FXML private Button btnVerLogsAuditoria;
    @FXML private Button btnMensajesAdmin; // Renombrado para evitar colisión si hay otro btnMensajes
    @FXML private Button btnCerrarSesion;

    // Labels del panel de resumen (adaptados para Administrador)
    @FXML private Label lblTituloUsuariosDashboard;
    @FXML private Label lblTotalUsuariosDashboard;
    @FXML private Label lblUsuariosActivosDashboard;

    @FXML private Label lblTituloSistemaDashboard;
    @FXML private Label lblPeriodoActualDashboard;
    @FXML private Label lblUltimoBackupDashboard; // Ejemplo de info relevante
    @FXML private Button btnGestionarConvenios;     // Nuevo


    public void setPersistenceAPI(API persistenceAPI) throws Exception {
        this.api = persistenceAPI;
        //actualizarIdioma();
        //cargarDatosResumen();
    }

    private void actualizarIdioma() throws UserException {
        if (api == null) {
            LOGGER.log(Level.WARNING, "API no inicializada al intentar actualizar idioma.");
            // Considerar cargar un bundle por defecto
            // bundle = ResourceBundle.getBundle("Frontend.com.gui.Idioma.messages");
            return;
        }
        bundle = api.obtenerIdioma();
        if (bundle == null) {
            LOGGER.log(Level.SEVERE, "No se pudo cargar el ResourceBundle.");
            return;
        }

        lblBienvenida.setText(bundle.getString("label.bienvenida.admin") + api.obtenerSesionDeUsuario().getNombre());
        if(lblMensajes != null) lblMensajes.setText(bundle.getString("label.mensajes.admin")); // Si es una etiqueta general

        btnGestionarUsuarios.setText(bundle.getString("button.gestionarUsuarios"));
        btnGestionarPeriodos.setText(bundle.getString("button.gestionarPeriodos"));
        btnGestionarCarreras.setText(bundle.getString("button.gestionarCarreras"));
        btnConfiguracionSistema.setText(bundle.getString("button.configuracionSistema"));
        btnVerLogsAuditoria.setText(bundle.getString("button.verLogsAuditoria"));
        if (btnMensajesAdmin != null) btnMensajesAdmin.setText(bundle.getString("button.mensajes")); // Texto común para mensajes
        btnCerrarSesion.setText(bundle.getString("button.cerrarSesion"));

        // Actualizar textos de los paneles de resumen
        if (lblTituloUsuariosDashboard != null) lblTituloUsuariosDashboard.setText(bundle.getString("panel.titulo.usuariosDashboard"));
        if (lblTituloSistemaDashboard != null) lblTituloSistemaDashboard.setText(bundle.getString("panel.titulo.sistemaDashboard"));
    }

    private void cargarDatosResumen() {
        // Lógica para cargar datos desde la API y actualizar los Labels del dashboard
        // Ejemplo:
        // if (lblTotalUsuariosDashboard != null) lblTotalUsuariosDashboard.setText(bundle.getString("panel.usuarios.total") + api.admin_getTotalUsuarios());
        // if (lblUsuariosActivosDashboard != null) lblUsuariosActivosDashboard.setText(bundle.getString("panel.usuarios.activos") + api.admin_getUsuariosActivos());
        // if (lblPeriodoActualDashboard != null) lblPeriodoActualDashboard.setText(bundle.getString("panel.sistema.periodoActual") + api.admin_getPeriodoAcademicoActual());
        // if (lblUltimoBackupDashboard != null) lblUltimoBackupDashboard.setText(bundle.getString("panel.sistema.ultimoBackup") + api.admin_getFechaUltimoBackup());
        LOGGER.info("Cargando datos de resumen para el Administrador del Sistema.");
    }

    @FXML
    public void gestionarUsuarios(ActionEvent event) {
    }

    @FXML
    public void gestionarConvenios(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Frontend/vistas/Administrador/GestionDeConveniosAdmin.fxml"));
            Parent root = loader.load();

            GestionDeConveniosController controller = loader.getController();
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
    public void gestionarCarreras(ActionEvent event) {
    }

    @FXML
    public void configuracionSistema(ActionEvent event) {
    }


    @FXML
    public void verMensajesAdmin(ActionEvent event) { // Método para mensajes del admin
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Frontend/vistas/mensajes.fxml"), bundle); // Ruta genérica de mensajes
            Parent root = loader.load();

            MensajesController controller = loader.getController();
            if (this.api != null) {
                controller.setPersistenceAPI(api);
            } else {
                LOGGER.log(Level.WARNING, "API no disponible para MensajesController en Admin.");
            }

            Stage stage = new Stage();
            stage.setTitle(bundle.getString("title.mensajes")); // Título genérico de mensajes
            stage.setScene(new Scene(root));
            stage.show();

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error al abrir mensajes para Admin", e);
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

            Object loadedController = loader.getController();
            if (loadedController instanceof ControllerNecesitaAPI) {
                ((ControllerNecesitaAPI) loadedController).setPersistenceAPI(api);
            }

            Stage stage = new Stage();
            stage.setTitle(tituloVentana);
            stage.setScene(new Scene(root));
            stage.show();
            // Considerar si cerrar la ventana actual es la UX deseada
            // ((Stage) ((Node) event.getSource()).getScene().getWindow()).close();

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error al navegar a " + tituloVentana + " para Admin", e);
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

    // Interfaz opcional para controladores que necesitan la API (reutilizada)
    public interface ControllerNecesitaAPI {
        void setPersistenceAPI(API api) throws Exception;
    }
}