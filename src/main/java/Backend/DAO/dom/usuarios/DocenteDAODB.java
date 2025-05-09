package Backend.DAO.dom.usuarios;

import Backend.DAO.DBAcces;
import Backend.DAO.UsuarioDAODB;
import Backend.DAO.interfaces.usuarios.DOCENTEDAO;

public class DocenteDAODB extends DBAcces implements DOCENTEDAO {
    UsuarioDAODB UsuarioDAODB=new UsuarioDAODB();
}
