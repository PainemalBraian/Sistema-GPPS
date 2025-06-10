package Frontend.com.gui.Controller.Administrador;

import Backend.API.API;
import Backend.DTO.*;
import Backend.Entidades.TutorExterno;
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
import javafx.scene.paint.Color;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.geometry.Insets;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
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


public class GestionDeConveniosController {

    private static final Logger LOGGER = Logger.getLogger(Frontend.com.gui.Controller.Administrador.GestionDeConveniosController.class.getName());

    @FXML private TableView<ConvenioPPSDTO> conveniosTableView;
    @FXML private TableColumn<ConvenioPPSDTO, String> colConvenio;
    @FXML private TableColumn<ConvenioPPSDTO, String> colHabilitado;
    @FXML private TableColumn<ConvenioPPSDTO, String> colEntidad;
    @FXML private TableColumn<ConvenioPPSDTO, String> colEstudiante;
    @FXML private TableColumn<ConvenioPPSDTO, String> colProyecto;
    @FXML private TableColumn<ConvenioPPSDTO, String> colTutor;
    @FXML private TableColumn<ConvenioPPSDTO, String> colPlanTrabajo;
    @FXML private Button habilitarButton;
    @FXML private Button deshabilitarButton;
    @FXML private Button verDetallesButton;
    @FXML private Button btnVolver;


    private final ObservableList<ConvenioPPSDTO> listaConvenios = FXCollections.observableArrayList();

    private API api;
    private ResourceBundle bundle;

    private void cargarTabla() {
        colConvenio.setCellValueFactory(new PropertyValueFactory<>("titulo"));  // título del convenio (heredado de ItemDTO)
        colHabilitado.setCellValueFactory(data -> {
            boolean habilitado = data.getValue().isHabilitado();
            return new SimpleStringProperty(habilitado ? "Habilitado" : "Inhabilitado");
        });

        colHabilitado.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item);
                    if ("Habilitado".equals(item)) {
                        setTextFill(Color.GREEN);
                        setStyle("-fx-font-weight: bold;");
                    } else {
                        setTextFill(Color.RED);
                        setStyle("-fx-font-weight: bold;");
                    }
                }
            }
        });


        colEntidad.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getEntidad().getNombre()));
        colEstudiante.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getEstudiante().getNombre()));
        colProyecto.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getProyecto().getTitulo()));
        colTutor.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getTutor().getNombre()));
        colPlanTrabajo.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getPlan() != null ? data.getValue().getPlan().getTitulo() : ""));
    }

    public void setPersistenceAPI(API persistenceAPI) {
        this.api = persistenceAPI;
        cargarTabla();
        cargarConvenios();
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Frontend/vistas/Administrador/homeAdministrador.fxml"));
            Parent root = loader.load();
            homeAdministradorController controller = loader.getController();

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
    public void asignarPlan(ActionEvent actionEvent) {
        ConvenioPPSDTO selected = conveniosTableView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            mostrarAlerta("Sin selección", "Por favor, selecciona un convenio de la lista.");
            return;
        }

        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setTitle("Asignar Plan de Trabajo");

// VBox con todo el contenido
        VBox contenido = new VBox(10);
        contenido.setPadding(new Insets(10));

// Campos de texto
        TextField txtTitulo = new TextField();
        txtTitulo.setPromptText("Título del Plan");

        TextArea txtDescripcion = new TextArea();
        txtDescripcion.setPromptText("Descripción del Plan");
        txtDescripcion.setWrapText(true);
        txtDescripcion.setPrefRowCount(4);

// Verificar si se puede editar o no
        boolean esEditable = selected.getPlan() == null ||
                selected.getPlan().getTitulo() == null ||
                selected.getPlan().getTitulo().equalsIgnoreCase("A Definir");

        txtTitulo.setDisable(!esEditable);
        txtDescripcion.setDisable(!esEditable);

// Si el plan ya existe y no está "A Definir", mostrar los datos actuales
        if (selected.getPlan() != null) {
            txtTitulo.setText(selected.getPlan().getTitulo());
            txtDescripcion.setText(selected.getPlan().getDescripcion());
        }


// Lista de docentes
        ListView<DocenteDTO> docenteList = new ListView<>();
        try {
            docenteList.getItems().addAll(api.obtenerDocentes());
        } catch (ReadException e) {
            mostrarAlerta("Error", "No se pudieron cargar los docentes: " + e.getMessage());
        }
        docenteList.setPrefHeight(100); // Altura limitada
        docenteList.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(DocenteDTO item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getNombre());
            }
        });

