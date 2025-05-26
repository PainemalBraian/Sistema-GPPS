package Frontend.com.gui.Controller.Estudiante;

import Backend.API.API;
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


import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class InformesController  {

    @FXML
    private Button btnSeleccionarArchivo;
    @FXML
    private Label lblNombreArchivoSeleccionado;
    @FXML
    private ComboBox<String> comboBoxTipoInforme;
    @FXML
    private Button btnCargarInforme;
    @FXML
    private Label lblMensajeCarga;

    @FXML
    private TableView<InformeCargado> tableViewInformes;
    @FXML
    private TableColumn<InformeCargado, String> columnaNombreArchivo;
    @FXML
    private TableColumn<InformeCargado, String> columnaTipoInforme;
    @FXML
    private TableColumn<InformeCargado, String> columnaFechaCarga;
    @FXML
    private TableColumn<InformeCargado, String> columnaEstado;

    private File archivoSeleccionado;
    private ObservableList<InformeCargado> listaInformesObservables;

    // Directorio donde se "guardarán" los informes (simulación)
    // En una aplicación real, esto sería un servicio de backend, S3, etc.
    private static final String DIRECTORIO_INFORMES = "datos_gpps/informes_estudiantes/";
    private API api;

    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Configurar ComboBox de tipo de informe
        comboBoxTipoInforme.setItems(FXCollections.observableArrayList("Parcial", "Final"));

        // Configurar TableView
        columnaNombreArchivo.setCellValueFactory(new PropertyValueFactory<>("nombreArchivo"));
        columnaTipoInforme.setCellValueFactory(new PropertyValueFactory<>("tipoInforme"));
        columnaFechaCarga.setCellValueFactory(new PropertyValueFactory<>("fechaCargaFormateada"));
        columnaEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));

        listaInformesObservables = FXCollections.observableArrayList();
        tableViewInformes.setItems(listaInformesObservables);

        // Cargar informes previamente guardados (simulación o desde BD)
        cargarInformesGuardados();

        // Deshabilitar botón de carga inicialmente
        btnCargarInforme.setDisable(true);
        lblNombreArchivoSeleccionado.setText("Ningún archivo seleccionado");

        // Crear directorio de informes si no existe (para simulación local)
        File dir = new File(DIRECTORIO_INFORMES);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    @FXML
    void handleSeleccionarArchivo(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar Informe PDF");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Archivos PDF", "*.pdf")
        );
        // Obtener el Stage actual para mostrar el FileChooser
        Stage stage = (Stage) btnSeleccionarArchivo.getScene().getWindow();
        archivoSeleccionado = fileChooser.showOpenDialog(stage);

        if (archivoSeleccionado != null) {
            lblNombreArchivoSeleccionado.setText(archivoSeleccionado.getName());
            btnCargarInforme.setDisable(comboBoxTipoInforme.getValue() == null); // Habilitar si tipo ya está seleccionado
            lblMensajeCarga.setText(""); // Limpiar mensajes anteriores
        } else {
            lblNombreArchivoSeleccionado.setText("Ningún archivo seleccionado");
            btnCargarInforme.setDisable(true);
        }
    }

    @FXML
    void handleCargarInforme(ActionEvent event) {
        if (archivoSeleccionado == null) {
            mostrarAlerta("Error", "No ha seleccionado ningún archivo PDF.", Alert.AlertType.ERROR);
            return;
        }
        String tipoInforme = comboBoxTipoInforme.getValue();
        if (tipoInforme == null || tipoInforme.isEmpty()) {
            mostrarAlerta("Error", "Debe seleccionar el tipo de informe (Parcial o Final).", Alert.AlertType.ERROR);
            return;
        }

        try {
            // Simulación de guardado del archivo y notificación
            // 1. "Guardar" el archivo (copiarlo a un directorio local para este ejemplo)
            // En una app real: subir a un servidor, S3, base de datos, etc.
            Path destino = Paths.get(DIRECTORIO_INFORMES + System.currentTimeMillis() + "_" + archivoSeleccionado.getName());
            Files.copy(archivoSeleccionado.toPath(), destino, StandardCopyOption.REPLACE_EXISTING);

            // 2. Registrar en la lista (y en la BD en una app real)
            InformeCargado nuevoInforme = new InformeCargado(
                    archivoSeleccionado.getName(), // Podrías usar el nombre del archivo en 'destino' si lo renombras
                    tipoInforme,
                    LocalDateTime.now(),
                    "Pendiente de Revisión", // Estado inicial
                    destino.toString() // Ruta del archivo "guardado"
            );
            listaInformesObservables.add(nuevoInforme);

            // 3. Simular notificación a docentes/tutores
            System.out.println("Notificación: Nuevo informe '" + nuevoInforme.getNombreArchivo() +
                    "' ('" + nuevoInforme.getTipoInforme() + "') cargado por el estudiante. Ruta: " + nuevoInforme.getRutaArchivo());
            // Aquí iría la lógica real de notificación (email, sistema de mensajes interno, etc.)


            lblMensajeCarga.setText("Informe cargado exitosamente: " + archivoSeleccionado.getName());
            lblMensajeCarga.getStyleClass().remove("error"); // Asegurar estilo de éxito
            limpiarSeleccionArchivo();

        } catch (Exception e) {
            e.printStackTrace();
            lblMensajeCarga.setText("Error al cargar el informe: " + e.getMessage());
            lblMensajeCarga.getStyleClass().add("error"); // Aplicar estilo de error
            mostrarAlerta("Error de Carga", "Ocurrió un problema al intentar cargar el archivo.", Alert.AlertType.ERROR);
        }
    }

    private void limpiarSeleccionArchivo() {
        archivoSeleccionado = null;
        lblNombreArchivoSeleccionado.setText("Ningún archivo seleccionado");
        comboBoxTipoInforme.getSelectionModel().clearSelection();
        btnCargarInforme.setDisable(true);
    }

    private void cargarInformesGuardados() {
        // Simulación: Aquí cargarías los informes desde una base de datos o sistema de archivos.
        // Por ahora, la lista estará vacía o puedes añadir ejemplos:
        /*
        listaInformesObservables.add(new InformeCargado("informe_avance_1.pdf", "Parcial",
                                    LocalDateTime.now().minusDays(10), "Revisado", "/ruta/simulada/1"));
        listaInformesObservables.add(new InformeCargado("propuesta_inicial.pdf", "Parcial",
                                    LocalDateTime.now().minusDays(20), "Aprobado", "/ruta/simulada/2"));
        */
    }

    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    // Listener para habilitar el botón de carga cuando ambos campos estén listos
    @FXML
    public void onTipoInformeChanged() {
        if (archivoSeleccionado != null && comboBoxTipoInforme.getValue() != null) {
            btnCargarInforme.setDisable(false);
        } else {
            btnCargarInforme.setDisable(true);
        }
    }

    @FXML
    public void VolverHome(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Frontend/vistas/Estudiante/home.fxml"));
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

    public void setPersistenceAPI(API persistenceAPI) throws Exception {
        this.api = persistenceAPI;
    }

    // --- Clase Interna para el modelo de InformeCargado ---
    public static class InformeCargado {
        private String nombreArchivo;
        private String tipoInforme;
        private LocalDateTime fechaCarga;
        private String estado;
        private String rutaArchivo; // Para referencia interna o futura descarga

        private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        public InformeCargado(String nombreArchivo, String tipoInforme, LocalDateTime fechaCarga, String estado, String rutaArchivo) {
            this.nombreArchivo = nombreArchivo;
            this.tipoInforme = tipoInforme;
            this.fechaCarga = fechaCarga;
            this.estado = estado;
            this.rutaArchivo = rutaArchivo;
        }

        public String getNombreArchivo() { return nombreArchivo; }
        public void setNombreArchivo(String nombreArchivo) { this.nombreArchivo = nombreArchivo; }

        public String getTipoInforme() { return tipoInforme; }
        public void setTipoInforme(String tipoInforme) { this.tipoInforme = tipoInforme; }

        public LocalDateTime getFechaCarga() { return fechaCarga; }
        public void setFechaCarga(LocalDateTime fechaCarga) { this.fechaCarga = fechaCarga; }

        public String getFechaCargaFormateada() {
            return (fechaCarga != null) ? fechaCarga.format(FORMATTER) : "";
        }

        public String getEstado() { return estado; }
        public void setEstado(String estado) { this.estado = estado; }

        public String getRutaArchivo() { return rutaArchivo; }
        public void setRutaArchivo(String rutaArchivo) { this.rutaArchivo = rutaArchivo; }
    }
}