package Backend.DTO;

import java.util.ArrayList;
import java.util.List;

public class EntidadColaborativaDTO extends UsuarioDTO{
    private List<ProyectoDTO> proyectos = new ArrayList();
    private String nombreEntidad;
    private String cuit;
    private String direccionEntidad;

    public EntidadColaborativaDTO() {}

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
}
