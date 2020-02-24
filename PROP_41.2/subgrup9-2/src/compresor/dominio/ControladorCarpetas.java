/** @file ControladorCarpetas.java
 * @author Marc Monfort
 * @brief Contiene métodos para comprimir y descomprimir carpetas
 */

package compresor.dominio;

import compresor.persistencia.ControladorGestorArchivo;

import java.io.File;
import java.nio.ByteBuffer;
import java.nio.file.Paths;

import static java.util.Arrays.copyOfRange;

public class ControladorCarpetas {

    private Comprimido comprimido;
    private Metadata metadata;
    private Estadistica estadistica;

    public ControladorCarpetas(String pathArchivo) {
        this.comprimido = new Comprimido(pathArchivo);
    }



    private byte[] comprimirCarpeta(final File folder) { //recursivo
        byte[] padre = new byte[0];
        byte[] meta_padre = new byte[0];  // 1==isDirectory

        int particiones = folder.listFiles().length; //error java.lang.NullPointerException

        try {
            for (final File fileEntry : folder.listFiles()) {

                byte[] hijo;
                if (fileEntry.isDirectory()) {
                    hijo = comprimirCarpeta(fileEntry);

                } else {

                    ControladorCompresor compresor = new ControladorCompresor(fileEntry.getAbsolutePath());

                    hijo = compresor.comprimirFichero(ControladorGestorArchivo.leerArchivoEnBytes(fileEntry.getAbsolutePath()));


                    //System.out.println(fileEntry.getName());
                }

                byte[] aux = new byte[padre.length + hijo.length];
                System.arraycopy(padre, 0, aux, 0, padre.length);
                System.arraycopy(hijo, 0, aux, padre.length, hijo.length);
                padre = aux;    //por valor??? (eso espero!) posible ERROR


                byte[] size_part = ByteBuffer.allocate(4).putInt(hijo.length).array();
                aux = new byte[meta_padre.length + size_part.length];
                //byte[] size = {(byte) hijo.length};
                System.arraycopy(meta_padre, 0, aux, 0, meta_padre.length);
                System.arraycopy(size_part, 0, aux, meta_padre.length, size_part.length);
                meta_padre = aux;

            }
        }catch (Exception e){ System.out.println("mmmm:   " + e);}

        byte[] nombreBytes = folder.getName().getBytes();    //puede que pete si nombre > 256 bytes +-
        if (nombreBytes.length > 255) nombreBytes = folder.getName().substring(0,128).getBytes();
        byte[] tamañoNombre = {(byte) nombreBytes.length}; //en bytes
        byte[] numParticiones = {(byte) particiones};
        byte[] isDirectory = {(byte) 0}; //yes
        byte[] tamañoTotal = {(byte) (isDirectory.length + meta_padre.length + nombreBytes.length + tamañoNombre.length + numParticiones.length +  1)};

        byte[] salida = new byte[(tamañoTotal[0] & 0xFF) + padre.length];

        int offset = 0;
        System.arraycopy(tamañoTotal, 0, salida, offset, 1);
        offset += tamañoTotal.length;
        System.arraycopy(isDirectory, 0, salida, offset, isDirectory.length);
        offset += isDirectory.length;
        System.arraycopy(tamañoNombre, 0, salida, offset, tamañoNombre.length);
        offset += tamañoNombre.length;
        System.arraycopy(nombreBytes, 0, salida, offset, nombreBytes.length);
        offset += nombreBytes.length;
        System.arraycopy(numParticiones, 0, salida, offset, numParticiones.length);
        offset += numParticiones.length;
        System.arraycopy(meta_padre, 0, salida, offset, meta_padre.length);
        offset += meta_padre.length;
        System.arraycopy(padre, 0, salida, offset, padre.length);


        return salida;
    }

    public byte[] comprimirCarpeta() {
        File file = new File(this.comprimido.getPath());
        return comprimirCarpeta(file);
    }




    private void descomprimirCarpeta(byte[] dataComprimida, String path) {
        try {
            boolean esCarpeta = (dataComprimida[1] & 0xFF) == 0;

            if (esCarpeta) {

                int tamañoTotal = dataComprimida[0] & 0xFF;
                //AlgoritmoEnum tipoAlgoritmo = AlgoritmoEnum.values()[(dataComprimida[1] & 0xFF)-1];
                int tamañoNombre = dataComprimida[2] & 0xFF;
                String nombre = new String(copyOfRange(dataComprimida, 3, 3 + tamañoNombre));


                int offset = tamañoNombre + 3;
                int numParticiones = dataComprimida[offset] & 0xFF;
                ++offset;


                String path_nuevo = path + File.separator + nombre;
                File tempFile = new File(path_nuevo);
                if (tempFile.exists()) {
                    int i = 1;
                    do {
                        String aux =  path + File.separator + nombre +  '_' + i;
                        ++i;
                        tempFile = new File(aux);
                        path_nuevo = aux;
                    } while (tempFile.exists());
                }
                tempFile.mkdir();   //hacer try y excepcion






                int acumulado = 0;
                for (int i = 0; i < 4 * numParticiones; i += 4) {

                    int size_i = ByteBuffer.wrap(copyOfRange(dataComprimida, offset + i, offset + i + 4)).getInt();
                    descomprimirCarpeta(copyOfRange(dataComprimida, tamañoTotal+acumulado, tamañoTotal + acumulado + size_i), path_nuevo);
                    acumulado += size_i;
                }


            } else {
                ControladorDescompresor descompresor = new ControladorDescompresor(this.comprimido.getPath());
                byte[] dataDescomprimida = descompresor.descomprimirFichero(dataComprimida);
                String path_final = path + File.separator + descompresor.getComprimido().getMetadata().getName();                                 //adaptar a cambios!!!
                ControladorGestorArchivo.guardarArchivoEnBytes(path_final, dataDescomprimida);
            }
        }catch (Exception e){ System.out.println(e);}

    }

    public void descomprimirCarpeta(byte[] dataComprimida) {
        String path = Paths.get(this.comprimido.getPath()).getParent().toString();
        descomprimirCarpeta(dataComprimida, path);
    }



    public String getName() {
        return this.comprimido.getName();
    }

    public String getExtension() {
        return this.metadata.getExtension();
    }

}