// Lista de tutores
        ListView<TutorExternoDTO> tutorList = new ListView<>();
        try {
            tutorList.getItems().addAll(api.obtenerTutores());
        } catch (ReadException e) {
            mostrarAlerta("Error", "No se pudieron cargar los tutores: " + e.getMessage());
        }
        tutorList.setPrefHeight(100); // Altura limitada
        tutorList.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(TutorExternoDTO item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getNombre());
            }
        });

// Botón para asignar
        Button btnAsignar = new Button("Asignar Plan Completo");
        btnAsignar.setOnAction(e -> {
            String titulo = txtTitulo.getText();
            String descripcion = txtDescripcion.getText();
            DocenteDTO docente = docenteList.getSelectionModel().getSelectedItem();
            TutorExternoDTO tutor = tutorList.getSelectionModel().getSelectedItem();

            if (titulo.isEmpty() || descripcion.isEmpty() || docente == null || tutor == null) {
                mostrarAlerta("Campos incompletos", "Debes completar todos los campos y seleccionar docente y tutor.");
                return;
            }

            try {
                PlanDeTrabajoDTO plan = selected.getPlan();
                plan.setTitulo(titulo);
                plan.setDescripcion(descripcion);
                plan.setTutor(tutor);
                plan.setDocente(docente);
                api.actualizarPlanDeTrabajo(plan);

                selected.setPlan(api.obtenerPlanByTitulo(titulo));
                selected.setEntidad(api.obtenerEntidadColaborativaByNombreEntidad(tutor.getNombreEntidadColaborativa()));
                selected.getProyecto().setTutorEncargado(tutor);
                api.actualizarProyecto(selected.getProyecto());
                api.actualizarConvenio(selected);
                cargarConvenios();

                mostrarAlerta("Éxito", "Plan asignado correctamente.");
                dialogStage.close();
            } catch (CreateException | ReadException ex) {
                mostrarAlerta("Error", "No se pudo asignar el plan: " + ex.getMessage());
            }
        });

// Agregar todo al VBox
        contenido.getChildren().addAll(
                new Label("Título del Plan:"), txtTitulo,
                new Label("Descripción del Plan:"), txtDescripcion,
                new Label("Selecciona un Docente:"), docenteList,
                new Label("Selecciona un Tutor Externo:"), tutorList,
                btnAsignar
        );

// ScrollPane
        ScrollPane scroll = new ScrollPane(contenido);
        scroll.setFitToWidth(true);
        scroll.setPrefViewportHeight(400); // Tamaño visible del scroll

