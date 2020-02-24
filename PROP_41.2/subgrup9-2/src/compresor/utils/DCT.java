package compresor.utils;

import java.util.ArrayList;

import static compresor.dominio.algoritmo.AlgoritmoJPEG.BLOC_SIZE;

/**
 * @file DCT.java
 * @author Felipe Ramis
 * @brief Contiene métodos aplicar la cuantización, transformación discreta del coseno y zigzag
 */
public class DCT {

    private int quality;

    private static final double factorEnCero = 1 / Math.sqrt(BLOC_SIZE);
    private static final double factorNoEnCero = Math.sqrt(2) / Math.sqrt(BLOC_SIZE);
    private static final int[] IDCTcoefficients = {1/(int)Math.sqrt(2.0), 1, 1, 1, 1, 1, 1, 1};

    private static final int[][] quantizationMatrix50 = {
            {16, 11, 10, 16, 24, 40, 51, 61},
            {12, 12, 14, 19, 26, 58, 60, 55},
            {14, 13, 16, 24, 40, 57, 69, 56},
            {14, 17, 22, 29, 51, 87, 80, 62},
            {18, 22, 37, 56, 68, 109, 103, 77},
            {24, 45, 55, 64, 81, 104, 113, 92},
            {49, 64, 78, 87, 103, 121, 120, 101},
            {72, 92, 95, 98, 112, 100, 103, 99}
    };


    /**
     * @brief Aplica la transformación discreta del coseno a un bloque 8x8. Dentro se hace la cuantización
     * @param MCU: MCU hace referencia a la unidad mínima de información dentro del algoritmo JPEG
     * @return Devuelve el mismo bloque 8x8 transformado y en formato double
     */
    public static double[][] applyDCT(int MCU[][]) {
        /* Para hacer más eficiente la transformación, ya directamente se aplica la quantization */
        // DCT + Q
        double dct1, sum;
        double[][] dctBlock = new double[BLOC_SIZE][BLOC_SIZE];

        for (int i = 0; i < BLOC_SIZE; i++) {
            for (int j = 0; j < BLOC_SIZE; j++) {
                // Sum es el sumatorio de las señales de coseno
                sum = 0;
                for (int k = 0; k < BLOC_SIZE; k++) {
                    for (int l = 0; l < BLOC_SIZE; l++) {
                        dct1 = normalizeValueByShifting(MCU[k][l]) *
                                Math.cos((2 * k + 1) * i * Math.PI / (2 * BLOC_SIZE)) *
                                Math.cos((2 * l + 1) * j * Math.PI / (2 * BLOC_SIZE));
                        sum = sum + dct1;
                    }
                }
                /* scaleFactor para hacer la transformación ortonormal  (Vector unitario + ortogonal) */
                // La cuantización se aplica después de DCT
                dctBlock[i][j] = quantization(scaleFactor(i, j) * sum, i, j);
            }
        }
        return dctBlock;
    }


    /**
     * @brief Aplica la inversa de la transformación discreta del coseno a un bloque 8x8. Dentro se hace la descuantización
     * @param DCTblock: Bloque 8x8 discretizado y cuantificado
     * @return Devuelve el bloque descuantificado y no discretizado. MCU original
     */
    public static int[][] applyIDCT(int DCTblock[][]) {
        /* Antes de pasar la inversa del coseno, se tiene que descuantificar con un producto hadamar de matrices (correspondecia de posiciones) */
        int[][] mcu = new int[BLOC_SIZE][BLOC_SIZE];
        for (int i=0; i < BLOC_SIZE; i++) {
            for (int j=0; j < BLOC_SIZE; j++) {
                int sum = 0;
                for (int u=0; u < BLOC_SIZE; u++) {
                    for (int v=0; v < BLOC_SIZE; v++) {
                        sum+=scaleFactor(u, v)*Math.cos(((2*i+1)/(2.0*BLOC_SIZE))*u*Math.PI)*Math.cos(((2*j+1)/(2.0*BLOC_SIZE))*v*Math.PI)*(unquantization(DCTblock[u][v], u, v));
                    }
                }
                mcu[i][j]= sum + 127;
            }
        }
        return mcu;
    }


    /**
     * @brief Descuantifica un valor
     * @param dctValue: valor a descuantificar
     * @param fila
     * @param columna
     * @return Devuelve el valor descuantificado
     */
    public static int unquantization(int dctValue, int fila, int columna) {
        return dctValue*quantizationMatrix50[fila][columna];
    }


    /**
     * @brief Divide los valores de la discreta del coseno para cuantificarlos (depende de la calidad. Se ha utilizado 50%)
     * @param dctValue: valor a cuantificar
     * @param fila
     * @param columna
     * @return Devuelve el valor cuantificado
     */
    public static double quantization(double dctValue, int fila, int columna) {
        /* Normalmente depente de la calidad -> Por defecto 50% según el estándar */
        return Math.round(dctValue/quantizationMatrix50[fila][columna]);
    }


