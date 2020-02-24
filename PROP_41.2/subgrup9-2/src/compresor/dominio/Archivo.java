/**
 * @file Archivo.java
 * @Author Álvaro Macías
 * @brief Contiene la implementación de un objeto archivo
 */

package compresor.dominio;

import java.io.File;
import java.nio.file.Paths;

/**
 * @class Archivo
 * @brief Estructura y métodos de un archivo
 */

public class Archivo {
    private String path;
    private String name;
    private int tamaño;
    private String extension;       //podria hacerse en utils

    /**
     * @brief Constructora de un objeto archivo
     * @param path
     * @param tamaño
     * @return Crea una archivo con el path y el tamaño especificados.
     *          El nombre y la extensión se obtienen directamente del path.
     */
    public Archivo(String path, int tamaño) {
        this.path = path;
        this.name = Paths.get(path).getFileName().toString();
        this.tamaño = tamaño;

        String extension = "";


        int i = name.lastIndexOf('.');
        int p = name.lastIndexOf(File.separator);

        if (i > p) {
            extension = name.substring(i);
        }
        this.extension = extension;
    }

    /**
     * @brief getter del atributo path
     * @return
     */
    public String getPath() {
        return path;
    }

    /**
     * @brief setter del atributo path
     * @param path
     */
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * @brief getter del atributo name
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * @brief setter del atributo name
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @brief getter del atributo tamaño
     * @return
     */
    public int getTamaño() {
        return tamaño;
    }

    /**
     * @brief setter del atributo tamaño
     * @param tamaño
     */
    public void setTamaño(int tamaño) {
        this.tamaño = tamaño;
    }

    /**
     * @brief Método público para obtener la extensión del archivo
     * @return Devuelve la extensión del archivo
     */
    public String getExtension() {return this.extension;};

}
