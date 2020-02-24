/** @file ControladorDescompresor.java
 * @author Marc Monfort
 @brief Especificación del controlador de descompresión
 */

package compresor.dominio;

import compresor.dominio.algoritmo.*;

import java.nio.ByteBuffer;

import static java.util.Arrays.copyOfRange;

/** @class ControladorDescompresor
 @brief Controlador de descompresión
 Contiene las funciones esenciales que mandan la ejecución
 de la descompresión de un archivo
 */

public class ControladorDescompresor {

    private Comprimido comprimido;


    /** @brief Creadora del controlador.
    \pre pathArchivo es un path válido
    \post El resultado es un controlador que tiene acceso
    al archivo referenciado por el path
     */

    public ControladorDescompresor(String pathArchivo) {
        this.comprimido = new Comprimido(pathArchivo);
    }

    /** @brief Llamada de ejecución a la descompresión
    \pre
    \post La función manda a descomprimir la "dataComprimida"
    después de extraer y usar la metadata
     */

    public byte[] descomprimirFichero(byte[] dataComprimida) {




        int tamañoTotal = dataComprimida[0] & 0xFF;
        AlgoritmoEnum tipoAlgoritmo = AlgoritmoEnum.values()[(dataComprimida[1] & 0xFF)-1];
        int tamañoNombre =  dataComprimida[2] & 0xFF;
        String nombre = new String(copyOfRange(dataComprimida,3,3+tamañoNombre));

        int offset = tamañoNombre + 3;
        float gradoCompresion = ByteBuffer.wrap(copyOfRange(dataComprimida,offset,offset+4)).getFloat();
        float velocidad = ByteBuffer.wrap(copyOfRange(dataComprimida,offset+4,offset+8)).getFloat();
        float diferenciaTamaño = ByteBuffer.wrap(copyOfRange(dataComprimida,offset+8,offset+12)).getFloat();
        float fiabilidad = ByteBuffer.wrap(copyOfRange(dataComprimida,offset+12,offset+16)).getFloat();
        Estadistica estadistica = new Estadistica(gradoCompresion, velocidad, diferenciaTamaño, fiabilidad);

        /* En caso de imagen también tendrá alto y ancho */
        int width = 0;
        int height = 0;
        Metadata metadata;
        if (tipoAlgoritmo.equals(AlgoritmoEnum.JPEG)) {
            width =  ByteBuffer.wrap(copyOfRange(dataComprimida,offset+16,offset+20)).getInt();
            height =  ByteBuffer.wrap(copyOfRange(dataComprimida,offset+20,offset+24)).getInt();
            tamañoTotal += 8;
            metadata = new MetadataImagen(tipoAlgoritmo, estadistica, nombre, width, height);
        } else {
            metadata = new Metadata(tipoAlgoritmo, estadistica, nombre);    //por si se quiere usar en el futuro...
        }

        this.comprimido.setMetadata(metadata);

        byte[] dataSinMetadata = copyOfRange(dataComprimida,tamañoTotal,dataComprimida.length);

        switch (tipoAlgoritmo) {
            case LZ78: this.ejecutarAlgoritmo(new AlgoritmoLZ78(), dataSinMetadata);
                break;
            case LZSS: this.ejecutarAlgoritmo(new AlgoritmoLZSS(), dataSinMetadata);
                break;
            case LZW: this.ejecutarAlgoritmo(new AlgoritmoLZW(), dataSinMetadata);
                break;
            case JPEG:
                AlgoritmoJPEG jpeg = new AlgoritmoJPEG();
                jpeg.prepareNewImage(width, height);
                this.ejecutarAlgoritmo(jpeg, dataSinMetadata);
                Imagen img = new Imagen(this.comprimido.getPath(), width, height, 255, this.comprimido.getData());
                comprimido.setData(img.convertirImagenEnPPM());
                break;
        }

        return this.comprimido.getData();
    }

    /** @brief Descompresión de dataSinComprimir
    \pre  algoritmo es uno de los 4 algoritmos de compresión del proyecto
    \post La función manda a descomprimir la "dataSinComprimir" con el descompresor
    del algoritmo seleccionado
     */

    private void ejecutarAlgoritmo(Algoritmo algoritmo, byte[] dataSinMetadata) {
        /* Ejecuta el algoritmo independientemente del algoritmo escogido */
        /* En cada método aquí habría que poner la metadata. Medir tiempo y cosas */
        long start = System.currentTimeMillis();
        this.comprimido.setData(algoritmo.descomprimir(dataSinMetadata));
        long end = System.currentTimeMillis();

        //no se si seria interesante crear dos metadatas, una de compresion y otra de descompresion
        //demomento lo comento y no calculo nada
        //en verdad lo único interesante es fiabilidad y velociadad (descompresion)
        //tratad como querais, pero ya se ha extraido la metadata del fichero compreso.

    }


    public Comprimido getComprimido() {
        return this.comprimido;
    }

}
