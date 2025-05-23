package Backend.DTO;

public class EstudianteDTO extends UsuarioDTO{
    private String matricula;
    private String carrera;

    public EstudianteDTO(UsuarioDTO user, String matricula, String carrera){
        super(user.getUsername(), user.getPassword(), user.getNombre(), user.getEmail(), user.getRol(), user.isActivo());
        this.matricula = matricula;
        this.carrera = carrera;
    }

    public String getCarrera() {
        return carrera;
    }

    public void setCarrera(String carrera) {
        this.carrera = carrera;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

}
