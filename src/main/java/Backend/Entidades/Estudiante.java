package Backend.Entidades;

import Backend.Exceptions.UserExceptions;

public class Estudiante extends Usuario{
    private int legajo;

    public Estudiante() {
    }

    public Estudiante(Usuario user, int legajo) throws UserExceptions {
        super(user.getUsername(), user.getContrasena(), user.getNombre(), user.getEmail(), user.getRol());
        // Verificar la cantidad de dígitos
        int cantidadDigitos = String.valueOf(Math.abs(legajo)).length();
        if (cantidadDigitos != 6) {
            throw new UserExceptions("El legajo debe contener 6 dígitos.");
        }
        this.legajo = legajo;
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
