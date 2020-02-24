/**
 * @file AlgoritmoJPEG.java
 * @Author Felipe Ramis
 * @brief Implementación del algoritmo de compresión/descompresión JPEG
 */

package compresor.dominio.algoritmo;

import compresor.utils.*;

import java.util.ArrayList;

/**
 * @class AlgoritmoJPEG
 * @brief Compresión/descompresión mediante LZ78
 */
public class AlgoritmoJPEG implements Algoritmo {
    private int width;
    private int height;
    private boolean wRegular;
    private boolean hRegular;

    private int bloquesVerticales;
    private int bloquesHorizontales;

    public static final int BLOC_SIZE = 8; //Depende del downsampling



    public AlgoritmoJPEG() {}

    /**
     * @brief Prepara valores del metadata de una imagen que usará el algoritmo
     * @param width: ancho de la imagen
     * @param height: altura de la imagen
     * @return
     */
    public void prepareNewImage(int width, int height) {
        this.width = width;
        this.height = height;
        this.wRegular = width % BLOC_SIZE == 0;
        this.hRegular = height % BLOC_SIZE == 0;
        this.bloquesVerticales = (hRegular) ? height/BLOC_SIZE : height/BLOC_SIZE + 1;
        this.bloquesHorizontales = (wRegular) ? width/BLOC_SIZE : width/BLOC_SIZE + 1;
    }

    /* Felipe */
    /**
     * @brief Compresión de un archivo aplicando el algoritmo JPEG
     * @param entrada
     * @return Archivo comprimido
     */
    @Override
    public byte[] comprimir(byte[] entrada) {

        /* 1. Color space transformation */
        int[] yVector = new int[entrada.length/3]; //data.length/3 (3 channels)
        int[] cbVector = new int[entrada.length/3];
        int[] crVector = new int[entrada.length/3];
        for (int i = 0; i < entrada.length; i+=3) {
            int[] colorVector = this.toYCbCr(entrada[i], entrada[i+1], entrada[i+2]);
            yVector[i/3] = colorVector[0];
            cbVector[i/3] = colorVector[1];
            crVector[i/3] = colorVector[2];
        }
        /* 2. Downsampling -> Using 4:4:4
        *   Por ahora nada -> Se puede intentar 4:2:0 16x16
        */

        /* 3. Split in 8x8 blocks */
        int[][][] vectorMCU_Y = this.blockSplitting(yVector);
        int[][][] vectorMCU_Cb = this.blockSplitting(cbVector);
        int[][][] vectorMCU_Cr = this.blockSplitting(crVector);

        /* 4. Discrete Cosine Transformation DCT and Coefficient Quantization */

        double[][][] dctYBlocks = this.convertToDCT(vectorMCU_Y);
        double[][][] dctCbBlocks = this.convertToDCT(vectorMCU_Cb);
        double[][][] dctCrBlocks = this.convertToDCT(vectorMCU_Cr);

        /* 5. Zigzag y encoding */
        return Huffman.encode(
                this.compressDCT(dctYBlocks),
                this.compressDCT(dctCbBlocks),
                this.compressDCT(dctCrBlocks)
        );
    }

