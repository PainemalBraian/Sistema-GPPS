package Frontend.com.gui.Controller.Estudiante;

import Backend.API.API;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Predicate;

public class MensajesController implements Initializable {

    @FXML
    private TextField buscarConversacionField;

    @FXML
    private ListView<Conversacion> listaConversacionesView;

    @FXML
    private Button btnNuevaConversacion;

    @FXML
    private Label nombreContactoChatLabel;

    @FXML
    private ScrollPane scrollPaneMensajes;

    @FXML
    private VBox contenedorMensajesVBox;

    @FXML
    private TextArea areaNuevoMensaje;

    @FXML
    private Button btnEnviarMensaje;

    @FXML
    private BorderPane panelChatActivo;

    @FXML
    private Button btnVolverHomeMensajes;

    private API api;
    private ObservableList<Conversacion> conversaciones;
    private ObservableList<Conversacion> conversacionesFiltradas;
    private Conversacion conversacionSeleccionada;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Configurar lista de conversaciones con celda personalizada
        configureConversacionesListView();

        // Configurar funcionalidad de búsqueda
        configureBusqueda();

        // Configurar el textarea para enviar mensajes con Enter
        configureTextArea();

        // Deshabilitar el panel de chat hasta que se seleccione una conversación
        habilitarChat(false);

