/**
 * @file ByteUtils.java
 * @author Felipe Ramis
 * @brief Contiene métodos para la manipulación de arrays de bytes
 */

package compresor.utils;

/**
 * @class ByteUtils
 * @brief Implementación de métodos útiles para la manipulación de arrays de bytes
 */
public class ByteUtils {

    /**
     * @brief Convierte un array de bytes a formato String
     * @param data
     * @return devuelve un String que contiene una copia del texto pasado por parámetro
     */
    public static String byteArrayToString(byte[] data) {
        String texto = "";
        if (data != null) {
            for (int i = 0; i < data.length; i++) {
                // Si un byte es negativo significa que será un caracter que necesite 2 bytes
                texto += (char) data[i];
            }
        }
        return texto;
    }

    /**
     * @brief Busca un entero sobre un array que contiene espacios
     * @param data: array de datos
     * @param startIndex: índice dónde empezar a buscar sobre el array
     * @return Entero en representación de cadena de caracteres
     */
    public static String nextIntFromBytes(byte[] data, int startIndex) {
        if (data.length == 0) {
            return "";
        }

        String text = "";
        for (int i = startIndex; !isWhitespace(data[i]); i++) {
            text += (char) data[i];
        }
        return text;
    }

    /**
     * @brief Devuelve true si el byte es de alguna forma un espacio, tabulación o salto de línea
     * @param dataByte: byte
     * @return Si se cumple la condición
     */
    private static boolean isWhitespace(byte dataByte) {
        char c = (char) dataByte;
        return c == '\n' || c == ' ' || c == '\t';
    }

    /**
     * @brief Convierte un byte en un entero sin signo aplicando una máscara
     * @param b: valor en byte
     * @return Valor entero
     */
    public static int convertByteToUnsignedInt(byte b) {
        return b & 0xFF;
    }
}
