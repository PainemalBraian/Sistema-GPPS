package Backend.Entidades;

import Backend.Exceptions.EmptyException;

import java.time.LocalDate;

import static java.util.Objects.isNull;

public class Informe extends Item{
    private byte[] archivoPDF; // Esto debe representar el PDF como binario
    private int porcentajeAvance = -1; // Campo a borrar en el futuro, no corresponder a la estructura del objeto, dejado para evitar errores de ultima instancia
    private LocalDate fecha = LocalDate.now();
    private String tituloActividad;
    private boolean aprobado;
    private int calificacionDocente;
    private int calificacionTutor;

    public Informe(int id, String titulo, String descripcion, byte[] archivo) throws EmptyException {
        super(id, titulo, descripcion);
//        if (isNull(archivo))
//            throw new EmptyException("El archivo del informe debe ser valido.");
        this.archivoPDF = archivo;
        fecha = LocalDate.now();
    }

    public Informe(String titulo, String descripcion, byte[] archivo) throws EmptyException {
        super(titulo, descripcion);
//        if (isNull(archivo))
//            throw new EmptyException("El archivo del informe debe ser valido.");
        this.archivoPDF = archivo;
        fecha = LocalDate.now();
    }

    public Informe() {
    }
///////////////////////// METHODS ///////////////////////////////

//////////////////////// GETTERS ///////////////////////////////////////////////

    public byte[] getArchivoPDF() {
        return archivoPDF;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public int getPorcentajeAvance() {
        return porcentajeAvance;
    }

    public String getTituloActividad() {
        return tituloActividad;
    }

    public boolean isAprobado() {
        return aprobado;
    }

    public int getCalificacionDocente() {
        return calificacionDocente;
    }

    public int getCalificacionTutor() {
        return calificacionTutor;
    }

    ///////////////////// SETTERS ///////////////////////////////////////////////
///
    public void setArchivoPDF(byte[] archivoPDF) throws EmptyException {
//    if (isNull(archivoPDF))
//            throw new EmptyException("El archivo del informe debe ser valido.");
        this.archivoPDF = archivoPDF;
    }

    public void setFecha(LocalDate fecha) throws EmptyException {
        if (isNull(fecha))
            throw new EmptyException("La fecha debe ser valida.");
        this.fecha = fecha;
    }

    public void setPorcentajeAvance(int porcentajeAvance) throws EmptyException {
     //   if (porcentajeAvance < 0 || porcentajeAvance > 100 && porcentajeAvance != -1)
       //     throw new EmptyException("El porcentaje debe ser valido y estar entre 0 - 100.");
        if (isNull(porcentajeAvance))
            porcentajeAvance = 0;
        this.porcentajeAvance = porcentajeAvance;
    }

    public void setTituloActividad(String tituloActividad) {
        this.tituloActividad = tituloActividad;
    }

    public void setAprobado(boolean aprobado) {
        this.aprobado = aprobado;
    }

    public void setCalificacionDocente(int calificacionDocente) {
        this.calificacionDocente = calificacionDocente;
    }

    public void setCalificacionTutor(int calificacionTutor) {
        this.calificacionTutor = calificacionTutor;
    }
}
