package Frontend.com.gui.Controller.DirectorCarrera;

import Backend.API.API;
import Backend.DTO.ProyectoDTO;
import Backend.Exceptions.CreateException;
import Backend.Exceptions.ReadException;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;

public class GestionProyectosController {
    API api;
    @FXML
    private TableView<ProyectoDTO> tablaProyectos;
    @FXML
    private TableColumn<ProyectoDTO, String> colTitulo, colDescripcion, colArea, colUbicacion, colObjetivos, colRequisitos, colTutor;
    @FXML
    private TableColumn<ProyectoDTO, String> colHabilitado;

    @FXML private Button btnVolver;


    private ObservableList<ProyectoDTO> proyectos = FXCollections.observableArrayList();

    public void setPersistenceAPI(API api) {
        this.api = api;
        cargarProyectos();
    }

    private void cargarProyectos() {
        try {
            proyectos.setAll(api.obtenerProyectos());
        } catch (ReadException e) {
            e.printStackTrace();
            mostrarAlerta("Error", e.getMessage());
        }

        colTitulo.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getTitulo()));
        colDescripcion.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDescripcion()));
        colArea.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getAreaDeInteres()));
        colUbicacion.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getUbicacion()));
        colObjetivos.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getObjetivos()));
        colRequisitos.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getRequisitos()));
        colTutor.setCellValueFactory(data -> {
            var tutor = data.getValue().getTutorEncargado();
            return new SimpleStringProperty(tutor != null ? tutor.getNombre() : "Sin asignar");
        });
        colHabilitado.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().isHabilitado() ? "Habilitado" : "Inhabilitado"));

        tablaProyectos.setItems(proyectos);
    }

    @FXML
    public void habilitarProyecto() {
        ProyectoDTO proyecto = tablaProyectos.getSelectionModel().getSelectedItem();
        if (proyecto != null && !proyecto.isHabilitado()) {
            proyecto.setHabilitado(true);
            try {
                api.actualizarProyecto(proyecto);
            } catch (CreateException e) {
                mostrarAlerta("Error", e.getMessage());
            }
            cargarProyectos();
        }
    }

    @FXML
    public void inhabilitarProyecto() {
        ProyectoDTO proyecto = tablaProyectos.getSelectionModel().getSelectedItem();
        if (proyecto != null && proyecto.isHabilitado()) {
            proyecto.setHabilitado(false);
            try {
                api.actualizarProyecto(proyecto);
            } catch (CreateException e) {
                mostrarAlerta("Error", e.getMessage());
            }
            cargarProyectos();
        }
    }
    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    @FXML
    public void verDetallesProyecto(ActionEvent actionEvent) {
        ProyectoDTO seleccionado = tablaProyectos.getSelectionModel().getSelectedItem();

        if (seleccionado != null) {
            StringBuilder detalles = new StringBuilder();
            detalles.append("Título: ").append(seleccionado.getTitulo()).append("\n")
                    .append("Descripción: ").append(seleccionado.getDescripcion()).append("\n")
                    .append("Área de Interés: ").append(seleccionado.getAreaDeInteres()).append("\n")
                    .append("Ubicación: ").append(seleccionado.getUbicacion()).append("\n")
                    .append("Objetivos: ").append(seleccionado.getObjetivos()).append("\n")
                    .append("Requisitos: ").append(seleccionado.getRequisitos()).append("\n")
                    .append("Estado: ").append(seleccionado.isHabilitado() ? "Habilitado" : "Inhabilitado").append("\n");

            if (seleccionado.getTutorEncargado() != null) {
                detalles.append("Tutor: ").append(seleccionado.getTutorEncargado().getNombre()).append("\n");
            } else {
                detalles.append("Tutor: No asignado\n");
            }

            Alert alerta = new Alert(Alert.AlertType.INFORMATION);
            alerta.setTitle("Detalles del Proyecto");
            alerta.setHeaderText("Información del Proyecto Seleccionado");
            alerta.setContentText(detalles.toString());
            alerta.showAndWait();

        } else {
            Alert alerta = new Alert(Alert.AlertType.WARNING);
            alerta.setTitle("Sin selección");
            alerta.setHeaderText(null);
            alerta.setContentText("Por favor, selecciona un proyecto de la lista.");
            alerta.showAndWait();
        }
    }

    @FXML
    public void volverHome() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Frontend/vistas/DirectorCarrera/homeDirectorCarrera.fxml"));
            Parent root = loader.load();
            homeDirectorCarreraController controller = loader.getController();

            controller.setPersistenceAPI(this.api);

            Stage stage = (Stage) btnVolver.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Home - GPPS");

        } catch (IOException e) {
            mostrarAlerta("Error de Navegación", "No se pudo volver a la pantalla principal.");
        } catch (Exception e) {
            mostrarAlerta("Error de Configuración", "No se pudo configurar la pantalla principal: " + e.getMessage());
        }
    }

}
