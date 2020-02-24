    /** @file ControladorGestorArchivos.java
     * @author Felipe Ramis
     @brief Especificación del controlador de gestión de archivos
     */

    package compresor.persistencia;

    import javax.swing.*;
    import javax.swing.filechooser.FileNameExtensionFilter;
    import java.io.*;

    import java.io.File;
    import java.io.IOException;

    /** @class ControladorGestorArchivos
     @brief Controlador de gestión de archivos
     Contiene funciones para obtener el path de un archivo, así
     como para leerlo y escribir en ellos.
     */


    public class ControladorGestorArchivo {

        /** @brief Lectura de un fichero.
        \pre
        \post Obtenemos un array de bytes con la información
        del texto que hay en el archivo con ruta path,
        si es que este existe
         */
        public static byte[] leerArchivoEnBytes(String path) {
            File file = new File(path);
            FileInputStream fileInputStream;
            byte[] bFile = new byte[(int) file.length()];
            try
            {
                fileInputStream = new FileInputStream(file);
                fileInputStream.read(bFile);
                fileInputStream.close();

            } catch (FileNotFoundException e) {
                logError("Error: Archivo no encontrado");
            } catch (IOException e) {
                logError("Error: Error leyendo el archivo");
            }
            return bFile;
        }

        /** @brief Guardado de un fichero.
        \pre
        \post Guardamos los bytes de data en un fichero con ruta
        path, si es que existe
         */
        public static void guardarArchivoEnBytes(String path, byte[] data) {
            try {
                // Con este código se agregan los bytes al archivo.
                FileOutputStream fileOutputStream = new FileOutputStream(path);
                fileOutputStream.write(data);
                fileOutputStream.close();
            } catch (IOException e) {
                logError(e.getMessage());
            }
        }

        /** @brief Obtención del path de un fichero para su compresión.
        \pre
        \post Devolvemos la ruta de un fichero que selecciona el usuario para
        comprimirlo, a no ser que cancele la selección
         */
        public static String getPathEntrada() throws IncorrectFileException {
            //Mostrar la ventana para elegir archivo de entrada, por defecto en la carpeta en la que están guardados.
            JFileChooser jFileChooser = new JFileChooser(getCurrentDirectory());
            int resultado = jFileChooser.showOpenDialog(null);

            //Si se ha cancelado, se sale.
            if (resultado == JFileChooser.CANCEL_OPTION) {
                throw new IncorrectFileException("Se ha cancelado la selección del archivo de entrada.");
            }

            //Si no existe el archivo, se sale.
            if (! jFileChooser.getSelectedFile().exists()) {
                throw new IncorrectFileException("No existe el archivo de entrada indicado.");
            }

            //Si existe el archivo, se devuelve su path.
            return jFileChooser.getSelectedFile().getAbsolutePath();
        }

        /** @brief Obtención del path de un fichero para su descompresión.
        \pre
        \post Devolvemos la ruta de un fichero con extensión ".prop" que selecciona
        el usuario para descomprimirlo, a no ser que cancele la selección
         */
        public static String getPathEntrada2() throws IncorrectFileException {
            //Mostrar la ventana para elegir archivo de entrada, por defecto en la carpeta en la que están guardados.
            JFileChooser jFileChooser = new JFileChooser(getCurrentDirectory());

            FileNameExtensionFilter filter = new FileNameExtensionFilter("PROP", "prop", "prop");
            jFileChooser.setFileFilter(filter);

            int resultado = jFileChooser.showOpenDialog(null);

            //Si se ha cancelado, se sale.
            if (resultado == JFileChooser.CANCEL_OPTION) {
                throw new IncorrectFileException("Se ha cancelado la selección del archivo de entrada.");
            }

            //Si no existe el archivo, se sale.
            if (! jFileChooser.getSelectedFile().exists()) {
                throw new IncorrectFileException("No existe el archivo de entrada indicado.");
            }

            //Si existe el archivo, se devuelve su path.
            return jFileChooser.getSelectedFile().getAbsolutePath();
        }

        public static String getPathDirectory() throws IncorrectFileException {
            //NO SE USA - no hace falta??

            JFrame parentFrame = new JFrame();

            JFileChooser jFileChooser = new JFileChooser(getCurrentDirectory());

            jFileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

            int resultado = jFileChooser.showSaveDialog(parentFrame);

            //Si se ha cancelado, se sale.
            if (resultado == JFileChooser.CANCEL_OPTION) {
                throw new IncorrectFileException("Se ha cancelado la selección del archivo de entrada.");
            }

            //Si no existe el archivo, se sale.
            if (! jFileChooser.getSelectedFile().exists()) {
                throw new IncorrectFileException("No existe el archivo de entrada indicado.");
            }

            //Si existe el archivo, se devuelve su path.

            jFileChooser.setSelectedFile(new File(jFileChooser.getSelectedFile().getAbsolutePath()));
            return jFileChooser.getSelectedFile().getAbsolutePath();
        }
    
        /** @brief Borra un fichero temporal.
        \pre El fichero en temp_path existe
        \post El fichero de temp_path deja de existir
         */
        public static void borrarFicheroTemporal(String path_dir, String temp_path) {
            File temp_file1 = new File(temp_path);
            temp_file1.deleteOnExit();
            File temp_file2 = new File(path_dir + ".prop");
            temp_file2.deleteOnExit();
        }

        public void listFilesForFolder(final File folder) {
            for (final File fileEntry : folder.listFiles()) {
                if (fileEntry.isDirectory()) {
                    listFilesForFolder(fileEntry);
                } else {
                    System.out.println(fileEntry.getName());
                }
            }
        }

        private static void logError(String mensajeError) {
            System.out.println("Error leyendo archivo: " + mensajeError);
        }

        private static String getCurrentDirectory() {
            return System.getProperty("user.dir");
        }

        /** @brief Lee un archivo para poder imprimirlo después
        \pre El fichero en temp_path existe
        \post El fichero de temp_path se elimina del dispositivo
         */
        public static void leerTexto(JTextArea txtHelp, String path_final) throws IOException {
            BufferedReader input =  new BufferedReader(new FileReader(path_final));
            try {
                String line = null;

                while (( line = input.readLine()) != null){
                    txtHelp.append(line+"\n");
                }
            } catch (IOException e) {
                System.out.println("Error");
            } finally {
                input.close();
            }
        }

        public static class IncorrectFileException extends Exception {
            public IncorrectFileException(String errorMessage) {
                super(errorMessage);
            }
        }
    }
