package Frontend.com.gui.Controller;

import Backend.API.API;
import Backend.DTO.UsuarioDTO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import java.util.Optional;
import java.util.ResourceBundle;

public class TareasController  {

    API api;
    // --- Componentes FXML ---
    @FXML
    private DatePicker datePickerFechaTarea;
    @FXML
    private TextArea textAreaDescripcionTarea;
    @FXML
    private TextField textFieldDuracionTarea;
    @FXML
    private Button btnAgregarTarea;
    @FXML
    private Button btnActualizarTarea;

    @FXML
    private TableView<Tarea> tableViewTareas;
    @FXML
    private TableColumn<Tarea, String> columnaFecha;
    @FXML
    private TableColumn<Tarea, String> columnaDescripcion;
    @FXML
    private TableColumn<Tarea, Double> columnaDuracion;

    @FXML
    private Button btnEditarTarea;
    @FXML
    private Button btnEliminarTarea;

    @FXML
    private ListView<String> listViewCronograma;

    @FXML
    private Label labelPorcentajeAvance;
    @FXML
    private ProgressBar progressBarAvance;
    @FXML
    private Label labelHorasCompletadas;


    // --- Listas y Datos ---
    private ObservableList<Tarea> listaTareasObservables;
    private ObservableList<String> listaCronogramaObservables;

    // Simulación: Total de horas esperadas para la práctica
    private final double HORAS_TOTALES_ESPERADAS = 160.0; // Ejemplo, ajústalo según necesidad

    private Tarea tareaSeleccionadaParaEdicion = null;


    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Configurar el DatePicker
        configurarDatePicker();

        // Inicializar listas
        listaTareasObservables = FXCollections.observableArrayList();
        listaCronogramaObservables = FXCollections.observableArrayList();

        // Configurar TableView
        columnaFecha.setCellValueFactory(new PropertyValueFactory<>("fechaFormateada"));
        columnaDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        columnaDuracion.setCellValueFactory(new PropertyValueFactory<>("duracion"));
        tableViewTareas.setItems(listaTareasObservables);

        // Configurar ListView para el cronograma
        listViewCronograma.setItems(listaCronogramaObservables);

        // Cargar datos de ejemplo o desde la base de datos
        cargarCronogramaEjemplo(); // Deberías reemplazar esto con la carga real
        // cargarTareasGuardadas(); // Si hubiera persistencia

