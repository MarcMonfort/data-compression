package compresor.tests;

import compresor.dominio.algoritmo.AlgoritmoLZ78;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class lz78Test {

    AlgoritmoLZ78 lz78;

    @Before
    public void before() {
        lz78 = new AlgoritmoLZ78();
    }

    @Test
    public void testComprimirArchivoPequeño() {
        System.out.println("Comprimiendo 6 bytes");
        byte[] data = {0, 1, 2, 3, 4, 5};
        byte[] dataComprimida = lz78.comprimir(data);
        byte[] dataDescomprimida = lz78.descomprimir(dataComprimida);
        assertArrayEquals(data, dataDescomprimida);
    }

    @Test
    public void testComprimirArchivoMediano() throws IOException {
        System.out.println("Comprimiendo archivo de 148 kB");
        File file = new File("src/compresor/tests/resources/alice29.txt");
        byte[] data = Files.readAllBytes(file.toPath());
        byte[] dataComprimida = lz78.comprimir(data);
        byte[] dataDescomprimida = lz78.descomprimir(dataComprimida);
        assertArrayEquals(data, dataDescomprimida);
    }

    @Test
    public void testComprimirArchivoGrande() throws IOException {
        System.out.println("Comprimiendo archivo de 5.2 MB");
        File file = new File("src/compresor/tests/resources/shakespeare.txt");
        byte[] data = Files.readAllBytes(file.toPath());
        byte[] dataComprimida = lz78.comprimir(data);
        byte[] dataDescomprimida = lz78.descomprimir(dataComprimida);
        assertArrayEquals(data, dataDescomprimida);
    }
}