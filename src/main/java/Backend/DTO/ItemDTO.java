package Backend.DTO;

public class ItemDTO {
    private int id;
    private String titulo;
    private String descripcion;

    public ItemDTO() {
    }

    public ItemDTO(int id, String titulo, String descripcion) {
        this.setId(id);
        this.setTitulo(titulo);
        this.setDescripcion(descripcion);
    }
    public ItemDTO(String titulo, String descripcion) {
        this.titulo = titulo;
        this.descripcion = descripcion;
    }

    public ItemDTO(int id) {
        this.setId(id);
    }

    public int getID() {
        return id;
    }

    public void setId(int id) {
        this.id = id;}

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

}

