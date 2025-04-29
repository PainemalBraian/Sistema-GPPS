package DTO;

public class ItemDTO {
    private int id;
    private String nombre;
    private String descripcion;

    public ItemDTO(int id, String nombre, String descripcion) {
        this.setId(id);
        this.setNombre(nombre);
        this.setDescripcion(descripcion);
    }
    public ItemDTO(String nombre, String descripcion) {
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    public ItemDTO(int id) {
        this.setId(id);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;}

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

}