// Escena y ventana
        Scene scene = new Scene(scroll, 400, 450); // Tamaño ajustado
        dialogStage.setScene(scene);
        dialogStage.showAndWait();

    }



    @FXML
    public void asignarEstudiante(ActionEvent actionEvent) {
        ConvenioPPSDTO selected = conveniosTableView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            insertarEstudiante(selected);
        } else {
            mostrarAlerta("Sin selección", "Por favor, selecciona un convenio de la lista.");
        }
    }
    private void insertarEstudiante(ConvenioPPSDTO convenio) {
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setTitle("Asignar Estudiante");

        Label infoLabel = new Label("Selecciona un estudiante para este convenio.");

        ListView<EstudianteDTO> listView = new ListView<>();
        try {
            listView.getItems().addAll(api.obtenerEstudiantes());
        } catch (ReadException e) {
            mostrarAlerta("Error al cargar estudiantes", e.getMessage());
        }

        listView.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(EstudianteDTO estudiante, boolean empty) {
                super.updateItem(estudiante, empty);
                setText(empty || estudiante == null ? null : estudiante.getNombre());
            }
        });

        Button btnAsignar = new Button("Asignar Estudiante");
        btnAsignar.setOnAction(e -> {
            EstudianteDTO seleccionado = listView.getSelectionModel().getSelectedItem();
            if (seleccionado != null) {
                convenio.setEstudiante(seleccionado);
                try {
                    api.actualizarConvenio(convenio);
                    cargarConvenios();
                } catch (CreateException ex) {
                    mostrarAlerta("Error al actualizar", ex.getMessage());
                }

                mostrarAlerta("Éxito", "Estudiante asignado correctamente.");
                dialogStage.close();
            } else {
                mostrarAlerta("Selección incompleta", "Debes seleccionar un estudiante.");
            }
        });

        VBox vbox = new VBox(10, infoLabel, listView, btnAsignar);
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

    @FXML
    public void iniciarNuevoConvenio(ActionEvent actionEvent) {
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setTitle("Nuevo Convenio PPS");

        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(15));

        // Campos convenio
        TextField txtTitulo = new TextField();
        txtTitulo.setPromptText("Título del Convenio");

        TextArea txtDescripcion = new TextArea();
        txtDescripcion.setPromptText("Descripción del Convenio");
        txtDescripcion.setWrapText(true);
        txtDescripcion.setPrefRowCount(3);

        // Campos plan
        TextField txtTituloPlan = new TextField();
        txtTituloPlan.setPromptText("Título del Plan de Trabajo");

        TextArea txtDescripcionPlan = new TextArea();
        txtDescripcionPlan.setPromptText("Descripción del Plan de Trabajo");
        txtDescripcionPlan.setWrapText(true);
        txtDescripcionPlan.setPrefRowCount(3);

//        TextField txtTutor = new TextField();
//        txtTutor.setPromptText("Tutor del Plan (se autocompleta con el tutor del proyecto)");

        ComboBox<DocenteDTO> comboDocente = new ComboBox<>();
        ComboBox<TutorExternoDTO> comboTutor = new ComboBox<>();

        // Combos de selección
        ComboBox<ProyectoDTO> comboProyecto = new ComboBox<>();
        ComboBox<EstudianteDTO> comboEstudiante = new ComboBox<>();
        ComboBox<EntidadColaborativaDTO> comboEntidad = new ComboBox<>();

        try {
            comboProyecto.getItems().addAll(api.obtenerProyectosHabilitados());
            comboEstudiante.getItems().addAll(api.obtenerEstudiantes());
            comboEntidad.getItems().addAll(api.obtenerEntidadesColaborativas());
            comboDocente.getItems().addAll(api.obtenerDocentes());
            comboTutor.getItems().addAll(api.obtenerTutores());
        } catch (ReadException e) {
            mostrarAlerta("Error", "No se pudo cargar información: " + e.getMessage());
            return;
        }

        // Auto-rellenar tutor según proyecto
