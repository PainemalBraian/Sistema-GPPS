package Frontend.com.gui.Controller.Estudiante;

import Backend.API.API;
import Backend.DTO.ActividadDTO;
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
import javafx.util.Callback;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class InformeDeTareasController {

    private static final Logger LOGGER = Logger.getLogger(InformeDeTareasController.class.getName());

    @FXML private Button volverButton;
    @FXML private ComboBox<ActividadDTO> actividadComboBox;
    @FXML private TextField tituloInformeField;
    @FXML private TextArea descripcionInformeArea;
    @FXML private TextField contenidoInformeField;
    @FXML private Button guardarInformeButton;
    @FXML private Label tituloActividadSeleccionada;
    @FXML private TableView<InformeDTO> informesTableView;
    @FXML private TableColumn<InformeDTO, String> colTituloInforme;
    @FXML private TableColumn<InformeDTO, LocalDate> colFechaInforme;
    @FXML private TableColumn<InformeDTO, String> colCalificacionDocente;
    @FXML private TableColumn<InformeDTO, String> colCalificacionTutor;
    @FXML private TableColumn<InformeDTO, Void> colContenidoInforme;

    private API api;
    private ObservableList<ActividadDTO> listaActividades = FXCollections.observableArrayList();
    private File archivoSeleccionado;

    @FXML
    public void initialize() {
        configurarComboBoxActividades();
        configurarTableViewInformes();

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
            cargarActividades();
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
        colCalificacionDocente.setCellValueFactory(new PropertyValueFactory<>("calificacionDocente"));
        colCalificacionTutor.setCellValueFactory(new PropertyValueFactory<>("calificacionTutor"));

        colContenidoInforme.setCellFactory(new Callback<TableColumn<InformeDTO, Void>, TableCell<InformeDTO, Void>>() {
            @Override
            public TableCell<InformeDTO, Void> call(TableColumn<InformeDTO, Void> param) {
                return new TableCell<InformeDTO, Void>() {
                    private final Button descargarButton = new Button("Descargar PDF");

                    {
                        descargarButton.setOnAction(event -> {
                            InformeDTO informe = getTableView().getItems().get(getIndex());
                            descargarInformePDF(informe);
                        });
                    }

                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);

                        if (empty) {
                            setGraphic(null);
                        } else {
                            InformeDTO informe = getTableView().getItems().get(getIndex());

                            // Deshabilitar el botón si no hay archivo
                            boolean tieneArchivo = informe.getArchivo() != null;
                            descargarButton.setDisable(!tieneArchivo);

                            setGraphic(descargarButton);
                        }
                    }
                };
            }
        });
    }


    private void cargarActividades() {
        if (api == null) {
            LOGGER.log(Level.WARNING, "API no disponible para cargar actividades.");
            listaActividades.clear();
            return;
        }

        try {
            // Obtener todas las actividades disponibles
            List<ActividadDTO> actividades = api.obtenerActividadesByEstudianteUsername(api.obtenerUsername());

            if (actividades != null && !actividades.isEmpty()) {
                listaActividades.clear();
                listaActividades.addAll(actividades);
//                LOGGER.log(Level.INFO, "Se cargaron " + actividades.size() + " actividades.");
            } else {
                LOGGER.log(Level.INFO, "No se encontraron actividades disponibles.");
                listaActividades.clear();
            }

        } catch (ReadException e) {
            LOGGER.log(Level.SEVERE, "Error al cargar actividades desde la API.", e);
            mostrarAlerta("Error de Carga", "No se pudieron cargar las actividades: " + e.getMessage(), Alert.AlertType.ERROR);
            listaActividades.clear();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error inesperado al cargar actividades.", e);
            mostrarAlerta("Error Inesperado", "Ocurrió un error inesperado al cargar las actividades: " + e.getMessage(), Alert.AlertType.ERROR);
            listaActividades.clear();
        }
    }

    private void cargarYMostrarInformesDeActividad(String actividadTitulo) {
        if (api == null || actividadTitulo == null || actividadTitulo.isEmpty()) {
            informesTableView.setItems(FXCollections.emptyObservableList());
            return;
        }
        try {
            List<InformeDTO> informes = api.obtenerInformesByActividadTitulo(actividadTitulo);

            if (informes != null && !informes.isEmpty()) {
                ObservableList<InformeDTO> informesObservable = FXCollections.observableArrayList(informes);
                informesTableView.setItems(informesObservable);
            } else {
                informesTableView.setItems(FXCollections.emptyObservableList());
                LOGGER.log(Level.INFO, "No hay informes para la actividad: " + actividadTitulo);
            }
        } catch (ReadException e) {
            LOGGER.log(Level.SEVERE, "Error al cargar informes de la actividad: " + actividadTitulo, e);
            mostrarAlerta("Error de Carga", "No se pudieron cargar los informes para la actividad: " + e.getMessage(), Alert.AlertType.ERROR);
            informesTableView.setItems(FXCollections.emptyObservableList());
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error inesperado al cargar informes para la actividad: " + actividadTitulo, e);
            mostrarAlerta("Error Inesperado", "Ocurrió un error inesperado al cargar los informes: " + e.getMessage(), Alert.AlertType.ERROR);
            informesTableView.setItems(FXCollections.emptyObservableList());
        }
    }

    private void descargarInformePDF(InformeDTO informe) {
        if (api == null || informe.getArchivo() == null) {
            mostrarAlerta("Error", "No se puede descargar el informe. Datos no disponibles.", Alert.AlertType.ERROR);
            return;
        }

        try {
            api.descargarArchivoPDF(informe.getTitulo());
            mostrarAlerta("Éxito", "El archivo PDF se ha descargado correctamente en la carpeta Downloads/.GPPS_Downloads", Alert.AlertType.INFORMATION);

        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error al descargar el archivo PDF: " + informe.getTitulo(), e);
            mostrarAlerta("Error de Descarga", "No se pudo descargar el archivo: " + e.getMessage(), Alert.AlertType.ERROR);
        } catch (ReadException e) {
            LOGGER.log(Level.SEVERE, "Error al leer el informe desde la base de datos: " + informe.getTitulo(), e);
            mostrarAlerta("Error de Lectura", "No se pudo leer el informe desde la base de datos: " + e.getMessage(), Alert.AlertType.ERROR);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error inesperado al descargar el informe: " + informe.getTitulo(), e);
            mostrarAlerta("Error Inesperado", "Ocurrió un error inesperado al descargar el informe: " + e.getMessage(), Alert.AlertType.ERROR);
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

        if (titulo.trim().isEmpty() || descripcion.trim().isEmpty() || archivoSeleccionado == null) {
            mostrarAlerta("Validación", "Todos los campos (Título, Descripción, Archivo PDF) son obligatorios.", Alert.AlertType.WARNING);
            return;
        }

        try {
            if (api == null) {
                LOGGER.log(Level.SEVERE, "API no disponible. No se puede guardar el informe.");
                mostrarAlerta("Error de Sistema", "La conexión con el sistema (API) no está disponible.", Alert.AlertType.ERROR);
                return;
            }

            LocalDate fechaCreacion = LocalDate.now();
            byte[] archivo = leerArchivoPDF(archivoSeleccionado.getAbsolutePath());

            // Cargar informe con el título de la actividad seleccionada
            api.cargarInforme(titulo, descripcion, archivo, fechaCreacion, actividadSeleccionadaEnComboBox.getTitulo());

            // Refrescar la tabla de informes
            cargarYMostrarInformesDeActividad(actividadSeleccionadaEnComboBox.getTitulo());
            limpiarFormularioInforme();
            mostrarAlerta("Éxito", "Informe guardado correctamente.", Alert.AlertType.INFORMATION);

        } catch (CreateException e) {
            LOGGER.log(Level.SEVERE, "Error al guardar el informe en la base de datos", e);
            mostrarAlerta("Error al Guardar", "No se pudo guardar el informe: " + e.getMessage(), Alert.AlertType.ERROR);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error al leer el archivo PDF", e);
            mostrarAlerta("Error de Archivo", "No se pudo leer el archivo PDF seleccionado: " + e.getMessage(), Alert.AlertType.ERROR);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error inesperado al guardar el informe", e);
            mostrarAlerta("Error Inesperado", "Ocurrió un error inesperado al intentar guardar el informe: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void limpiarFormularioInforme() {
        tituloInformeField.clear();
        descripcionInformeArea.clear();
        contenidoInformeField.clear();
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