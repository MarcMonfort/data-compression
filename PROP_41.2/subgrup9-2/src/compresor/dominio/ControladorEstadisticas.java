/** @file ControladorEstadisticas.java
 * @author Daniel Donate
 * @brief Especificación del controlador de estadísticas
 */


package compresor.dominio;

import compresor.persistencia.ControladorGestorArchivo;
import compresor.vistas.ImprimirEstadisticas;

import javax.swing.*;
import java.io.*;

import java.io.File;
import java.io.IOException;

import static java.util.Arrays.copyOfRange;
import java.nio.ByteBuffer;
import java.util.ArrayList;

/** @class ControladorEstadisticas
 @brief Controlador de estadísticas
 Contiene las funciones que controlan
 el guardado y muestreo de las estadísticas generadas
 a partir de nuestros ficheros comprimidos
 */

public class ControladorEstadisticas {

    BufferedWriter bw = null;
    FileWriter fw = null;

    /** @brief Gestora de estadísticas globales.
    \pre estadistica es una estadística válida para un ficero que se
    ha comprimido; y algoritmoUsado es un algoritmo válido
    \post Si es la primera vez que el usuario comprime un archivo,
    se crea un .dat que contiene el resultado de estadistica.
    Si no, se añade el resultado de estadistica a un .dat
    ya existente
     */
    public void escribirEstadisticas(Estadistica estadistica, String algoritmoUsado) {
        try {
            float[] data = estadistica.getEstadisticasInFloat();
            String output_Data ="";
            File file = new File(".estadisticas.dat");
            // Si el archivo no existe, se crea
            if (!file.exists()) {
                file.createNewFile();
                output_Data = "Grado," + " " + "Velocidad," +  " " + "DifMida," + " " + "Fiabil," + " " + "Algoritmo" + "\n";
            }
            output_Data += Float.toString(data[0]);
            for (int k = 1; k < 4; k++) {
                output_Data += "   " + Float.toString(data[k]);
            }
            output_Data +=  "   " + algoritmoUsado +"\n";
            // flag true, indica adjuntar información al archivo.
            fw = new FileWriter(file.getAbsoluteFile(), true);
            bw = new BufferedWriter(fw);
            bw.write(output_Data);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                //Cierra instancias de FileWriter y BufferedWriter
                if (bw != null)
                    bw.close();
                if (fw != null)
                    fw.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    /** @brief Consultadora de historial de estadísticas.
    \pre
    \post Se llama a una vista para que imprima el registro de
    todas las compresiones hechas por el usuario, si es
    que ha hecho alguna
     */
    public void consultarRegistro() {
        String sFichero = ".estadisticas.dat";
        File fichero = new File(sFichero);
        if (!fichero.exists()) {
            System.out.println("El fichero de estadísticas no existe");
            return;
        }

        SwingUtilities.invokeLater(new Runnable(){
            public void run(){
                ImprimirEstadisticas st = new ImprimirEstadisticas();
            }
        });
    }

    /** @brief Consultadora de estadísticas para un comprimido.
    \pre
    \post Se llama a una vista para que imprima las estadísticas
    asociadas a un fichero comprimido que seleccionará el
    usuario
     */
    public void imprimirEstadisticasFichero() throws ControladorGestorArchivo.IncorrectFileException {
        ControladorGestorArchivo GA = new ControladorGestorArchivo();
        String path = GA.getPathEntrada2();
        byte[] data = GA.leerArchivoEnBytes(path);
        int tamañoNombre =  data[2] & 0xFF;
        float gradoCompresion = ByteBuffer.wrap(copyOfRange(data,tamañoNombre+3,tamañoNombre+7)).getFloat();
        float velocidad = ByteBuffer.wrap(copyOfRange(data,tamañoNombre+7,tamañoNombre+11)).getFloat();
        float diferenciaTamaño = ByteBuffer.wrap(copyOfRange(data,tamañoNombre+11,tamañoNombre+15)).getFloat();
        float fiabilidad = ByteBuffer.wrap(copyOfRange(data,tamañoNombre+15,tamañoNombre+19)).getFloat();
        String algoritmoUsado = (AlgoritmoEnum.values()[(data[1] & 0xFF)-1]).toString();
        ArrayList<String> info = new ArrayList<>();
        info.add(Float.toString(gradoCompresion)); info.add(Float.toString(velocidad)); info.add(Float.toString(diferenciaTamaño)); info.add(Float.toString(fiabilidad)); info.add(algoritmoUsado);
        ImprimirEstadisticas.imprimirEstadisticas(info, "default");
    }
    public void imprimirEstadisticasFichero(String path) throws ControladorGestorArchivo.IncorrectFileException {
        ControladorGestorArchivo GA = new ControladorGestorArchivo();
        byte[] data = GA.leerArchivoEnBytes(path);
        int tamañoNombre =  data[2] & 0xFF;
        float gradoCompresion = ByteBuffer.wrap(copyOfRange(data,tamañoNombre+3,tamañoNombre+7)).getFloat();
        float velocidad = ByteBuffer.wrap(copyOfRange(data,tamañoNombre+7,tamañoNombre+11)).getFloat();
        float diferenciaTamaño = ByteBuffer.wrap(copyOfRange(data,tamañoNombre+11,tamañoNombre+15)).getFloat();
        float fiabilidad = ByteBuffer.wrap(copyOfRange(data,tamañoNombre+15,tamañoNombre+19)).getFloat();
        String algoritmoUsado = (AlgoritmoEnum.values()[(data[1] & 0xFF)-1]).toString();
        System.out.println("\nAlgoritmo usado: " + algoritmoUsado +
                "\nGrado compresión: " + gradoCompresion +
                "\nVelocidad: " + velocidad +
                "\nDiferencia tamaño: " + diferenciaTamaño +
                "\nFiabilidad: " + fiabilidad + "\n");
    }
}