package Frontend.com.gui.Controller.Docente;

import Backend.API.API;
import Backend.DTO.ActividadDTO;
import Backend.DTO.ConvenioPPSDTO;
import Backend.DTO.EstudianteDTO;
import Backend.Exceptions.ReadException;
import Frontend.com.gui.Controller.Estudiante.HomeController;
import javafx.beans.property.SimpleStringProperty;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class ListadoDeEstudiantesController {

    private static final Logger LOGGER = Logger.getLogger(ListadoDeEstudiantesController.class.getName());

    @FXML private TableView<EstudianteDTO> tablaEstudiantes;

    @FXML private TableColumn<EstudianteDTO, String> colNombre;
    @FXML private TableColumn<EstudianteDTO, String> colCarrera;
    @FXML private TableColumn<EstudianteDTO, String> colCorreo;
    @FXML private TableColumn<EstudianteDTO, String> colProyecto;
    @FXML private TableColumn<EstudianteDTO, String> colTutor;
    @FXML private TableColumn<EstudianteDTO, String> colEmpresa;
    @FXML private TableColumn<EstudianteDTO, String> colDuracion;
    @FXML private TableColumn<EstudianteDTO, String> colEstado;

    // Botones de acciones
    @FXML private Button verCronogramaButton;
    @FXML private Button verAvancesButton;
    @FXML private Button verDetallesButton;
    @FXML private Button btnVolver;

    // Filtros
    @FXML private ComboBox<String> filtroEstado;
    @FXML private Button btnFiltrar;
    @FXML private Button btnLimpiarFiltro;

    // Labels informativos
    @FXML private Label lblTotalEstudiantes;
    @FXML private Label lblEstudiantesSeleccionados;

    private final ObservableList<EstudianteDTO> listaEstudiantes = FXCollections.observableArrayList();
    private final ObservableList<EstudianteDTO> listaEstudiantesFiltrada = FXCollections.observableArrayList();

    private API api;

    public void setPersistenceAPI(API api) {
        this.api = api;
        inicializarComponentes();
        cargarTabla();
        cargarEstudiantes();
    }

    private void inicializarComponentes() {
        // Configurar ComboBox de filtros
        filtroEstado.setItems(FXCollections.observableArrayList(
                "Todos", "En Curso", "Finalizada", "Pendiente"
        ));
        filtroEstado.setValue("Todos");

        // Configurar listeners para la selección de la tabla
        tablaEstudiantes.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> actualizarBotonesAccion(newValue != null)
        );

        // Inicialmente deshabilitar botones de acción
        actualizarBotonesAccion(false);
    }

    private void cargarTabla() {
        // Configurar columnas básicas
        colNombre.setCellValueFactory(data -> new SimpleStringProperty(
                data.getValue().getNombre() != null ? data.getValue().getNombre() : "N/A"
        ));

        colCarrera.setCellValueFactory(data -> new SimpleStringProperty(
                data.getValue().getCarrera() != null ? data.getValue().getCarrera() : "N/A"
        ));

        colCorreo.setCellValueFactory(data -> new SimpleStringProperty(
                data.getValue().getEmail() != null ? data.getValue().getEmail() : "N/A"
        ));

        // Configurar columnas de práctica profesional
        colProyecto.setCellValueFactory(data -> {
            try {
                ConvenioPPSDTO convenio = api.obtenerConvenioPPSByEstudianteUsername(data.getValue().getUsername());
                return new SimpleStringProperty(
                        convenio != null && convenio.getProyecto() != null
                                ? convenio.getProyecto().getTitulo()
                                : "Sin proyecto asignado"
                );
            } catch (Exception e) {
                LOGGER.warning("Error al obtener proyecto para estudiante: " + e.getMessage());
                return new SimpleStringProperty("Sin proyecto");
            }
        });

        colTutor.setCellValueFactory(data -> {
            try {
                ConvenioPPSDTO convenio = api.obtenerConvenioPPSByEstudianteUsername(data.getValue().getUsername());
                return new SimpleStringProperty(
                        convenio != null && convenio.getPlan() != null && convenio.getPlan().getTutor() != null
                                ? convenio.getPlan().getTutor().getNombre()
                                : "Sin tutor asignado"
                );
            } catch (Exception e) {
                LOGGER.warning("Error al obtener tutor para estudiante: " + e.getMessage());
                return new SimpleStringProperty("Sin tutor");
            }
        });

        colEmpresa.setCellValueFactory(data -> {
            try {
                ConvenioPPSDTO convenio = api.obtenerConvenioPPSByEstudianteUsername(data.getValue().getUsername());
                return new SimpleStringProperty(
                        convenio != null && convenio.getEntidad() != null
                                ? convenio.getEntidad().getNombre()
                                : "Sin empresa asignada"
                );
            } catch (Exception e) {
                LOGGER.warning("Error al obtener empresa para estudiante: " + e.getMessage());
                return new SimpleStringProperty("Sin empresa");
            }
        });

        colDuracion.setCellValueFactory(data -> {
            try {
                ConvenioPPSDTO convenio = api.obtenerConvenioPPSByEstudianteUsername(data.getValue().getUsername());
                if (convenio != null && convenio.getPlan() != null && convenio.getPlan().getActividades() != null) {
                    int totalHoras = calcularDuracionTotalActividades(convenio.getPlan().getActividades());
                    return new SimpleStringProperty(totalHoras + " horas");
                } else {
                    return new SimpleStringProperty("No definida");
                }
            } catch (Exception e) {
                LOGGER.warning("Error al obtener duración para estudiante: " + e.getMessage());
                return new SimpleStringProperty("No definida");
            }
        });

        colEstado.setCellValueFactory(data -> {
            try {
                ConvenioPPSDTO convenio = api.obtenerConvenioPPSByEstudianteUsername(data.getValue().getUsername());
                String estado = determinarEstadoPractica(convenio);
                return new SimpleStringProperty(estado);
            } catch (Exception e) {
                LOGGER.warning("Error al obtener estado para estudiante: " + e.getMessage());
                return new SimpleStringProperty("Pendiente");
            }
        });
    }

    private String determinarEstadoPractica(ConvenioPPSDTO convenio) {
        if (convenio == null) {
            return "Pendiente";
        }

        // Falta la logica
        if (convenio.getPlan() != null && convenio.getProyecto() != null) {
            // Si tiene plan y proyecto, está en curso
            return "En Curso";
        } else if (convenio.getProyecto() != null) {
            // Si solo tiene proyecto pero no plan, está pendiente
            return "Pendiente";
        } else {
            return "Pendiente";
        }
    }

    private void cargarEstudiantes() {
        listaEstudiantes.clear();
        try {
            List<ConvenioPPSDTO> convenios = api.obtenerConvenios();

            Set<String> usuariosVistos = new HashSet<>();

            for (ConvenioPPSDTO convenio : convenios) {
                EstudianteDTO estudiante = convenio.getEstudiante();
                if (estudiante != null && estudiante.getUsername() != null) {
                    // Solo agregar si no hemos visto este usuario antes
                    if (!usuariosVistos.contains(estudiante.getUsername())) {
                        listaEstudiantes.add(estudiante);
                        usuariosVistos.add(estudiante.getUsername());
                    }
                }
            }

            LOGGER.info("Cargados " + listaEstudiantes.size() + " estudiantes únicos de " + convenios.size() + " convenios");

            // Aplicar filtro actual
            aplicarFiltro();
            actualizarLabelsInformativos();

        } catch (ReadException e) {
            LOGGER.severe("Error al cargar estudiantes: " + e.getMessage());
            mostrarAlerta("Error al cargar estudiantes", e.getMessage());
        }
    }

    @FXML
    private void filtrarEstudiantes() {
        aplicarFiltro();
    }

    @FXML
    private void limpiarFiltro() {
        filtroEstado.setValue("Todos");
        aplicarFiltro();
    }

    private void aplicarFiltro() {
        String estadoSeleccionado = filtroEstado.getValue();

        if ("Todos".equals(estadoSeleccionado)) {
            listaEstudiantesFiltrada.setAll(listaEstudiantes);
        } else {
            List<EstudianteDTO> estudiantesFiltrados = listaEstudiantes.stream()
                    .filter(estudiante -> {
                        try {
                            ConvenioPPSDTO convenio = api.obtenerConvenioPPSByEstudianteUsername(estudiante.getUsername());
                            String estado = determinarEstadoPractica(convenio);
                            return estadoSeleccionado.equals(estado);
                        } catch (Exception e) {
                            return false;
                        }
                    })
                    .collect(Collectors.toList());

            listaEstudiantesFiltrada.setAll(estudiantesFiltrados);
        }

        tablaEstudiantes.setItems(listaEstudiantesFiltrada);
        actualizarLabelsInformativos();
    }

    @FXML
    private void verCronograma() {
        EstudianteDTO estudiante = tablaEstudiantes.getSelectionModel().getSelectedItem();
        if (estudiante != null) {
            try {
                // Aquí implementarías la navegación a la vista de cronograma
                LOGGER.info("Abriendo cronograma para estudiante: " + estudiante.getNombre());
                mostrarInfo("Cronograma", "Abriendo cronograma para: " + estudiante.getNombre());
                // Ejemplo: navegarACronograma(estudiante);
            } catch (Exception e) {
                mostrarAlerta("Error", "Error al abrir cronograma: " + e.getMessage());
            }
        }
    }

    @FXML
    private void verAvances() {
        EstudianteDTO estudiante = tablaEstudiantes.getSelectionModel().getSelectedItem();
        if (estudiante != null) {
            try {
                // Aquí implementarías la navegación a la vista de avances
                LOGGER.info("Abriendo avances para estudiante: " + estudiante.getNombre());
                mostrarInfo("Avances", "Abriendo avances para: " + estudiante.getNombre());
                // Ejemplo: navegarAAvances(estudiante);
            } catch (Exception e) {
                mostrarAlerta("Error", "Error al abrir avances: " + e.getMessage());
            }
        }
    }

    @FXML
    private void verDetalles() {
        EstudianteDTO estudiante = tablaEstudiantes.getSelectionModel().getSelectedItem();
        if (estudiante != null) {
            try {
                ConvenioPPSDTO convenio = api.obtenerConvenioPPSByEstudianteUsername(estudiante.getUsername());
                mostrarDetallesEstudiante(estudiante, convenio);
            } catch (Exception e) {
                mostrarAlerta("Error", "Error al obtener detalles: " + e.getMessage());
            }
        }
    }

    private void mostrarDetallesEstudiante(EstudianteDTO estudiante, ConvenioPPSDTO convenio) {
        StringBuilder detalles = new StringBuilder();
        detalles.append("=== INFORMACIÓN DEL ESTUDIANTE ===\n");
        detalles.append("Nombre: ").append(estudiante.getNombre()).append("\n");
        detalles.append("Carrera: ").append(estudiante.getCarrera()).append("\n");
        detalles.append("Correo: ").append(estudiante.getEmail()).append("\n\n");

        detalles.append("=== INFORMACIÓN DE LA PRÁCTICA ===\n");
        if (convenio != null) {
            if (convenio.getProyecto() != null) {
                detalles.append("Proyecto: ").append(convenio.getProyecto().getTitulo()).append("\n");
            }
            if (convenio.getEntidad() != null) {
                detalles.append("Empresa: ").append(convenio.getEntidad().getNombre()).append("\n");
            }
            if (convenio.getPlan() != null) {
                if (convenio.getPlan().getTutor() != null) {
                    detalles.append("Tutor: ").append(convenio.getPlan().getTutor().getNombre()).append("\n");
                }
                detalles.append("Horas totales trabajadas: ").append(calcularDuracionTotalActividades(convenio.getPlan().getActividades()) + " Horas\n");
            }
            detalles.append("Estado: ").append(determinarEstadoPractica(convenio)).append("\n");
        } else {
            detalles.append("No hay información de práctica disponible\n");
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Detalles del Estudiante");
        alert.setHeaderText("Información completa");
        alert.setContentText(detalles.toString());
        alert.getDialogPane().setPrefSize(400, 300);
        alert.showAndWait();
    }

    @FXML
    public void volverHome(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Frontend/vistas/Docente/homeDocente.fxml"));
            Parent root = loader.load();
            HomeDocenteController controller = loader.getController();

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

    private void actualizarBotonesAccion(boolean habilitado) {
        verCronogramaButton.setDisable(!habilitado);
        verAvancesButton.setDisable(!habilitado);
        verDetallesButton.setDisable(!habilitado);
    }

    private void actualizarLabelsInformativos() {
        lblTotalEstudiantes.setText("Total de estudiantes: " + listaEstudiantes.size());
        lblEstudiantesSeleccionados.setText("Mostrando: " + listaEstudiantesFiltrada.size());
    }

    private int calcularDuracionTotalActividades(List<ActividadDTO> actividades) {
        if (actividades == null || actividades.isEmpty()) {
            return 0;
        }

        return actividades.stream()
                .mapToInt(ActividadDTO::getDuracion)
                .sum();
    }

    private String obtenerDetalleActividades(List<ActividadDTO> actividades) {
        if (actividades == null || actividades.isEmpty()) {
            return "Sin actividades definidas";
        }

        int totalHoras = calcularDuracionTotalActividades(actividades);
        int cantidadActividades = actividades.size();

        return String.format("%d horas (%d actividades)", totalHoras, cantidadActividades);
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private void mostrarInfo(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}