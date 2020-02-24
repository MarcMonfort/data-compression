/**
 * @file Comprimido.java
 * @author Álvaro Macías
 * @brief Contiene la implementación de un objeto comprimido
 */

package compresor.dominio;


import compresor.utils.ByteUtils;

/**
 * @class Comprimido
 * @brief Estructura y métodos de un archivo comprimido
 */

public class Comprimido extends Archivo {
    protected byte[] data;
    protected Metadata metadata;

    /**
     * @brief Constructura de un comprimido
     * @param path
     * @return Crea un archivo comprimido con el path especificado
     */
    public Comprimido(String path) {
        super(path, 0);
    }

    /**
     * @brief getter del atributo data
     * @return
     */
    public byte[] getData() {
        return data;
    }

    /**
     * @brief setter del atributo data
     * @param data
     */
    public void setData(byte[] data) {
        this.data = data;
        this.setTamaño(this.data.length);
    }

    /**
     * @brief Convierte un array de bytes a String
     * @return Devuelve el array data en formato String
     */
    @Override
    public String toString() {
        String output = "Tamaño: " + this.getTamaño() + "\n\n";
        output += ByteUtils.byteArrayToString(this.data);
        return output;
    }

    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
    }

    public Metadata getMetadata() {
        return this.metadata;
    }


}
