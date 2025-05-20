package Frontend.com.gui.Controller;

import Backend.API.API;
import Backend.DTO.ProyectoDTO;
import Backend.DTO.TutorExternoDTO; // Asumiendo que tienes este DTO
import Backend.DTO.ItemDTO;

import Frontend.com.gui.Controller.HomeController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


import java.io.IOException;

public class PropuestaPropiaController {

    @FXML
    private TextField tituloField;

    @FXML
    private TextArea descripcionArea;

    @FXML
    private TextField areaInteresField;

    @FXML
    private TextField ubicacionField;

    @FXML
    private TextArea objetivosArea;

    @FXML
    private TextArea requisitosArea;

    @FXML
    private TextField tutorNombreField; // Campo para el nombre del tutor

    @FXML
    private TextField tutorEmailField; // Campo para el email del tutor (ejemplo)
    private API api;

    @FXML
    private Button btnRegistrarPropuesta;

    @FXML
    public void initialize() {
        // Puedes configurar listeners o valores iniciales aquí si es necesario
        // Ejemplo: anadir listeners para validacion en tiempo real
    }

    @FXML
    private void handleRegistrarPropuesta() {
        String titulo = tituloField.getText();
        String descripcion = descripcionArea.getText();
        String areaDeInteres = areaInteresField.getText();
        String ubicacion = ubicacionField.getText();
        String objetivos = objetivosArea.getText();
        String requisitos = requisitosArea.getText();
        String nombreTutor = tutorNombreField.getText();
        String emailTutor = tutorEmailField.getText(); // Suponiendo que TutorExternoDTO tiene email

        // Validación básica (puedes expandirla)
        if (titulo.isEmpty() || descripcion.isEmpty() || areaDeInteres.isEmpty() ||
                ubicacion.isEmpty() || objetivos.isEmpty() || requisitos.isEmpty() ||
                nombreTutor.isEmpty()) {
            mostrarAlerta("Error de Validación", "Todos los campos son obligatorios.", Alert.AlertType.ERROR);
            return;
        }

        // Crear el TutorExternoDTO.
        // Necesitarás ajustar esto según la definición real de TutorExternoDTO.
        // Por ejemplo, si TutorExternoDTO solo tiene un campo 'nombre' o si necesita más datos.
        // Aquí asumo un constructor simple o setters.
        TutorExternoDTO tutorEncargado = new TutorExternoDTO();
        // Suponiendo que TutorExternoDTO tiene un método setNombre() y setContacto() o similar
        // Esto es un EJEMPLO, DEBES ADAPTARLO a tu TutorExternoDTO real:
        // tutorEncargado.setNombreCompleto(nombreTutor);
        // tutorEncargado.setEmail(emailTutor);
        // Si TutorExternoDTO tiene un constructor como: public TutorExternoDTO(String nombre, String email)
        // tutorEncargado = new TutorExternoDTO(nombreTutor, emailTutor);

        // Para este ejemplo, si TutorExternoDTO es simple y tiene un constructor que toma el nombre:
        // (Debes tener la clase TutorExternoDTO definida con un constructor o setters adecuados)
        // Ejemplo hipotético de TutorExternoDTO:
        // package Backend.DTO;
        // public class TutorExternoDTO {
        //     private String nombre;
        //     private String email;
        //     // Constructores, getters, setters
        //     public TutorExternoDTO() {}
        //     public TutorExternoDTO(String nombre, String email) { this.nombre = nombre; this.email = email; }
        //     public String getNombre() { return nombre; }
        //     public void setNombre(String nombre) { this.nombre = nombre; }
        //     public String getEmail() { return email; }
        //     public void setEmail(String email) { this.email = email; }
        // }
        // Usando el constructor hipotético:
        tutorEncargado = new TutorExternoDTO(); // O new TutorExternoDTO(nombreTutor, emailTutor);
        // Si TutorExternoDTO tiene campos nombre y email
        // y un constructor que los acepte, o setters.
        // Por ahora, lo dejo vacío y deberás completarlo
        // según tu definición de TutorExternoDTO.
        // Por ejemplo:
        // tutorEncargado.setAlgunAtributo(nombreTutor);
        // tutorEncargado.setOtroAtributo(emailTutor);


        // Crear el ProyectoDTO
        ProyectoDTO nuevaPropuesta = new ProyectoDTO(
                titulo,
                descripcion,
                areaDeInteres,
                ubicacion,
                objetivos,
                requisitos,
                tutorEncargado // Pasar el objeto TutorExternoDTO creado
        );

        // Aquí iría la lógica para enviar `nuevaPropuesta` al backend
        // Ejemplo: servicioDeProyectos.registrar(nuevaPropuesta);

        System.out.println("Propuesta a registrar:");
        System.out.println("Título: " + nuevaPropuesta.getTitulo());
        System.out.println("Descripción: " + nuevaPropuesta.getDescripcion());
        System.out.println("Área de Interés: " + nuevaPropuesta.getAreaDeInteres());
        System.out.println("Ubicación: " + nuevaPropuesta.getUbicacion());
        System.out.println("Objetivos: " + nuevaPropuesta.getObjetivos());
        System.out.println("Requisitos: " + nuevaPropuesta.getRequisitos());
        if (nuevaPropuesta.getTutorEncargado() != null) {
            // System.out.println("Tutor Encargado: " + nuevaPropuesta.getTutorEncargado().getNombre()); // Si TutorExternoDTO tiene getNombre()
            System.out.println("Tutor Encargado (objeto): " + nuevaPropuesta.getTutorEncargado().toString());
        } else {
            System.out.println("Tutor Encargado: No especificado o error al crear.");
        }


        mostrarAlerta("Registro Exitoso", "La propuesta ha sido registrada (simulado).", Alert.AlertType.INFORMATION);

        // Opcional: Limpiar los campos después del registro
        limpiarCampos();
    }

    private void limpiarCampos() {
        tituloField.clear();
        descripcionArea.clear();
        areaInteresField.clear();
        ubicacionField.clear();
        objetivosArea.clear();
        requisitosArea.clear();
        tutorNombreField.clear();
        tutorEmailField.clear();
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

}