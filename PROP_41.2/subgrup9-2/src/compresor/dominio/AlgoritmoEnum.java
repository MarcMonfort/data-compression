/**
 * @file AlgoritmoEnum.java
 * @author Felipe Ramis
 */

package compresor.dominio;


/**
 * @brief Contiene, ordenados, los algoritmos de compresión implementados en el programa
 * 0: LZ78
 * 1: LZSS
 * 2: LZW
 * 3: JPEG
 */
/* Si hubieran nuevos algoritmos, sólo se cambiaría aquí y el programa se adaptaría */
public enum AlgoritmoEnum { //NO MODIFICAR ORDEN (metadata)!!!
    LZ78,   //0
    LZSS,   //1
    LZW,    //2
    JPEG    //3
}

