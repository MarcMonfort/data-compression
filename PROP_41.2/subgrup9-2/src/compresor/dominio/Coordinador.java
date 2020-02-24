/** @file Coordinador.java
 * @author Daniel Donate
 @brief Un coordinador para controlarlos a todos
 */

package compresor.dominio;

import compresor.persistencia.ControladorGestorArchivo;

import javax.swing.*;
import java.io.IOException;

public class Coordinador {
    private ControladorCarpetas carpeta;
    private ControladorCompresor compresor;
    private ControladorDescompresor descompresor;
    private ControladorEstadisticas estadisticas;

    public void leerTexto(JTextArea txtHelp, String path_final) throws IOException {
        ControladorGestorArchivo.leerTexto(txtHelp, path_final);;
    }

    public String getPathDirectory() throws ControladorGestorArchivo.IncorrectFileException {
        return ControladorGestorArchivo.getPathDirectory();
    }

    public void setControladorCarpetas(String path) {
        this.carpeta = new ControladorCarpetas(path);
    }
    
    public void setControladorCompresor(String path) {
        this.compresor = new ControladorCompresor(path);
    }

    public void setControladorDescompresor(String path){
        this.descompresor = new ControladorDescompresor(path);
    }

    public void setControladorEstadisticas(){
        this.estadisticas = new ControladorEstadisticas();
    }

    public byte[] comprimirCarpeta() {
        return carpeta.comprimirCarpeta();
    }

    public String getNameCarpeta() {
        return carpeta.getName();
    }

    public void guardarArchivoEnBytes(String path_final, byte[] dataComprimida) {
        ControladorGestorArchivo.guardarArchivoEnBytes(path_final, dataComprimida);
    }
    
    public String getPathEntrada() throws ControladorGestorArchivo.IncorrectFileException {
        return ControladorGestorArchivo.getPathEntrada();
    }

    public String getPathEntrada2() throws ControladorGestorArchivo.IncorrectFileException {
        return ControladorGestorArchivo.getPathEntrada2();
    }

    public byte[] leerArchivoEnBytes(String path) {
        return ControladorGestorArchivo.leerArchivoEnBytes(path);
    }

    public byte[] descomprimirFichero(byte[] dataComprimida) {
        return descompresor.descomprimirFichero(dataComprimida);
    }

    public String getNameDescomprimido() {
        return descompresor.getComprimido().getMetadata().getName();
    }

    public String getNameDescomprimidoSinExtension() {
        return descompresor.getComprimido().getMetadata().getNombreSinExtension();
    }

    public String getNameComprimidoSinExtension() {
        return compresor.getComprimido().getMetadata().getNombreSinExtension();
    }

    public String getExtension() {
        return descompresor.getComprimido().getMetadata().getExtension();
    }

    public String getAlgoritmoUsadoCompresor() {
        return compresor.getComprimido().getMetadata().getAlgoritmoUsado().toString();
    }

    public String getAlgoritmoUsadoDescompresor() {
        return descompresor.getComprimido().getMetadata().getAlgoritmoUsado().toString();
    }

    public float getVelocidad() {
        return descompresor.getComprimido().getMetadata().getEstadistica().getVelocidad();
    }

    public void descomprimirCarpeta(byte[] dataComprimida) {
        carpeta.descomprimirCarpeta(dataComprimida);
    }

    public byte[] comprimirFichero(byte[] dataSinComprimir) throws IOException {
        return compresor.comprimirFichero(dataSinComprimir);
    }

    public AlgoritmoEnum algoritmoEnum(int opcion) {
        return AlgoritmoEnum.values()[opcion - 1];
    }

    public byte[] comprimirFicheroConAlgoritmo(byte[] dataSinComprimir, int opcion) throws IOException {
        AlgoritmoEnum algoritmo= algoritmoEnum(opcion);
        return compresor.comprimirFicheroConAlgoritmo(dataSinComprimir, algoritmo);
    }

    public float[] getEstadisticasComprimido() {
        return compresor.getComprimido().getMetadata().getEstadistica().getEstadisticasInFloat();
    }

    public void borrarFicheroTemporal(String path_dir, String temp_path) {
        ControladorGestorArchivo.borrarFicheroTemporal(path_dir, temp_path);
    }

    public void consultarRegistro() {
        estadisticas.consultarRegistro();
    }

    public void imprimirEstadisticasFichero() throws ControladorGestorArchivo.IncorrectFileException {
        estadisticas.imprimirEstadisticasFichero();
    }
}
