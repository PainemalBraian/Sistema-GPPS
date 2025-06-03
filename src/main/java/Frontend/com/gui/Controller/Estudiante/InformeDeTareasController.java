package Frontend.com.gui.Controller.Estudiante;

import Backend.API.API;
import Backend.DTO.ActividadDTO;
import Backend.DTO.InformeDTO;
import Backend.Exceptions.CreateException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;


public class InformeDeTareasController {

    private static final Logger LOGGER = Logger.getLogger(InformeDeTareasController.class.getName());

    @FXML private Button volverButton;
    @FXML private ComboBox<ActividadDTO> actividadComboBox;
    @FXML private TextField tituloInformeField;
    @FXML private TextArea descripcionInformeArea;
    @FXML private TextField contenidoInformeField;
    @FXML private DatePicker fechaInformePicker;
    @FXML private Button guardarInformeButton;
    @FXML private Label tituloActividadSeleccionada;
    @FXML private TableView<InformeDTO> informesTableView;
    @FXML private TableColumn<InformeDTO, String> colTituloInforme;
    @FXML private TableColumn<InformeDTO, LocalDate> colFechaInforme;
    @FXML private TableColumn<InformeDTO, Integer> colPorcentajeInforme;
    @FXML private TableColumn<InformeDTO, String> colContenidoInforme;

    private API api;
    private ResourceBundle bundle;
    private ObservableList<ActividadDTO> listaActividades = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        configurarComboBoxActividades();
        configurarTableViewInformes();


        actividadComboBox.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                tituloActividadSeleccionada.setText("Informes de la Actividad: " + newSelection.getTitulo());
        //        informesTableView.setItems(newSelection.getInformes()); // Obtiene la lista de informes de la api
            } else {
                tituloActividadSeleccionada.setText("Informes de la Actividad: (Ninguna seleccionada)");
                informesTableView.setItems(FXCollections.emptyObservableList());
            }
        });
        if (api == null) {
            LOGGER.log(Level.INFO, "API not yet initialized. Dummy data might be loaded if applicable.");
            // cargarActividadesDummy();
        }
    }

    public void setPersistenceAPI(API persistenceAPI) throws Exception { // Added throws Exception
        this.api = persistenceAPI;
        // this.bundle = api.obtenerIdioma();
        // actualizarIdioma();
    //    cargarActividades();
    }

    private void actualizarIdioma() {
        if (bundle == null) {
            LOGGER.log(Level.WARNING, "ResourceBundle no cargado. No se puede actualizar idioma.");
            return;
        }

    }

    private void configurarComboBoxActividades() {
        actividadComboBox.setItems(listaActividades);
        actividadComboBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(ActividadDTO actividad) {
                return actividad == null ? null : actividad.getTitulo();
            }
            @Override
            public ActividadDTO fromString(String string) {
                return null;
            }
        });
    }

    private void configurarTableViewInformes() {
        colTituloInforme.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        colFechaInforme.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        colContenidoInforme.setCellValueFactory(new PropertyValueFactory<>("contenido"));
    }


    @FXML
    private void subirArchivoInforme(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar archivo PDF");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Archivos PDF", "*.pdf"));

        File selectedFile = fileChooser.showOpenDialog(null); // Puede ser tu Stage principal si tenés referencia

        if (selectedFile != null) {
            contenidoInformeField.setText(selectedFile.getAbsolutePath());
        } else {
            mostrarAlerta("Información", "No se seleccionó ningún archivo.", Alert.AlertType.INFORMATION);
        }
    }