//        comboProyecto.setOnAction(e -> {
//            ProyectoDTO proyecto = comboProyecto.getValue();
//            if (proyecto != null && proyecto.getTutorEncargado() != null) {
//                txtTutor.setText(proyecto.getTutorEncargado().getNombre());
//            } else {
//                txtTutor.clear();
//            }
//        });

        // Configuraciones visuales de combos
        comboProyecto.setCellFactory(l -> new ListCell<>() {
            @Override
            protected void updateItem(ProyectoDTO item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getTitulo());
            }
        });
        comboProyecto.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(ProyectoDTO item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getTitulo());
            }
        });

        comboEstudiante.setCellFactory(l -> new ListCell<>() {
            @Override
            protected void updateItem(EstudianteDTO item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getNombre());
            }
        });
        comboEstudiante.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(EstudianteDTO item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getNombre());
            }
        });

        comboEntidad.setCellFactory(l -> new ListCell<>() {
            @Override
            protected void updateItem(EntidadColaborativaDTO item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getNombre());
            }
        });
        comboEntidad.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(EntidadColaborativaDTO item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getNombre());
            }
        });

        comboDocente.setCellFactory(l -> new ListCell<>() {
            @Override
            protected void updateItem(DocenteDTO item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getNombre());
            }
        });
        comboDocente.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(DocenteDTO item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getNombre());
            }
        });

        comboTutor.setCellFactory(l -> new ListCell<>() {
            @Override
            protected void updateItem(TutorExternoDTO item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getNombre());
            }
        });
        comboTutor.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(TutorExternoDTO item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getNombre());
            }
        });
        comboProyecto.setOnAction(e -> {
            ProyectoDTO proyecto = comboProyecto.getValue();
            if (proyecto != null && proyecto.getTutorEncargado() != null) {
                TutorExternoDTO tutor = proyecto.getTutorEncargado();
                for (TutorExternoDTO t : comboTutor.getItems()) {
                    if (t.getIdUsuario() == tutor.getIdUsuario()) {
                        comboTutor.setValue(t);
                        for (EntidadColaborativaDTO ec:comboEntidad.getItems()){
                            if(ec.getNombreEntidad().equals(t.getNombreEntidadColaborativa()) ){
                                comboEntidad.setValue(ec);
                                break;
                            }
                        }
                        break;
                    }
                }
            } else {
                comboTutor.setValue(null);
            }
        });


        // Botón guardar
        Button btnGuardar = new Button("Crear Convenio");
        btnGuardar.setOnAction(e -> {
            // Convenio
            String titulo = txtTitulo.getText();
            String descripcion = txtDescripcion.getText();
            ProyectoDTO proyecto = comboProyecto.getValue();
            EstudianteDTO estudiante = comboEstudiante.getValue();
            EntidadColaborativaDTO entidad = comboEntidad.getValue();

            // Plan
            String tituloPlan = txtTituloPlan.getText();
            String descripcionPlan = txtDescripcionPlan.getText();
            TutorExternoDTO tutor = comboTutor.getValue();
            DocenteDTO docente = comboDocente.getValue();

            if (titulo.isEmpty() || descripcion.isEmpty() || tituloPlan.isEmpty()
                    || descripcionPlan.isEmpty() || tutor == null
                    || proyecto == null || estudiante == null || entidad == null || docente == null) {
                mostrarAlerta("Campos incompletos", "Debes completar todos los campos.");
                return;
            }

            try {
                api.cargarConvenio(titulo, descripcion, proyecto, estudiante, entidad, tituloPlan, descripcionPlan, tutor, docente);
                mostrarAlerta("Éxito", "Convenio y Plan creados correctamente.");
                cargarConvenios(); // refrescar tabla
                dialogStage.close();
            } catch (CreateException ex) {
                mostrarAlerta("Error", "No se pudo crear el convenio: " + ex.getMessage());
            }
        });

        // Armado del formulario
        vbox.getChildren().addAll(
                new Label("Título del Convenio:"), txtTitulo,
                new Label("Descripción del Convenio:"), txtDescripcion,
                new Label("Selecciona un Proyecto:"), comboProyecto,
                new Label("Selecciona un Estudiante:"), comboEstudiante,
                new Label("Selecciona una Entidad Colaboradora:"), comboEntidad,
                new Separator(),
                new Label("Título del Plan de Trabajo:"), txtTituloPlan,
                new Label("Descripción del Plan:"), txtDescripcionPlan,
                new Label("Tutor del Plan:"), comboTutor,
                new Label("Docente Responsable del Plan:"), comboDocente,
                btnGuardar
        );

        // Scroll en caso de que sea largo
        ScrollPane scrollPane = new ScrollPane(vbox);
        scrollPane.setFitToWidth(true);
        Scene scene = new Scene(scrollPane, 450, 600);
        dialogStage.setScene(scene);
        dialogStage.showAndWait();
    }


}
