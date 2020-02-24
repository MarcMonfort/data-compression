/**
 * @file Metadata.java
 * @author Marc Monfort, Álvaro Macías, Daniel Donate
 * @brief Implementación de la metadata de un archivo
 */

package compresor.dominio;

import java.io.File;
import java.util.Date;

/**
 * @class Metadata
 * @brief Estructura y métodos de la metadata de un archivo
 */
public class Metadata {
    private Date fechaCompresion;   // (32 bytes)
    private AlgoritmoEnum algoritmoUsado;   //4-8 bits (1 byte)
    private Estadistica estadistica;    // (16 bytes)
    private String nombre; //variable
    private boolean isDirectory = false;

    private String extension; //no se guarda en bytes
    private String nombreSinExtension;

    /**
     * @brief Constructora de un objeto metadata
     * @param algoritmoUsado
     * @param estadistica
     * @param nombre
     * @return Crea un objeto metadata con el algoritmo, las estadísticas y el nombre especificados.
     *          La fecha se obtiene de manera autómatica, la extensión se deriva directamente del nombre
     */
    public Metadata(AlgoritmoEnum algoritmoUsado, Estadistica estadistica, String nombre) {
        this.fechaCompresion = new Date(); //Obtiene la fecha actual
        this.algoritmoUsado = algoritmoUsado;
        this.estadistica = estadistica;
        this.nombre = nombre;

        String extension = "";


        int i = nombre.lastIndexOf('.');
        int p = nombre.lastIndexOf(File.separator);

        if (i > p) {
            extension = nombre.substring(i);
        }
        this.extension = extension;
        this.nombreSinExtension = nombre.substring(0,i);
    }

    /**
     * @brief Obtiene toda la información de la metadata
     * @return Devuelve todos los atributos del objeto en metadata en forma de array de bytes
     */
    public byte[] getMetadataInBytes() {


        byte[] alg = {(byte)(algoritmoUsado.ordinal()+1)}; //1 byte
        byte[] nombreBytes = this.nombre.getBytes();    //puede que pete si nombre > 256 bytes +-
        if (nombreBytes.length > 255) nombreBytes = this.nombre.substring(0,128).getBytes();
        byte[] tamañoNombre = {(byte) nombreBytes.length}; //en bytes
        byte[] est = estadistica.getEstadisticaInBytes();   //16 bytes

        byte[] tamañoTotal = {(byte) (alg.length + est.length + nombreBytes.length + tamañoNombre.length +  1)};

        byte[] salida = new byte[alg.length + est.length + nombreBytes.length + tamañoNombre.length + tamañoTotal.length];

        int offset = 0;

        System.arraycopy(tamañoTotal, 0, salida, offset, 1);
        offset += tamañoTotal.length;
        System.arraycopy(alg, 0, salida, offset, alg.length);
        offset += alg.length;
        System.arraycopy(tamañoNombre, 0, salida, offset, tamañoNombre.length);
        offset += tamañoNombre.length;
        System.arraycopy(nombreBytes, 0, salida, offset, nombreBytes.length);
        offset += nombreBytes.length;
        System.arraycopy(est, 0, salida, offset, est.length);



        return salida;

    }

    public void setEstadistica(Estadistica estadistica) {
        this.estadistica = estadistica;
    }

    public Estadistica getEstadistica() {
        return this.estadistica;
    }

    /**
     * @brief getter del atributo fechaCompresion
     */
    public Date getFechaCompresion() {
        return fechaCompresion;
    }

    /**
     * @brief setter del atributo fechaCompresion
     * @param fechaCompresion
     */
    public void setFechaCompresion(Date fechaCompresion) {
        this.fechaCompresion = fechaCompresion;
    }

    /**
     * @brief setter del atributo algoritmoUsado
     * @param algoritmoUsado
     */
    public void setAlgoritmoUsado(AlgoritmoEnum algoritmoUsado) {
        this.algoritmoUsado = algoritmoUsado;
    }

    /**
     * @brief getter del atributo algoritmoUsado
     */
    public AlgoritmoEnum getAlgoritmoUsado() {return this.algoritmoUsado;}

    /**
     * @brief getter del atributo nombre
     */
    public String getName () {
        return this.nombre;
    }

    /**
     * @brief getter del atributo extension
     */
    public String getExtension() {
        return this.extension;
    }

    /**
     * @brief getter del atributo nombreSinExtension
     */
    public String getNombreSinExtension() {
        return this.nombreSinExtension;
    }





}
