package Backend.DAO.interfaces.elementos;

import Backend.Entidades.Informe;
import Backend.Exceptions.CreateException;
import Backend.Exceptions.DeleteException;
import Backend.Exceptions.ReadException;

import java.util.List;

public interface INFORMEDAO {
    void create(Informe informe) throws CreateException;

    void delete(int id) throws DeleteException;

    Informe buscarByID(int id) throws ReadException;

    List<Informe> obtenerInformes() throws ReadException;

    boolean validarTituloUnico(String titulo) throws ReadException;

    Informe buscarByTitulo(String titulo) throws ReadException;
}
