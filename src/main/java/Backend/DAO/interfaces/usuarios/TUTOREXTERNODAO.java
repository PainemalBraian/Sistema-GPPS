package Backend.DAO.interfaces.usuarios;

import Backend.Exceptions.RegisterExceptions;

public interface TUTOREXTERNODAO {
    void create(Object tutor) throws RegisterExceptions;
}
