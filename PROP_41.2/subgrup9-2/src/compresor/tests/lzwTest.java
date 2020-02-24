package compresor.tests;

import compresor.dominio.algoritmo.AlgoritmoLZW;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import org.junit.*;

import static org.junit.Assert.assertArrayEquals;


public class lzwTest {

    AlgoritmoLZW lzw;

    @Before
    public void before() {
        lzw = new AlgoritmoLZW();
    }

    @Test
    public void testComprimirArchivoPequeño() {
        System.out.println("Comprimiendo 6 bytes");
        byte[] data = {0, 1, 2, 3, 4, 5};
        byte[] dataComprimida = lzw.comprimir(data);
        byte[] dataDescomprimida = lzw.descomprimir(dataComprimida);
        assertArrayEquals(data, dataDescomprimida);
    }

    @Test
    public void testComprimirArchivoMediano() throws IOException {
        System.out.println("Comprimiendo archivo de 148 kB");
        File file = new File("src/compresor/tests/resources/alice29.txt");
        byte[] data = Files.readAllBytes(file.toPath());
        byte[] dataComprimida = lzw.comprimir(data);
        byte[] dataDescomprimida = lzw.descomprimir(dataComprimida);
        assertArrayEquals(data, dataDescomprimida);
    }

    @Test
    public void testComprimirArchivoGrande() throws IOException {
        System.out.println("Comprimiendo archivo de 5.2 MB");
        File file = new File("src/compresor/tests/resources/shakespeare.txt");
        byte[] data = Files.readAllBytes(file.toPath());
        byte[] dataComprimida = lzw.comprimir(data);
        byte[] dataDescomprimida = lzw.descomprimir(dataComprimida);
        assertArrayEquals(data, dataDescomprimida);
    }
}