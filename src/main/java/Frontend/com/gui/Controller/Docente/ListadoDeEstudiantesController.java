package Frontend.com.gui.Controller.Docente;

import Backend.API.API;
import Backend.DTO.ActividadDTO;
import Backend.DTO.ConvenioPPSDTO;
import Backend.DTO.EstudianteDTO;
import Backend.DTO.InformeDTO;
import Backend.DTO.UsuarioDTO;
import Backend.Exceptions.ReadException;
import Backend.Exceptions.UserException;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class ListadoDeEstudiantesController implements Initializable {

    private static final Logger LOGGER = Logger.getLogger(ListadoDeEstudiantesController.class.getName());

    // Tabla de estudiantes actualizada según la historia de usuario
    @FXML private TableView<EstudianteConPracticaDTO> tablaEstudiantes;
    @FXML private TableColumn<EstudianteConPracticaDTO, String> colNombre;
    @FXML private TableColumn<EstudianteConPracticaDTO, String> colCarrera;
    @FXML private TableColumn<EstudianteConPracticaDTO, String> colCorreo;
    @FXML private TableColumn<EstudianteConPracticaDTO, String> colProyecto;
    @FXML private TableColumn<EstudianteConPracticaDTO, String> colTutor;
    @FXML private TableColumn<EstudianteConPracticaDTO, String> colEmpresa;
    @FXML private TableColumn<EstudianteConPracticaDTO, String> colDuracion;

    // ComboBox para filtrar por estado
    @FXML private ComboBox<String> filtroEstadoPractica;

    // Botones de acción
    @FXML private Button btnVerCronograma;
    @FXML private Button btnVerAvances;
    @FXML private Button btnVerInformes;
    @FXML private Button btnComunicaciones;
    @FXML private Button btnVolver;

    private final ObservableList<EstudianteConPracticaDTO> listaEstudiantes = FXCollections.observableArrayList();
    private final ObservableList<EstudianteConPracticaDTO> listaEstudiantesFiltrada = FXCollections.observableArrayList();
    private API api;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        inicializarComboBoxFiltro();
        configurarEventosFiltro();
    }

    public void setPersistenceAPI(API persistenceAPI) {
        this.api = persistenceAPI;
        inicializarTabla();
        cargarEstudiantesDelDocente();
    }

    private void inicializarComboBoxFiltro() {
        filtroEstadoPractica.getItems().addAll("Todos", "En curso", "Finalizada", "Pendiente");
        filtroEstadoPractica.setValue("Todos");
    }

    private void configurarEventosFiltro() {
        filtroEstadoPractica.setOnAction(e -> aplicarFiltro());
    }

    private void inicializarTabla() {
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombreEstudiante"));
        colCarrera.setCellValueFactory(new PropertyValueFactory<>("carrera"));
        colCorreo.setCellValueFactory(new PropertyValueFactory<>("correo"));
        colProyecto.setCellValueFactory(new PropertyValueFactory<>("tituloProyecto"));
        colTutor.setCellValueFactory(new PropertyValueFactory<>("nombreTutor"));
        colEmpresa.setCellValueFactory(new PropertyValueFactory<>("nombreEmpresa"));
        colDuracion.setCellValueFactory(new PropertyValueFactory<>("duracion"));

        tablaEstudiantes.setItems(listaEstudiantesFiltrada);

        // Configurar selección de fila para habilitar/deshabilitar botones
        tablaEstudiantes.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSelection, newSelection) -> actualizarEstadoBotones(newSelection != null)
        );
    }

    private void cargarEstudiantesDelDocente() {
        listaEstudiantes.clear();
        try {
            String username = api.obtenerUsername();
            List<EstudianteDTO> estudiantes = api.obtenerEstudiantesByDocenteUsername(username);

            for (EstudianteDTO estudiante : estudiantes) {
                ConvenioPPSDTO convenio = api.obtenerConvenioPPSByEstudianteUsername(estudiante.getUsername());

                EstudianteConPracticaDTO estudianteConPractica = new EstudianteConPracticaDTO();
                estudianteConPractica.setUsernameEstudiante(estudiante.getUsername());
                estudianteConPractica.setNombreEstudiante(estudiante.getNombre());
                estudianteConPractica.setCarrera(estudiante.getCarrera() != null ? estudiante.getCarrera() : "No especificada");
                estudianteConPractica.setCorreo(estudiante.getEmail() != null ? estudiante.getEmail() : "No especificado");

                if (convenio != null) {
                    estudianteConPractica.setTituloProyecto(convenio.getProyecto() != null ?
                            convenio.getProyecto().getTitulo() : "Sin proyecto asignado");
                    estudianteConPractica.setNombreTutor(convenio.getTutor() != null ?
                            convenio.getTutor().getNombre() : "Sin tutor asignado");
                    estudianteConPractica.setNombreEmpresa(convenio.getEntidad() != null ?
                            convenio.getEntidad().getNombre() : "Sin empresa asignada");
                    estudianteConPractica.setDuracion(calcularDuracionPractica(convenio));
                    estudianteConPractica.setEstadoPractica(determinarEstadoPractica(convenio));
                    estudianteConPractica.setConvenio(convenio);
                } else {
                    estudianteConPractica.setTituloProyecto("Sin práctica asignada");
                    estudianteConPractica.setNombreTutor("N/A");
                    estudianteConPractica.setNombreEmpresa("N/A");
                    estudianteConPractica.setDuracion("N/A");
                    estudianteConPractica.setEstadoPractica("Pendiente");
                }

                listaEstudiantes.add(estudianteConPractica);
            }

            aplicarFiltro();

        } catch (ReadException | UserException e) {
            mostrarAlerta("Error al cargar datos", e.getMessage());
            LOGGER.log(Level.SEVERE, "Error al cargar estudiantes del docente", e);
        }
    }

    private String calcularDuracionPractica(ConvenioPPSDTO convenio) {
        // Implementar lógica para calcular duración basada en fechas del convenio
        // Por ahora retornamos un valor por defecto
        return "6 meses";
    }

    private String determinarEstadoPractica(ConvenioPPSDTO convenio) {
        if (convenio.isHabilitado()) {
            // Aquí se podría implementar lógica más compleja basada en fechas
            return "En curso";
        } else {
            return "Pendiente";
        }
    }

    private void aplicarFiltro() {
        String filtroSeleccionado = filtroEstadoPractica.getValue();
        listaEstudiantesFiltrada.clear();

        if ("Todos".equals(filtroSeleccionado)) {
            listaEstudiantesFiltrada.addAll(listaEstudiantes);
        } else {
            List<EstudianteConPracticaDTO> estudiantesFiltrados = listaEstudiantes.stream()
                    .filter(estudiante -> filtroSeleccionado.equals(estudiante.getEstadoPractica()))
                    .collect(Collectors.toList());
            listaEstudiantesFiltrada.addAll(estudiantesFiltrados);
        }
    }

    private void actualizarEstadoBotones(boolean tieneSeleccion) {
        btnVerCronograma.setDisable(!tieneSeleccion);
        btnVerAvances.setDisable(!tieneSeleccion);
        btnVerInformes.setDisable(!tieneSeleccion);
        btnComunicaciones.setDisable(!tieneSeleccion);
    }

    @FXML
    public void verCronograma() {
        EstudianteConPracticaDTO selected = tablaEstudiantes.getSelectionModel().getSelectedItem();
        if (selected != null) {
            try {
                // Lógica para mostrar cronograma del estudiante seleccionado
                System.out.println("Mostrando cronograma para: " + selected.getNombreEstudiante());
                // Aquí se abriría la ventana del cronograma
            } catch (Exception e) {
                mostrarAlerta("Error", "No se pudo cargar el cronograma: " + e.getMessage());
            }
        }
    }

    @FXML
    public void verAvances() {
        EstudianteConPracticaDTO selected = tablaEstudiantes.getSelectionModel().getSelectedItem();
        if (selected != null) {
            try {
                List<ActividadDTO> actividades = api.obtenerActividadesByEstudianteUsername(selected.getUsernameEstudiante());
                System.out.println("Mostrando " + actividades.size() + " avances para: " + selected.getNombreEstudiante());
                // Aquí se abriría la ventana de avances
            } catch (ReadException e) {
                mostrarAlerta("Error", "No se pudieron cargar los avances: " + e.getMessage());
            }
        }
    }

    @FXML
    public void verInformes() {
        EstudianteConPracticaDTO selected = tablaEstudiantes.getSelectionModel().getSelectedItem();
        if (selected != null && selected.getConvenio() != null) {
            try {
                List<InformeDTO> informes = api.obtenerInformesByConvenioTitulo(selected.getConvenio().getTitulo());
                System.out.println("Mostrando " + informes.size() + " informes para: " + selected.getNombreEstudiante());
                // Aquí se abriría la ventana de informes
            } catch (ReadException e) {
                mostrarAlerta("Error", "No se pudieron cargar los informes: " + e.getMessage());
            }
        }
    }

    @FXML
    public void verComunicaciones() {
        EstudianteConPracticaDTO selected = tablaEstudiantes.getSelectionModel().getSelectedItem();
        if (selected != null) {
            try {
                System.out.println("Mostrando comunicaciones para: " + selected.getNombreEstudiante());
                // Aquí se abriría la ventana de comunicaciones
            } catch (Exception e) {
                mostrarAlerta("Error", "No se pudieron cargar las comunicaciones: " + e.getMessage());
            }
        }
    }

    @FXML
    public void volverHome() {
        // Lógica para volver a la pantalla principal del docente
        System.out.println("Volviendo al menú principal");
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    // Clase interna para representar un estudiante con su práctica
    public static class EstudianteConPracticaDTO {
        private String usernameEstudiante;
        private String nombreEstudiante;
        private String carrera;
        private String correo;
        private String tituloProyecto;
        private String nombreTutor;
        private String nombreEmpresa;
        private String duracion;
        private String estadoPractica;
        private ConvenioPPSDTO convenio;

        // Getters y Setters
        public String getUsernameEstudiante() { return usernameEstudiante; }
        public void setUsernameEstudiante(String usernameEstudiante) { this.usernameEstudiante = usernameEstudiante; }

        public String getNombreEstudiante() { return nombreEstudiante; }
        public void setNombreEstudiante(String nombreEstudiante) { this.nombreEstudiante = nombreEstudiante; }

        public String getCarrera() { return carrera; }
        public void setCarrera(String carrera) { this.carrera = carrera; }

        public String getCorreo() { return correo; }
        public void setCorreo(String correo) { this.correo = correo; }

        public String getTituloProyecto() { return tituloProyecto; }
        public void setTituloProyecto(String tituloProyecto) { this.tituloProyecto = tituloProyecto; }

        public String getNombreTutor() { return nombreTutor; }
        public void setNombreTutor(String nombreTutor) { this.nombreTutor = nombreTutor; }

        public String getNombreEmpresa() { return nombreEmpresa; }
        public void setNombreEmpresa(String nombreEmpresa) { this.nombreEmpresa = nombreEmpresa; }

        public String getDuracion() { return duracion; }
        public void setDuracion(String duracion) { this.duracion = duracion; }

        public String getEstadoPractica() { return estadoPractica; }
        public void setEstadoPractica(String estadoPractica) { this.estadoPractica = estadoPractica; }

        public ConvenioPPSDTO getConvenio() { return convenio; }
        public void setConvenio(ConvenioPPSDTO convenio) { this.convenio = convenio; }
    }
}