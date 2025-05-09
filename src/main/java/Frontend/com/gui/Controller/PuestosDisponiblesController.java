package Frontend.com.gui.Controller;

import Backend.API.API; // Asume que tienes una API backend similar a la del HomeController
//import Backend.API.DTOs.OportunidadPPS_DTO; // Asume un DTO para representar una oportunidad
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PuestosDisponiblesController {

    private static final Logger LOGGER = Logger.getLogger(PuestosDisponiblesController.class.getName());

    private API api; // Instancia de tu API para interactuar con el backend
    private ResourceBundle bundle; // Para localización, similar al HomeController
    private ObservableList<OportunidadPPS_DTO> oportunidadesList; // Lista observable para la ListView
    private OportunidadPPS_DTO oportunidadSeleccionada; // Oportunidad que el usuario ha seleccionado

    @FXML private ListView<OportunidadPPS_DTO> lvOportunidades;
    @FXML private Label lblTituloOportunidad;
    @FXML private Label lblEmpresa;
    @FXML private Label lblArea;
    @FXML private Label lblVacantes;
    @FXML private TextArea taDescripcion;
    @FXML private TextArea taRequisitos;
    @FXML private Button btnInscribir;
    @FXML public Button btnVolverHome;

    //@FXML private VBox vboxDetail; // Contenedor de detalles para ocultar/mostrar inicialmente

    /**
     * Método llamado automáticamente después de que el FXML ha sido cargado.
     */
    @FXML
    public void initialize() {
        // Ocultar el panel de detalles hasta que se seleccione una oportunidad
     //   vboxDetail.setVisible(false);

        // Configurar el listener para la selección en la ListView
        lvOportunidades.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> mostrarDetallesOportunidad(newValue));
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
        cargarOportunidadesDisponibles();
    }

    /**
     * Carga las oportunidades de PPS disponibles desde la API y las muestra en la lista.
     */
    private void cargarOportunidadesDisponibles() {
        try {
            // Aquí se implementaría la llamada a tu API para obtener las oportunidades
            // List<OportunidadPPS_DTO> oportunidades = api.obtenerOportunidadesDisponibles();

            // --- datos para prueba ---
            List<OportunidadPPS_DTO> oportunidades = List.of(
                    new OportunidadPPS_DTO(1, "Desarrollador Java Junior", "Empresa Tech Solutions", "Desarrollo de Software", 5, "Desarrollo de nuevas funcionalidades y mantenimiento de aplicaciones existentes.", "Conocimiento en Java, Spring Boot, SQL. Estudiante avanzado de Ingeniería en Sistemas."),
                    new OportunidadPPS_DTO(2, "Analista de Datos", "Data Insights S.A.", "Análisis de Datos", 2, "Análisis de grandes volúmenes de datos, creación de informes y dashboards.", "Conocimiento en Python, Pandas, SQL, herramientas de visualización (Tableau/PowerBI)."),
                    new OportunidadPPS_DTO(3, "Diseñador UX/UI", "Creative Minds", "Diseño", 3, "Diseño de interfaces de usuario y experiencia de usuario para aplicaciones web y móviles.", "Conocimiento en herramientas de diseño (Figma, Adobe XD), principios de UX/UI.")
            );


            oportunidadesList = FXCollections.observableArrayList(oportunidades);
            lvOportunidades.setItems(oportunidadesList);

            // Personalizar cómo se muestra cada elemento en la lista
            lvOportunidades.setCellFactory(lv -> new ListCell<OportunidadPPS_DTO>() {
                @Override
                protected void updateItem(OportunidadPPS_DTO oportunidad, boolean empty) {
                    super.updateItem(oportunidad, empty);
                    if (empty || oportunidad == null) {
                        setText(null);
                    } else {
                        // Mostrar solo el título de la oportunidad en la lista
                        setText(oportunidad.getTitulo());
                    }
                }
            });

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Error al cargar las oportunidades disponibles", ex);
            mostrarAlerta("Error de Carga", "No se pudieron cargar las oportunidades de PPS disponibles.", Alert.AlertType.ERROR);
        }
    }

    /**
     * Muestra los detalles de la oportunidad seleccionada en el panel derecho.
     * @param oportunidad La oportunidad seleccionada, o null si no hay selección.
     */
    private void mostrarDetallesOportunidad(OportunidadPPS_DTO oportunidad) {
        oportunidadSeleccionada = oportunidad;
        if (oportunidadSeleccionada != null) {
            lblTituloOportunidad.setText(oportunidadSeleccionada.getTitulo());
            lblEmpresa.setText("Empresa: " + oportunidadSeleccionada.getEmpresa());
            lblArea.setText("Área: " + oportunidadSeleccionada.getArea());
            lblVacantes.setText("Vacantes: " + oportunidadSeleccionada.getVacantes());
            taDescripcion.setText(oportunidadSeleccionada.getDescripcion());
            taRequisitos.setText(oportunidadSeleccionada.getRequisitos());
            //vboxDetail.setVisible(true); // Mostrar el panel de detalles
            btnInscribir.setDisable(false); // Habilitar el botón de inscripción
        } else {
            // Si no hay selección, limpiar los detalles y ocultar el panel
            lblTituloOportunidad.setText("Selecciona una oportunidad");
            lblEmpresa.setText("Empresa: -");
            lblArea.setText("Área: -");
            lblVacantes.setText("Vacantes: -");
            taDescripcion.setText("");
            taRequisitos.setText("");
            //vboxDetail.setVisible(false);
            btnInscribir.setDisable(true);
        }
    }

    /**
     * Maneja la acción del botón "Inscribirme a esta PPS".
     */
    @FXML
    private void handleInscribir(ActionEvent event) {
        if (oportunidadSeleccionada != null) {
            Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
            confirmation.setTitle("Confirmar Inscripción");
            confirmation.setHeaderText(null);
            confirmation.setContentText("¿Estás seguro de que deseas inscribirte a la oportunidad: \"" + oportunidadSeleccionada.getTitulo() + "\" en " + oportunidadSeleccionada.getEmpresa() + "?");

            Optional<ButtonType> result = confirmation.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                try {
                    // Aquí se llamaría a tu API para realizar la inscripción
                    // api.inscribirEstudiantePPS(oportunidadSeleccionada.getIdOportunidad());

                    LOGGER.log(Level.INFO, "Estudiante inscrito a PPS: " + oportunidadSeleccionada.getTitulo());
                    mostrarAlerta("Inscripción Exitosa", "Te has inscrito correctamente a la PPS: \"" + oportunidadSeleccionada.getTitulo() + "\".", Alert.AlertType.INFORMATION);

                    // Opcional: Actualizar la lista o cerrar la ventana después de la inscripción
                    // cargarOportunidadesDisponibles(); // Para remover la oportunidad inscrita de la lista
                    // ((Stage)((Button)event.getSource()).getScene().getWindow()).close(); // Para cerrar la ventana

                } catch (Exception ex) { // Capturar la excepción específica de tu API si la tienes
                    LOGGER.log(Level.SEVERE, "Error al inscribir al estudiante", ex);
                    mostrarAlerta("Error de Inscripción", "No se pudo completar la inscripción a la PPS.", Alert.AlertType.ERROR);
                }
            }
        } else {
            mostrarAlerta("Selección Requerida", "Por favor, selecciona una oportunidad de la lista para inscribirte.", Alert.AlertType.WARNING);
        }
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
            // Obtener el controlador
            HomeController controller = loader.getController();

            // Crear y pasar la instancia de PersistenceAPI
            controller.setPersistenceAPI(api);

            Stage stage = new Stage();
            stage.setTitle(" Home - GPPS");
            stage.setScene(new Scene(root));
            stage.show();

            // Opcional: cerrar la ventana actual
            ((Stage) ((Button) event.getSource()).getScene().getWindow()).close();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /*
     * Si necesitas localización para esta vista, descomenta este método
     * y las llamadas en setPersistenceAPI y initialize.
     */
    // private void actualizarIdioma() {
    //     try {
    //         // Ejemplo:
    //         // lblTituloOportunidadesDisponibles.setText(bundle.getString("label.oportunidadesDisponibles"));
    //         // btnInscribir.setText(bundle.getString("button.inscribirPPS"));
    //         // etc.
    //     } catch (Exception ex) {
    //         LOGGER.log(Level.WARNING, "Error al cargar los textos del idioma en InscripcionPPSController", ex);
    //     }
    // }



    public static class OportunidadPPS_DTO {
        private int idOportunidad;
        private String titulo;
        private String empresa;
        private String area;
        private int vacantes;
        private String descripcion;
        private String requisitos;

        public OportunidadPPS_DTO(int idOportunidad, String titulo, String empresa, String area, int vacantes, String descripcion, String requisitos) {
            this.idOportunidad = idOportunidad;
            this.titulo = titulo;
            this.empresa = empresa;
            this.area = area;
            this.vacantes = vacantes;
            this.descripcion = descripcion;
            this.requisitos = requisitos;
        }

        // Getters (necesarios para acceder a los datos)
        public int getIdOportunidad() { return idOportunidad; }
        public String getTitulo() { return titulo; }
        public String getEmpresa() { return empresa; }
        public String getArea() { return area; }
        public int getVacantes() { return vacantes; }
        public String getDescripcion() { return descripcion; }
        public String getRequisitos() { return requisitos; }

        // Opcional: Sobrescribir toString para que la ListView muestre el título
        @Override
        public String toString() {
            return titulo;
        }
    }
}