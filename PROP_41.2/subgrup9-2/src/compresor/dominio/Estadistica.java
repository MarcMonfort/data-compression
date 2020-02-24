/**
 * @file Estadistica.java
 * @author Álvaro Macías, Marc Monfort, Daniel Donate
 * @brief Contiene la implementación de las estadísticas de compresión/descompresión
 */

package compresor.dominio;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * @class Estadistica
 * @brief Estructura y métodos de las estadísticas de compresión/descompresión de los ficheros por parte del programa
 */
public class Estadistica {
    private float gradoCompresion;  //(4 bytes)
    private float velocidad;    //ms    (4bytes)
    private float diferenciaTamaño; //  (4 bytes)
    private float fiabilidad;       //  ((4bytes)

    /**
     * @brief Constructora de un objeto estadistica
     * @param gradoCompresion
     * @param velocidad
     * @param diferenciaTamaño
     * @param fiabilidad
     * @return Crea unobjeto estadistica, asiganando los valores de
     *          grado de compresión, velocidad de compresión,
     *          diferencia de tamaño, fiabilidad de la compresión
     */
    public Estadistica(float gradoCompresion, float velocidad, float diferenciaTamaño, float fiabilidad) {
        this.gradoCompresion = gradoCompresion;
        this.velocidad = velocidad;
        this.diferenciaTamaño = diferenciaTamaño;
        this.fiabilidad = fiabilidad;
    }

    /**
     * @brief Obtiene todas las estadísticas en formato byte
     * @return Devuelve un array de bytes con los valores de los parámetros contemplados para las estadísticas
     */
    /* No me funciona... */ //????
    public byte[] getEstadisticaInBytes() {
        byte[] salida = new byte[16];
        byte[] aux = ByteBuffer.allocate(4).putFloat(gradoCompresion).array();
        System.arraycopy(aux, 0, salida, 0, 4);
        aux = ByteBuffer.allocate(4).putFloat(velocidad).array();
        System.arraycopy(aux, 0, salida, 4, 4);
        aux = ByteBuffer.allocate(4).putFloat(diferenciaTamaño).array();
        System.arraycopy(aux, 0, salida, 8, 4);
        aux = ByteBuffer.allocate(4).putFloat(fiabilidad).array();
        System.arraycopy(aux, 0, salida, 12, 4);
        return salida;
    }

    /**
     * @brief Obtiene todas las estadísticas en formato float
     * @return Devuelve un array de floats con los valores de los parámetros contemplados para las estadísticas
     */
    public float[] getEstadisticasInFloat() {
        float[] salida  = new float[4];
        salida[0] = gradoCompresion;
        salida[1] = velocidad;
        salida[2] = diferenciaTamaño;
        salida[3] = fiabilidad;
        return salida;
    }

    /**
     * @brief getter del atributo gradoCompresion
     */
    public float getGradoCompresion() {
        return gradoCompresion;
    }

    /**
     * @brief setter del atributo gradoCompresion
     * @param gradoCompresion
     */
    public void setGradoCompresion(float gradoCompresion) {
        this.gradoCompresion = gradoCompresion;
    }

    /**
     * @brief getter del atributo velocidad
     */
    public float getVelocidad() {
        return velocidad;
    }

    /**
     * @brief setter del atributo velocidad
     * @param velocidad
     */
    public void setVelocidad(float velocidad) {
        this.velocidad = velocidad;
    }

    /**
     * @brief getter del atributo diferenciaTamaño
     */
    public float getDiferenciaTamaño() {
        return diferenciaTamaño;
    }

    /**
     * @brief setter del atributo diferenciaTamaño
     * @param diferenciaTamaño
     */
    public void setDiferenciaTamaño(float diferenciaTamaño) {
        this.diferenciaTamaño = diferenciaTamaño;
    }

    /**
     * @brief getter del atributo fiabilidad
     */
    public float getFiabilidad() {
        return fiabilidad;
    }

    /**
     * @brief setter del atributo fiabilidad
     * @param fiabilidad
     */
    public void setFiabilidad(float fiabilidad) {
        this.fiabilidad = fiabilidad;
    }


    public void printEstadisticas() {
        System.out.println("\n\t Grado compresion: " + gradoCompresion +
                "\n\t Velocidad: " + velocidad + " ms" +
                "\n\t Diferencia Tamaño: " + diferenciaTamaño + " bytes" +
                "\n\t Fiabilidad: " + fiabilidad + "\n");
    }

}
