package Backend.DTO;

public class DirectorCarreraDTO extends UsuarioDTO{
    public DirectorCarreraDTO(UsuarioDTO user) {
        super(user.getIdUsuario(),user.getUsername(), user.getPassword(), user.getNombre(), user.getEmail(), user.getRol(), user.isActivo());
        super.setActivo(user.isActivo());
    }
    // Este tipo de usuario no tiene datos adicionales, (usa los de herencia)
}
