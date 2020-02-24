/** @file ventanaCompresion.java
 * @author Daniel Donate
    @brief Especificación del menú de compresión/descompresión
*/


package compresor.vistas;

import compresor.dominio.Coordinador;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;

import static compresor.vistas.ImprimirEstadisticas.imprimirEstadisticas;

/** @class ventanaCompresion
    @brief Vista del menú de compresión/descompresión
    Contiene las funciones para visualizar el menú de compresión,
    que permite al usuario seleccionar entre comprimir un fichero de 
    forma manual o automática (así como descomprimirlo)
*/

public class VentanaCompresion extends JFrame implements ActionListener {

    private JLabel labelTitulo;
    private JTextArea areaIntroduccion;
    private JButton botonCompresionAutomatica, botonCompresionManual;

    private String path;
    private Coordinador compresor = null;
    private byte[] dataComprimida = null;
    private byte[] dataSinComprimir = null;
    private String nombreSinExtension;

/** @brief Setter del path del archivo
*/
    public void setPath(String path) {
        this.path = path;
    }

/** @brief Setter del controlador de compresión (a nivel String)
*/
    public void setCoordinador(Coordinador coordinador) {
        this.compresor = coordinador;
    }

/** @brief Setter de datos comprimidos (a nivel byte)
*/
    public void setDataComprimida(byte[] dataComprimida) {
        this.dataComprimida = dataComprimida;
    }

/** @brief Setter de datos no-comprimidos (a nivel byte)
*/
    public void setDataSinComprimir(byte[] dataSinComprimir) {
        this.dataSinComprimir = dataSinComprimir;
    }

/** @brief Setter del nombre del archivo
*/
    public void setNombreSinExtension(String nombreSinExtension) {
        this.nombreSinExtension = nombreSinExtension;
    }

/** @brief Constructor de la clase donde se inicializan todos los componentes
 * de la ventana de compresió
 */  
    public VentanaCompresion() {

        botonCompresionAutomatica = new JButton();
        botonCompresionAutomatica.setBounds(110, 190, 120, 25);
        botonCompresionAutomatica.setText("Automática");

        botonCompresionManual = new JButton();
        botonCompresionManual.setBounds(250, 190, 120, 25);
        botonCompresionManual.setText("Manual");

        labelTitulo = new JLabel();
        labelTitulo.setText("Menú de Compresión");
        labelTitulo.setForeground(Color.green);
        labelTitulo.setBounds(120, 20, 380, 30);
        labelTitulo.setFont(new java.awt.Font("Verdana", 1, 18));

        String textoIntroduccion = "Haga click en el botón de la izquierda para realizar una compresión automática." + "\n\n"
                + "Haga click en el botón de la derecha para escoger el algoritmo que quiere emplear.\n\n";
        areaIntroduccion = new JTextArea();
        areaIntroduccion.setBackground(Color.lightGray);
        areaIntroduccion.setBounds(50, 70, 380, 100);
        areaIntroduccion.setEditable(false);
        areaIntroduccion.setFont(new java.awt.Font("Verdana", 0, 14));
        areaIntroduccion.setLineWrap(true);
        areaIntroduccion.setText(textoIntroduccion);
        areaIntroduccion.setWrapStyleWord(true);
        areaIntroduccion.setBorder(javax.swing.BorderFactory.createBevelBorder(
                javax.swing.border.BevelBorder.LOWERED, null, null, null,
                new java.awt.Color(0, 0, 0)));

        getContentPane().setBackground(Color.darkGray);
        botonCompresionAutomatica.addActionListener(this);
        botonCompresionManual.addActionListener(this);
        add(botonCompresionAutomatica);
        add(botonCompresionManual);
        add(labelTitulo);
        add(areaIntroduccion);
        setSize(480, 300);
        setTitle("PROP: Menú de Compresión");
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(null);
    }

/** @brief Ordena la descompresión de la información guardada a través del setter "setDataSinComprimir"
    */ 
    public void descomprimir() {
        try {
            Coordinador descompresor = new Coordinador();
            String path = descompresor.getPathEntrada2();
            descompresor.setControladorDescompresor(path);
            byte[] dataComprimida = descompresor.leerArchivoEnBytes(path);

            boolean esCarpeta = (dataComprimida[1] & 0xFF) == 0;

            if (!esCarpeta) {
                byte[] dataDescomprimida = descompresor.descomprimirFichero(dataComprimida);
                String path_dir = Paths.get(path).getParent().toString() + File.separator;
                String path_final = path_dir + descompresor.getNameDescomprimido();
                File tempFile = new File(path_final);
                if (tempFile.exists()) {
                    int i = 1;
                    do {
                        String aux = path_dir + descompresor.getNameDescomprimidoSinExtension()  +
                                '(' + i + ')' + descompresor.getExtension();
                        ++i;
                        tempFile = new File(aux);
                        path_final = aux;
                    } while (tempFile.exists());
                }

                descompresor.guardarArchivoEnBytes(path_final, dataDescomprimida);

                String algoritmoUsado = descompresor.getAlgoritmoUsadoDescompresor();
                String finalPath_final = path_final;
                this.visualizarResultado(finalPath_final, algoritmoUsado);

                ArrayList<String> info = new ArrayList<>();
                float velocidad = descompresor.getVelocidad();
                info.add(algoritmoUsado);
                info.add(String.valueOf(velocidad));
                info.add(path_final);
                imprimirEstadisticas(info, "descompresion");
            }
            else {
                descompresor.setControladorCarpetas(path);
                descompresor.descomprimirCarpeta(dataComprimida);
            }

        } catch (Exception e) {
            System.out.println("Error");
        }
    }

