package Frontend.com.gui.Controller.Tutor;

import Backend.API.API;
import Backend.DTO.ActividadDTO;
import Backend.DTO.InformeDTO;
import Backend.DTO.EstudianteDTO;
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
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.StringConverter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class evaluarInformesController {

    private static final Logger LOGGER = Logger.getLogger(Frontend.com.gui.Controller.Tutor.evaluarInformesController.class.getName());

    @FXML
    private Button volverButton;
    @FXML
    private ComboBox<EstudianteDTO> estudiantesComboBox;
    @FXML
    private ComboBox<ActividadDTO> actividadComboBox;
    @FXML
    private Button evaluarActividad;
    @FXML
    private Label tituloEstudianteSeleccionado;
    @FXML
    private TableView<InformeDTO> informesTableView;
    @FXML
    private TableColumn<InformeDTO, String> colTituloInforme;
    @FXML
    private TableColumn<InformeDTO, LocalDate> colFechaInforme;
    @FXML
    private TableColumn<InformeDTO, String> btnCalificar;
    @FXML
    private TableColumn<InformeDTO, Void> btnDescargarPDF;

    private API api;
    private ObservableList<EstudianteDTO> listaEstudiantes = FXCollections.observableArrayList();
    private ObservableList<ActividadDTO> listaActividades = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        configurarComboBoxEstudiantes();
        configurarComboBoxActividades();
        configurarTableViewInformes();

        // Listener para cuando se selecciona un estudiante
        estudiantesComboBox.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                cargarActividadesDelEstudiante(newSelection.getUsername());
                actualizarTituloEstudiante(newSelection.getNombre());
                // Limpiar la tabla cuando cambia el estudiante
                informesTableView.setItems(FXCollections.emptyObservableList());
                // Resetear el combo de actividades
                actividadComboBox.getSelectionModel().clearSelection();
            } else {
                tituloEstudianteSeleccionado.setText("Seleccione un Estudiante para ver sus informes");
                informesTableView.setItems(FXCollections.emptyObservableList());
                listaActividades.clear();
                actividadComboBox.getSelectionModel().clearSelection();
            }
        });

        // Listener para cuando se selecciona una actividad
        actividadComboBox.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            EstudianteDTO estudianteSeleccionado = estudiantesComboBox.getValue();
            if (newSelection != null && estudianteSeleccionado != null) {
                cargarYMostrarInformesDeEstudianteYActividad(estudianteSeleccionado.getUsername(), newSelection.getTitulo());
            } else {
                informesTableView.setItems(FXCollections.emptyObservableList());
            }
        });
    }

    public void setPersistenceAPI(API persistenceAPI) {
        this.api = persistenceAPI;
        if (this.api != null) {
            cargarEstudiantes();
        } else {
            LOGGER.log(Level.WARNING, "Persistence API set to null.");
            listaEstudiantes.clear();
            listaActividades.clear();
            informesTableView.setItems(FXCollections.emptyObservableList());
            tituloEstudianteSeleccionado.setText("Seleccione un Estudiante para ver sus informes (API no disponible)");
        }
    }

    private void configurarComboBoxEstudiantes() {
        estudiantesComboBox.setItems(listaEstudiantes);
        estudiantesComboBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(EstudianteDTO estudiante) {
                return estudiante == null ? null : estudiante.getNombre();
            }
            @Override
            public EstudianteDTO fromString(String string) {
                if (string == null) return null;
                return listaEstudiantes.stream()
                        .filter(est -> est.getNombre().equals(string))
                        .findFirst()
                        .orElse(null);
            }
        });
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

        // Configurar la columna de calificar con botones
        btnCalificar.setCellFactory(new Callback<TableColumn<InformeDTO, String>, TableCell<InformeDTO, String>>() {
            @Override
            public TableCell<InformeDTO, String> call(TableColumn<InformeDTO, String> param) {
                return new TableCell<InformeDTO, String>() {
                    private final Button calificarButton = new Button("Calificar");

                    {
                        calificarButton.setOnAction(event -> {
                            InformeDTO informe = getTableView().getItems().get(getIndex());
                            mostrarDialogoCalificacion(informe);
                        });
                    }

                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            // Mostrar el porcentaje actual si existe
                            InformeDTO informe = getTableView().getItems().get(getIndex());
                            if (informe.getPorcentajeAvance() > 0) {
                                calificarButton.setText(informe.getPorcentajeAvance() + "%");
                            } else {
                                calificarButton.setText("Calificar");
                            }
                            setGraphic(calificarButton);
                        }
                    }
                };
            }
        });
        // Configurar la columna de descargar PDF
        btnDescargarPDF.setCellFactory(new Callback<TableColumn<InformeDTO, Void>, TableCell<InformeDTO, Void>>() {
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

    private void cargarEstudiantes() {
        if (api == null) {
            LOGGER.log(Level.WARNING, "API no disponible para cargar estudiantes.");
            listaEstudiantes.clear();
            return;
        }

        try {
            // Obtener todos los estudiantes disponibles
            List<EstudianteDTO> estudiantes = api.obtenerEstudiantesByTutorUsername(api.obtenerUsername());

            if (estudiantes != null && !estudiantes.isEmpty()) {
                listaEstudiantes.clear();
                listaEstudiantes.addAll(estudiantes);
                LOGGER.log(Level.INFO, "Se cargaron " + estudiantes.size() + " estudiantes.");
            } else {
                LOGGER.log(Level.INFO, "No se encontraron estudiantes disponibles.");
                listaEstudiantes.clear();
            }

        } catch (ReadException e) {
            LOGGER.log(Level.SEVERE, "Error al cargar estudiantes desde la API.", e);
            mostrarAlerta("Error de Carga", "No se pudieron cargar los estudiantes: " + e.getMessage(), Alert.AlertType.ERROR);
            listaEstudiantes.clear();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error inesperado al cargar estudiantes.", e);
            mostrarAlerta("Error Inesperado", "Ocurrió un error inesperado al cargar los estudiantes: " + e.getMessage(), Alert.AlertType.ERROR);
            listaEstudiantes.clear();
        }
    }

    private void cargarActividadesDelEstudiante(String usernameEstudiante) {
        if (api == null || usernameEstudiante == null || usernameEstudiante.isEmpty()) {
            listaActividades.clear();
            return;
        }

        try {
            List<ActividadDTO> actividades = api.obtenerActividadesByEstudianteUsername(usernameEstudiante);

            if (actividades != null && !actividades.isEmpty()) {
                listaActividades.clear();
                listaActividades.addAll(actividades);
            } else {
                LOGGER.log(Level.INFO, "No se encontraron actividades para el estudiante: " + usernameEstudiante);
                listaActividades.clear();
            }

        } catch (ReadException e) {
            LOGGER.log(Level.SEVERE, "Error al cargar actividades del estudiante: " + usernameEstudiante, e);
            mostrarAlerta("Error de Carga", "No se pudieron cargar las actividades del estudiante: " + e.getMessage(), Alert.AlertType.ERROR);
            listaActividades.clear();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error inesperado al cargar actividades del estudiante: " + usernameEstudiante, e);
            mostrarAlerta("Error Inesperado", "Ocurrió un error inesperado al cargar las actividades: " + e.getMessage(), Alert.AlertType.ERROR);
            listaActividades.clear();
        }
    }

    private void cargarYMostrarInformesDeEstudianteYActividad(String usernameEstudiante, String actividadTitulo) {
        if (api == null || usernameEstudiante == null || actividadTitulo == null ||
                usernameEstudiante.isEmpty() || actividadTitulo.isEmpty()) {
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
                LOGGER.log(Level.INFO, "No hay informes para el estudiante: " + usernameEstudiante +
                        " en la actividad: " + actividadTitulo);
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error inesperado al cargar informes", e);
            mostrarAlerta("Error Inesperado", "Ocurrió un error inesperado al cargar los informes: " + e.getMessage(), Alert.AlertType.ERROR);
            informesTableView.setItems(FXCollections.emptyObservableList());
        }
    }

    private void mostrarDialogoCalificacion(InformeDTO informe) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Calificar Informe");
        dialog.setHeaderText("Calificar el informe: " + informe.getTitulo());
        dialog.setContentText("Ingrese el porcentaje de avance (0-100):");

        // Si ya tiene una calificación, mostrarla como valor por defecto
        if (informe.getPorcentajeAvance() > 0) {
            dialog.getEditor().setText(String.valueOf(informe.getPorcentajeAvance()));
        }

        dialog.showAndWait().ifPresent(porcentaje -> {
            try {
                int porcentajeInt = Integer.parseInt(porcentaje);
                if (porcentajeInt < 0 || porcentajeInt > 100) {
                    mostrarAlerta("Error de Validación", "El porcentaje debe estar entre 0 y 100.", Alert.AlertType.ERROR);
                    return;
                }

                // Debug: Mostrar información del informe
                String tituloInforme = informe.getTitulo();
                LOGGER.info("Intentando actualizar calificación para informe:");
                LOGGER.info("Título: '" + tituloInforme + "'");
                LOGGER.info("Longitud del título: " + tituloInforme.length());
                LOGGER.info("Título sin espacios: '" + tituloInforme.trim() + "'");

                // Obtener estudiante seleccionado para pasar como parámetro
                EstudianteDTO estudianteSeleccionado = estudiantesComboBox.getValue();
                if (estudianteSeleccionado == null) {
                    mostrarAlerta("Error", "Debe seleccionar un estudiante.", Alert.AlertType.ERROR);
                    return;
                }

                // Actualizar la calificación del informe con información adicional
                // Intenta pasar más parámetros si tu método lo permite
                api.actualizarCalificacionInformeTutor(
                        tituloInforme.trim(), // Eliminar espacios
                        porcentajeInt
                );

                // Refrescar la tabla
                ActividadDTO actividadSeleccionada = actividadComboBox.getValue();
                if (estudianteSeleccionado != null && actividadSeleccionada != null) {
                    cargarYMostrarInformesDeEstudianteYActividad(estudianteSeleccionado.getUsername(),
                            actividadSeleccionada.getTitulo());
                }

                mostrarAlerta("Éxito", "Calificación actualizada correctamente.", Alert.AlertType.INFORMATION);

            } catch (NumberFormatException e) {
                mostrarAlerta("Error de Formato", "Por favor ingrese un número válido.", Alert.AlertType.ERROR);
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Error al actualizar calificación", e);

                // Debug adicional
                LOGGER.severe("Detalles del error:");
                LOGGER.severe("Mensaje: " + e.getMessage());
                LOGGER.severe("Tipo de excepción: " + e.getClass().getSimpleName());

                mostrarAlerta("Error", "No se pudo actualizar la calificación: " + e.getMessage(), Alert.AlertType.ERROR);
            }
        });
    }

    private void actualizarTituloEstudiante(String nombre) {
        ActividadDTO actividadSeleccionada = actividadComboBox.getValue();
        if (actividadSeleccionada != null) {
            tituloEstudianteSeleccionado.setText("Informes de " + nombre + " - Actividad: " + actividadSeleccionada.getTitulo());
        } else {
            tituloEstudianteSeleccionado.setText("Informes de " + nombre + " (Seleccione una actividad)");
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

    @FXML
    private void evaluarActividad(ActionEvent event) {
        EstudianteDTO estudianteSeleccionado = estudiantesComboBox.getValue();
        ActividadDTO actividadSeleccionada = actividadComboBox.getValue();

        if (estudianteSeleccionado == null) {
            mostrarAlerta("Validación", "Debe seleccionar un estudiante primero.", Alert.AlertType.WARNING);
            return;
        }

        if (actividadSeleccionada == null) {
            mostrarAlerta("Validación", "Debe seleccionar una actividad primero.", Alert.AlertType.WARNING);
            return;
        }

        // Mostrar diálogo para ingresar el porcentaje de avance general de la actividad
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Evaluar Actividad");
        dialog.setHeaderText("Evaluar la actividad: " + actividadSeleccionada.getTitulo());
        dialog.setContentText("Ingrese el porcentaje de avance general (0-100):");

        dialog.showAndWait().ifPresent(porcentaje -> {
            try {
                int porcentajeInt = Integer.parseInt(porcentaje);
                if (porcentajeInt < 0 || porcentajeInt > 100) {
                    mostrarAlerta("Error de Validación", "El porcentaje debe estar entre 0 y 100.", Alert.AlertType.ERROR);
                    return;
                }

                // Actualizar el porcentaje de avance de la actividad para el estudiante
                api.actualizarPorcentajeAvanceActividad(estudianteSeleccionado.getUsername(),
                        actividadSeleccionada.getTitulo(),
                        porcentajeInt);

                mostrarAlerta("Éxito", "Porcentaje de avance actualizado correctamente.", Alert.AlertType.INFORMATION);

            } catch (NumberFormatException e) {
                mostrarAlerta("Error de Formato", "Por favor ingrese un número válido.", Alert.AlertType.ERROR);
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Error al actualizar porcentaje de avance", e);
                mostrarAlerta("Error", "No se pudo actualizar el porcentaje de avance: " + e.getMessage(), Alert.AlertType.ERROR);
            }
        });
    }


    @FXML
    public void VolverHome(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Frontend/vistas/Tutor/homeTutor.fxml"));
            Parent root = loader.load();
            HomeTutorController controller = loader.getController();

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