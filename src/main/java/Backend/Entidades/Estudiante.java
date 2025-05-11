package Backend.Entidades;

import Backend.Exceptions.UserException;

import static java.util.Objects.isNull;

public class Estudiante extends Usuario{
    private String matricula;
    private String carrera;

    public Estudiante() {
    }

    public Estudiante(Usuario user, String matricula, String carrera) throws UserException {
        super(user.getUsername(), user.getContrasena(), user.getNombre(), user.getEmail(), user.getRol());
        // Verificar la cantidad de dígitos

        if ( isNull(matricula)) {
            throw new UserException("El campo Matrícula es obligatorio para estudiantes.");
        }
        int cantidadDigitos = matricula.length();
        if (cantidadDigitos != 6) {
            throw new UserException("El matricula debe contener 6 dígitos.");
        }
        if ( isNull(carrera) || carrera.isEmpty()) {
            throw new UserException("La carrera debe existir.");
        }
        this.matricula = matricula;
        this.carrera = carrera;
    }

    public String getCarrera() {
        return carrera;
    }

    public String getMatricula() {
        return matricula;
    }
    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public void setCarrera(String carrera) {
        this.carrera = carrera;
    }

    //metodos
    public void seleccionarProyecto(Proyecto proyecto){
    }
    public void proponerProyecto(Proyecto proyecto){
    }
    public void registrarActividad(){
    }
    public void cargarInforme(){
    }
//    public void verAvance(){
//    }
}
