package compresor.dominio;

import java.nio.ByteBuffer;
/**
 * @file MetadataImagen.java
 * @author Felipe Ramis
 * @brief Hereda de metadata para contener información sobre el ancho y alto de una imagen
 */
public class MetadataImagen extends Metadata {

    /* Las imágenes deberán guardar información extra sobre el ancho y alto de la imagen */
    private int ancho;
    private int alto;

    private static final int tamañoMetadata = Integer.BYTES * 2;

    public MetadataImagen(AlgoritmoEnum algoritmoUsado, Estadistica estadistica, String nombre, int ancho, int alto) {
        super(algoritmoUsado, estadistica, nombre);
        this.ancho = ancho;
        this.alto = alto;
    }


    /**
     * @brief Utiliza los bytes de la clase padre y le añade el ancho y alto
     * @param
     * @return Objeto en bytes
     */
    public byte[] getMetadataInBytes() {
        byte[] metadataOriginal = super.getMetadataInBytes();
        byte[] metadataFinal = new byte[metadataOriginal.length + tamañoMetadata];
        System.arraycopy(metadataOriginal, 0, metadataFinal, 0, metadataOriginal.length);

        byte[] anchoEnBytes = ByteBuffer.allocate(4).putInt(this.ancho).array();
        System.arraycopy(anchoEnBytes, 0, metadataFinal, metadataOriginal.length, Integer.BYTES);
        byte[] altoEnBytes = ByteBuffer.allocate(4).putInt(this.alto).array();
        System.arraycopy(altoEnBytes, 0, metadataFinal, metadataOriginal.length + Integer.BYTES, Integer.BYTES);

        return metadataFinal;
    }

    public int getAncho() {
        return this.ancho;
    }

    public int getAlto() {
        return this.alto;
    }
}
