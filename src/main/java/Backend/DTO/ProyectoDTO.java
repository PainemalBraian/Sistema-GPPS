package Backend.DTO;

public class ProyectoDTO extends ItemDTO {
    private String areaDeInteres;
    private String ubicacion;
    private String objetivos;
    private String requisitos;
    private TutorExternoDTO tutorEncargado;
    private boolean habilitado = false;


    public ProyectoDTO(int id, String titulo, String descripcion, String areaDeInteres, String ubicacion, String objetivos, String requisitos, TutorExternoDTO tutorEncargado) {
        super(id, titulo, descripcion);
        this.areaDeInteres = areaDeInteres;
        this.ubicacion = ubicacion;
        this.objetivos = objetivos;
        this.requisitos = requisitos;
        this.tutorEncargado = tutorEncargado;
    }
    public ProyectoDTO(String titulo, String descripcion, String areaDeInteres, String ubicacion, String objetivos, String requisitos, TutorExternoDTO tutorEncargado) {
        super(titulo, descripcion);
        this.areaDeInteres = areaDeInteres;
        this.ubicacion = ubicacion;
        this.objetivos = objetivos;
        this.requisitos = requisitos;
        this.tutorEncargado = tutorEncargado;
    }

    public ProyectoDTO() {

    }

    public String getAreaDeInteres() {
        return areaDeInteres;
    }

    public void setAreaDeInteres(String areaDeInteres) {
        this.areaDeInteres = areaDeInteres;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public String getObjetivos() {
        return objetivos;
    }

    public void setObjetivos(String objetivos) {
        this.objetivos = objetivos;
    }

    public String getRequisitos() {
        return requisitos;
    }

    public void setRequisitos(String requisitos) {
        this.requisitos = requisitos;
    }

    public TutorExternoDTO getTutorEncargado() {
        return tutorEncargado;
    }

    public void setTutorEncargado(TutorExternoDTO tutorEncargado) {
        this.tutorEncargado = tutorEncargado;
    }

    public boolean isHabilitado() {
        return habilitado;
    }

    public void setHabilitado(boolean habilitado) {
        this.habilitado = habilitado;
    }
}
