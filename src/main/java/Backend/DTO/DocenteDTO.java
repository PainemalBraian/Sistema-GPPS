package Backend.DTO;

import java.util.ArrayList;
import java.util.List;

public class DocenteDTO extends UsuarioDTO{
    private String legajo;
    private List<EstudianteDTO> estudiantesAsignados = new ArrayList<>();

    public DocenteDTO() {}

    public DocenteDTO(UsuarioDTO user, String legajo) {
        super(user.getUsername(), user.getPassword(), user.getNombre(), user.getEmail(), user.getRol(),user.isActivo());
        this.legajo = legajo;
    }

    public DocenteDTO(UsuarioDTO user, String legajo, List<EstudianteDTO> estudiantesAsignados) {
        super(user.getUsername(), user.getPassword(), user.getNombre(), user.getEmail(), user.getRol(),user.isActivo());
        this.legajo = legajo;
        this.estudiantesAsignados = estudiantesAsignados;
    }

    public String getLegajo() {
        return legajo;
    }

    public List<EstudianteDTO> getEstudiantesAsignados() {
        return estudiantesAsignados;
    }

    public void setLegajo(String legajo) {
        this.legajo = legajo;
    }

    public void setEstudiantesAsignados(List<EstudianteDTO> estudiantesAsignados) {
        this.estudiantesAsignados = estudiantesAsignados;
    }
}
