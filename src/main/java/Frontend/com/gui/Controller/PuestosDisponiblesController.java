package Frontend.com.gui.Controller;

import Backend.API.API;
import Backend.DTO.ProyectoDTO;
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
    public Label lblTutor;

    private API api;
    private ResourceBundle bundle;
    private ObservableList<ProyectoDTO> oportunidadesList;
    private ProyectoDTO oportunidadSeleccionada;

    @FXML private ListView<ProyectoDTO> lvOportunidades;
    @FXML private Label lblTituloOportunidad;
    @FXML private Label lblEmpresa;
    @FXML private Label lblArea;
    @FXML private Label lblUbicacion;
    @FXML private TextArea taDescripcion;
    @FXML private TextArea taRequisitos;
    @FXML private Button btnInscribir;
    @FXML public Button btnVolverHome;


    @FXML
    public void initialize() {
        lvOportunidades.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> mostrarDetallesOportunidad(newValue));
        mostrarDetallesOportunidad(null);
    }

    public void setPersistenceAPI(API persistenceAPI) throws Exception {
        this.api = persistenceAPI;
        // this.bundle = api.obtenerIdioma();
        // actualizarIdioma();
        cargarOportunidadesDisponibles();
    }

    private void cargarOportunidadesDisponibles() {
        if (this.api == null) {
            LOGGER.log(Level.SEVERE, "API no inicializada. No se pueden cargar oportunidades.");
            mostrarAlerta("Error de Configuración", "La conexión con el sistema (API) no está disponible.", Alert.AlertType.ERROR);
            return;
        }

        try {
            List<ProyectoDTO> proyectosHabilitados = api.obtenerProyectosHabilitados();

            oportunidadesList = FXCollections.observableArrayList(proyectosHabilitados);
            lvOportunidades.setItems(oportunidadesList);

            lvOportunidades.setCellFactory(lv -> new ListCell<ProyectoDTO>() {
                @Override
                protected void updateItem(ProyectoDTO proyecto, boolean empty) {
                    super.updateItem(proyecto, empty);
                    if (empty || proyecto == null) {
                        setText(null);
                    } else {
                        setText(proyecto.getTitulo());
                    }
                }
            });

            if (proyectosHabilitados.isEmpty()) {
                mostrarAlerta("Información", "No hay puestos disponibles actualmente.", Alert.AlertType.INFORMATION);
            }
        } catch (Backend.Exceptions.ReadException e) {
            throw new RuntimeException(e);
        }
    }

    private void mostrarDetallesOportunidad(ProyectoDTO proyecto) {
        oportunidadSeleccionada = proyecto;
        if (oportunidadSeleccionada != null) {
            lblTituloOportunidad.setText(oportunidadSeleccionada.getTitulo());

            lblEmpresa.setText("Empresa: " + (oportunidadSeleccionada.getUbicacion() ));

            lblArea.setText("Área: " + (oportunidadSeleccionada.getAreaDeInteres()));

            taDescripcion.setText(oportunidadSeleccionada.getDescripcion());

            taRequisitos.setText(oportunidadSeleccionada.getRequisitos());

            lblTutor.setText("Tutor Externo: " + oportunidadSeleccionada.getTutorEncargado().getNombre());

            lblUbicacion.setText("Ubicación: " +oportunidadSeleccionada.getUbicacion());

            btnInscribir.setDisable(false);
        } else {
            lblTituloOportunidad.setText("Selecciona una oportunidad");
            lblEmpresa.setText("Empresa: -");
            lblArea.setText("Área: -");
            lblTutor.setText("Tutor Externo: -");
            lblUbicacion.setText("Ubicación: -");
            taDescripcion.setText("");
            taRequisitos.setText("");
            btnInscribir.setDisable(true);
        }
    }

    @FXML
    private void handleInscribir(ActionEvent event) {
        if (oportunidadSeleccionada != null) {
            Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
            confirmation.setTitle("Confirmar Inscripción");
            confirmation.setHeaderText(null);

            String empresaNombre = oportunidadSeleccionada.getUbicacion() != null ? oportunidadSeleccionada.getUbicacion() : "Empresa no especificada";

            confirmation.setContentText("¿Estás seguro de que deseas postularte para el proyecto: \"" + oportunidadSeleccionada.getTitulo() + "\" en " + empresaNombre + "?");

            Optional<ButtonType> result = confirmation.showAndWait();

        } else {
            mostrarAlerta("Selección Requerida", "Por favor, selecciona un proyecto de la lista para postularte.", Alert.AlertType.WARNING);
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
}