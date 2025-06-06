package Frontend.com.gui.Controller.DirectorCarrera;

import Backend.API.API;
import Backend.DTO.ConvenioPPSDTO;
import Backend.DTO.DocenteDTO;
import Backend.DTO.PlanDeTrabajoDTO;
import Backend.Entidades.PlanDeTrabajo;
import Backend.Exceptions.CreateException;
import Backend.Exceptions.ReadException;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.geometry.Insets;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.stage.FileChooser;

import java.io.File;

import java.io.FileOutputStream;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;


public class GestionConveniosController {

    private static final Logger LOGGER = Logger.getLogger(GestionConveniosController.class.getName());

    @FXML private TableView<ConvenioPPSDTO> conveniosTableView;
    @FXML private TableColumn<ConvenioPPSDTO, String> colConvenio;
    @FXML private TableColumn<ConvenioPPSDTO, Boolean> colHabilitado;
    @FXML private TableColumn<ConvenioPPSDTO, String> colEntidad;
    @FXML private TableColumn<ConvenioPPSDTO, String> colEstudiante;
    @FXML private TableColumn<ConvenioPPSDTO, String> colProyecto;
    @FXML private TableColumn<ConvenioPPSDTO, String> colTutor;
    @FXML private TableColumn<ConvenioPPSDTO, String> colPlanTrabajo;
    @FXML private Button habilitarButton;
    @FXML private Button deshabilitarButton;
    @FXML private Button verDetallesButton;
    @FXML private Button btnAsignarDocente;
    @FXML private Button btnAsignarTituloYDescripcion;
    @FXML private Button btnVolver;

    private final ObservableList<ConvenioPPSDTO> listaConvenios = FXCollections.observableArrayList();

    private API api;
    private ResourceBundle bundle;

