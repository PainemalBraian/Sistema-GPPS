package Frontend.com.gui.Controller.Docente;

import Backend.API.API;
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

public class HomeDocenteController {

    private static final Logger LOGGER = Logger.getLogger(HomeDocenteController.class.getName());

    private API api;
    private ResourceBundle bundle;

    // Labels de bienvenida y títulos
    @FXML private Label lblBienvenida;
    @FXML private Label lblTituloEstudiantes;
    @FXML private Label lblTituloInformes;
    @FXML private Label lblTituloActividades;
    @FXML private Label lblTituloMensajes;

    // Labels de estadísticas
    @FXML private Label lblTotalEstudiantes;
    @FXML private Label lblEnCurso;
    @FXML private Label lblFinalizadas;
    @FXML private Label lblInformesPendientes;
    @FXML private Label lblParaEvaluar;
    @FXML private Label lblVencimientoProximo;
    @FXML private Label lblMensajesNoLeidos;
    @FXML private Label lblUltimoMensaje;
    @FXML private Label lblNoActividades;

    // Contenedores
    @FXML private VBox vboxActividades;

    // Botones del menú
    @FXML private Button btnEstudiantesAsignados;
    @FXML private Button btnEvaluarInformes;
    @FXML private Button btnSeguimientoAvances;
    @FXML private Button btnMensajes;
    @FXML private Button btnCerrarSesion;
    @FXML private Button btnVerTodosMensajes;

    /**
     * Configura los textos de la interfaz según el idioma seleccionado.
     */
    private void actualizarIdioma() {
        ResourceBundle bundle = api.obtenerIdioma();

        // Labels principales
        lblBienvenida.setText(bundle.getString("label.bienvenida.docente"));
        lblTituloEstudiantes.setText(bundle.getString("label.estudiantesAsignados"));
        lblTituloInformes.setText(bundle.getString("label.informesPendientes"));
        lblTituloActividades.setText(bundle.getString("label.actividadesRecientes"));
        lblTituloMensajes.setText(bundle.getString("label.mensajesNoLeidos"));
        lblNoActividades.setText(bundle.getString("label.noActividades"));

        // Botones del menú
        btnEstudiantesAsignados.setText(bundle.getString("button.estudiantesAsignados"));
        btnEvaluarInformes.setText(bundle.getString("button.evaluarInformes"));
        btnSeguimientoAvances.setText(bundle.getString("button.seguimientoAvances"));
        btnMensajes.setText(bundle.getString("button.mensajes"));
        btnCerrarSesion.setText(bundle.getString("button.cerrarSesion"));
        btnVerTodosMensajes.setText(bundle.getString("button.verTodos"));
    }

    public void setPersistenceAPI(API persistenceAPI) throws Exception {
        this.api = persistenceAPI;
        actualizarIdioma();
        cargarDashboard();
    }

    /**
     * Carga los datos del dashboard del docente.
     */
    private void cargarDashboard() {
        try {
            cargarEstadisticasEstudiantes();
            cargarInformesPendientes();
            cargarActividadesRecientes();
            cargarMensajesNoLeidos();
        } catch (Exception ex) {
            LOGGER.log(Level.WARNING, "Error al cargar el dashboard", ex);
        }
    }

    /**
     * Carga las estadísticas de estudiantes asignados.
     */
    private void cargarEstadisticasEstudiantes() {
        try {
            // Placeholder para cuando esté disponible la API
            // List<EstudianteAsignado> estudiantes = api.obtenerEstudiantesAsignados(api.obtenerIdDocente());
            // int total = estudiantes.size();
            // int enCurso = (int) estudiantes.stream().filter(e -> "EN_CURSO".equals(e.getEstadoPPS())).count();
            // int finalizadas = (int) estudiantes.stream().filter(e -> "FINALIZADA".equals(e.getEstadoPPS())).count();

            int total = 0;
            int enCurso = 0;
            int finalizadas = 0;

            lblTotalEstudiantes.setText(String.valueOf(total));
            lblEnCurso.setText("En curso: " + enCurso);
            lblFinalizadas.setText("Finalizadas: " + finalizadas);

        } catch (Exception ex) {
            LOGGER.log(Level.WARNING, "Error al cargar estadísticas de estudiantes", ex);
            lblTotalEstudiantes.setText("--");
            lblEnCurso.setText("En curso: --");
            lblFinalizadas.setText("Finalizadas: --");
        }
    }

