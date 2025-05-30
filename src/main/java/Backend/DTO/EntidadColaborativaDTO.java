package Backend.DTO;

import java.util.ArrayList;
import java.util.List;

public class EntidadColaborativaDTO extends UsuarioDTO{
    private List<ProyectoDTO> proyectos = new ArrayList();
    private String nombreEntidad;
    private String cuit;
    private String direccionEntidad;

    public EntidadColaborativaDTO(UsuarioDTO user, String nombreEntidad, String cuit, String direccionEntidad){
        super(user.getIdUsuario(),user.getUsername(), user.getPassword(), user.getNombre(), user.getEmail(), user.getRol(), user.isActivo());
        this.nombreEntidad = nombreEntidad;
        this.cuit = cuit;
        this.direccionEntidad = direccionEntidad;
    }
    public EntidadColaborativaDTO(UsuarioDTO user, String nombreEntidad, String cuit, String direccionEntidad,List<ProyectoDTO> proyectos){
        super(user.getIdUsuario(),user.getUsername(), user.getPassword(), user.getNombre(), user.getEmail(), user.getRol(), user.isActivo());
        this.nombreEntidad = nombreEntidad;
        this.cuit = cuit;
        this.direccionEntidad = direccionEntidad;
        this.proyectos = proyectos;
    }

    public List<ProyectoDTO> getProyectos() {
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

    public void setProyectos(List<ProyectoDTO> proyectos) {
        this.proyectos = proyectos;
    }

    public void setNombreEntidad(String nombreEntidad) {
        this.nombreEntidad = nombreEntidad;
    }

    public void setCuit(String cuit) {
        this.cuit = cuit;
    }

    public void setDireccionEntidad(String direccionEntidad) {
        this.direccionEntidad = direccionEntidad;
    }
}
