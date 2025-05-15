package Frontend.com.gui.Controller;

import Backend.API.API;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Controlador para la pantalla de selección y registro de propuestas de PPS.
 * Permite a los estudiantes seleccionar propuestas existentes o registrar sus propias propuestas.
 */
public class PropuestasController {

    private static final Logger LOGGER = Logger.getLogger(PropuestasController.class.getName());

    private API api; // Instancia de la API para interactuar con el backend
    private ResourceBundle bundle; // Para localización
    private ObservableList<PropuestaDTO> propuestasList; // Lista observable para la ListView
    private PropuestaDTO propuestaSeleccionada; // Propuesta que el usuario ha seleccionado

    // Elementos de UI para la pestaña de Selección de Propuestas Existentes
    @FXML private TabPane tabPanePrincipal;
    @FXML private ListView<PropuestaDTO> lvPropuestasExistentes;
    @FXML private VBox vboxDetallePropuesta;
    @FXML private Label lblTituloPropuesta;
    @FXML private Label lblTipo;
    @FXML private Label lblOrganizacion;
    @FXML private Label lblTutorPropuesto;
    @FXML private Label lblVacantes;
    @FXML private TextArea taDescripcionExistente;
    @FXML private TextArea taObjetivosExistente;
    @FXML private TextArea taTecnologiasExistente;
    @FXML private Button btnSeleccionarPropuesta;

    // Elementos de UI para la pestaña de Cargar Proyecto Propio
    @FXML private VBox vboxCargarPropia;
    @FXML private TextField tfTituloPropio;
    @FXML private TextField tfTipoPropio;
    @FXML private TextField tfOrganizacionPropia;
    @FXML private TextField tfTutorSugeridoPropio;
    @FXML private TextArea taDescripcionPropia;
    @FXML private TextArea taObjetivosPropios;
    @FXML private TextArea taTecnologiasPropias;
    @FXML private Button btnCargarPropuestaPropia;
    @FXML private Button btnVolverHome;

    /**
     * Método llamado automáticamente después de que el FXML ha sido cargado.
     */
    @FXML
    public void initialize() {
        // Configurar el listener para la selección en la ListView
        lvPropuestasExistentes.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> mostrarDetallesPropuesta(newValue));

