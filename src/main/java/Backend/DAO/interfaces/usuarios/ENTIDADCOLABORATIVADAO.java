package Backend.DAO.interfaces.usuarios;

import Backend.Entidades.EntidadColaborativa;
import Backend.Exceptions.RegisterExceptions;
import Backend.Exceptions.UserException;

import java.util.List;

public interface ENTIDADCOLABORATIVADAO {
    void create(EntidadColaborativa entidad) throws RegisterExceptions;

    List<EntidadColaborativa> obtenerEntidades() throws  UserException;

    EntidadColaborativa buscarByUsername(String username) throws  UserException;

    EntidadColaborativa buscarById(int id) throws  UserException;

    boolean validarDatosUnicos(String nombreEntidad,String cuit,String direccionEntidad) throws  UserException;
}
