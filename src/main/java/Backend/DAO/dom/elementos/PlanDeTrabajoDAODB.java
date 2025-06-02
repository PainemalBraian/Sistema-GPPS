package Backend.DAO.dom.elementos;

import Backend.DAO.interfaces.elementos.PLANDETRABAJODAO;
import Backend.Entidades.PlanDeTrabajo;
import Backend.Exceptions.*;

import java.util.List;

public class PlanDeTrabajoDAODB implements PLANDETRABAJODAO {


    @Override
    public void create(PlanDeTrabajo plan) throws CreateException {

    }

    @Override
    public void delete(int id) throws DeleteException {

    }

    @Override
    public PlanDeTrabajo buscarByID(int id) throws ReadException {
        return null;
    }

    @Override
    public List<PlanDeTrabajo> obtenerPlanes() throws ReadException {
        return List.of();
    }

    @Override
    public boolean validarTituloUnico(String titulo) throws ReadException {
        return false;
    }

    @Override
    public PlanDeTrabajo buscarByTitulo(String titulo) throws ReadException {
        return null;
    }
}