    @Override
     /** @brief Lógica de los botones
      \pre 
      \post Mandamos a realizar la acción que pertoque según 
            el botón que ha pulsado el usuario
  */

    public void actionPerformed(ActionEvent e) {
        /* Independientemente del tipo de compresion, pedimos la entrada de un archivo/carpeta */

        try {

            if (e.getSource() == botonCompresionAutomatica) {

                dataComprimida = compresor.comprimirFichero(dataSinComprimir);

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

                compresor.guardarArchivoEnBytes(path_final, dataComprimida);
            }

            if (e.getSource() == botonCompresionManual) {
                String algoritmoOpcion = JOptionPane.showInputDialog("1:LZ78, 2:LZSS, 3:LZW, 4:JPEG");
                int opcion = Integer.parseInt(algoritmoOpcion);
                if (opcion < 1 || opcion > 4) {
                    JOptionPane.showMessageDialog(null, "Opción inválida");
                    return;
                }
                dataComprimida = compresor.comprimirFicheroConAlgoritmo(dataSinComprimir, opcion);
            }

            String path_dir = Paths.get(path).getParent().toString() + File.separator;
            String path_final = path_dir + compresor.getNameComprimidoSinExtension() + ".prop";
            File tempFile = new File(path_final);
            if (tempFile.exists()) {
                int i = 1;
                do {
                    String aux = path_dir + compresor.getNameComprimidoSinExtension() + '(' + i + ')' + ".prop";
                    ++i;
                    tempFile = new File(aux);
                    path_final = aux;
                } while (tempFile.exists());
            }

            compresor.guardarArchivoEnBytes(path_final, dataComprimida);
            String algoritmoUsado = compresor.getAlgoritmoUsadoCompresor();

            /*  Descomprimos en un fichero temporal para visualiz el contenido con el JTextArea  */
            String temp_path = path_final + ".tmp";
            Coordinador coord_aux = new Coordinador();
            coord_aux.setControladorDescompresor(temp_path);
            byte[] dataDescomprimida = coord_aux.descomprimirFichero(dataComprimida);
            coord_aux.guardarArchivoEnBytes(temp_path, dataDescomprimida);

            float[] S = compresor.getEstadisticasComprimido();
            ArrayList<String> info = new ArrayList<>();
            info.add(String.valueOf(S[0])); info.add(String.valueOf(S[1])); info.add(String.valueOf(S[2]));
            info.add(String.valueOf(S[3])); info.add(algoritmoUsado);
            info.add(path_final);
            imprimirEstadisticas(info, "default");

            String finalPath_final = temp_path;
            this.visualizarResultado(finalPath_final, algoritmoUsado);
            coord_aux.borrarFicheroTemporal(path_dir, temp_path);

            setVisible(false);

        } catch (Exception ex) {
            System.out.println("Error");
        }
    }

    /** @brief Visualizar el resultado de un archivo tras compresión o descompresión
    \pre
    \post Mandamos a realizar la acción que pertoque según
    el botón que ha pulsado el usuario
     */
    public void visualizarResultado(String pathArchivo, String algoritmoUsado) {
        SwingUtilities.invokeLater(new Runnable(){
            public void run(){  
                if (algoritmoUsado.equals("JPEG")) {
                    Desktop desktop = Desktop.getDesktop();
                    try {
                        desktop.open(new File(pathArchivo));
                    } catch (IOException e) {
                        System.out.println("Error leyendo la imagen");
                    }
                } else{
                    ImprimirTexto it = new ImprimirTexto(pathArchivo);
                }
            }
        });
    }
}