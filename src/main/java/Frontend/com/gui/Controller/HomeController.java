package Frontend.com.gui.Controller;

import Backend.API.API;
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

    @FXML private Label lblBienvenida;

    @FXML private Label lblNombreUsuario;

    @FXML private Label lblEstadoPPS;

    @FXML private Label lblFechaInicio;

    @FXML private Label lblFechaFin;

    @FXML private Label lblNoAvisos;

    @FXML private VBox vboxNotificaciones;

    @FXML private Button btnPuestosDisponibles;

    @FXML public Button btnTareas;

    @FXML private Button btnPresentarPropuesta;

    @FXML private Button btnMensajes;

    @FXML private Button btnCerrarSesion;
    @FXML public Button btnInformes;



    /**
     * Configura los textos de la interfaz según el idioma seleccionado.
     */
    private void actualizarIdioma() {
        try {
            // Obtener textos del bundle de idioma
            lblBienvenida.setText(bundle.getString("label.bienvenida"));
            btnPuestosDisponibles.setText(bundle.getString("button.puestos"));
            btnTareas.setText(bundle.getString("button.Tareas"));
            btnPresentarPropuesta.setText(bundle.getString("button.presentarPropuesta"));
            btnMensajes.setText(bundle.getString("button.mensajes"));
            btnCerrarSesion.setText(bundle.getString("button.cerrarSesion"));

        } catch (Exception ex) {
            LOGGER.log(Level.WARNING, "Error al cargar los textos del idioma", ex);
            // Usar textos predeterminados en caso de error
            lblBienvenida.setText("Bienvenido a GPPS,");
        }
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
    public void PuestosDisponibles(ActionEvent event) {
        try {
            navegarA("/Frontend/vistas/PuestosDisponibles.fxml", "Puestos para PPS Disponibles", event);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error al navegar a inscripción PPS", e);
            mostrarAlerta("Error", "No se pudo abrir el listado de puestos", Alert.AlertType.ERROR);
        }
    }


    @FXML
    public void Tareas(ActionEvent event) {
        try {
            navegarA("/Frontend/vistas/tareas.fxml", "Tareas - GPPS", event);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error al navegar a listado de puestos", e);
            mostrarAlerta("Error", "No se pudo abrir el listado de puestos", Alert.AlertType.ERROR);
        }
    }


    @FXML
    public void presentarPropuesta(ActionEvent event) {
        try {
            navegarA("/Frontend/vistas/propuestas.fxml", "Presentación de Propuesta - GPPS", event);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error al navegar a presentación de propuesta", e);
            mostrarAlerta("Error", "No se pudo abrir el formulario de presentación de propuesta", Alert.AlertType.ERROR);
        }
    }
    public void Informes(ActionEvent event) {
        try {
            navegarA("/Frontend/vistas/informes.fxml", "Informes - GPPS", event);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error al navegar a informes", e);
            mostrarAlerta("Error", "No se pudo abrir la pantalla de informes", Alert.AlertType.ERROR);
        }
    }


    @FXML
    public void verMensajes(ActionEvent event) {
        try {
            navegarA("/Frontend/vistas/Mensajes.fxml", "Mensajes - GPPS", event);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error al navegar a mensajes", e);
            mostrarAlerta("Error", "No se pudo abrir la bandeja de mensajes", Alert.AlertType.ERROR);
        }
    }

    @FXML
    public void verInformes(ActionEvent event) {
        try {
            navegarA("/Frontend/vistas/Informes.fxml", "Informes - GPPS", event);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error al navegar a informes", e);
            mostrarAlerta("Error", "No se pudo abrir la ventana de informes", Alert.AlertType.ERROR);
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
            mostrarAlerta("Error", "No se pudo cerrar la sesión", Alert.AlertType.ERROR);
        }
    }

    private void navegarA(String fxmlPath, String title, ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
        Parent root = loader.load();

        // Obtener el controlador si necesitas usarlo
        Object controller = loader.getController();
        // Puedes verificar si implementa una interfaz común si es necesario

        Stage stage = new Stage();
        stage.setTitle(title);
        stage.setScene(new Scene(root));
        stage.show();

        // Cerrar la ventana actual que disparó el evento
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.close();
    }



    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    public void setPersistenceAPI(API persistenceAPI) throws Exception {
        this.api = persistenceAPI;
       // this.bundle = api.obtenerIdioma();
        actualizarIdioma();
        verificarEstadoPPS();
        cargarAvisosRecientes();
    }

}