package compresor.tests;

import compresor.dominio.algoritmo.AlgoritmoJPEG;
import compresor.utils.DCT;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;


public class AlgoritmoJPEGTest {

    AlgoritmoJPEG jpeg;

    int[] mockVector = {16, 11, 10, 16, 24, 40, 51, 61, 12, 12, 14, 19, 26, 58, 60, 55,
                        14, 13, 16, 24, 40, 57, 69, 56, 14, 17, 22, 29, 51, 87, 80, 62,
                        18, 22, 37, 56, 68, 109, 103, 77, 24, 45, 55, 64, 81, 104, 113, 92,
                        49, 64, 78, 87, 103, 121, 120, 101, 72, 92, 95, 98, 112, 100, 103, 99};
    int[][] mockBlock = {
            {16, 11, 10, 16, 24, 40, 51, 61},
            {12, 12, 14, 19, 26, 58, 60, 55},
            {14, 13, 16, 24, 40, 57, 69, 56},
            {14, 17, 22, 29, 51, 87, 80, 62},
            {18, 22, 37, 56, 68, 109, 103, 77},
            {24, 45, 55, 64, 81, 104, 113, 92},
            {49, 64, 78, 87, 103, 121, 120, 101},
            {72, 92, 95, 98, 112, 100, 103, 99}
    };

    @Before
    public void init() {
        jpeg = new AlgoritmoJPEG();
        jpeg.prepareNewImage(16, 16);
    }

    @Test
    public void get_same_pixel_when_color_space_changed() {
        byte[] pixel = {(byte)255, (byte)255, (byte)255};
        int[] pixelYCBCR = jpeg.toYCbCr(pixel[0], pixel[1], pixel[2]);
        assertArrayEquals(pixel, jpeg.toRGB(pixelYCBCR[0], pixelYCBCR[1], pixelYCBCR[2]));
    }

    @Test
    public void get_same_value_when_q () {
        double value = 47.0;
        double qvalue = DCT.quantization(value, 2, 2);
        int uqvalue = DCT.unquantization((int) qvalue, 2, 2);
        boolean sonIguales = Math.abs((int)value - uqvalue) <= 2;

        assertTrue(sonIguales);
    }

    @Test
    public void get_same_block_when_zigzag () {
        double[][] doubleMockBlock = new double[8][8];
        for (int i = 0; i< 8; i++) {
            for (int j = 0; j < 8; j++) {
                doubleMockBlock[i][j] = mockBlock[i][j];
            }
        }
        ArrayList<Integer> vector = DCT.toZigZagMatrix(doubleMockBlock);
        int[] vectorArray = vector.stream().mapToInt(i -> i).toArray();
        assertArrayEquals(mockBlock,DCT.inverseZigZag(vectorArray));
    }

}
