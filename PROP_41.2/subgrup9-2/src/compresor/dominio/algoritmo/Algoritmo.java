/**
 * @file Algoritmo.java
 * @Author Felipe Ramis
 * @brief Interfaz de los diferentes algoritmos
 */

package compresor.dominio.algoritmo;

/**
 * @brief Funciones básicas de los algoritmos de compresión
 */
public interface Algoritmo {
    byte[] comprimir(byte[] entrada);
    byte[] descomprimir(byte[] entrada);
}