    private void cargarTabla() {
        colConvenio.setCellValueFactory(new PropertyValueFactory<>("titulo"));  // título del convenio (heredado de ItemDTO)
        colHabilitado.setCellValueFactory(new PropertyValueFactory<>("habilitado"));
        colEntidad.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getEntidad().getNombre()));
        colEstudiante.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getEstudiante().getNombre()));
        colProyecto.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getProyecto().getTitulo()));
        colTutor.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getTutor().getNombre()));
        colPlanTrabajo.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getPlan() != null ? data.getValue().getPlan().getDescripcion() : ""));
    }

    private void actualizarIdioma() {
        habilitarButton.setText(bundle.getString("button.habilitarConvenio"));
        deshabilitarButton.setText(bundle.getString("button.deshabilitarConvenio"));
        verDetallesButton.setText(bundle.getString("button.verDetallesConvenio"));
        btnVolver.setText(bundle.getString("button.volver"));
    }

    public void setPersistenceAPI(API persistenceAPI) {
        this.api = persistenceAPI;
        cargarTabla();
        cargarConvenios();
//        actualizarIdioma();
    }

    private void cargarConvenios() {
        listaConvenios.clear();
        try {
            listaConvenios.addAll(api.obtenerConvenios());
        } catch (ReadException e) {
            mostrarAlerta("Error al Cargar convenios",e.getMessage());
        }
        conveniosTableView.setItems(listaConvenios);
    }

    @FXML
    private void habilitarConvenio() {
        ConvenioPPSDTO selected = conveniosTableView.getSelectionModel().getSelectedItem();
        if (selected != null && !selected.isHabilitado()) {
            boolean exito = false;
            try {
                exito = api.habilitarConvenio(selected.getID(), true);
            } catch (CreateException e) {
                mostrarAlerta("Error al habilitar: ",e.getMessage());
            }
            if (exito) {
                selected.setHabilitado(true);
                conveniosTableView.refresh();
            } else {
                mostrarAlerta("Error", "No se pudo habilitar el convenio.");
            }
        }
    }

    @FXML
    private void deshabilitarConvenio() {
        ConvenioPPSDTO selected = conveniosTableView.getSelectionModel().getSelectedItem();
        if (selected != null && selected.isHabilitado()) {
            boolean exito = false;
            try {
                exito = api.habilitarConvenio(selected.getID(), false);
            } catch (CreateException e) {
                mostrarAlerta("Error al deshabilitar", e.getMessage());
            }
            if (exito) {
                selected.setHabilitado(false);
                conveniosTableView.refresh();
            } else {
                mostrarAlerta("Error", "No se pudo deshabilitar el convenio.");
            }
        }
    }

    @FXML
    private void verDetallesConvenio() {
        ConvenioPPSDTO selected = conveniosTableView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            mostrarDetallesConvenio(selected);
        } else {
            mostrarAlerta("Sin selección", "Por favor, selecciona un convenio de la lista.");
        }
    }

    private void mostrarDetallesConvenio(ConvenioPPSDTO convenio) {
        StringBuilder detalles = new StringBuilder();

        detalles.append("CONVENIO\n");
        detalles.append("--------------------------------------------------\n");
        detalles.append(String.format("%-34s: %s\n", "Título", convenio.getTitulo()));
        detalles.append(String.format("%-34s: %s\n", "Habilitado", convenio.isHabilitado() ? "Sí" : "No"));
        detalles.append(String.format("%-34s: %s\n", "Descripción", convenio.getDescripcion()));

        detalles.append("\nENTIDAD\n");
        detalles.append("--------------------------------------------------\n");
        detalles.append(String.format("%-34s: %s\n", "Nombre", convenio.getEntidad().getNombre()));
        detalles.append(String.format("%-34s: %s\n", "Dirección", convenio.getEntidad().getDireccionEntidad()));
        detalles.append(String.format("%-34s: %s\n", "CUIT", convenio.getEntidad().getCuit()));
        detalles.append(String.format("%-34s: %s\n", "Email", convenio.getEntidad().getEmail()));

        detalles.append("\nESTUDIANTE\n");
        detalles.append("--------------------------------------------------\n");
        detalles.append(String.format("%-34s: %s\n", "Nombre", convenio.getEstudiante().getNombre()));
        detalles.append(String.format("%-34s: %s\n", "Carrera", convenio.getEstudiante().getCarrera()));
        detalles.append(String.format("%-34s: %s\n", "Matrícula", convenio.getEstudiante().getMatricula()));
        detalles.append(String.format("%-34s: %s\n", "Email", convenio.getEstudiante().getEmail()));

        detalles.append("\nPROYECTO\n");
        detalles.append("--------------------------------------------------\n");
        detalles.append(String.format("%-34s: %s\n", "Título", convenio.getProyecto().getTitulo()));
        detalles.append(String.format("%-34s: %s\n", "Descripción", convenio.getProyecto().getDescripcion()));
        detalles.append(String.format("%-34s: %s\n", "Objetivos", convenio.getProyecto().getObjetivos()));
        detalles.append(String.format("%-34s: %s\n", "Requisitos", convenio.getProyecto().getRequisitos()));
        detalles.append(String.format("%-34s: %s\n", "Ubicación", convenio.getProyecto().getUbicacion()));
        detalles.append(String.format("%-34s: %s\n", "Área de Interés", convenio.getProyecto().getAreaDeInteres()));
        detalles.append(String.format("%-34s: %s\n", "Tutor a Cargo", convenio.getProyecto().getTutorEncargado().getNombre()));

        detalles.append("\nPLAN DE TRABAJO\n");
        detalles.append("--------------------------------------------------\n");
        if (convenio.getPlan() != null) {
            detalles.append(String.format("%-34s: %s\n", "Título", convenio.getPlan().getTitulo()));
            detalles.append(String.format("%-34s: %s\n", "Descripción", convenio.getPlan().getDescripcion()));
            detalles.append(String.format("\n%-34s: %s\n", "Docente a Cargo", convenio.getPlan().getDocente().getNombre()));
            detalles.append(String.format("%-34s: %s\n", "Email Docente", convenio.getPlan().getDocente().getEmail()));

            detalles.append(String.format("\n%-34s: %s\n", "Tutor a Cargo", convenio.getTutor().getNombre()));
            detalles.append(String.format("%-34s: %s\n", "Empleado en", convenio.getTutor().getNombreEntidadColaborativa()));
            detalles.append(String.format("%-34s: %s\n", "Email Tutor", convenio.getTutor().getEmail()));

            String tieneActividades = (convenio.getPlan().getActividades() != null && !convenio.getPlan().getActividades().isEmpty()) ? "Sí" : "No";
            detalles.append(String.format("\n%-34s: %s\n", "Tiene actividades propuestas", tieneActividades));
        } else {
            detalles.append("No asignado\n");
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Detalles del Convenio");
        alert.setHeaderText("Información completa del Convenio");

        // Crear el TextArea con el texto de detalles
        TextArea textArea = new TextArea(detalles.toString());
        textArea.setEditable(false);
        textArea.setWrapText(true);
        textArea.setPrefSize(800, 600);

        // Crear un Dialog personalizado
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Detalles del Convenio");
        dialog.setHeaderText("Información completa del Convenio");

        // Añadir botones personalizados
        ButtonType exportButtonType = new ButtonType("Exportar", ButtonBar.ButtonData.LEFT);
        ButtonType cerrarButtonType = new ButtonType("Cerrar", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(exportButtonType, cerrarButtonType);

        dialog.getDialogPane().setContent(textArea);
        dialog.getDialogPane().setPrefSize(850, 650);

        // Obtener el botón Exportar y agregar acción personalizada
        Button exportButton = (Button) dialog.getDialogPane().lookupButton(exportButtonType);
        exportButton.setOnAction(e -> {
            exportarDetallesAArchivo(detalles.toString());
        });

        dialog.showAndWait();
    }
    private void exportarDetallesAArchivo(String contenido) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Guardar Detalles del Convenio");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Archivo PDF", "*.pdf"));
        fileChooser.setInitialFileName("detalles_convenio.pdf");

        Stage stage = (Stage) conveniosTableView.getScene().getWindow();
        File file = fileChooser.showSaveDialog(stage);

        if (file != null) {
            try {
                Document document = new Document();
                PdfWriter.getInstance(document, new FileOutputStream(file));
                document.open();
                document.add(new Paragraph(contenido));
                document.close();

                mostrarAlertaExito("Éxito", "Archivo exportado correctamente.");

                // Abrir el archivo automáticamente después de guardarlo
                if (Desktop.isDesktopSupported()) {
                    Desktop.getDesktop().open(file);
                }
            } catch (DocumentException | IOException ex) {
                mostrarAlerta("Error", "No se pudo guardar el archivo: " + ex.getMessage());
            }
        }
    }
    private void mostrarAlertaExito(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);

        if (mensaje == null || mensaje.trim().isEmpty()) {
            alert.setHeaderText("Operación realizada con éxito."); // Mensaje predeterminado
        } else {
            alert.setHeaderText(mensaje);
        }

        ButtonType okButton = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        alert.getButtonTypes().setAll(okButton);

        alert.showAndWait();
    }

