/** @file ventanaCarpetaArchivo.java
 * @author Daniel Donate
    @brief Especificación del submenú de tipo de archivo a comprimir
*/


package compresor.vistas;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;

import javax.swing.*;

import compresor.dominio.*;

import static compresor.vistas.ImprimirEstadisticas.imprimirEstadisticas;

/** @class ventanaCarpetaArchivo
    @brief Vista del submenú de tipo de archivo a comprimir
    Contiene las funciones para visualizar el submenú de tipo de archivo,
    que permite al usuario seleccionar entre comprimir una carpeta o un
    fichero individual
*/

public class VentanaCarpetaArchivo extends JFrame implements ActionListener {

    private ControladorVistas controladorVistas;
    private JLabel labelTitulo;
    private JTextArea areaIntroduccion;
    private JButton botonCarpeta, botonArchivo;

    private VentanaCompresion miVentanaCompresion;
    private Coordinador coordinador = new Coordinador();

    /** @brief Constructor de la clase donde se inicializan todos los componentes
      * del submenú
    */  

    public VentanaCarpetaArchivo() {

        botonCarpeta = new JButton();
        botonCarpeta.setBounds(110, 190, 120, 25);
        botonCarpeta.setText("Carpeta");

        botonArchivo = new JButton();
        botonArchivo.setBounds(250, 190, 120, 25);
        botonArchivo.setText("Archivo");

        labelTitulo = new JLabel();
        labelTitulo.setText("Tipo de archivo");
        labelTitulo.setForeground(Color.green);
        labelTitulo.setBounds(150, 20, 380, 30);
        labelTitulo.setFont(new java.awt.Font("Verdana", 1, 18));

        String textoIntroduccion = "Haga click en el botón de la izquierda para comprimir una carpeta." + "\n\n"
                + "Haga click en el botón de la derecha para comprimir un solo archivo.\n\n";
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
        botonCarpeta.addActionListener(this);
        botonArchivo.addActionListener(this);
        add(botonCarpeta);
        add(botonArchivo);
        add(labelTitulo);
        add(areaIntroduccion);
        setSize(480, 300);
        setTitle("Tipo de Archivo");
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(null);
    }

/** @brief Establece un enlace con su controlador de vistas
      \pre El controlador de vistas, controladorVistas, existe 
      \post La función permite establecer una conexión entre la 
            clase y el controlador de vistas
  */

    public void setControlador(ControladorVistas controladorVistas) {
        this.controladorVistas = controladorVistas;
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
            String path =null;
            byte[] dataComprimida = null;
            byte[] dataSinComprimir = null;
            String nombreSinExtension = "";

            if (e.getSource() == botonCarpeta) {
                path = coordinador.getPathDirectory();
                coordinador.setControladorCarpetas(path);
                dataComprimida = coordinador.comprimirCarpeta();
                nombreSinExtension = coordinador.getNameCarpeta();

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

                coordinador.guardarArchivoEnBytes(path_final, dataComprimida);
                ArrayList<String> info = new ArrayList<>(); info.add(path_final);
                imprimirEstadisticas(info, "carpeta");
            }

            if (e.getSource() == botonArchivo) {
                miVentanaCompresion = new VentanaCompresion();
                path = coordinador.getPathEntrada();

                coordinador.setControladorCompresor(path);

                dataSinComprimir = coordinador.leerArchivoEnBytes(path);

                miVentanaCompresion.setPath(path);
                coordinador.setControladorCompresor(path);
                miVentanaCompresion.setCoordinador(coordinador);
                miVentanaCompresion.setDataSinComprimir(dataSinComprimir);
                miVentanaCompresion.setNombreSinExtension(nombreSinExtension);
                miVentanaCompresion.setVisible(true);
            }

            setVisible(false);

        } catch (Exception ex) {
            System.out.println("Error");
        }
    }
}