    /**
     * @brief Descompresión de un archivo comprimido mediante JPEG
     * @param entrada
     * @return Archivo descomprimido
     */
    @Override
    public byte[] descomprimir(byte[] entrada) {
        /* La entrada con listas de enteros donde [Y, Cb, Cr] hay separación de canales
        Y después cada canal tiene una lista de bloques separados por 127 [bloque1]127[bloque2]
        Y cada bloque   [DC AC1 AC2 .. ACN] donde N puede no ser 64
         */
        int indice = 0;
        int[][][] bloqueY = new int[bloquesHorizontales*bloquesVerticales][BLOC_SIZE][BLOC_SIZE];
        int[][][] bloqueCb = new int[bloquesHorizontales*bloquesVerticales][BLOC_SIZE][BLOC_SIZE];
        int[][][] bloqueCr = new int[bloquesHorizontales*bloquesVerticales][BLOC_SIZE][BLOC_SIZE];

        for (int canal = 0; canal < 3; canal++) {
            for (int bloque = 0; bloque < bloquesVerticales*bloquesHorizontales; bloque++) {
                int value = entrada[indice++];
                ArrayList<Integer> listaBloque = new ArrayList<>();
                while (!this.esFinalDeBloque(value)) {
                    listaBloque.add(value);
                    value = entrada[indice++];
                }
                int[] vector = listaBloque.stream().mapToInt(i -> i).toArray();
                switch (canal) {
                    case 0: bloqueY[bloque] = DCT.inverseZigZag(vector); break;
                    case 1: bloqueCb[bloque] = DCT.inverseZigZag(vector); break;
                    case 2: bloqueCr[bloque] = DCT.inverseZigZag(vector); break;
                }
            }
        }
        byte[] salida = new byte[width*height*3];
        for (int b = 0; b < bloquesHorizontales*bloquesVerticales; b++) {
            // Aplicamos la inversa de Q, y IDCT además de devolver el factor de normalización
            bloqueY[b] = DCT.applyIDCT(bloqueY[b]);
            bloqueCb[b] = DCT.applyIDCT(bloqueCb[b]);
            bloqueCr[b] = DCT.applyIDCT(bloqueCr[b]);

            /* Sólo falta convertir al espacio RGB y juntar los 3 canales */
            int maxI = (!hRegular && (b/bloquesHorizontales) == bloquesVerticales-1) ? height % BLOC_SIZE : BLOC_SIZE;
            int maxJ = (!wRegular && (b%bloquesHorizontales) == bloquesHorizontales-1) ? width % BLOC_SIZE : BLOC_SIZE;

            //Funciona sólo con imagenes regulares
            int hActual = b/bloquesVerticales;
            int wActual = b%bloquesHorizontales;
            for (int i = 0; i < maxI; i++) {
                for (int j = 0; j < maxJ; j++) {
                    byte[] rgbVector = this.toRGB(bloqueY[b][i][j], bloqueCb[b][i][j], bloqueCr[b][i][j]);
                    int ind = (hActual * width * BLOC_SIZE) + (wActual * BLOC_SIZE) + (i * width) + j;
                    //int ind = (hActual * bloquesHorizontales * 64) + (wActual * BLOC_SIZE) + (i * 8) + j;
                    salida[ind*3] = rgbVector[0];
                    salida[ind*3+1] = rgbVector[1];
                    salida[ind*3+2] = rgbVector[2];

                }
            }

        }
        return salida;
    }

    /**
     * @brief Convierte un vector unidimensional en una matrix que contiene bloques de 8x8
     * @param vector
     * @return vector de matrices 8x8
     */
    public int[][][] blockSplitting(int[] vector) {
        int[][][] mcuBlocks = new int[bloquesHorizontales*bloquesVerticales][BLOC_SIZE][BLOC_SIZE];
        // Recorrido = O(n*m)
        /* Cálculo índice vector = ((h*BLOC_SIZE + hb) * width) + (w*BLOC_SIZE + wb) */
        int indiceVector = 0;
        for (int h = 0; h < bloquesVerticales; h++) {
            int maxI = (!hRegular && h == bloquesVerticales-1) ? height % BLOC_SIZE : BLOC_SIZE;

            for (int hb = 0; hb < maxI; hb++) {
                // Recorrido sobre el vector que tendrá tantos width como height. w0,w1..wh (h=height)
                // Se recorre por todos los bloques y luego cada bloque por separado

                for (int w = 0; w < bloquesHorizontales; w++) {
                    int maxJ = (!wRegular && w == bloquesHorizontales-1) ? width % BLOC_SIZE : BLOC_SIZE;
                    for (int wb = 0; wb < maxJ; wb++) {
                        mcuBlocks[h*bloquesHorizontales + w][hb][wb] = vector[indiceVector++];
                    }
                    if (!wRegular && w == bloquesHorizontales - 1) {
                        for (int wb = maxJ; wb < BLOC_SIZE; wb++) {
                            // Rellena con los bordes
                            mcuBlocks[h*bloquesHorizontales + w][hb][wb] = vector[indiceVector - 1];
                        }
                    }
                }
            }
            if (!hRegular && h == bloquesVerticales - 1) {
                for (int hb = maxI; hb < BLOC_SIZE; hb++) {
                    // Rellena con los bordes en la parte de abajo
                    for (int w = 0; w < bloquesHorizontales; w++) {
                        for (int wb = 0; wb < BLOC_SIZE; wb++) {
                            mcuBlocks[h*bloquesHorizontales + w][hb][wb] = mcuBlocks[h*bloquesHorizontales + w][maxI - 1][wb];
                        }
                    }
                }
            }

        }
        return mcuBlocks;
    }

