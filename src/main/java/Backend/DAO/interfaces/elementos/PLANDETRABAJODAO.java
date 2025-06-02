package Backend.DAO.interfaces.elementos;

import Backend.Entidades.PlanDeTrabajo;
import Backend.Exceptions.CreateException;
import Backend.Exceptions.DeleteException;
import Backend.Exceptions.ReadException;

import java.util.List;

public interface PLANDETRABAJODAO {

    void create(PlanDeTrabajo plan) throws CreateException;

    void delete(int id) throws DeleteException;

    PlanDeTrabajo buscarByID(int id) throws ReadException;

    List<PlanDeTrabajo> obtenerPlanes() throws ReadException;

    boolean validarTituloUnico(String titulo) throws ReadException;

    PlanDeTrabajo buscarByTitulo(String titulo) throws ReadException;

}
