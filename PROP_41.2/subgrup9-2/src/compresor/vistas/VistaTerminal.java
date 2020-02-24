package compresor.vistas;

import compresor.dominio.*;
import compresor.dominio.AlgoritmoEnum;
import compresor.persistencia.ControladorGestorArchivo;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class VistaTerminal {
    private enum OpcionesMenu {
        COMPRIMIR, DESCOMPRIMIR, ESTADISTICAS, SALIR
    }

    private enum OpcionesAlgoritmo {
        MANUAL, AUTOMÁTICO
    }

    private enum OpcionesEstadisticas {
        GLOBAL, FICHERO
    }

    private enum OpcionesComprimir {
        ARCHIVO, CARPETA
    }

    static Scanner scanner = new Scanner(System.in);
    static String menuString = "\n\nPráctica PROP - Compresor\n" +
            "\t1. Comprimir\n" +
            "\t2. Descomprimir\n" +
            "\t3. Estadísticas\n" +
            "\t4. Salir\n";
    static String opcionAlgoritmo = "\nSelección de algoritmo\n" +
            "\t1. Manual\n" +
            "\t2. Automático\n";
    static String opcionAlgoritmoManual ="\n¿Qué algoritmo quieres utilizar?\n" + algoritmosAsString();
    static String opcionEstadisticas ="\nSelección de tipo de consulta\n" +
            "\t1. Estadísticas globales (Historial de compresiones)\n" +
            "\t2. Estadísticas de un fichero comprimido\n";
    static String opcionComprimir = "\n¿Quiere comprimir un archivo o una carpeta?\n" +
            "\t1. Archivo\n" +
            "\t2. Carpeta\n";

    static final int opcionError = -1;
    /** @brief Menú de selección de opciones del programa</em>.
     */
    public static void menuTerminal() {
     /* Estas tres lineas crean nuestro menu principal en su version PROVISIONAL. Notese que hay que linkar los botones con los controladores,
        modificando el codigo que hay en esta funcion
      */
        int opcionEscogida = leerInput(menuString, OpcionesMenu.values().length);
        if (opcionEscogida != opcionError) {
            OpcionesMenu opcion =  OpcionesMenu.values()[opcionEscogida - 1];
            /* Vista 1 -> Menú */
            while (opcion != null) {
                switch (opcion) {
                    case COMPRIMIR:
                        try {
                            //Escoge entre comprimir un archivo o una carpeta
                            opcionEscogida = leerInput(opcionComprimir, OpcionesComprimir.values().length);
                            /* Vista 2 -> Esta vista debe primero pedir un archivo */
                            System.out.println("Introduzca el path del archivo o carpeta a comprimir");
                            String path = scanner.next();
                            /* Pasarlo al Controlador compresor para que cree el comprimido */
                            if (opcionEscogida == 1) {
                                /* Vista 3 ->  Pedir que algoritmo utilizar */
                                ControladorCompresor compresor = new ControladorCompresor(path);
                                byte[] dataSinComprimir = ControladorGestorArchivo.leerArchivoEnBytes(path);
                                byte[] dataComprimida = null;
                                String nombreSinExtension = nombreSinExt(path);
                                opcionEscogida = leerInput(opcionAlgoritmo, OpcionesAlgoritmo.values().length);
                                if (opcionEscogida != opcionError) {
                                    OpcionesAlgoritmo opcionAlgoritmo = OpcionesAlgoritmo.values()[opcionEscogida - 1];
                                    if (opcionAlgoritmo.equals(OpcionesAlgoritmo.AUTOMÁTICO)) {
                                        dataComprimida = compresor.comprimirFichero(dataSinComprimir);
                                    } else {
                                        opcionEscogida = leerInput(opcionAlgoritmoManual, AlgoritmoEnum.values().length);
                                        if (opcionEscogida != opcionError) {
                                            AlgoritmoEnum algoritmo = AlgoritmoEnum.values()[opcionEscogida - 1];
                                            dataComprimida = compresor.comprimirFicheroConAlgoritmo(dataSinComprimir, algoritmo);
                                        }
                                    }

                                    String path_dir = Paths.get(path).getParent().toString() + File.separator;
                                    String path_final = path_dir + compresor.getComprimido().getMetadata().getNombreSinExtension() + ".prop";
                                    File tempFile = new File(path_final);
                                    if (tempFile.exists()) {
                                        int i = 1;
                                        do {
                                            String aux = path_dir + nombreSinExtension + '_' + i + ".prop";

                                            ++i;
                                            tempFile = new File(aux);
                                            path_final = aux;
                                        } while (tempFile.exists());
                                    }

                                    ControladorGestorArchivo.guardarArchivoEnBytes(path_final, dataComprimida);
                                    System.out.println("\nArchivo guardado en:  " + path_final);

                                    System.out.println("\nEstadisticas " + '(' + compresor.getComprimido().getMetadata().getAlgoritmoUsado() + ')');
                                    compresor.getComprimido().getMetadata().getEstadistica().printEstadisticas();
                                    menuTerminal();


                                }
                            }
                            else{
                                ControladorCarpetas carpeta = new ControladorCarpetas(path);
                                byte [] dataComprimida = carpeta.comprimirCarpeta();
                                String nombreSinExtension = carpeta.getName();

                                String path_dir = Paths.get(path).getParent().toString() + File.separator;
                                String path_final = path_dir + nombreSinExtension + ".prop";
                                File tempFile = new File(path_final);
                                if (tempFile.exists()) {
                                    int i = 1;
                                    do {
                                        String aux = path_dir + nombreSinExtension + '_' + i + ".prop";
                                        ++i;
                                        tempFile = new File(aux);
                                        path_final = aux;
                                    } while (tempFile.exists());
                                }

                                ControladorGestorArchivo.guardarArchivoEnBytes(path_final, dataComprimida);
                                ArrayList<String> info = new ArrayList<>(); info.add(path_final);
                                System.out.println("Compresión finalizada");
                                menuTerminal();
                            }

                            /* Vista 4 -> Mostrar resultados */
                            return;
                        } catch (Exception e) {
                            System.out.println("Error");
                        }
                        break;
                    case DESCOMPRIMIR:
                        try {
                            /* Vita 2 -> Esta vista debe primero pedir un archivo */
                            System.out.println("Introduzca el path del archivo a descomprimir");
                            String path = scanner.next();
                            /* Pasarlo al Controlador compresor para que cree el comprimido */
                            ControladorDescompresor descompresor = new ControladorDescompresor(path);
                            byte[] dataComprimida = ControladorGestorArchivo.leerArchivoEnBytes(path);
                            boolean esCarpeta = (dataComprimida[1] & 0xFF) == 0;
                            if (!esCarpeta) {
                                byte[] dataDescomprimida = descompresor.descomprimirFichero(dataComprimida);

                                String path_dir = Paths.get(path).getParent().toString() + File.separator;
                                String path_final = path_dir + descompresor.getComprimido().getMetadata().getName();
                                File tempFile = new File(path_final);
                                if (tempFile.exists()) {
                                    int i = 1;
                                    do {
                                        String aux = path_dir + descompresor.getComprimido().getMetadata().getNombreSinExtension() +
                                                '(' + i + ')' + descompresor.getComprimido().getMetadata().getExtension();
                                        ++i;
                                        tempFile = new File(aux);
                                        path_final = aux;
                                    } while (tempFile.exists());
                                }

                                ControladorGestorArchivo.guardarArchivoEnBytes(path_final, dataDescomprimida);

                                System.out.println("\nArchivo guardado en:  " + path_final);

                                System.out.println("\nEstadisticas " + '(' + descompresor.getComprimido().getMetadata().getAlgoritmoUsado() + ')');
                                float velocidad = descompresor.getComprimido().getMetadata().getEstadistica().getVelocidad();
                                System.out.println("Tiempo de descompresión: " + velocidad + " ms");
                            }else {
                                ControladorCarpetas carpeta = new ControladorCarpetas(path);
                                carpeta.descomprimirCarpeta(dataComprimida);
                            }
                            menuTerminal();

                            /* Vista 4 -> Mostrar resultados */
                            return;
                        } catch (Exception e) {
                            System.out.println("Error");
                        }
                        break;

                    case ESTADISTICAS:
                        try {
                            ControladorEstadisticas estadisticas = new ControladorEstadisticas();
                            opcionEscogida = leerInput(opcionEstadisticas, OpcionesEstadisticas.values().length);
                            if (opcionEscogida != opcionError) {
                                OpcionesEstadisticas opcionEst = OpcionesEstadisticas.values()[opcionEscogida - 1];
                                if (opcionEst.equals(OpcionesEstadisticas.GLOBAL)) {
                                    estadisticas.consultarRegistro();
                                } else {
                                    System.out.println("Introduzca el path del archivo a consultar");
                                    String path = scanner.next();
                                    estadisticas.imprimirEstadisticasFichero(path);
                                }
                                menuTerminal();
                                return;
                            }
                        } catch (Exception e) {
                            System.out.println("Error consultando estadísticas");
                        }
                        break;
                    case SALIR:
                        System.out.println("¡Hasta luego!");
                        System.exit(0);
                }
                opcionEscogida = leerInput(menuString, OpcionesMenu.values().length);
                if (opcionEscogida != opcionError) {
                    opcion = OpcionesMenu.values()[opcionEscogida - 1];
                } else {
                    opcion = null;
                }
            }
        }

    }
    private static String nombreSinExt(String s){
        int i = s.length();
        int j = 0;
        while (s.charAt(--i) != '/') if (s.charAt(i) == '.') j = i;
        ++i;
        return s.substring(i, j);
    }

    /** @brief Leemos una opción escogida por el usuario para ejecutarla.
    \pre numeroOpciones > 0
    \post Devolvemos la opción seleccionada, si es válida, o un mensaje de error, si no lo es.
     */
    private static int leerInput(String text, int numeroOpciones) {
        int opcionEscogida = -1;
        try {
            do {
                if (opcionEscogida != -1) {
                    System.out.println("No es una opción");
                }
                System.out.println(text);
                System.out.print("Escoja una opción: ");
                opcionEscogida = scanner.nextInt();
            } while(opcionEscogida <= 0 || opcionEscogida > numeroOpciones);
            return opcionEscogida;
        } catch (InputMismatchException exception) {
            System.out.println("No es un número");
            return -1;
        }
    }

    /** @brief Lista de los 4 algoritmos del programa
     * @return Devuelve una lista ordenada con los algoritmos que implementa el proyecto
     */
    private static String algoritmosAsString() {
        String texto = "";
        for (AlgoritmoEnum algoritmo : AlgoritmoEnum.values()) {
            texto += "\t" + (algoritmo.ordinal()+1) + ". " + algoritmo + "\n";
        }
        return texto;
    }
}