    /**
     * @brief Transforma los bloques a un vector recorriendo en zigzag y comprime los ceros finales
     * @param dctBlocks: bloques 8x8 una vez ya aplicado la transformación discreta del coseno
     * @return Lista de bytes comprimidos, separando cada bloque con el entero 127 (separador)
     */
    public ArrayList<Integer> compressDCT(double[][][] dctBlocks) {
        ArrayList<Integer> dataCompressed = new ArrayList<>();
        for (int b = 0; b < bloquesHorizontales*bloquesVerticales; b++) {
            ArrayList<Integer> blockList = DCT.toZigZagMatrix(dctBlocks[b]);
            // Eliminamos ceros por la derecha
            int nonzeroIndex = blockList.size() - 1;
            while (nonzeroIndex != 0 && blockList.get(nonzeroIndex) == 0) {
                nonzeroIndex--;
            }
            dataCompressed.addAll(blockList.subList(0, nonzeroIndex+1));
            dataCompressed.add(127);
        }
        return dataCompressed;
    }

    /**
     * @brief Devuelve true si el valor es el separados
     * @param valor
     * @return Es el valor separador
     */
    public boolean esFinalDeBloque(int valor) {
        return valor == 127;
    }

    /**
     * @brief Aplica la transformación discreta del coseno a los bloques 8x8
     * @param mcuBlocks: bloques 8x8 de un canal de color o luminancia
     * @return bloques 8x8 transformados y cuantificados
     */
    public double[][][] convertToDCT(int[][][] mcuBlocks) {
        double[][][] dctBlocks = new double[bloquesHorizontales*bloquesVerticales][BLOC_SIZE][BLOC_SIZE];
        for (int b = 0; b < bloquesVerticales*bloquesHorizontales; b++) {
            dctBlocks[b] = DCT.applyDCT(mcuBlocks[b]);
        }
        return dctBlocks;
    }

    /**
     * @brief Transforma un pixel del espacio YCbCr a RGB
     * @param Y: Luminanci
     * @param Cb: Croma azul
     * @param Cr: Croma rojo
     * @return Array de de 3 bytes rgb
     */
    public byte[] toRGB(int Y, int Cb, int Cr) {
        int red = (int) (Y + (1.402 * (Cr - 128)));
        int green = (int) (Y - (0.34414 * (Cb - 128)) - (0.71414 * (Cr - 128)));
        int blue = (int) (Y + 1.772 * (Cb - 128));

        /*
        Otro método
        int red = (int) (1.164 * (Y - 16) + 1.596 * (Cr - 128));
        int green = (int) (1.164 * (Y - 16) - 0.813 * (Cr - 128) - 0.391 * (Cb - 128));
        int blue = (int) (1.164 * (Y - 16) + 2.018 * (Cb - 128));
        */
        red = Math.max(0, Math.min(255, red));
        green = Math.max(0, Math.min(255, green));
        blue = Math.max(0, Math.min(255, blue));

        return new byte[] { (byte) red, (byte) green, (byte) blue};
    }

    /**
     * @brief Transforma un pixel del espacio RGB a YCbCr separando la luminancia del croma
     * @param red: color rojo
     * @param blue: color azul
     * @param green: color verde
     * @return Array de 3 enteros YCBCr
     */
    public int[] toYCbCr(byte red, byte green, byte blue) {
        /* Los valores deben estar entre 0 y 255 */

        int valorRojo = ByteUtils.convertByteToUnsignedInt(red);
        int valorVerde = ByteUtils.convertByteToUnsignedInt(green);
        int valorAzul = ByteUtils.convertByteToUnsignedInt(blue);
        int y = (int) ((0.299* valorRojo) + (0.587 * valorVerde) + (0.114 * valorAzul));
        int Cb = 128 + (int)((-0.168736 * valorRojo) - (0.331264 * valorVerde) + (0.5 * valorAzul));
        int Cr = 128 +  (int)((0.5 * valorRojo) - (0.418688 * valorVerde) - (0.081312 * valorAzul));

        /*
        Otro método
        int y =  (int)( (0.257 * valorRojo) + (0.504 * valorVerde) + (0.098 * valorAzul)) + 16;
        int Cb = (int)( (-0.148 * valorRojo) - (0.291 * valorVerde) + (0.439 * valorAzul) ) + 128;
        int Cr = (int)( (0.439 * valorRojo) - (0.368 * valorVerde) - (0.071 * valorAzul) ) + 128;
        */
        return new int[] {y, Cb, Cr};
    }
}