//    private void exportarDetallesAArchivo(String contenido) { //.txt
//        FileChooser fileChooser = new FileChooser();
//        fileChooser.setTitle("Guardar Detalles del Convenio");
//        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Archivo de texto", "*.txt"));
//        fileChooser.setInitialFileName("detalles_convenio.txt");
//
//        Stage stage = (Stage) conveniosTableView.getScene().getWindow();
//        File file = fileChooser.showSaveDialog(stage);
//
//        if (file != null) {
//            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
//                writer.write(contenido);
//                mostrarAlerta("Éxito", "Archivo exportado correctamente.");
//            } catch (IOException ex) {
//                mostrarAlerta("Error", "No se pudo guardar el archivo: " + ex.getMessage());
//            }
//        }
//    }

    private void mostrarAlerta(String titulo, String contenido) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(contenido);
        alerta.showAndWait();
    }

    @FXML
    public void volverHome(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Frontend/vistas/DirectorCarrera/homeDirectorCarrera.fxml"));
            Parent root = loader.load();
            homeDirectorCarreraController controller = loader.getController();

            if (this.api != null) {
                controller.setPersistenceAPI(this.api);
            } else {
                LOGGER.log(Level.WARNING, "API no inicializada al volver a Home.");
            }

            Stage stage = (Stage) btnVolver.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Home - GPPS");

        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error al cargar home.fxml", e);
            mostrarAlerta("Error de Navegación", "No se pudo volver a la pantalla principal.");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error al configurar la pantalla Home", e);
            mostrarAlerta("Error de Configuración", "No se pudo configurar la pantalla principal: " + e.getMessage());
        }
    }

    @FXML
    public void asignarDocente(ActionEvent actionEvent) {
        ConvenioPPSDTO selected = conveniosTableView.getSelectionModel().getSelectedItem();
        selected.getID();
        if (selected != null) {
            insertarDocente(selected);
        } else {
            mostrarAlerta("Sin selección", "Por favor, selecciona un convenio de la lista.");
        }
    }

    private void insertarDocente(ConvenioPPSDTO convenio) {
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setTitle("Asignar Docente y Plan");

        // Label informativo
        Label infoLabel = new Label("Para asignar un docente se debe inicializar un Plan de Trabajo");


        // Crear la lista de selección
        ListView<DocenteDTO> listView = new ListView<>();
        try {
            listView.getItems().addAll(api.obtenerDocentes());
        } catch (ReadException e) {
            mostrarAlerta("Error al buscar los docentes", e.getMessage());
        }
        listView.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(DocenteDTO docente, boolean empty) {
                super.updateItem(docente, empty);
                if (empty || docente == null) {
                    setText(null);
                } else {
                    setText(docente.getNombre());
                }
            }
        });

        // Campos para Título y Descripción
        TextField txtTitulo = new TextField();
        txtTitulo.setPromptText("Título del Plan");

        TextArea txtDescripcion = new TextArea();
        txtDescripcion.setPromptText("Descripción del Plan");
        txtDescripcion.setWrapText(true);
        txtDescripcion.setPrefRowCount(4);

        // Mostrar estado actual del plan
        PlanDeTrabajoDTO plan = convenio.getPlan();
        String actividadesStr = (plan.getActividades() == null || plan.getActividades().isEmpty()) ? "Sin definir" : "Actividades existentes para el plan: " + plan.getTitulo();
        String informeStr = (plan.getInformeFinal() == null) ? "Sin definir" : plan.getInformeFinal().getTitulo();

        Label lblPlanActual = new Label("Datos actuales del Plan");
        Label lblActividades = new Label("Actividades: " + actividadesStr);
        Label lblTutor = new Label("Tutor Externo: " + (plan.getTutor() != null ? plan.getTutor().getNombre() : "Sin definir"));

        // Botón para asignar
        Button btnAsignar = new Button("Asignar Docente y Plan");
        btnAsignar.setOnAction(e -> {
            DocenteDTO seleccionado = listView.getSelectionModel().getSelectedItem();
            if (seleccionado != null && !txtTitulo.getText().isEmpty() && !txtDescripcion.getText().isEmpty()) {
                plan.setId(-9);
                plan.setTitulo(txtTitulo.getText());
                plan.setDescripcion(txtDescripcion.getText());
                plan.setDocente(seleccionado);
                plan.setHabilitado(true);
                convenio.setPlan(plan);

                try {
                    api.actualizarConvenio(convenio);
                    cargarConvenios();
                } catch (CreateException ex) {
                    mostrarAlerta("Error al Actualizar: ",ex.getMessage());
                }
                mostrarAlerta("Éxito", "Docente y Plan asignados correctamente.");
                dialogStage.close();
            } else {
                mostrarAlerta("Campos incompletos", "Debes seleccionar un docente y completar el título y descripción.");
            }
        });

        VBox vbox = new VBox(10,
                infoLabel,
                new Label("Selecciona un docente:"), listView,
                new Label("Título del Plan:"), txtTitulo,
                new Label("Descripción del Plan:"), txtDescripcion,
                lblPlanActual, lblActividades, lblTutor,
                btnAsignar
        );

        vbox.setPadding(new Insets(10));
        dialogStage.setScene(new Scene(vbox));
        dialogStage.showAndWait();
    }

    @FXML
    public void asignarTituloyDescripción(ActionEvent actionEvent) {
        ConvenioPPSDTO selected = conveniosTableView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            insertarTituloYDescripcion(selected);
        } else {
            mostrarAlerta("Sin selección", "Por favor, selecciona un convenio de la lista.");
        }
    }
    private void insertarTituloYDescripcion(ConvenioPPSDTO convenio) {
        Stage ventana = new Stage();
        ventana.initModality(Modality.APPLICATION_MODAL);
        ventana.setTitle("Editar Título y Descripción del convenio");

        if (convenio == null) {
            mostrarAlerta("Error", "Este convenio no existe.");
            return;
        }

        // Campos de texto con datos actuales
        TextField txtTitulo = new TextField(convenio.getTitulo() != null ? convenio.getTitulo() : "");
        txtTitulo.setPromptText("Título del Convenio");

        TextArea txtDescripcion = new TextArea(convenio.getDescripcion() != null ? convenio.getDescripcion() : "");
        txtDescripcion.setPromptText("Descripción del Convenio");
        txtDescripcion.setPrefRowCount(4);

        Button btnGuardar = new Button("Guardar");
        btnGuardar.setOnAction(e -> {
            String titulo = txtTitulo.getText().trim();
            String descripcion = txtDescripcion.getText().trim();

            if (titulo.isEmpty() || descripcion.isEmpty()) {
                mostrarAlerta("Campos obligatorios", "Debes completar el título y la descripción.");
                return;
            }

            // Actualizar los datos del plan
            convenio.setTitulo(titulo);
            convenio.setDescripcion(descripcion);

            try {
                api.actualizarConvenio(convenio);
                cargarConvenios();
            } catch (Exception ex) {
                mostrarAlerta("Error",  ex.getMessage());
                return;
            }

            ventana.close();
        });

        VBox layout = new VBox(10,
                new Label("Título del Convenio:"), txtTitulo,
                new Label("Descripción del Convenio:"), txtDescripcion,
                btnGuardar
        );
        layout.setPadding(new Insets(15));

        Scene scene = new Scene(layout, 400, 300);
        ventana.setScene(scene);
        ventana.showAndWait();
    }

}
