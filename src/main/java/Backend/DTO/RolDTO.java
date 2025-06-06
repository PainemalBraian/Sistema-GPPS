package Backend.DTO;

public class RolDTO {

    private int id;
    private String nombre;
    private boolean activo;

    public RolDTO() {
    }

    public RolDTO(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public RolDTO(int id, String nombre, boolean activo) {
        this.id = id;
        this.nombre = nombre;
        this.activo = activo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    @Override
    public String toString() {
        return nombre;
    }

}
