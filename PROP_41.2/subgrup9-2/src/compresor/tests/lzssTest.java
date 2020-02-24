package compresor.tests;

import compresor.dominio.algoritmo.AlgoritmoLZSS;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class lzssTest {

    AlgoritmoLZSS lzss;

    @Before
    public void before() {
        lzss = new AlgoritmoLZSS();
    }

    @Test
    public void testComprimirArchivoPequeño() {
        System.out.println("Comprimiendo 6 bytes");
        byte[] data = {0, 1, 2, 3, 4, 5};
        byte[] dataComprimida = lzss.comprimir(data);
        byte[] dataDescomprimida = lzss.descomprimir(dataComprimida);
        assertArrayEquals(data, dataDescomprimida);
    }

    @Test
    public void testComprimirArchivoMediano() throws IOException {
        System.out.println("Comprimiendo archivo de 148 kB");
        File file = new File("src/compresor/tests/resources/alice29.txt");
        byte[] data = Files.readAllBytes(file.toPath());
        byte[] dataComprimida = lzss.comprimir(data);
        byte[] dataDescomprimida = lzss.descomprimir(dataComprimida);
        assertArrayEquals(data, dataDescomprimida);
    }

    @Test
    public void testComprimirArchivoGrande() throws IOException {
        System.out.println("Comprimiendo archivo de 5.2 MB");
        File file = new File("src/compresor/tests/resources/shakespeare.txt");
        byte[] data = Files.readAllBytes(file.toPath());
        byte[] dataComprimida = lzss.comprimir(data);
        byte[] dataDescomprimida = lzss.descomprimir(dataComprimida);
        assertArrayEquals(data, dataDescomprimida);
    }
}