        // Deshabilitar el botón de selección inicialmente hasta que se elija una propuesta
        btnSeleccionarPropuesta.setDisable(true);
    }

    /**
     * Establece la instancia de la API de persistencia.
     * Este método debe ser llamado por la pantalla anterior (e.g., HomeController)
     * después de cargar este FXML.
     * @param persistenceAPI La instancia de la API.
     * @throws Exception Si ocurre un error al configurar la API o el idioma.
     */
    public void setPersistenceAPI(API persistenceAPI) throws Exception {
        this.api = persistenceAPI;
        //this.bundle = api.obtenerIdioma(); // Descomentar si usas ResourceBundle aquí
        //actualizarIdioma(); // Descomentar si usas ResourceBundle aquí
        cargarPropuestasDisponibles();
    }

    /**
     * Carga las propuestas de PPS disponibles desde la API y las muestra en la lista.
     */
    private void cargarPropuestasDisponibles() {
        try {
            // Aquí se implementaría la llamada a tu API para obtener las propuestas
            // List<PropuestaDTO> propuestas = api.obtenerPropuestasDisponibles();

            // --- datos para prueba ---
            List<PropuestaDTO> propuestas = List.of(
                    new PropuestaDTO(1, "Sistema de Gestión de Inventarios", "Desarrollo Web",
                            "TechSoluciones S.A.", "Ing. Carlos Mendoza", 2,
                            "Desarrollo de un sistema completo para gestión de inventarios y control de stock para una empresa de comercio electrónico.",
                            "1. Implementar módulo de altas/bajas/modificaciones de productos\n2. Desarrollar sistema de alertas de stock mínimo\n3. Integrar con plataforma de ventas existente",
                            "Java, Spring Boot, MySQL, React JS"),
                    new PropuestaDTO(2, "Aplicación de Seguimiento Deportivo", "Desarrollo Mobile",
                            "FitTrack", "Ing. Laura Sánchez", 1,
                            "Aplicación móvil para el seguimiento de entrenamientos deportivos, con funcionalidades de GPS, medición de rendimiento y estadísticas personalizadas.",
                            "1. Implementar seguimiento GPS de rutas\n2. Desarrollar panel de estadísticas personalizadas\n3. Crear sistema de metas y logros",
                            "Kotlin, Android Studio, Firebase, Google Maps API"),
                    new PropuestaDTO(3, "Investigación en Algoritmos de Machine Learning", "Investigación Aplicada",
                            "Cátedra de Inteligencia Artificial", "Dra. Martina Gómez", 2,
                            "Proyecto de investigación aplicada sobre algoritmos de machine learning para detección temprana de fallas en sistemas industriales.",
                            "1. Recopilar y preparar datasets industriales\n2. Implementar y comparar diferentes algoritmos de ML\n3. Desarrollar un prototipo funcional de sistema de alarmas",
                            "Python, TensorFlow, Pandas, scikit-learn")
            );

            propuestasList = FXCollections.observableArrayList(propuestas);
            lvPropuestasExistentes.setItems(propuestasList);

            // Personalizar cómo se muestra cada elemento en la lista
            lvPropuestasExistentes.setCellFactory(lv -> new ListCell<PropuestaDTO>() {
                @Override
                protected void updateItem(PropuestaDTO propuesta, boolean empty) {
                    super.updateItem(propuesta, empty);
                    if (empty || propuesta == null) {
                        setText(null);
                    } else {
                        // Mostrar solo el título de la propuesta en la lista
                        setText(propuesta.getTitulo());
                    }
                }
            });

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Error al cargar las propuestas disponibles", ex);
            mostrarAlerta("Error de Carga", "No se pudieron cargar las propuestas de PPS disponibles.", Alert.AlertType.ERROR);
        }
    }

    /**
     * Muestra los detalles de la propuesta seleccionada en el panel derecho.
     * @param propuesta La propuesta seleccionada, o null si no hay selección.
     */
    private void mostrarDetallesPropuesta(PropuestaDTO propuesta) {
        propuestaSeleccionada = propuesta;
        if (propuestaSeleccionada != null) {
            lblTituloPropuesta.setText(propuestaSeleccionada.getTitulo());
            lblTipo.setText(propuestaSeleccionada.getTipo());
            lblOrganizacion.setText(propuestaSeleccionada.getOrganizacion());
            lblTutorPropuesto.setText(propuestaSeleccionada.getTutorPropuesto());
            lblVacantes.setText(String.valueOf(propuestaSeleccionada.getVacantes()));
            taDescripcionExistente.setText(propuestaSeleccionada.getDescripcion());
            taObjetivosExistente.setText(propuestaSeleccionada.getObjetivos());
            taTecnologiasExistente.setText(propuestaSeleccionada.getTecnologias());
            btnSeleccionarPropuesta.setDisable(false); // Habilitar el botón de selección
        } else {
            // Si no hay selección, limpiar los detalles
            lblTituloPropuesta.setText("Seleccione una propuesta");
            lblTipo.setText("-");
            lblOrganizacion.setText("-");
            lblTutorPropuesto.setText("-");
            lblVacantes.setText("-");
            taDescripcionExistente.setText("");
            taObjetivosExistente.setText("");
            taTecnologiasExistente.setText("");
            btnSeleccionarPropuesta.setDisable(true);
        }
    }

    /**
     * Maneja la acción del botón "Seleccionar esta Propuesta".
     */
    @FXML
    public void handleSeleccionarPropuesta(ActionEvent event) {
        if (propuestaSeleccionada != null) {
            Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
            confirmation.setTitle("Confirmar Selección");
            confirmation.setHeaderText(null);
            confirmation.setContentText("¿Estás seguro de que deseas seleccionar la propuesta: \"" +
                    propuestaSeleccionada.getTitulo() + "\"?");

            Optional<ButtonType> result = confirmation.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                try {
                    // Aquí se llamaría a tu API para registrar la selección
                    // api.seleccionarPropuestaPPS(propuestaSeleccionada.getId(), usuarioActual.getId());

                    LOGGER.log(Level.INFO, "Propuesta seleccionada: " + propuestaSeleccionada.getTitulo());
                    mostrarAlerta("Selección Exitosa",
                            "Has seleccionado correctamente la propuesta: \"" + propuestaSeleccionada.getTitulo() + "\".",
                            Alert.AlertType.INFORMATION);

                    // Opcional: Redirigir al usuario a una pantalla de confirmación o al Home
                    volverAlHome();

                } catch (Exception ex) {
                    LOGGER.log(Level.SEVERE, "Error al seleccionar la propuesta", ex);
                    mostrarAlerta("Error de Selección", "No se pudo completar la selección de la propuesta.", Alert.AlertType.ERROR);
                }
            }
        } else {
            mostrarAlerta("Selección Requerida", "Por favor, selecciona una propuesta de la lista.", Alert.AlertType.WARNING);
        }
    }

    /**
     * Maneja la acción del botón "Cargar Mi Propuesta".
     */
    @FXML
    public void handleCargarPropuestaPropia(ActionEvent event) {
        // Validar campos requeridos
        if (tfTituloPropio.getText().trim().isEmpty() ||
                tfTipoPropio.getText().trim().isEmpty() ||
                taDescripcionPropia.getText().trim().isEmpty() ||
                taObjetivosPropios.getText().trim().isEmpty()) {

            mostrarAlerta("Campos Incompletos",
                    "Por favor, completa al menos los campos de Título, Tipo, Descripción y Objetivos.",
                    Alert.AlertType.WARNING);
            return;
        }

        try {
            // Crear un objeto con los datos de la propuesta
            PropuestaDTO nuevaPropuesta = new PropuestaDTO(
                    0, // ID temporal
                    tfTituloPropio.getText().trim(),
                    tfTipoPropio.getText().trim(),
                    tfOrganizacionPropia.getText().trim(),
                    tfTutorSugeridoPropio.getText().trim(),
                    1, // Por defecto, una vacante para el proyecto propio
                    taDescripcionPropia.getText().trim(),
                    taObjetivosPropios.getText().trim(),
                    taTecnologiasPropias.getText().trim()
            );

            // Aquí se llamaría a tu API para guardar la nueva propuesta
            // int idNuevaPropuesta = api.guardarPropuestaEstudiante(nuevaPropuesta, usuarioActual.getId());

            // Mostrar confirmación
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Propuesta Registrada");
            alert.setHeaderText(null);
            alert.setContentText("Tu propuesta \"" + nuevaPropuesta.getTitulo() + "\" ha sido registrada correctamente y está pendiente de validación por un docente.");
            alert.showAndWait();

            // Limpiar formulario o volver al Home
            limpiarFormularioPropuestaPropia();
            volverAlHome();

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Error al guardar la propuesta propia", ex);
            mostrarAlerta("Error de Registro",
                    "No se pudo registrar tu propuesta. Por favor, intenta nuevamente.",
                    Alert.AlertType.ERROR);
        }
    }

    /**
     * Limpia el formulario de carga de propuesta propia.
     */
    private void limpiarFormularioPropuestaPropia() {
        tfTituloPropio.clear();
        tfTipoPropio.clear();
        tfOrganizacionPropia.clear();
        tfTutorSugeridoPropio.clear();
        taDescripcionPropia.clear();
        taObjetivosPropios.clear();
        taTecnologiasPropias.clear();
    }

    /**
     * Maneja la acción del botón "Volver a Inicio".
     */
    @FXML
    public void handleVolverHome(ActionEvent event) {
        volverAlHome();
    }

    /**
     * Navega de vuelta a la pantalla de inicio (Home).
     */
    private void volverAlHome() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Frontend/vistas/home.fxml"));
            Parent root = loader.load();

            // Obtener el controlador
            HomeController controller = loader.getController();

            // Pasar la instancia de API
            controller.setPersistenceAPI(api);

            Stage stage = new Stage();
            stage.setTitle("Home - GPPS");
            stage.setScene(new Scene(root));
            stage.show();

            // Cerrar la ventana actual
            Stage currentStage = (Stage) btnVolverHome.getScene().getWindow();
            currentStage.close();

        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error al cargar la pantalla de Home", e);
            e.printStackTrace();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error general al navegar a Home", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * Muestra una alerta con el título, mensaje y tipo especificados.
     */
    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    /**
     * Clase interna para representar una propuesta de PPS.
     */
    public static class PropuestaDTO {
        private int id;
        private String titulo;
        private String tipo;
        private String organizacion;
        private String tutorPropuesto;
        private int vacantes;
        private String descripcion;
        private String objetivos;
        private String tecnologias;

        public PropuestaDTO(int id, String titulo, String tipo, String organizacion, String tutorPropuesto, int vacantes,
                            String descripcion, String objetivos, String tecnologias) {
            this.id = id;
            this.titulo = titulo;
            this.tipo = tipo;
            this.organizacion = organizacion;
            this.tutorPropuesto = tutorPropuesto;
            this.vacantes = vacantes;
            this.descripcion = descripcion;
            this.objetivos = objetivos;
            this.tecnologias = tecnologias;
        }

        // Getters
        public int getId() { return id; }
        public String getTitulo() { return titulo; }
        public String getTipo() { return tipo; }
        public String getOrganizacion() { return organizacion; }
        public String getTutorPropuesto() { return tutorPropuesto; }
        public int getVacantes() { return vacantes; }
        public String getDescripcion() { return descripcion; }
        public String getObjetivos() { return objetivos; }
        public String getTecnologias() { return tecnologias; }

        @Override
        public String toString() {
            return titulo;
        }
    }
}