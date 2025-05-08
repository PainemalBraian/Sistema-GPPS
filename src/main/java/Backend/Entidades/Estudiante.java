package Backend.Entidades;

import Backend.Exceptions.UserExceptions;

import static java.util.Objects.isNull;

public class Estudiante extends Usuario{
    private int legajo;
    private String carrera;

    public Estudiante() {
    }

    public Estudiante(Usuario user, int legajo,String carrera) throws UserExceptions {
        super(user.getUsername(), user.getContrasena(), user.getNombre(), user.getEmail(), user.getRol());
        // Verificar la cantidad de dígitos
        int cantidadDigitos = String.valueOf(Math.abs(legajo)).length();
        if (cantidadDigitos != 6) {
            throw new UserExceptions("El legajo debe contener 6 dígitos.");
        }
        if ( isNull(carrera) || carrera.isEmpty()) {
            throw new UserExceptions("La carrera debe existir.");
        }
        this.legajo = legajo;
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