    /**
     * Carga los informes pendientes de evaluación.
     */
    private void cargarInformesPendientes() {
        try {
            // Placeholder para cuando esté disponible la API
            // List<InformePendiente> informes = api.obtenerInformesPendientesEvaluacion(api.obtenerIdDocente());
            // int pendientes = informes.size();
            // int paraEvaluar = (int) informes.stream().filter(i -> "PENDIENTE_EVALUACION".equals(i.getEstado())).count();
            // int vencimientoProximo = (int) informes.stream().filter(i -> i.tieneVencimientoProximo()).count();

            int pendientes = 0;
            int paraEvaluar = 0;
            int vencimientoProximo = 0;

            lblInformesPendientes.setText(String.valueOf(pendientes));
            lblParaEvaluar.setText("Para evaluar: " + paraEvaluar);
            lblVencimientoProximo.setText("Vencimiento próximo: " + vencimientoProximo);

        } catch (Exception ex) {
            LOGGER.log(Level.WARNING, "Error al cargar informes pendientes", ex);
            lblInformesPendientes.setText("--");
            lblParaEvaluar.setText("Para evaluar: --");
            lblVencimientoProximo.setText("Vencimiento próximo: --");
        }
    }

    /**
     * Carga las actividades recientes del docente.
     */
    private void cargarActividadesRecientes() {
        try {
            vboxActividades.getChildren().clear();

            // Placeholder para cuando esté disponible la API
            // List<ActividadReciente> actividades = api.obtenerActividadesRecientesDocente(api.obtenerIdDocente());
            // if (actividades != null && !actividades.isEmpty()) {
            //     lblNoActividades.setVisible(false);
            //
            //     for (ActividadReciente actividad : actividades) {
            //         Label lblActividad = new Label(actividad.getDescripcion());
            //         lblActividad.getStyleClass().add("activity-item");
            //
            //         Label lblFecha = new Label(actividad.getFecha().toString());
            //         lblFecha.getStyleClass().add("activity-date");
            //
            //         VBox itemActividad = new VBox(2, lblActividad, lblFecha);
            //         itemActividad.getStyleClass().add("activity-container");
            //
            //         vboxActividades.getChildren().add(itemActividad);
            //     }
            // } else {
            //     lblNoActividades.setVisible(true);
            // }

            lblNoActividades.setVisible(true);

        } catch (Exception ex) {
            LOGGER.log(Level.WARNING, "Error al cargar actividades recientes", ex);
            lblNoActividades.setText("Error al cargar actividades");
            lblNoActividades.setVisible(true);
        }
    }

    /**
     * Carga el resumen de mensajes no leídos.
     */
    private void cargarMensajesNoLeidos() {
        try {
            // Placeholder para cuando esté disponible la API
            // List<MensajeNoLeido> mensajes = api.obtenerMensajesNoLeidos(api.obtenerIdDocente());
            // int noLeidos = mensajes.size();
            // String ultimoMensaje = mensajes.isEmpty() ? "--" : mensajes.get(0).getRemitente() + " - " + mensajes.get(0).getFecha();

            int noLeidos = 0;
            String ultimoMensaje = "--";

            lblMensajesNoLeidos.setText(String.valueOf(noLeidos));
            lblUltimoMensaje.setText("Último mensaje: " + ultimoMensaje);

        } catch (Exception ex) {
            LOGGER.log(Level.WARNING, "Error al cargar mensajes no leídos", ex);
            lblMensajesNoLeidos.setText("--");
            lblUltimoMensaje.setText("Último mensaje: Error");
        }
    }

    @FXML
    public void verEstudiantesAsignados(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Frontend/vistas/Docente/estudiantesAsignados.fxml"));
            Parent root = loader.load();

            ListadoEstudiantesController controller = loader.getController();
            controller.setPersistenceAPI(api);

            Stage stage = new Stage();
            stage.setTitle("Estudiantes Asignados - GPPS");
            stage.setScene(new Scene(root));
            stage.show();

            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.close();

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error al navegar a estudiantes asignados", e);
            mostrarAlerta("Error", "No se pudo abrir la lista de estudiantes asignados");
        }
    }

    @FXML
    public void evaluarInformes(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Frontend/vistas/Docente/evaluarInformes.fxml"));
            Parent root = loader.load();

            EvaluarInformesController controller = loader.getController();
            controller.setPersistenceAPI(api);

            Stage stage = new Stage();
            stage.setTitle("Evaluar Informes - GPPS");
            stage.setScene(new Scene(root));
            stage.show();

            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.close();

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error al navegar a evaluación de informes", e);
            mostrarAlerta("Error", "No se pudo abrir la pantalla de evaluación de informes");
        }
    }

    @FXML
    public void seguimientoAvances(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Frontend/vistas/Docente/seguimientoAvances.fxml"));
            Parent root = loader.load();

            SeguimientoDePPSController controller = loader.getController();
            controller.setPersistenceAPI(api);

            Stage stage = new Stage();
            stage.setTitle("Seguimiento de Avances - GPPS");
            stage.setScene(new Scene(root));
            stage.show();

            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.close();

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error al navegar a seguimiento de avances", e);
            mostrarAlerta("Error", "No se pudo abrir el seguimiento de avances");
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