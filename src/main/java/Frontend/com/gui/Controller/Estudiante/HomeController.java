package Frontend.com.gui.Controller.Estudiante;

import Backend.API.API;
import Backend.Exceptions.UserException;
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
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;


public class HomeController  {

    private static final Logger LOGGER = Logger.getLogger(HomeController.class.getName());

    private API api;
    private ResourceBundle bundle;
    //Labels
    @FXML private Label lblBienvenida;
    @FXML private Label lblEstadoPPS;
    @FXML private Label lblFechaInicio;
    @FXML private Label lblFechaFin;
    @FXML private Label lblNoAvisos;
    @FXML private VBox vboxNotificaciones;
    @FXML public Label lblNotificaciones;
    @FXML public Label lblStatus;


    //Botones
    @FXML private Button btnPuestosDisponibles;
    @FXML private Button btnPresentarPropuesta;
    @FXML private Button btnMensajes;
    @FXML private Button btnCerrarSesion;
    @FXML public Button btnInformeDeTareas;

    /**
     * Configura los textos de la interfaz según el idioma seleccionado.
     */

    private void actualizarIdioma() throws UserException {
        ResourceBundle bundle = api.obtenerIdioma();
        // Labels
            // Labels
            lblBienvenida.setText(bundle.getString("label.bienvenida") + api.obtenerSesionDeUsuario().getNombre());
            lblEstadoPPS.setText(bundle.getString("label.EstadoPPS"));
            lblFechaInicio.setText(bundle.getString("label.FechaInicio"));
            lblFechaFin.setText(bundle.getString("label.FechaFin"));
            lblNoAvisos.setText(bundle.getString("label.NoAvisos"));
            lblNotificaciones.setText(bundle.getString("label.Notificaciones"));
            lblStatus.setText(bundle.getString("label.Estado"));

            // Botones
            btnPuestosDisponibles.setText(bundle.getString("button.puestos"));
            btnInformeDeTareas.setText(bundle.getString("button.InformeDeTareas"));
            btnPresentarPropuesta.setText(bundle.getString("button.presentarPropuesta"));
            btnMensajes.setText(bundle.getString("button.mensajes"));
            btnCerrarSesion.setText(bundle.getString("button.cerrarSesion"));
    }

    public void setPersistenceAPI(API persistenceAPI) throws Exception {
        this.api = persistenceAPI;
        actualizarIdioma();
        //verificarEstadoPPS();
        //cargarAvisosRecientes();
    }

    /**
     * Verifica y muestra el estado actual de la PPS del estudiante.
     */
    private void verificarEstadoPPS() {
        try {
            // Aquí se implementaría la lógica para verificar el estado de la PPS
            // Este es un placeholder hasta que se implemente la API correspondiente
            String estadoPPS = "Sin PPS activa"; // Por defecto

            // Ejemplo de código que se podría usar cuando la API esté disponible:
            // EstadoPPS estado = api.obtenerEstadoPPS(api.obtenerNombreUsuario());
            // if (estado != null) {
            //     estadoPPS = estado.getDescripcion();
            //     lblFechaInicio.setText("Fecha de inicio: " + estado.getFechaInicio());
            //     lblFechaFin.setText("Fecha de finalización estimada: " + estado.getFechaFin());
            //
            //     // Aplicar estilo según el estado
            //     if ("En curso".equals(estadoPPS)) {
            //         lblEstadoPPS.getStyleClass().add("status-active");
            //     } else if ("Pendiente de aprobación".equals(estadoPPS)) {
            //         lblEstadoPPS.getStyleClass().add("status-pending");
            //     } else if ("Completada".equals(estadoPPS)) {
            //         lblEstadoPPS.getStyleClass().add("status-completed");
            //     }
            // }

            lblEstadoPPS.setText(estadoPPS);

        } catch (Exception ex) {
            LOGGER.log(Level.WARNING, "No se pudo obtener el estado de la PPS", ex);
            lblEstadoPPS.setText("Estado no disponible");
        }
    }