        // Listener para selección en la tabla (para habilitar botones de editar/eliminar)
        tableViewTareas.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            boolean haySeleccion = newSelection != null;
            btnEditarTarea.setDisable(!haySeleccion);
            btnEliminarTarea.setDisable(!haySeleccion);
            if (haySeleccion) {
                tareaSeleccionadaParaEdicion = newSelection;
            } else {
                tareaSeleccionadaParaEdicion = null;
                limpiarFormulario(); // Limpia el formulario si se deselecciona
                btnActualizarTarea.setVisible(false);
                btnAgregarTarea.setVisible(true);
            }
        });

        // Deshabilitar botones de acción inicialmente
        btnEditarTarea.setDisable(true);
        btnEliminarTarea.setDisable(true);
        btnActualizarTarea.setVisible(false); // El botón de actualizar solo aparece al editar

        // Actualizar UI de avance inicial
        actualizarAvance();
    }

    private void configurarDatePicker() {
        // Formateador para mostrar y parsear fechas
        StringConverter<LocalDate> converter = new StringConverter<>() {
            final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

            @Override
            public String toString(LocalDate date) {
                return (date != null) ? dateFormatter.format(date) : "";
            }

            @Override
            public LocalDate fromString(String string) {
                return (string != null && !string.isEmpty()) ? LocalDate.parse(string, dateFormatter) : null;
            }
        };
        datePickerFechaTarea.setConverter(converter);
        datePickerFechaTarea.setPromptText("dd/mm/aaaa");
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


    @FXML
    void handleAgregarTarea(ActionEvent event) {
        LocalDate fecha = datePickerFechaTarea.getValue();
        String descripcion = textAreaDescripcionTarea.getText().trim();
        String duracionStr = textFieldDuracionTarea.getText().trim();

        if (fecha == null) {
            mostrarAlerta("Error", "Debe seleccionar una fecha para la tarea.", Alert.AlertType.ERROR);
            return;
        }
        if (descripcion.isEmpty()) {
            mostrarAlerta("Error", "La descripción de la tarea no puede estar vacía.", Alert.AlertType.ERROR);
            return;
        }
        if (duracionStr.isEmpty()) {
            mostrarAlerta("Error", "Debe ingresar la duración de la tarea.", Alert.AlertType.ERROR);
            return;
        }

        double duracion;
        try {
            duracion = Double.parseDouble(duracionStr);
            if (duracion <= 0) {
                mostrarAlerta("Error", "La duración debe ser un número positivo.", Alert.AlertType.ERROR);
                return;
            }
        } catch (NumberFormatException e) {
            mostrarAlerta("Error", "La duración debe ser un número válido (ej: 2.5).", Alert.AlertType.ERROR);
            return;
        }

        Tarea nuevaTarea = new Tarea(fecha, descripcion, duracion);
        listaTareasObservables.add(nuevaTarea);
        // Aquí iría la lógica para guardar en base de datos

        limpiarFormulario();
        actualizarAvance();
        mostrarAlerta("Éxito", "Tarea agregada correctamente.", Alert.AlertType.INFORMATION);
    }

    @FXML
    void handleEditarTarea(ActionEvent event) {
        Tarea tareaSeleccionada = tableViewTareas.getSelectionModel().getSelectedItem();
        if (tareaSeleccionada != null) {
            datePickerFechaTarea.setValue(tareaSeleccionada.getFecha());
            textAreaDescripcionTarea.setText(tareaSeleccionada.getDescripcion());
            textFieldDuracionTarea.setText(String.valueOf(tareaSeleccionada.getDuracion()));

            tareaSeleccionadaParaEdicion = tareaSeleccionada; // Guardar referencia para actualizar
            btnAgregarTarea.setVisible(false);
            btnActualizarTarea.setVisible(true);
            btnActualizarTarea.setDisable(false);
        } else {
            mostrarAlerta("Advertencia", "Seleccione una tarea de la lista para editar.", Alert.AlertType.WARNING);
        }
    }


    @FXML
    void handleActualizarTarea(ActionEvent event) {
        if (tareaSeleccionadaParaEdicion == null) {
            mostrarAlerta("Error", "No hay tarea seleccionada para actualizar.", Alert.AlertType.ERROR);
            return;
        }

        LocalDate fecha = datePickerFechaTarea.getValue();
        String descripcion = textAreaDescripcionTarea.getText().trim();
        String duracionStr = textFieldDuracionTarea.getText().trim();

        if (fecha == null || descripcion.isEmpty() || duracionStr.isEmpty()) {
            mostrarAlerta("Error", "Todos los campos son obligatorios.", Alert.AlertType.ERROR);
            return;
        }

        double duracion;
        try {
            duracion = Double.parseDouble(duracionStr);
            if (duracion <= 0) {
                mostrarAlerta("Error", "La duración debe ser un número positivo.", Alert.AlertType.ERROR);
                return;
            }
        } catch (NumberFormatException e) {
            mostrarAlerta("Error", "La duración debe ser un número válido.", Alert.AlertType.ERROR);
            return;
        }

        // Actualizar el objeto Tarea
        tareaSeleccionadaParaEdicion.setFecha(fecha);
        tareaSeleccionadaParaEdicion.setDescripcion(descripcion);
        tareaSeleccionadaParaEdicion.setDuracion(duracion);

        // Refrescar la tabla (JavaFX TableView puede necesitar esto si el objeto no es property-observable internamente)
        tableViewTareas.refresh();
        // Aquí iría la lógica para actualizar en base de datos

        limpiarFormulario();
        btnAgregarTarea.setVisible(true);
        btnActualizarTarea.setVisible(false);
        btnActualizarTarea.setDisable(true);
        tableViewTareas.getSelectionModel().clearSelection(); // Deseleccionar
        tareaSeleccionadaParaEdicion = null;

        actualizarAvance();
        mostrarAlerta("Éxito", "Tarea actualizada correctamente.", Alert.AlertType.INFORMATION);
    }


    @FXML
    void handleEliminarTarea(ActionEvent event) {
        Tarea tareaSeleccionada = tableViewTareas.getSelectionModel().getSelectedItem();
        if (tareaSeleccionada != null) {
            Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
            confirmacion.setTitle("Confirmar Eliminación");
            confirmacion.setHeaderText("¿Está seguro de que desea eliminar la tarea seleccionada?");
            confirmacion.setContentText(tareaSeleccionada.getDescripcion());

            Optional<ButtonType> resultado = confirmacion.showAndWait();
            if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
                listaTareasObservables.remove(tareaSeleccionada);
                // Aquí iría la lógica para eliminar de la base de datos
                actualizarAvance();
                limpiarFormulario(); // Por si estaba en modo edición
                btnAgregarTarea.setVisible(true);
                btnActualizarTarea.setVisible(false);
                mostrarAlerta("Información", "Tarea eliminada.", Alert.AlertType.INFORMATION);
            }
        } else {
            mostrarAlerta("Advertencia", "Seleccione una tarea de la lista para eliminar.", Alert.AlertType.WARNING);
        }
    }

    private void limpiarFormulario() {
        datePickerFechaTarea.setValue(null);
        textAreaDescripcionTarea.clear();
        textFieldDuracionTarea.clear();
        tableViewTareas.getSelectionModel().clearSelection();
        btnAgregarTarea.setVisible(true);
        btnActualizarTarea.setVisible(false);
        tareaSeleccionadaParaEdicion = null;
    }

    private void actualizarAvance() {
        double horasCompletadas = 0;
        for (Tarea tarea : listaTareasObservables) {
            horasCompletadas += tarea.getDuracion();
        }

        double porcentaje = 0;
        if (HORAS_TOTALES_ESPERADAS > 0) {
            porcentaje = (horasCompletadas / HORAS_TOTALES_ESPERADAS);
        }
        // Asegurarse que el porcentaje no exceda el 100% si se registran más horas de las esperadas
        porcentaje = Math.min(porcentaje, 1.0);


        labelPorcentajeAvance.setText(String.format("%.1f%%", porcentaje * 100));
        progressBarAvance.setProgress(porcentaje);
        labelHorasCompletadas.setText(String.format("Horas completadas: %.1f / %.1f", horasCompletadas, HORAS_TOTALES_ESPERADAS));
    }

    private void cargarCronogramaEjemplo() {
        // Esto debería venir de una base de datos o configuración del sistema
        listaCronogramaObservables.add("Semana 1-2: Inducción y reconocimiento del entorno.");
        listaCronogramaObservables.add("Semana 3-5: Desarrollo de módulo A.");
        listaCronogramaObservables.add("Semana 6: Revisión y pruebas módulo A.");
        listaCronogramaObservables.add("Semana 7-9: Desarrollo de módulo B.");
        listaCronogramaObservables.add("Semana 10: Pruebas integrales.");
        listaCronogramaObservables.add("Semana 11-12: Documentación y entrega final.");
    }

    // Deberías tener un método para cargar tareas desde la BD al iniciar
    /*
    private void cargarTareasGuardadas() {
        // Lógica para obtener tareas de la base de datos y agregarlas a listaTareasObservables
        // Ejemplo:
        // List<Tarea> tareasDesdeDB = servicioTareas.obtenerTareasDelEstudianteActual();
        // listaTareasObservables.addAll(tareasDesdeDB);
        // actualizarAvance();
    }
    */

    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    // --- Clase Interna para el modelo de Tarea ---
    public static class Tarea {
        private LocalDate fecha;
        private String descripcion;
        private double duracion;
        private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");


        public Tarea(LocalDate fecha, String descripcion, double duracion) {
            this.fecha = fecha;
            this.descripcion = descripcion;
            this.duracion = duracion;
        }

        public LocalDate getFecha() {
            return fecha;
        }

        public void setFecha(LocalDate fecha) {
            this.fecha = fecha;
        }

        public String getDescripcion() {
            return descripcion;
        }

        public void setDescripcion(String descripcion) {
            this.descripcion = descripcion;
        }

        public double getDuracion() {
            return duracion;
        }

        public void setDuracion(double duracion) {
            this.duracion = duracion;
        }

        public String getFechaFormateada() {
            return (fecha != null) ? fecha.format(formatter) : "";
        }

        // toString puede ser útil para debugging o si se usa en un ComboBox sin cell factory
        @Override
        public String toString() {
            return "Tarea{" +
                    "fecha=" + getFechaFormateada() +
                    ", descripcion='" + descripcion + '\'' +
                    ", duracion=" + duracion +
                    '}';
        }
    }
}