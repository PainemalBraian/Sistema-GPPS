package Backend.Entidades;

import Backend.Exceptions.EmptyException;

import java.time.LocalDate;

import static java.util.Objects.isNull;

public class Informe extends Item{
    private byte[] archivoPDF; // Esto debe representar el PDF como binario
    private int porcentajeAvance; // Validaciónes aún pendientes 0-10
    private LocalDate fecha = LocalDate.now();
    private String tituloActividad;

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
        if (isNull(porcentajeAvance) || porcentajeAvance < 0 || porcentajeAvance > 10)
            throw new EmptyException("El porcentaje debe ser valido y estar entre 0 - 10.");
        this.porcentajeAvance = porcentajeAvance;
    }

    public void setTituloActividad(String tituloActividad) {
        this.tituloActividad = tituloActividad;
    }
}
