package compresor.dominio;

import compresor.utils.ByteUtils;

import java.io.IOException;
import java.util.Arrays;
/**
 * @file Imagen.java
 * @author Felipe Ramis
 * @brief Objeto que abstrae la conversión de imágenes formato .ppm
 */
public class Imagen extends Comprimido {
    protected int ancho;
    protected int alto;
    protected int maximoValorColor;

    private final int BYTES_POR_PIXEL = 3;

    public Imagen(String path, int ancho, int alto, int maxVal, byte[] data) {
        super(path);
        this.alto = alto;
        this.ancho = ancho;
        this.maximoValorColor = maxVal;
        this.setData(data);
    }

    public int getAncho() {
        return ancho;
    }

    public int getAlto() {
        return alto;
    }

    @Override
    public String toString() {
        String output = "Tiene " + this.getTamaño()/BYTES_POR_PIXEL + " píxels, " + this.ancho + "x" + this.alto +  "\n\n";
        return output;
    }



    /**
     * @brief Convierte una imagen en formato .ppm (P6) en la clase Imagen
     * @param path: Path de la imagen
     * @param dataPPM: Array de bytes con el metadata de ppm y los píxeles
     * @return Objeto Imagen
     */
    public static Imagen generarConPPM(String path, byte[] dataPPM) throws IOException {
        if (!((char) dataPPM[0] == 'P' && (char) dataPPM[1] == '6')) {
            throw new IOException("No es un .ppm válido");
        }
        // 2 es whitespace
        int index = 3;

        int[] metadataImagen = new int[3];

        for (int i = 0; i < metadataImagen.length; i++) {
            String metadata = ByteUtils.nextIntFromBytes(dataPPM, index);
            metadataImagen[i] = Integer.parseInt(metadata);
            index += metadata.length() + 1; //+1 por el espacio en blanco
        }

        return new Imagen(
                path,
                metadataImagen[0], metadataImagen[1], metadataImagen[2],
                Arrays.copyOfRange(dataPPM, index, dataPPM.length)
        );
    }


    /**
     * @brief Convierte la imagen en un array de bytes en formato .ppm (P6)
     * @param
     * @return Array de bytes con metadata y data
     */
    public byte[] convertirImagenEnPPM() {
        byte[] salida = new byte[this.getTamañoMetadata() + this.getTamaño()];
        salida[0] = 'P';
        salida[1] = '6';
        salida[2] = ' ';
        int indice = 3;
        for (char c: String.valueOf(this.ancho).toCharArray()) {
            salida[indice++] = (byte) c;
        }
        salida[indice++] = ' ';
        for (char c: String.valueOf(this.alto).toCharArray()) {
            salida[indice++] = (byte) c;
        }
        salida[indice++] = ' ';
        for (char c: String.valueOf(this.maximoValorColor).toCharArray()) {
            salida[indice++] = (byte) c;
        }
        salida[indice++] = '\n';

        for (int i = 0; i < getTamaño(); i++) {
            salida[indice++] = this.data[i];
        }
        return salida;
    }

    public int getTamañoMetadata() {
        return 6 + String.valueOf(this.ancho).length() + String.valueOf(this.alto).length() + String.valueOf(this.maximoValorColor).length();
    }

}