    /**
     * @brief Recorre una matriz bidimensional en zigzag y guarda los valores en una lista
     * @param bloque: matriz de 8x8 doubles
     * @return Lista de enteros una vez se ha recorrido la matriz en zigzag
     */
    public static ArrayList<Integer> toZigZagMatrix(double[][] bloque) {
        int row = 0, col = 0;

        ArrayList<Integer> secuencia = new ArrayList<>();

        boolean row_inc = false;

        /*
            1º recorrido sobre el lado mínimo (len <= Math.min(width, height))
            2º recorrido sobre el lado máximo (len <= Math.max(width, height))
            pero al ser un cuadrado mínimo y máximo son iguales
         */
        for (int len = 1; len <= BLOC_SIZE; ++len) {
            for (int i = 0; i < len; ++i) {
                secuencia.add((int) bloque[row][col]);
                if (i + 1 == len)
                    break;
                /* Diagonal */
                if (row_inc) {
                   ++row;
                   --col;
                } else {
                   --row;
                   ++col;
                }
            }

            if (len == BLOC_SIZE)
                // Hemos llegado a un borde
                break;

            // Actualizamos tras el último incremento
            if (row_inc) {
                ++row;
                row_inc = false;
            } else {
                ++col;
                row_inc = true;
            }
        }

        /* Tras hacer el primer recorrido actualizamos */
        if (row == 0) {
            if (col == BLOC_SIZE - 1)
                ++row;
            else
                ++col;
            row_inc = true;
        } else {
            if (row == BLOC_SIZE - 1)
                ++col;
            else
                ++row;
            row_inc = false;
        }

        // 2º recorrido
        for (int len, diag = BLOC_SIZE-1; diag > 0; --diag) {

            if (diag > BLOC_SIZE)
                len = BLOC_SIZE;
            else
                len = diag;

            for (int i = 0; i < len; ++i) {
                secuencia.add((int) bloque[row][col]);

                if (i + 1 == len)
                    break;

                if (row_inc) {
                    ++row;
                    --col;
                } else {
                    ++col;
                    --row;
                }
            }

            if (row == 0 || col == BLOC_SIZE - 1) {
                if (col == BLOC_SIZE - 1)
                    ++row;
                else
                    ++col;

                row_inc = true;
            }

            else if (col == 0 || row == BLOC_SIZE - 1) {
                if (row == BLOC_SIZE - 1)
                    ++col;
                else
                    ++row;

                row_inc = false;
            }
        }
        return secuencia;
    }


    /**
     * @brief Convierte un vector en una matriz 8x8. Si el vector no tiene 64 valores, se rellena con ceros
     * @param vector: Vector de 64 valores
     * @return Devuelve un bloque MCU 8x8 (con Q y DCT aplicados)
     */
    public static int[][] inverseZigZag(int[] vector) {
        //inverse transform from the zigzag format to the matrix form
        int[][] matrix = new int[BLOC_SIZE][BLOC_SIZE] ; //Init con ceros por defecto en Java

        int cur_row=0, cur_col=0, cur_index=0;

        while (cur_index < vector.length) {
            if (cur_row == 0 && (cur_row+cur_col)%2 == 0 && cur_col != BLOC_SIZE-1) {
                matrix[cur_row][cur_col++]=vector[cur_index++];
                //move right at the top
            }
            else if (cur_row==BLOC_SIZE-1 && (cur_row+cur_col)%2 != 0 && cur_col != BLOC_SIZE-1) {
                matrix[cur_row][cur_col++] = vector[cur_index++];
                //move right at the bottom
            }
            else if (cur_col == 0 && (cur_row+cur_col)%2 != 0 && cur_row != BLOC_SIZE-1) {
                matrix[cur_row++][cur_col]=vector[cur_index++];
                //move down at the left
            }
            else if (cur_col==BLOC_SIZE-1 && (cur_row+cur_col)%2 == 0 && cur_row != BLOC_SIZE-1) {
                matrix[cur_row++][cur_col]=vector[cur_index++];
                //move down at the right
            }
            else if (cur_col != 0 && cur_row != BLOC_SIZE-1 && (cur_row+cur_col)%2 != 0) {
                matrix[cur_row++][cur_col--]=vector[cur_index++];
                //move diagonally left down
            }
            else if (cur_row != 0 && cur_col != BLOC_SIZE && (cur_row+cur_col)%2 == 0) {
                matrix[cur_row--][cur_col++]=vector[cur_index++];
            	//move diagonally right up
            }
        }
        return matrix;
    }

    /**
     * @brief Aplica un desplazamiento al valor para llevarlo a unos rangos menores representables con un byte
     * @param value
     * @return Entero desplazado
     */
    private static int normalizeValueByShifting(int value) {
        return value - 127;
    }


    /**
     * @brief Transformación ortonormal  (Vector unitario + ortogonal)
     * @param i: fila
     * @param j: columna
     * @return El factor de escala respecto al vector
     */
    private static double scaleFactor(int i, int j) {
        /* El factor depende de la frecuencia y del número de filas y columnas */
        double factorI = (i == 0) ? factorEnCero: factorNoEnCero;
        double factorJ = (j == 0) ? factorEnCero: factorNoEnCero;

        return factorI * factorJ;
    }
}
