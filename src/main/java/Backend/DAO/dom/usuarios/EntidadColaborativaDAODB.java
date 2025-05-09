package Backend.DAO.dom.usuarios;

import Backend.DAO.DBAcces;
import Backend.DAO.UsuarioDAODB;
import Backend.DAO.interfaces.usuarios.ENTIDADCOLABORATIVADAO;

public class EntidadColaborativaDAODB extends DBAcces implements ENTIDADCOLABORATIVADAO {
    UsuarioDAODB UsuarioDAODB=new UsuarioDAODB();
}
