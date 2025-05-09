package Frontend.com.main;

import Backend.API.API;
import Backend.DTO.UsuarioDTO;
import Backend.Exceptions.UserExceptions;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Controlador para la vista Home de estudiantes en la aplicación GPPS.
 * Maneja la interacción del usuario con la interfaz principal.
 */
public class HomeController implements Initializable {

    private static final Logger LOGGER = Logger.getLogger(HomeController.class.getName());

    private API api;
    private ResourceBundle bundle;

    @FXML
    private Label lblBienvenida;

    @FXML
    private Label lblNombreUsuario;

    @FXML
    private Label lblEstadoPPS;

    @FXML
    private Label lblFechaInicio;

    @FXML
    private Label lblFechaFin;

    @FXML
    private Label lblNoAvisos;

    @FXML
    private VBox vboxNotificaciones;

    @FXML
    private Button btnInscribir;

    @FXML
    private Button btnListarPuestos;

    @FXML
    private Button btnEntregarProyecto;

    @FXML
    private Button btnPresentarPropuesta;

    @FXML
    private Button btnMensajes;

    @FXML
    private Button btnCerrarSesion;

    /**
     * Constructor del controlador.
     */
    public HomeController() {
        // El constructor se deja vacío ya que la inicialización se hará en el método initialize
    }

    /**
     * Inicializa el controlador después de que se carga el FXML.
     * Configura los componentes de la interfaz y carga los datos iniciales.
     *
     * @param location La ubicación utilizada para resolver rutas relativas para el objeto raíz.
     * @param resources Los recursos utilizados para localizar el objeto raíz.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
/*
            actualizarIdioma();
            verificarEstadoPPS();
            cargarAvisosRecientes();
*/

    }

    /**
     * Configura los textos de la interfaz según el idioma seleccionado.
     */
    private void actualizarIdioma() {
        try {
            // Obtener textos del bundle de idioma
            lblBienvenida.setText(bundle.getString("label.bienvenida"));
            btnInscribir.setText(bundle.getString("button.inscribir"));
            btnListarPuestos.setText(bundle.getString("button.listarPuestos"));
            btnEntregarProyecto.setText(bundle.getString("button.entregarProyecto"));
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

    /**
     * Maneja la acción de inscribir a una PPS.
     */
    @FXML
    public void inscribirPPS(ActionEvent event) {
        try {
            navegarA("/Frontend/vistas/InscripcionPPS.fxml", "Inscripción a PPS - GPPS");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error al navegar a inscripción PPS", e);
            mostrarAlerta("Error", "No se pudo abrir el formulario de inscripción", Alert.AlertType.ERROR);
        }
    }

    /**
     * Maneja la acción de listar puestos disponibles.
     */
    @FXML
    public void listarPuestosDisponibles(ActionEvent event) {
        try {
            navegarA("/Frontend/vistas/ListadoPuestos.fxml", "Puestos Disponibles - GPPS");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error al navegar a listado de puestos", e);
            mostrarAlerta("Error", "No se pudo abrir el listado de puestos", Alert.AlertType.ERROR);
        }
    }

    /**
     * Maneja la acción de entregar un proyecto.
     */
    @FXML
    public void entregarProyecto(ActionEvent event) {
        try {
            navegarA("/Frontend/vistas/EntregaProyecto.fxml", "Entrega de Proyecto - GPPS");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error al navegar a entrega de proyecto", e);
            mostrarAlerta("Error", "No se pudo abrir el formulario de entrega de proyecto", Alert.AlertType.ERROR);
        }
    }

    /**
     * Maneja la acción de presentar una propuesta.
     */
    @FXML
    public void presentarPropuesta(ActionEvent event) {
        try {
            navegarA("/Frontend/vistas/PresentacionPropuesta.fxml", "Presentación de Propuesta - GPPS");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error al navegar a presentación de propuesta", e);
            mostrarAlerta("Error", "No se pudo abrir el formulario de presentación de propuesta", Alert.AlertType.ERROR);
        }
    }

    /**
     * Maneja la acción de ver mensajes.
     */
    @FXML
    public void verMensajes(ActionEvent event) {
        try {
            navegarA("/Frontend/vistas/Mensajes.fxml", "Mensajes - GPPS");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error al navegar a mensajes", e);
            mostrarAlerta("Error", "No se pudo abrir la bandeja de mensajes", Alert.AlertType.ERROR);
        }
    }

    /**
     * Maneja la acción de cerrar sesión.
     */
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

    /**
     * Método auxiliar para navegar entre ventanas.
     *
     * @param fxmlPath Ruta al archivo FXML de destino
     * @param title Título para la nueva ventana
     * @throws IOException Si ocurre un error al cargar el FXML
     */
    private void navegarA(String fxmlPath, String title) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
        Parent root = loader.load();

        // Intentar configurar el controlador con la API si implementa la interfaz apropiada
        Object controller = loader.getController();
        // Aquí se debería verificar si el controlador implementa alguna interfaz común
        // y configurar la API según corresponda

        Stage stage = new Stage();
        stage.setTitle(title);
        stage.setScene(new Scene(root));
        stage.show();
    }

    /**
     * Muestra una alerta al usuario.
     *
     * @param titulo El título de la alerta.
     * @param mensaje El mensaje a mostrar.
     * @param tipo El tipo de alerta (INFO, WARNING, ERROR, etc.).
     */
    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    /**
     * Método público para establecer la API, utilizado cuando se navega desde otra vista.
     *
     * @throws Exception Si ocurre un error al configurar la API
     */
    public void setPersistenceAPI(API persistenceAPI) throws Exception {
        this.api = persistenceAPI;
        this.bundle = api.obtenerIdioma();
        actualizarIdioma();
        verificarEstadoPPS();
        cargarAvisosRecientes();
    }
}