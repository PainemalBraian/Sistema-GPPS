package Backend.Entidades;

import Backend.Exceptions.UserExceptions;

public class Usuario {
    private int idUsuario;
    private String username;
    private String contrasena;
    private String nombre;
    private String email;
    private boolean activo;
    private int dni;
    private Rol rol;

//    private String matricula;
//    private String carrera;

    private String Legajo;

    private String nombreEntidad;
    private String cuit;
    private String direccionEntidad;


    public Usuario() {}

    public Usuario(String username, String contrasena, String nombre, String email, Rol rol) throws UserExceptions {

        if(isNull(username)){
            throw new UserExceptions("El username no puede estar vacio");
        }
        if(isNull(email)){
            throw new UserExceptions("El email no puede estar vacio");
        }
        if(isNull(contrasena)){
            throw new UserExceptions("La contraseña no puede estar vacía");
        }

        if (isNull(contrasena)  || contrasena.length() < 8) {
            throw new UserExceptions("Error: La contraseña debe tener al menos 8 caracteres");
        }
        if(isNull(nombre)){
            throw new UserExceptions("Debe completar el nombre");
        }

        this.username = username;
        this.contrasena = contrasena;
        this.nombre = nombre;
        this.email = email;
        this.rol = rol;
    }

    public Usuario(int id, String username, String contrasena, String nombre, String email, Rol rol) throws UserExceptions {

        if(isNull(username)){
            throw new UserExceptions("El username no puede estar vacio");
        }
        if(isNull(email)){
            throw new UserExceptions("El email no puede estar vacio");
        }
        if(isNull(contrasena)){
            throw new UserExceptions("La contraseña no puede estar vacia");
        }
        if(isNull(nombre)){
            throw new UserExceptions("Debe completar el nombre");
        }

        this.idUsuario = id;
        this.username = username;
        this.contrasena = contrasena;
        this.nombre = nombre;
        this.email = email;
        this.rol = rol;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

//    public void setMatricula(int matricula) {
//        this.matricula = matricula;
//    }
//
//    public String getMatricula() {
//        return matricula;
//    }

    public String getLegajo() {
        return Legajo;
    }

    public void setLegajo(String legajo) {
        Legajo = legajo;
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

//    public void setCarrera(String carrera) {
//        this.carrera = carrera;
//    }
//
//    public String getCarrera() {
//        return carrera;
//    }

    public boolean isActivo() {
        return activo;
    }

    public String obtenerEstado() {
        return isActivo() ? "ACTIVO" : "INACTIVO";
    }

    public void activar() {
        if (!isActivo())
            this.activo = true;
    }

    public void desactivar() {
        if (isActivo())
            this.activo = false;
    }

    public void setActivo(boolean activo){
        this.activo = 	activo;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((username == null) ? 0 : username.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Usuario other = (Usuario) obj;
        if (username == null) {
            if (other.username != null)
                return false;
        } else if (!username.equals(other.username))
            return false;
        return true;
    }

    private boolean isNull(String dato) {
        return dato == null || dato.trim().isEmpty();
    }

    public int getDni() {
        return dni;
    }

    public void setDni(int dni) {
        this.dni = dni;
    }
}


