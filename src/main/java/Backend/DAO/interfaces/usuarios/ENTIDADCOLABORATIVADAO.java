package Backend.DAO.interfaces.usuarios;

import Backend.Entidades.EntidadColaborativa;
import Backend.Entidades.Usuario;
import Backend.Exceptions.RegisterExceptions;
import Backend.Exceptions.UserException;

import java.util.List;

public interface ENTIDADCOLABORATIVADAO {
    void create(EntidadColaborativa entidad) throws RegisterExceptions;

    List<Usuario> read() throws  UserException;

    Usuario findByUsername(String username) throws  UserException;

    Usuario findById(int id) throws  UserException;

    boolean validarDatosUnicos(String nombreEntidad,String cuit,String direccionEntidad) throws  UserException;
}
