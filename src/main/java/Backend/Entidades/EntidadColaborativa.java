package Backend.Entidades;

import Backend.Exceptions.UserException;

import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.isNull;

public class EntidadColaborativa extends Usuario{
    private List<Proyecto> proyectos = new ArrayList();
    private String nombreEntidad;
    private String cuit;
    private String direccionEntidad;

    public EntidadColaborativa() {}

    public EntidadColaborativa(Usuario user,String nombreEntidad,String cuit,String direccionEntidad) throws UserException {
        super(user.getIdUsuario(),user.getUsername(), user.getContrasena(), user.getNombre(), user.getEmail(), user.getRol());
        if (isNull(nombreEntidad) || nombreEntidad.isEmpty()) {
            throw new UserException("El nombre de la Entidad Colabodora debe existir.");
        }
        if (isNull(cuit) || cuit.isEmpty()) {
            throw new UserException("El CUIT debe existir.");
        }
        if (isNull(direccionEntidad) || direccionEntidad.isEmpty()) {
            throw new UserException("La direccion de la Entidad Colabodora debe existir.");
        }
        if (cuit.length()!=11) {
            throw new UserException("El CUIT debe contener 11 digitos.");
        }
        this.nombreEntidad = nombreEntidad;
        this.cuit = cuit;
        this.direccionEntidad = direccionEntidad;
    }

//////////////////////////// METHODS ///////////////////////////////
    public void cargarProyecto(Proyecto proyecto){
    }
    public void borrarProyecto(Proyecto proyecto){
    }

//////////////////////// GETTERS ///////////////////////////////////////////////
    public List<Proyecto> getProyectos() {
        return proyectos;
    }

    public String getNombreEntidad() {
        return nombreEntidad;
    }

    public String getCuit() {
        return cuit;
    }

    public String getDireccionEntidad() {
        return direccionEntidad;
    }

//////////////////////// SETTERS ///////////////////////////////////////////////
    public void setDireccionEntidad(String direccionEntidad) {
    this.direccionEntidad = direccionEntidad;
}

    public void setProyectos(List<Proyecto> proyectos) {
        this.proyectos = proyectos;
    }

    public void setNombreEntidad(String nombreEntidad) {
        this.nombreEntidad = nombreEntidad;
    }

    public void setCuit(String cuit) {
        this.cuit = cuit;
    }


}
