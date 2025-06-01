package Frontend.com.gui.Controller.EntidadColaboradora; // Paquete ajustado

import Backend.API.API;
import Frontend.com.gui.Controller.IngresoController;
import Frontend.com.gui.Controller.MensajesController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class homeEmpresaController { // Nombre de clase cambiado

    private static final Logger LOGGER = Logger.getLogger(homeEmpresaController.class.getName());

    private API api;
    private ResourceBundle bundle;

    @FXML private Label lblBienvenida;
    @FXML private Label lblGestionProyectos; // Cambiado
    @FXML private Label lblConsultas;      // Cambiado
    @FXML private Label lblMensajes;

    // Botones del menú lateral
    @FXML private Button btnPublicarProyecto;       // Nuevo/Renombrado
    @FXML private Button btnGestionarProyectos;     // Nuevo
    @FXML private Button btnConsultarConvenios;     // Nuevo
    @FXML private Button btnConsultarEstudiantes;   // Nuevo/Renombrado
    @FXML private Button btnMensajes;
    @FXML private Button btnCerrarSesion;
    // Podrías mantener un botón para "Actualizar Datos de Entidad" si es relevante
    @FXML private Button btnActualizarDatosEntidad;

    // Labels del panel de resumen (opcional, se pueden adaptar más específicamente)
    @FXML private Label lblTituloProyectosPublicados;
    @FXML private Label lblTotalProyectos;
    @FXML private Label lblProyectosActivos;
    @FXML private Label lblProyectosConEstudiantes;

    @FXML private Label lblTituloConvenios;
    @FXML private Label lblConveniosActivos;
    @FXML private Label lblEstudiantesAsignadosTotal;


    public void setPersistenceAPI(API persistenceAPI) throws Exception {
        this.api = persistenceAPI;
        // actualizarIdioma();
        // Aquí podrías cargar datos iniciales para los paneles de resumen
        //cargarDatosResumen();
    }

    private void actualizarIdioma() {
        if (api == null) {
            LOGGER.log(Level.WARNING, "API no inicializada al intentar actualizar idioma.");
            // Cargar un bundle por defecto o manejar el error
            // bundle = ResourceBundle.getBundle("Frontend.com.gui.Idioma.messages"); // Ejemplo
            return;
        }
        bundle = api.obtenerIdioma();
        if (bundle == null) {
            LOGGER.log(Level.SEVERE, "No se pudo cargar el ResourceBundle.");
            // Manejar el caso en que el bundle no se pueda cargar
            return;
        }


        lblBienvenida.setText(bundle.getString("label.bienvenida.colaborador")); // Ajustado
        // lblGestionProyectos.setText(bundle.getString("label.gestionProyectos")); // Si tienes etiquetas agrupadoras
        // lblConsultas.setText(bundle.getString("label.consultas")); // Si tienes etiquetas agrupadoras
        lblMensajes.setText(bundle.getString("label.mensajes"));

        btnPublicarProyecto.setText(bundle.getString("button.publicarProyecto"));
        btnGestionarProyectos.setText(bundle.getString("button.gestionarProyectos"));
        btnConsultarConvenios.setText(bundle.getString("button.consultarConvenios"));
        btnConsultarEstudiantes.setText(bundle.getString("button.consultarEstudiantesAsignados"));
        btnMensajes.setText(bundle.getString("button.mensajes"));
        btnCerrarSesion.setText(bundle.getString("button.cerrarSesion"));
        if (btnActualizarDatosEntidad != null) { // Si decides mantenerlo
            btnActualizarDatosEntidad.setText(bundle.getString("button.actualizarDatosEntidad"));
        }

        // Actualizar textos de los paneles de resumen
        if (lblTituloProyectosPublicados != null) lblTituloProyectosPublicados.setText(bundle.getString("panel.titulo.proyectosPublicados"));
        if (lblTituloConvenios != null) lblTituloConvenios.setText(bundle.getString("panel.titulo.convenios"));
        // ... y así para las demás etiquetas de los paneles
    }

    private void cargarDatosResumen() {
        // Lógica para cargar datos desde la API y actualizar los Labels del dashboard
        // Ejemplo:
        // if (lblTotalProyectos != null) lblTotalProyectos.setText(String.valueOf(api.colaborador_getTotalProyectosPublicados()));
        // if (lblProyectosActivos != null) lblProyectosActivos.setText("Activos: " + api.colaborador_getTotalProyectosActivos());
        // if (lblConveniosActivos != null) lblConveniosActivos.setText("Activos: " + api.colaborador_getTotalConveniosActivos());
        // if (lblEstudiantesAsignadosTotal != null) lblEstudiantesAsignadosTotal.setText("Total: " + api.colaborador_getTotalEstudiantesAsignados());
        LOGGER.info("Cargando datos de resumen para el colaborador.");
    }

    @FXML
    public void publicarProyecto(ActionEvent event) {
        // Navega a la pantalla para publicar un nuevo proyecto.
        // US: Como entidad colaboradora, quiero publicar proyectos...
        navegar("/Frontend/vistas/Colaborador/publicarProyecto.fxml", bundle.getString("title.publicarProyecto"), event);
    }

    @FXML
    public void gestionarProyectos(ActionEvent event) {
        // Navega a la pantalla para editar, actualizar o dar de baja proyectos.
        // US: Como entidad colaboradora, quiero editar, actualizar o dar de baja los proyectos publicados...
        navegar("/Frontend/vistas/Colaborador/gestionarProyectos.fxml", bundle.getString("title.gestionarProyectos"), event);
    }

    @FXML
    public void consultarConvenios(ActionEvent event) {
        // Navega a la pantalla para ver información sobre convenios.
        // US: Como entidad colaboradora, quiero consultar información sobre convenios...
        navegar("/Frontend/vistas/Colaborador/consultarConvenios.fxml", bundle.getString("title.consultarConvenios"), event);
    }

    @FXML
    public void consultarEstudiantesAsignados(ActionEvent event) {
        // Navega a la pantalla para ver estudiantes asignados a los proyectos de la entidad.
        // US: Como entidad colaboradora, quiero consultar información sobre ... estudiantes asignados
        navegar("/Frontend/vistas/Colaborador/consultarEstudiantes.fxml", bundle.getString("title.consultarEstudiantesAsignados"), event);
    }

    @FXML
    public void actualizarDatosEntidad(ActionEvent event) {
        // Similar a actualizarEmpresa, pero para la entidad colaboradora
        navegar("/Frontend/vistas/Colaborador/actualizarDatosEntidad.fxml", bundle.getString("title.actualizarDatosEntidad"), event);
    }


    @FXML
    public void verMensajes(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Frontend/vistas/mensajes.fxml"));
            Parent root = loader.load();

            MensajesController controller = loader.getController();
            controller.setPersistenceAPI(api); // Asumiendo que MensajesController necesita la API

            Stage stage = new Stage();
            stage.setTitle(bundle.getString("title.mensajes"));
            stage.setScene(new Scene(root));
            stage.show();

            // No cierres la ventana actual si quieres que el home permanezca abierto
            // ((Stage) ((Node) event.getSource()).getScene().getWindow()).close();

        } catch (Exception e) { // Captura Exception más general por si setPersistenceAPI lanza algo
            LOGGER.log(Level.SEVERE, "Error al abrir mensajes", e);
            mostrarAlerta(bundle.getString("alert.error.titulo"), bundle.getString("alert.error.abrirMensajes"));
        }
    }

    @FXML
    public void cerrarSesion(ActionEvent event) {
        try {
            // Volver a la pantalla de login
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Frontend/vistas/Ingreso.fxml"));
            Parent root = loader.load();

            // Obtener el controlador y pasarle la API
            IngresoController controller = loader.getController();
            controller.setPersistenceAPI(api);

            // Mostrar la ventana de login
            Stage stage = new Stage();
            stage.setTitle("Login - GPPS");
            stage.setScene(new Scene(root));
            stage.show();

            // Cerrar la ventana actual
            ((Stage) ((Button) event.getSource()).getScene().getWindow()).close();

        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, "Error al cerrar sesión", ex);
            mostrarAlerta("Error", "No se pudo cerrar la sesión");
        }
    }

    private void navegar(String rutaFXML, String tituloVentana, ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(rutaFXML), bundle); // Pasar bundle al loader
            Parent root = loader.load();

            // Si el controlador destino necesita la API, puedes obtenerlo y pasarlo:
            // Object controller = loader.getController();
            // if (controller instanceof TuControladorDestino) {
            //    ((TuControladorDestino) controller).setPersistenceAPI(api);
            // }


            Stage stage = new Stage();
            stage.setTitle(tituloVentana);
            stage.setScene(new Scene(root));
            stage.show();

            // Opcional: cerrar la ventana actual si es un flujo de pantalla única
            // ((Stage) ((Node) event.getSource()).getScene().getWindow()).close();

        } catch (Exception e) { // Capturar Exception por si setPersistenceAPI u otro método en el controller destino falla
            LOGGER.log(Level.SEVERE, "Error al navegar a " + tituloVentana, e);
            mostrarAlerta(bundle.getString("alert.error.titulo"), bundle.getString("alert.error.navegacion") + ": " + tituloVentana);
        }
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}