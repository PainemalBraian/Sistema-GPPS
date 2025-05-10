package Backend.Entidades;

import Backend.Exceptions.UserException;

import static java.util.Objects.isNull;

public class Usuario {
    private int idUsuario;
    private String username;
    private String contrasena;
    private String nombre;
    private String email;
    private boolean activo;
    private int dni;
    private Rol rol;

    public Usuario() {}

    public Usuario(String username, String contrasena, String nombre, String email, Rol rol) throws UserException {
        if(isNull(username)|| username.isEmpty()){
            throw new UserException("El username no puede estar vacio");
        }
        if(isNull(email)||email.isEmpty()){
            throw new UserException("El email no puede estar vacio");
        }
        if(isNull(contrasena)||contrasena.isEmpty()){
            throw new UserException("La contraseña no puede estar vacía");
        }
        if(isNull(nombre)||nombre.isEmpty()){
            throw new UserException("Debe completar el nombre");
        }
        if (contrasena.length() < 8) {
            throw new UserException("Error: La contraseña debe tener al menos 8 caracteres");
        }
        this.username = username;
        this.contrasena = contrasena;
        this.nombre = nombre;
        this.email = email;
        this.rol = rol;
        this.activo=true;
    }
    //Constructor para DAO
    public Usuario(int id, String username, String contrasena, String nombre, String email, Rol rol) throws UserException {
        if(isNull(username)|| username.isEmpty()){
            throw new UserException("El username no puede estar vacio");
        }
        if(isNull(email)||email.isEmpty()){
            throw new UserException("El email no puede estar vacio");
        }
        if(isNull(contrasena)||contrasena.isEmpty()){
            throw new UserException("La contraseña no puede estar vacía");
        }
        if(isNull(nombre)||nombre.isEmpty()){
            throw new UserException("Debe completar el nombre");
        }
        if (contrasena.length() < 8) {
            throw new UserException("Error: La contraseña debe tener al menos 8 caracteres");
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

    public int getDni() {
        return dni;
    }

    public void setDni(int dni) {
        this.dni = dni;
    }

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

}