/*
    private void mostrarActividades() {
        try {
            Replace 'api.obtenerActividadesDTOs()'
            List<ActividadDTO> actividadesDesdeAPI = api.obtenerActividadesDTOs();
            listaActividades.setAll(actividadesDesdeAPI);
            if (api.mostrarActividades()) {
                mostrarAlerta("Información", "No hay actividades disponibles actualmente.", Alert.AlertType.INFORMATION);
            }
        } catch (ReadException e) {
            LOGGER.log(Level.SEVERE, "Error al cargar actividades desde la API", e);
            mostrarAlerta("Error de Carga", "No se pudieron cargar las actividades: " + e.getMessage(), Alert.AlertType.ERROR);
            cargarActividadesDummy();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error inesperado al cargar actividades", e);
            mostrarAlerta("Error Inesperado", "Ocurrió un error inesperado al cargar las actividades.", Alert.AlertType.ERROR);
            cargarActividadesDummy();
        }
    }

    private void mostrarEjemplosActividades() {
        LOGGER.log(Level.INFO, "Cargando actividades dummy para demostración.");
        ActividadDTO act1 = new ActividadDTO("Proyecto Alpha (DTO)", "Desarrollo frontend con DTOs");
        InformeDTO inf1_1 = new InformeDTO("Avance UI Login (DTO)", "Login DTO completo", "path/dto/login.pdf", LocalDate.now().minusDays(5), 50);
        InformeDTO inf1_2 = new InformeDTO("Revisión Componentes (DTO)", "Componentes DTO definidos", "path/dto/comps.pdf", LocalDate.now().minusDays(2), 75);
        act1.addInforme(inf1_1);
        act1.addInforme(inf1_2);

        ActividadDTO act2 = new ActividadDTO("Proyecto Beta (DTO)", "Backend API con DTOs");
        listaActividades.setAll(act1, act2);
    }
*/

    @FXML
    private void GuardarInforme(ActionEvent event) {
        ActividadDTO actividadSeleccionada = actividadComboBox.getValue();
        if (actividadSeleccionada == null) {
            mostrarAlerta("Error", "Debe seleccionar una actividad primero.", Alert.AlertType.WARNING);
            return;
        }

        String titulo = tituloInformeField.getText();
        String descripcion = descripcionInformeArea.getText();
        String contenido = contenidoInformeField.getText();

        if (titulo.isEmpty() || descripcion.isEmpty() || contenido.isEmpty()) {
            mostrarAlerta("Error de Validación", "Todos los campos (Título, Descripción, Contenido, Fecha) son obligatorios.", Alert.AlertType.WARNING);
            return;
        }

        try {
            if (api == null) {
                LOGGER.log(Level.SEVERE, "API no disponible. No se puede guardar el informe.");
                mostrarAlerta("Error de Sistema", "La conexión con el sistema (API) no está disponible.", Alert.AlertType.ERROR);
                return;
            }

            LocalDate fechaCreacion = LocalDate.now();
            byte[] archivo = new byte[0]; // se necesita cargar el archivo pdf seleccionado en este tipo y pasarlo en lugar de "contenido"
            // Carga de informe además se va a llevar el nombre de la actividad a la que corresponder la entrega del informe.
            //Usar algo como "getSelectedRows" de la lista de actividades que tenes para seleccionar y tomar su nombre
            api.cargarInforme(titulo, descripcion, archivo, fechaCreacion,"");



            InformeDTO nuevoInformeDTO = new InformeDTO(titulo, descripcion, archivo, fechaCreacion);
            actividadSeleccionada.addInforme(nuevoInformeDTO);

            limpiarFormularioInforme();
            mostrarAlerta("Éxito", "Informe guardado correctamente.", Alert.AlertType.INFORMATION);
            LOGGER.log(Level.INFO, "Informe guardado: " + titulo + " para actividad: " + actividadSeleccionada.getTitulo());

        } catch (CreateException e) {
            LOGGER.log(Level.SEVERE, "Error al guardar el informe en la base de datos", e);
            mostrarAlerta("Error al Guardar", "No se pudo guardar el informe: " + e.getMessage(), Alert.AlertType.ERROR);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error inesperado al guardar el informe", e);
            mostrarAlerta("Error Inesperado", "Ocurrió un error inesperado al intentar guardar el informe.", Alert.AlertType.ERROR);
        }
    }

    private void limpiarFormularioInforme() {
        tituloInformeField.clear();
        descripcionInformeArea.clear();
        contenidoInformeField.clear();
        fechaInformePicker.setValue(LocalDate.now());
    }

    @FXML
    public void VolverHome(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Frontend/vistas/Estudiante/home.fxml"));
            Parent root = loader.load();
            HomeController controller = loader.getController();

            if (this.api != null) {
                controller.setPersistenceAPI(this.api);
            } else {
                LOGGER.log(Level.WARNING, "API no inicializada al volver a Home.");
            }

            Stage stage = new Stage();
            stage.setTitle("Home - GPPS");
            stage.setScene(new Scene(root));
            stage.show();

            ((Stage) ((Button) event.getSource()).getScene().getWindow()).close();

        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error al cargar home.fxml", e);
            mostrarAlerta("Error de Navegación", "No se pudo volver a la pantalla principal.", Alert.AlertType.ERROR);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error al configurar API en HomeController", e);
            mostrarAlerta("Error de Configuración", "Hubo un problema al configurar la pantalla principal.", Alert.AlertType.ERROR);
        }
    }

    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}