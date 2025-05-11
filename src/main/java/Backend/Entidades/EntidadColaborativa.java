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
        super(user.getUsername(), user.getContrasena(), user.getNombre(), user.getEmail(), user.getRol());
        if (isNull(nombreEntidad) || nombreEntidad.isEmpty()) {
            throw new UserException("El nombre de la Entidad Colabodora debe existir.");
        }
        if (isNull(cuit) || cuit.isEmpty()) {
            throw new UserException("El CUIT debe existir.");
        }
<<<<<<< HEAD
        if (isNull(direccionEntidad) || direccionEntidad.isEmpty()) {
            throw new UserException("La direccion de la Entidad Colabodora debe existir.");
        }
        if (cuit.length()!=11) {
            throw new UserException("El CUIT debe contener 11 digitos.");
        }
=======
        if (cuit.length()!=11) {
            throw new UserException("El CUIT debe contener 11 digitos.");
        }
        if (isNull(direccionEntidad) || direccionEntidad.isEmpty()) {
            throw new UserException("La direccion de la Entidad Colabodora debe existir.");
        }

>>>>>>> b23489114d35815ce1fb0bf2fa6b3a671ca73759
        this.nombreEntidad = nombreEntidad;
        this.cuit = cuit;
        this.direccionEntidad = direccionEntidad;
    }

    public List<Proyecto> getProyectos() {
        return proyectos;
    }

    public void setProyectos(List<Proyecto> proyectos) {
        this.proyectos = proyectos;
    }

    public String getNombreEntidad() {
        return nombreEntidad;
    }

    public void setNombreEntidad(String nombreEntidad) {
        this.nombreEntidad = nombreEntidad;
    }

    public String getCuit() {
        return cuit;
    }

    public void setCuit(String cuit) {
        this.cuit = cuit;
    }

    public String getDireccionEntidad() {
        return direccionEntidad;
    }

    public void setDireccionEntidad(String direccionEntidad) {
        this.direccionEntidad = direccionEntidad;
    }

    // metodos
    public void cargarProyecto(Proyecto proyecto){
    }
    public void borrarProyecto(Proyecto proyecto){
    }

}
