/** @file ControladorCompresor.java
 * @author Daniel Donate, Felipe Ramis, Marc Monfort
 @brief Especificación del controlador de compresión
 */

package compresor.dominio;


import compresor.dominio.algoritmo.*;

import java.io.IOException;

/** @class ControladorCompresor
 @brief Controlador de compresión
 Contiene las funciones esenciales que mandan la ejecución
 de un algoritmo de compresor; tanto en su versión automática,
 como en la manual
 */

public class ControladorCompresor {

    private Comprimido comprimido;


    /** @brief Creadora del controlador.
    \pre pathArchivo es un path válido
    \post El resultado es un controlador que tiene acceso
    al archivo referenciado por el path
     */
    public ControladorCompresor(String pathArchivo) {
        this.comprimido = new Comprimido(pathArchivo);
    }

    /** @brief Llamada de ejecución automática
    \pre
    \post La función manda a comprimir la "dataSinComprimir" con el
    algoritmo adecuado, sin que el usuario especifique cuál es
     */
    public byte[] comprimirFichero(byte[] dataSinComprimir) throws IOException {
        /* Algoritmo automático - Escoger algoritmo según estadísticas */
        String extension = comprimido.getExtension();
        AlgoritmoEnum algEnum;

        if (extension.equals(".ppm")) {
            algEnum = AlgoritmoEnum.JPEG;
        }
        else if (extension.equals(".txt") && dataSinComprimir.length < 1000000){
            algEnum = AlgoritmoEnum.LZW;
        }
        else algEnum = AlgoritmoEnum.LZ78;

        if (true == false) algEnum = AlgoritmoEnum.LZSS;

        return this.comprimirFicheroConAlgoritmo(
                dataSinComprimir,
                algEnum
        );
    }


    /** @brief Llamada de ejecución manual
    \pre  tipoAlgoritmo es uno de los 4 algoritmos que implementamos
    \post La función manda a comprimir la "dataSinComprimir" con el
    algoritmo escogido por el usuario. Devuelve el archivo comprimido en bytes.
     */
    public byte[] comprimirFicheroConAlgoritmo(byte[] dataSinComprimir, AlgoritmoEnum tipoAlgoritmo) throws IOException {

        Estadistica estadistica;
        Metadata metadata;
        Imagen img = null;

        if (tipoAlgoritmo.equals(AlgoritmoEnum.JPEG)) {
            img = Imagen.generarConPPM(this.comprimido.getPath(), dataSinComprimir);
        }

        switch (tipoAlgoritmo) {
            case LZ78: estadistica = this.ejecutarAlgoritmo(new AlgoritmoLZ78(), dataSinComprimir, tipoAlgoritmo);
                break;
            case LZSS: estadistica = this.ejecutarAlgoritmo(new AlgoritmoLZSS(), dataSinComprimir, tipoAlgoritmo);
                break;
            case LZW: estadistica = this.ejecutarAlgoritmo(new AlgoritmoLZW(), dataSinComprimir, tipoAlgoritmo);
                break;
            case JPEG: estadistica = this.comprimirImagen(img);
                break;
            default:    throw new ArithmeticException("no se que excepcion poner??");   //ayuda
        }

        if (tipoAlgoritmo.equals(AlgoritmoEnum.JPEG)) {
            metadata = new MetadataImagen(tipoAlgoritmo, estadistica, this.comprimido.getName(), img.getAncho(), img.getAlto());
        } else {
            metadata = new Metadata(tipoAlgoritmo, estadistica, this.comprimido.getName());
        }

        this.comprimido.setMetadata(metadata);

        byte[] met = metadata.getMetadataInBytes();
        byte[] allData = new byte[met.length + this.comprimido.getTamaño()];
        System.arraycopy(met, 0, allData, 0, met.length);
        System.arraycopy(this.comprimido.getData(), 0, allData, met.length, this.comprimido.getTamaño());

        return allData;
    }

    /** @brief Compresión de dataSinComprimir
    \pre
    \post La función manda a comprimir la "dataSinComprimir" con el algoritmo JPEG,
    creando una Imagen en vez de Comprimido para conservar el ancho y alto
    Devuelve las estadisticas generadas.
     */
    private Estadistica comprimirImagen(Imagen img) throws IOException {
        AlgoritmoJPEG jpeg = new AlgoritmoJPEG();
        jpeg.prepareNewImage(img.getAncho(), img.getAlto());
        return this.ejecutarAlgoritmo(jpeg, img.getData(), AlgoritmoEnum.JPEG);
    }

    /** @brief Compresión de dataSinComprimir
    \pre
    \post La función manda a comprimir la "dataSinComprimir" con el algoritmo
    préviamente escogido por una de las dos funciones anteriores.
    Devuelve las estadisticas generadas.
     */
    private Estadistica ejecutarAlgoritmo(Algoritmo algoritmo, byte[] dataSinComprimir, AlgoritmoEnum tipoAlgoritmo) {

        long start = System.currentTimeMillis();
        this.comprimido.setData(algoritmo.comprimir(dataSinComprimir));
        long end = System.currentTimeMillis();

        float gradoCompresion = 1 - (comprimido.getData().length / (float)dataSinComprimir.length);
        float velocidad = end - start;
        float diferenciaTamaño = (float)dataSinComprimir.length - (float)comprimido.getData().length;
        float fiabilidad = 1.0f;   //por poner algo...

        Estadistica estadistica = new Estadistica(gradoCompresion, velocidad, diferenciaTamaño, fiabilidad);

        ControladorEstadisticas Estadisticas = new ControladorEstadisticas();
        Estadisticas.escribirEstadisticas(estadistica, tipoAlgoritmo.toString());

        return estadistica; //tambien se podria inicializar metadata vacio, añadir estadistica y devolver metadata (pero esto es mas limpio)
    }


    public Comprimido getComprimido() {
        return this.comprimido;
    };
}
