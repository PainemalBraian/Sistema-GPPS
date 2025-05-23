package Backend.Entidades;

import java.util.ArrayList;
import java.util.List;

public class PlanDeTrabajo {
    private Docente docente;
    private TutorExterno tutor;
    private List<Actividad> actividades = new ArrayList<>();
    // Agregar Lista de informes acá  ?
    private Informe informeFinal;
    private boolean habilitado = false; // Direccion y Entidad deben aprobarlo

}
