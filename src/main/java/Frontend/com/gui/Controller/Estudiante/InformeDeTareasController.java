package Frontend.com.gui.Controller.Estudiante;

import Backend.API.API;
import Backend.DTO.ActividadDTO;
import Backend.DTO.ConvenioPPSDTO;
import Backend.DTO.InformeDTO;
import Backend.Exceptions.CreateException;
import Backend.Exceptions.ReadException;
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
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

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
    @FXML private TableColumn<InformeDTO, String> colDescripcionInforme;

    private API api;
    private ObservableList<ActividadDTO> listaActividades = FXCollections.observableArrayList();
    private File archivoSeleccionado;

    @FXML
    public void initialize() {
        configurarComboBoxActividades();
        configurarTableViewInformes();
        fechaInformePicker.setValue(LocalDate.now());

        actividadComboBox.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                tituloActividadSeleccionada.setText("Informes de la Actividad: " + newSelection.getTitulo());
                cargarYMostrarInformesDeActividad(newSelection.getTitulo());
            } else {
                tituloActividadSeleccionada.setText("Informes de la Actividad: (Ninguna seleccionada)");
                informesTableView.setItems(FXCollections.emptyObservableList());
            }
        });
    }

    public void setPersistenceAPI(API persistenceAPI) {
        this.api = persistenceAPI;
        if (this.api != null) {
            cargarActividades(); // Load activities as soon as API is available
        } else {
            LOGGER.log(Level.WARNING, "Persistence API set to null.");
            listaActividades.clear();
            informesTableView.setItems(FXCollections.emptyObservableList());
            tituloActividadSeleccionada.setText("Informes de la Actividad: (API no disponible)");
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
                return listaActividades.stream()
                        .filter(act -> act.getTitulo().equals(string))
                        .findFirst()
                        .orElse(null);
            }
        });
    }

    private void configurarTableViewInformes() {
        colTituloInforme.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        colFechaInforme.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        //colDescripcionInforme.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
    }

    private void cargarActividades() {
        if (api == null) {
            LOGGER.log(Level.WARNING, "API no disponible para cargar actividades.");
            listaActividades.clear();
            informesTableView.setItems(FXCollections.emptyObservableList());
            tituloActividadSeleccionada.setText("Informes de la Actividad: (API no disponible)");
            return;
        }

        try {
            // Convenios hardcodeados
            List<String> convenios = List.of(
                    "Convenio Energía Renovable",
                    "Convenio IoT Inteligente"
            );

            for (String convenio : convenios) {
                LOGGER.log(Level.INFO, "Cargando informes del convenio: " + convenio);

                List<InformeDTO> informes = api.obtenerInformesByConvenioTitulo(convenio);

                if (informes != null && !informes.isEmpty()) {
                    LOGGER.log(Level.INFO, "Se encontraron " + informes.size() + " informes para " + convenio);

                    // Podés mostrar uno por uno en consola o hacer algo con ellos
                    for (InformeDTO informe : informes) {
                        System.out.println("[" + convenio + "] Informe: " + informe);
                    }

                    // Opcional: mostrar los informes en la UI (por ejemplo, del primer convenio)
                    // Esto se ejecuta solo para el primero y termina.
                    informesTableView.setItems(FXCollections.observableArrayList(informes));
                    tituloActividadSeleccionada.setText("Informes del Convenio: " + convenio);
                    break; // mostramos solo el primero en UI
                } else {
                    LOGGER.log(Level.INFO, "No hay informes para el convenio: " + convenio);
                }
            }

        } catch (ReadException e) {
            LOGGER.log(Level.SEVERE, "Error al cargar informes desde la API.", e);
            mostrarAlerta("Error de Carga", "No se pudieron cargar los informes: " + e.getMessage(), Alert.AlertType.ERROR);
            informesTableView.setItems(FXCollections.emptyObservableList());
            tituloActividadSeleccionada.setText("Informes de la Actividad: (Error al cargar)");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error inesperado al cargar informes.", e);
            mostrarAlerta("Error Inesperado", "Ocurrió un error inesperado al cargar los informes: " + e.getMessage(), Alert.AlertType.ERROR);
            informesTableView.setItems(FXCollections.emptyObservableList());
            tituloActividadSeleccionada.setText("Informes de la Actividad: (Error inesperado)");
        }
    }



    private void cargarYMostrarInformesDeActividad(String convenioTitulo) {
        if (api == null || convenioTitulo == null || convenioTitulo.isEmpty()) {
            informesTableView.setItems(FXCollections.emptyObservableList());
            return;
        }
        try {
            List<InformeDTO> informes = api.obtenerInformesByConvenioTitulo(convenioTitulo);

            if (informes != null && !informes.isEmpty()) {
                ObservableList<InformeDTO> informesObservable = FXCollections.observableArrayList(informes);
                informesTableView.setItems(informesObservable);
            } else {
                informesTableView.setItems(FXCollections.emptyObservableList());
                LOGGER.log(Level.INFO, "No hay informes para el convenio: " + convenioTitulo);
            }
        } catch (ReadException e) {
            LOGGER.log(Level.SEVERE, "Error al cargar informes del convenio: " + convenioTitulo, e);
            mostrarAlerta("Error de Carga", "No se pudieron cargar los informes para el convenio: " + e.getMessage(), Alert.AlertType.ERROR);
            informesTableView.setItems(FXCollections.emptyObservableList());
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error inesperado al cargar informes para el convenio: " + convenioTitulo, e);
            mostrarAlerta("Error Inesperado", "Ocurrió un error inesperado al cargar los informes: " + e.getMessage(), Alert.AlertType.ERROR);
            informesTableView.setItems(FXCollections.emptyObservableList());
        }
    }


    public byte[] leerArchivoPDF(String path) throws IOException {
        return Files.readAllBytes(Paths.get(path));
    }

    public void refrescarActividades() {
        cargarActividades();
    }

    public void refrescarInformes() {
        ActividadDTO actividadSeleccionada = actividadComboBox.getValue();
        if (actividadSeleccionada != null) {
            cargarYMostrarInformesDeActividad(actividadSeleccionada.getTitulo());
        } else {
            informesTableView.setItems(FXCollections.emptyObservableList());
            tituloActividadSeleccionada.setText("Informes de la Actividad: (Ninguna seleccionada)");
        }
    }

    @FXML
    private void subirArchivoInforme(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar archivo PDF");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Archivos PDF", "*.pdf"));

        Stage ownerStage = (Stage) volverButton.getScene().getWindow();
        File selectedFile = fileChooser.showOpenDialog(ownerStage);

        if (selectedFile != null) {
            if (!selectedFile.getName().toLowerCase().endsWith(".pdf")) {
                mostrarAlerta("Error de Archivo", "Por favor seleccione un archivo PDF válido.", Alert.AlertType.ERROR);
                this.archivoSeleccionado = null;
                contenidoInformeField.clear();
                return;
            }
            if (!selectedFile.exists() || !selectedFile.canRead()) {
                mostrarAlerta("Error de Archivo", "El archivo seleccionado no existe o no se puede leer.", Alert.AlertType.ERROR);
                this.archivoSeleccionado = null;
                contenidoInformeField.clear();
                return;
            }

            this.archivoSeleccionado = selectedFile;
            contenidoInformeField.setText(selectedFile.getName());
        } else {
            LOGGER.log(Level.INFO, "Selección de archivo cancelada.");
        }
    }

    @FXML
    private void GuardarInforme(ActionEvent event) {
        ActividadDTO actividadSeleccionadaEnComboBox = actividadComboBox.getValue();
        if (actividadSeleccionadaEnComboBox == null) {
            mostrarAlerta("Validación", "Debe seleccionar una actividad primero.", Alert.AlertType.WARNING);
            return;
        }

        String titulo = tituloInformeField.getText();
        String descripcion = descripcionInformeArea.getText();
        LocalDate fechaInforme = fechaInformePicker.getValue();

        if (titulo.trim().isEmpty() || descripcion.trim().isEmpty() || archivoSeleccionado == null || fechaInforme == null) {
            mostrarAlerta("Validación", "Todos los campos (Título, Descripción, Archivo PDF, Fecha) son obligatorios.", Alert.AlertType.WARNING);
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
            actividadSeleccionadaEnComboBox.addInforme(nuevoInformeDTO);
            cargarYMostrarInformesDeActividad(actividadSeleccionadaEnComboBox.getTitulo());
            limpiarFormularioInforme();
            mostrarAlerta("Éxito", "Informe guardado correctamente.", Alert.AlertType.INFORMATION);

        } catch (CreateException e) {
            LOGGER.log(Level.SEVERE, "Error al guardar el informe en la base de datos", e);
            mostrarAlerta("Error al Guardar", "No se pudo guardar el informe: " + e.getMessage(), Alert.AlertType.ERROR);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error inesperado al guardar el informe", e);
            mostrarAlerta("Error Inesperado", "Ocurrió un error inesperado al intentar guardar el informe: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void limpiarFormularioInforme() {
        tituloInformeField.clear();
        descripcionInformeArea.clear();
        contenidoInformeField.clear();
        fechaInformePicker.setValue(LocalDate.now());
        archivoSeleccionado = null;
    }

    @FXML
    public void VolverHome(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Frontend/vistas/Estudiante/home.fxml"));
            Parent root = loader.load();
            HomeController controller = loader.getController();

            if (this.api != null) {
                controller.setPersistenceAPI(this.api);
                // No more tituloConvenio to pass
            } else {
                LOGGER.log(Level.WARNING, "API no inicializada al volver a Home.");
            }

            Stage stage = (Stage) volverButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Home - GPPS");

        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error al cargar home.fxml", e);
            mostrarAlerta("Error de Navegación", "No se pudo volver a la pantalla principal.", Alert.AlertType.ERROR);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error al configurar la pantalla Home", e);
            mostrarAlerta("Error de Configuración", "No se pudo configurar la pantalla principal: " + e.getMessage(), Alert.AlertType.ERROR);
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