    /**
     * Carga y muestra los avisos recientes para el usuario.
     */
    private void cargarAvisosRecientes() {
        try {
            // Aquí se implementaría la lógica para obtener avisos recientes
            // Este es un placeholder hasta que se implemente la API correspondiente

            // Ejemplo de código que se podría usar cuando la API esté disponible:
            // List<AvisoDTO> avisos = api.obtenerAvisosRecientes(api.obtenerNombreUsuario());
            // if (avisos != null && !avisos.isEmpty()) {
            //     lblNoAvisos.setVisible(false);
            //     vboxNotificaciones.getChildren().clear();
            //
            //     for (AvisoDTO aviso : avisos) {
            //         Label lblAviso = new Label(aviso.getMensaje());
            //         lblAviso.getStyleClass().add("notification-item");
            //
            //         Label lblFecha = new Label(aviso.getFecha().toString());
            //         lblFecha.getStyleClass().add("notification-date");
            //
            //         VBox itemAviso = new VBox(5, lblAviso, lblFecha);
            //         itemAviso.getStyleClass().add("notification-item");
            //
            //         vboxNotificaciones.getChildren().add(itemAviso);
            //     }
            // } else {
            //     lblNoAvisos.setVisible(true);
            //     vboxNotificaciones.getChildren().clear();
            // }

        } catch (Exception ex) {
            LOGGER.log(Level.WARNING, "No se pudieron cargar los avisos recientes", ex);
            lblNoAvisos.setText("Error al cargar avisos");
            lblNoAvisos.getStyleClass().add("alert-text");
        }
    }

    @FXML
    public void presentarPropuesta(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Frontend/vistas/Estudiante/propuestaPropia.fxml"));
            Parent root = loader.load();

            // Inyectás el persistenceAPI al nuevo controlador
            PropuestaPropiaController propuestaController = loader.getController();
            propuestaController.setPersistenceAPI(api);

            // Ahora sí creás la nueva ventana con ese root
            Stage stage = new Stage();
            stage.setTitle("Presentación de Propuesta - GPPS");
            stage.setScene(new Scene(root));
            stage.show();

            // Cerrás la ventana actual
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.close();

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error al navegar a presentación de propuesta", e);
            mostrarAlerta("Error", "No se pudo abrir el formulario de presentación de propuesta");
        }
    }


    @FXML
    public void PuestosDisponibles(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Frontend/vistas/Estudiante/puestosDisponibles.fxml"));
            Parent root = loader.load();

            PuestosDisponiblesController controller = loader.getController();
            controller.setPersistenceAPI(api);

            Stage stage = new Stage();
            stage.setTitle("Puestos para PPS Disponibles");
            stage.setScene(new Scene(root));
            stage.show();

            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.close();
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.log(Level.SEVERE, "Error al navegar a inscripción PPS", e);
            mostrarAlerta("Error", "No se pudo abrir el listado de puestos");
        }
    }

    @FXML
    public void InformeDeTareas(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Frontend/vistas/Estudiante/InformeDeTareas.fxml"));
            Parent root = loader.load();

            InformeDeTareasController controller = loader.getController();
            controller.setPersistenceAPI(api);

            Stage stage = new Stage();
            stage.setTitle("Tareas - GPPS");
            stage.setScene(new Scene(root));
            stage.show();

            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.close();
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.log(Level.SEVERE, "Error al navegar a listado de tareas", e);
            mostrarAlerta("Error", "No se pudo abrir el listado de tareas");
        }
    }



    @FXML
    public void verMensajes(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Frontend/vistas/mensajes.fxml"));
            Parent root = loader.load();

            MensajesController controller = loader.getController();
            controller.setPersistenceAPI(api);

            Stage stage = new Stage();
            stage.setTitle("Mensajes - GPPS");
            stage.setScene(new Scene(root));
            stage.show();

            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.close();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error al navegar a mensajes", e);
            mostrarAlerta("Error", "No se pudo abrir la bandeja de mensajes");
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



    private void mostrarAlerta(String titulo, String mensaje) {
        Alert.AlertType tipo = Alert.AlertType.ERROR;
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }


}