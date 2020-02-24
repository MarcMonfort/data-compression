/**
 * @file AlgoritmoLZSS.java
 * @Author Álvaro Macías
 * @brief Implementación del algoritmo de compresión/descompresión LZSS
 */

package compresor.dominio.algoritmo;

import java.util.ArrayList;
import java.util.List;

public class AlgoritmoLZSS implements Algoritmo {
    /* Álvaro */

    private int searchSize = 4096;
    private int lookSize = 16;
    private int minimum = 2;

    /**
     * @brief Calcula la posición referenciada por hi+lo
     * @param hi
     * @param lo
     * @return Posición referenciada
     */
    private int getPos(byte hi, byte lo){
        int ret = lo & 0xF0;
        ret >>= 4;
        int ret_aux = hi;
        ret_aux <<= 4;
        ret |= ret_aux;
        ret &= 0x00000FFF;
        return ret;
    }

    /**
     * @brief Calcula el desplazamiento respecto a una posición codificado en lo
     * @param lo
     * @return Desplazamiento codificado en lo
     */
    private int getDes(byte lo){
        return ((int)lo & 0x0F);
    }

    private String intToBin(int n, int l){
        StringBuilder s = new StringBuilder(Integer.toBinaryString(n));
        while(s.length() < l) s.insert(0, '0');
        return s.toString();
    }

    /**
     * @brief Compresión aplicando el algoritmo LZSS
     * @param entrada
     * @return Archivo comprimido
     */
    @Override
    public byte[] comprimir(byte[] entrada) {

        if (entrada.length == 0) return new byte[0];


        if (entrada.length > searchSize) {

            byte mask = 0x00;
            int posSearch = 0;
            int matchLength = 0;
            int lastMatch = 0;
            int punt = 0;

            List<Byte> salida_aux = new ArrayList<>();
            byte[] aux = new byte[16];

            short k = 0;


            for (int i = 0; i < searchSize; ++i) salida_aux.add(entrada[i]);
            int posLook = searchSize;

            while (posLook < entrada.length) {
                short i;
                if (entrada.length - posLook > 2) {
                    for (int j = posLook - searchSize; j < posLook; ++j) {
                        if (entrada[posLook] == entrada[j]) {
                            i = 1;
                            matchLength = 1;
                            while ((i < lookSize && posLook + i < entrada.length) && entrada[posLook + i] == entrada[++j] && j < posLook) {
                                ++matchLength;
                                if (matchLength > lastMatch) {
                                    lastMatch = matchLength;
                                    posSearch = j - (posLook - searchSize);
                                }
                                ++i;
                            }
                        }
                        if (lastMatch == lookSize) break;
                    }
                }

                if (lastMatch < minimum) lastMatch = minimum;

                mask = (byte) (mask << 1);
                if (lastMatch > minimum) {
                    mask |= 0x01;
                    String bin = intToBin(posSearch, 12);
                    byte lo = 0x00;
                    byte hi = 0x00;
                    for (int l = 0; l < 8; ++l){
                        if (bin.regionMatches(l, String.valueOf(1), 0, 1)) lo |= 0x01 << 7-l;
                    }
                    for (int l = 0; l < 4; ++l){
                        if (bin.regionMatches(8+l, String.valueOf(1), 0, 1)) hi |= 0x01 << 7-l;
                    }
                    bin = intToBin(lastMatch - 1, 4);
                    for (int l = 0; l < 4; ++l){
                        if (bin.regionMatches(l, String.valueOf(1), 0, 1)) hi |= 0x01 << 3-l;
                    }
                    aux[2*k] = lo;
                    aux[2*k + 1] = hi;

                } else {
                    aux[2*k] = entrada[posLook];
                    if (posLook + 1 < entrada.length) aux[2*k + 1] = entrada[posLook + 1];
                }
                ++k;
                if (k == 8) {
                    k = 0;

                    salida_aux.add(mask);
                    mask = 0x00;

                    for(int punt_aux = 0; punt_aux < 16; ++punt_aux){
                        salida_aux.add(aux[punt_aux]);
                        aux[punt_aux] = 0x00;
                    }
                }
                posLook += lastMatch;
                lastMatch = 0;
            }
            if (k > 0) {
                for (;k < 8; ++k) {
                    mask = (byte) (mask << 1);
                    aux[2*k] = 0x00;
                    aux[2*k + 1] = 0x00;
                }

                salida_aux.add(mask);
                for(int punt_aux = 0; punt_aux < 16; ++punt_aux) salida_aux.add(aux[punt_aux]);
            }
            byte[] salida = new byte[salida_aux.size()];
            for(int i = 0; i < salida_aux.size(); ++i) salida[i] = salida_aux.get(i);
            return salida;
        } return entrada;
    }

    /**
     * @brief Descompresión de un archivo comprimido mediante LZSS
     * @param entrada
     * @return Archivo descomprimido
     */
    @Override
    public byte[] descomprimir(byte[] entrada) {
        if (entrada.length == 0) return new byte[0];
        if (entrada.length > searchSize){
            List<Byte> aux = new ArrayList<>();
            for(int i = 0; i < searchSize; ++i) aux.add(entrada[i]);
            for(int i = searchSize; i < entrada.length; i += 17){
                for(int j = 1; j < 16; j += 2){
                    if ((entrada[i] & -128) == -128){
                        int pos = getPos(entrada[i + j], entrada[i + j + 1]);
                        int des = getDes(entrada[i + j + 1]);
                        for(int k = des; k >= 0; --k) aux.add(aux.get((i - searchSize) + pos - k));
                    }
                    else {
                        if (entrada[i + j] == 0x00) break;
                        aux.add(entrada[i + j]);
                        if (entrada[i + j + 1] == 0x00) break;
                        if(i + j + 1 < entrada.length) aux.add(entrada[i + j + 1]);
                    }
                    entrada[i] <<= 1;
                }
            }
            byte[] salida = new byte[aux.size()];
            for(int i = 0; i < aux.size(); ++i) salida[i] = aux.get(i);
            return salida;
        }
        return entrada;
    }

}