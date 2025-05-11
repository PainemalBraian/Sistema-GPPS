package Backend.DTO;

import java.util.ArrayList;
import java.util.List;

public class TutorExternoDTO extends UsuarioDTO{
    private String nombreEntidadColaborativa;
    private List<ProyectoDTO> proyectosAsignados = new ArrayList<>();

    public TutorExternoDTO() {
    }

    public TutorExternoDTO(UsuarioDTO user, String nombreEntidadColaborativa) {
        super(user.getUsername(), user.getPassword(), user.getNombre(), user.getEmail(), user.getRol(), user.isActivo());
        this.nombreEntidadColaborativa = nombreEntidadColaborativa;
    }

    public TutorExternoDTO(UsuarioDTO user, String nombreEntidadColaborativa,List<ProyectoDTO> proyectosAsignados) {
        super(user.getUsername(), user.getPassword(), user.getNombre(), user.getEmail(), user.getRol(), user.isActivo());
        this.nombreEntidadColaborativa = nombreEntidadColaborativa;
        this.proyectosAsignados = proyectosAsignados;
    }

    public String getNombreEntidadColaborativa() {
        return nombreEntidadColaborativa;
    }

    public void setNombreEntidadColaborativa(String nombreEntidadColaborativa) {
        this.nombreEntidadColaborativa = nombreEntidadColaborativa;
    }

    public List<ProyectoDTO> getProyectosAsignados() {
        return proyectosAsignados;
    }

    public void setProyectosAsignados(List<ProyectoDTO> proyectosAsignados) {
        this.proyectosAsignados = proyectosAsignados;
    }
}