        // Cargar conversaciones de prueba (en la versión final, esto vendría de la API)
        cargarConversacionesDePrueba();
    }

    private void configureConversacionesListView() {
        conversaciones = FXCollections.observableArrayList();
        conversacionesFiltradas = FXCollections.observableArrayList();
        listaConversacionesView.setItems(conversacionesFiltradas);

        // Configurar celda personalizada para mostrar conversaciones
        listaConversacionesView.setCellFactory(listView -> new ListCell<>() {
            @Override
            protected void updateItem(Conversacion conversacion, boolean empty) {
                super.updateItem(conversacion, empty);

                if (empty || conversacion == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    HBox container = new HBox(10);
                    container.setPadding(new Insets(5));

                    VBox infoContainer = new VBox(2);
                    Label nombreLabel = new Label(conversacion.getNombreContacto());
                    nombreLabel.getStyleClass().add("conversacion-nombre");

                    Label ultimoMensajeLabel = new Label(
                            conversacion.getMensajes().isEmpty() ?
                                    "No hay mensajes" :
                                    conversacion.getMensajes().get(conversacion.getMensajes().size() - 1).getContenido()
                    );
                    ultimoMensajeLabel.getStyleClass().add("conversacion-ultimo-mensaje");
                    ultimoMensajeLabel.setMaxWidth(180);
                    ultimoMensajeLabel.setWrapText(true);

                    infoContainer.getChildren().addAll(nombreLabel, ultimoMensajeLabel);
                    container.getChildren().add(infoContainer);

                    setGraphic(container);
                }
            }
        });

        // Listener para cuando se selecciona una conversación
        listaConversacionesView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if (newValue != null) {
                        cargarConversacion(newValue);
                    }
                }
        );
    }

    private void configureBusqueda() {
        buscarConversacionField.textProperty().addListener((observable, oldValue, newValue) -> {
            filtrarConversaciones(newValue);
        });
    }

    private void configureTextArea() {
        // Permitir enviar mensaje con Enter (Shift+Enter para nueva línea)
        areaNuevoMensaje.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.ENTER && !event.isShiftDown()) {
                event.consume(); // Evitar nueva línea
                handleEnviarMensaje(null);
            }
        });
    }

    private void habilitarChat(boolean habilitado) {
        areaNuevoMensaje.setDisable(!habilitado);
        btnEnviarMensaje.setDisable(!habilitado);

        if (!habilitado) {
            nombreContactoChatLabel.setText("Seleccione una conversación");
            contenedorMensajesVBox.getChildren().clear();
        }
    }

    private void cargarConversacionesDePrueba() {
        // Esto es solo para pruebas, en la versión real vendría de la API
        List<Mensaje> mensajesConv1 = new ArrayList<>();
        mensajesConv1.add(new Mensaje("Hola, ¿cómo va tu proyecto?", LocalDateTime.now().minusDays(1), false));
        mensajesConv1.add(new Mensaje("Bien, estoy avanzando con la interfaz", LocalDateTime.now().minusDays(1).plusHours(1), true));
        mensajesConv1.add(new Mensaje("¿Podrías revisar mi código mañana?", LocalDateTime.now().minusHours(5), true));

        List<Mensaje> mensajesConv2 = new ArrayList<>();
        mensajesConv2.add(new Mensaje("Recuerda la entrega del informe para el viernes", LocalDateTime.now().minusDays(3), false));
        mensajesConv2.add(new Mensaje("¿Qué formato debe tener?", LocalDateTime.now().minusDays(3).plusMinutes(30), true));
        mensajesConv2.add(new Mensaje("PDF con la plantilla oficial", LocalDateTime.now().minusDays(3).plusHours(2), false));
        mensajesConv2.add(new Mensaje("Gracias, lo tendré listo", LocalDateTime.now().minusDays(2), true));

        List<Mensaje> mensajesConv3 = new ArrayList<>();
        mensajesConv3.add(new Mensaje("¿Asistirás a la presentación del jueves?", LocalDateTime.now().minusDays(1), false));

        conversaciones.addAll(
                new Conversacion("Prof. Martínez", mensajesConv1),
                new Conversacion("Coordinador de Prácticas", mensajesConv2),
                new Conversacion("Grupo de Proyecto", mensajesConv3)
        );

        conversacionesFiltradas.setAll(conversaciones);
    }

    private void filtrarConversaciones(String filtro) {
        if (filtro == null || filtro.isEmpty()) {
            conversacionesFiltradas.setAll(conversaciones);
            return;
        }

        String filtroLowerCase = filtro.toLowerCase();
        Predicate<Conversacion> contieneFiltro = conversacion ->
                conversacion.getNombreContacto().toLowerCase().contains(filtroLowerCase);

        conversacionesFiltradas.setAll(
                conversaciones.filtered(contieneFiltro)
        );
    }

    private void cargarConversacion(Conversacion conversacion) {
        conversacionSeleccionada = conversacion;
        nombreContactoChatLabel.setText(conversacion.getNombreContacto());
        habilitarChat(true);

        // Limpiar mensajes anteriores
        contenedorMensajesVBox.getChildren().clear();

        // Cargar mensajes
        for (Mensaje mensaje : conversacion.getMensajes()) {
            agregarMensajeAVista(mensaje);
        }

        // Desplazar al último mensaje
        Platform.runLater(() -> {
            scrollPaneMensajes.setVvalue(1.0);
        });
    }

    private void agregarMensajeAVista(Mensaje mensaje) {
        HBox contenedorMensaje = new HBox(10);
        contenedorMensaje.setPadding(new Insets(5));

        // Ajustar alineación según quien envía el mensaje
        if (mensaje.isEnviadoPorUsuario()) {
            contenedorMensaje.setAlignment(Pos.CENTER_RIGHT);
        } else {
            contenedorMensaje.setAlignment(Pos.CENTER_LEFT);
        }

        // Crear burbuja de mensaje
        VBox burbujaMensaje = new VBox(2);
        burbujaMensaje.setPadding(new Insets(10));
        burbujaMensaje.setMaxWidth(400);

        // Aplicar estilo según quien envía
        if (mensaje.isEnviadoPorUsuario()) {
            burbujaMensaje.getStyleClass().add("mensaje-enviado");
        } else {
            burbujaMensaje.getStyleClass().add("mensaje-recibido");
        }

        // Texto del mensaje
        Label textoMensaje = new Label(mensaje.getContenido());
        textoMensaje.setWrapText(true);

        // Hora del mensaje
        Label horaMensaje = new Label(mensaje.getFechaHora().format(formatter));
        horaMensaje.getStyleClass().add("hora-mensaje");

        burbujaMensaje.getChildren().addAll(textoMensaje, horaMensaje);
        contenedorMensaje.getChildren().add(burbujaMensaje);

        contenedorMensajesVBox.getChildren().add(contenedorMensaje);
    }

    @FXML
    void handleEnviarMensaje(ActionEvent event) {
        String contenidoMensaje = areaNuevoMensaje.getText().trim();

        if (contenidoMensaje.isEmpty() || conversacionSeleccionada == null) {
            return;
        }

        // Crear y agregar mensaje
        Mensaje nuevoMensaje = new Mensaje(contenidoMensaje, LocalDateTime.now(), true);
        conversacionSeleccionada.getMensajes().add(nuevoMensaje);
        agregarMensajeAVista(nuevoMensaje);

        // Limpiar campo de texto
        areaNuevoMensaje.clear();

        // Desplazar al nuevo mensaje
        Platform.runLater(() -> {
            scrollPaneMensajes.setVvalue(1.0);
        });

        // Aquí iría la lógica para enviar el mensaje al servidor
        // api.enviarMensaje(conversacionSeleccionada.getId(), contenidoMensaje);

        // Simulación de respuesta (solo para demo)
        if (Math.random() > 0.5) {
            simularRespuestaAutomatica();
        }
    }

    private void simularRespuestaAutomatica() {
        // Solo para demo - simular respuesta después de 1-3 segundos
        new Thread(() -> {
            try {
                Thread.sleep((long) (Math.random() * 2000 + 1000));

                Platform.runLater(() -> {
                    String[] respuestas = {
                            "Entendido, gracias por la información.",
                            "¿Podrías proporcionar más detalles sobre esto?",
                            "Veré esto lo antes posible.",
                            "Gracias por tu mensaje.",
                            "Lo revisaré y te contesto pronto."
                    };

                    String respuesta = respuestas[(int)(Math.random() * respuestas.length)];
                    Mensaje respuestaMensaje = new Mensaje(respuesta, LocalDateTime.now(), false);
                    conversacionSeleccionada.getMensajes().add(respuestaMensaje);
                    agregarMensajeAVista(respuestaMensaje);

                    scrollPaneMensajes.setVvalue(1.0);
                });

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    @FXML
    void handleNuevaConversacion(ActionEvent event) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Nueva Conversación");
        dialog.setHeaderText("Crear nueva conversación");
        dialog.setContentText("Nombre del contacto:");

        dialog.showAndWait().ifPresent(nombre -> {
            if (!nombre.trim().isEmpty()) {
                Conversacion nuevaConversacion = new Conversacion(nombre.trim(), new ArrayList<>());
                conversaciones.add(0, nuevaConversacion);
                filtrarConversaciones(buscarConversacionField.getText());

                // Seleccionar la nueva conversación
                listaConversacionesView.getSelectionModel().select(
                        conversacionesFiltradas.indexOf(nuevaConversacion)
                );
            }
        });
    }

    @FXML
    void handleVolverHome(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Frontend/vistas/home.fxml"));
            Parent root = loader.load();

            HomeController controller = loader.getController();
            controller.setPersistenceAPI(api);

            Stage stage = (Stage) btnVolverHomeMensajes.getScene().getWindow();
            stage.setTitle("Home - GPPS");
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta("Error", "No se pudo cargar la pantalla de inicio: " + e.getMessage(), Alert.AlertType.ERROR);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void setPersistenceAPI(API api) {
        this.api = api;
    }

    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    // Clases internas para manejar los datos

    public static class Conversacion {
        private String nombreContacto;
        private List<Mensaje> mensajes;
        private String id; // Para referencias en la API

        public Conversacion(String nombreContacto, List<Mensaje> mensajes) {
            this.nombreContacto = nombreContacto;
            this.mensajes = mensajes;
            this.id = java.util.UUID.randomUUID().toString(); // Generar ID único
        }

        public String getNombreContacto() {
            return nombreContacto;
        }

        public void setNombreContacto(String nombreContacto) {
            this.nombreContacto = nombreContacto;
        }

        public List<Mensaje> getMensajes() {
            return mensajes;
        }

        public String getId() {
            return id;
        }
    }

    public static class Mensaje {
        private String contenido;
        private LocalDateTime fechaHora;
        private boolean enviadoPorUsuario;

        public Mensaje(String contenido, LocalDateTime fechaHora, boolean enviadoPorUsuario) {
            this.contenido = contenido;
            this.fechaHora = fechaHora;
            this.enviadoPorUsuario = enviadoPorUsuario;
        }

        public String getContenido() {
            return contenido;
        }

        public LocalDateTime getFechaHora() {
            return fechaHora;
        }

        public boolean isEnviadoPorUsuario() {
            return enviadoPorUsuario;
        }
    }
}