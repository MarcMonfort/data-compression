package compresor.utils;

import java.util.ArrayList;

/**
 * @file Huffman.java
 * @author Felipe Ramis
 * @brief Contiene métodos para codificar los bytes en el Algoritmo JPEG
 */
public class Huffman {

    /**
     * @brief Codifica los 3 canales juntandolos (Se debería convertir a símbolos RUNLENGHT-AMPLITUDE) pero por
     *        tiempo no se ha podido realizar.
     * @param y: Canal de luminancia comprimido
     * @param Cb: Canal de croma azul comprimido
     * @param Cr: Canal de croma rojo comprimido
     * @return Array de bytes que se escribirá en memoria
     */
    public static byte[] encode(ArrayList<Integer> y, ArrayList<Integer> Cb, ArrayList<Integer> Cr) {
        /* Crear Símbolos sobre Runlength-size | amplitude para comprimir al máximo el número de bits */
        ArrayList<Integer> listaConjunta = new ArrayList<>();
        listaConjunta.addAll(y); listaConjunta.addAll(Cb); listaConjunta.addAll(Cr);
        byte[] encoded = new byte[listaConjunta.size()];
        for (int i = 0; i < listaConjunta.size(); i++) {
            int value = listaConjunta.get(i);
            encoded[i] = (byte) value;
        }
        return encoded;
    }

    public static ArrayList<Integer>[] decode (byte[] dataCompressed) {
        return null;
    }

}
