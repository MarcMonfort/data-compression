/**
 * @mainpage Práctica de PROP:  Compresor de Archivos.

En este proyecto creamos una aplicación Java para comprimir ficheros de texto y imagen mediante 4 algoritmos distintos. Se introducen las clases <em>Algoritmo</em>, <em>Archivo</em>,  <em>Comprimido</em>,  <em>Estadística</em> y  <em>Metadata</em>.
 */

/** @file Main.java
 @brief Programa principal de la práctica <em>Compresor de Archivos</em>.
 */
/**
 * @file Main.java
 * @author Felipe Ramis
 * @brief Contiene la clase main del programa
 */

package compresor;

import compresor.dominio.*;
import compresor.vistas.*;

/**
 * @class Main
 * @brief Clase main del programa. Contiene los menús, sus estrucduras y los métodos para gestionarlos
 */
public class MainT {

    /** @brief Programa principal del proyecto <em>Compresor de Archivos</em>.
     */
    public static void main(String[] args) {
        VistaTerminal.menuTerminal();
    }

}
