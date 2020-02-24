/** @file menuPrincipal.java
 * @author Daniel Donate
    @brief Especificación del menú principal del programa (Versión GUI)
*/


package compresor.vistas;

import compresor.dominio.ControladorVistas;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;

/** @class menuPrincipal
    @brief Vista del menú principal
    Contiene las funciones para visualizar el menú principal
    del programa, mandando la ejecución de las ordenes 
    introducidas por el usuario
*/


public class MenuPrincipal extends JFrame implements ActionListener{

    private JTextArea areaIntroduccion;
    private JLabel labelTitulo, labelSeleccion;
    private JButton botonComprimir,botonDescomprimir, botonEstadisticas;
    private ControladorVistas Coordinador;

    /**
     * Establece la informacion que se presentara como introduccion del sistema
     */
    public String textoIntroduccion = "";
     
     /** @brief Constructor de la clase donde se inicializan todos los componentes
      * de la ventana principal
    */  
    public MenuPrincipal() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        botonComprimir = new JButton();
        botonComprimir.setBounds(45, 260, 110, 25);
        botonComprimir.setText("Comprimir");

        botonDescomprimir = new JButton();
        botonDescomprimir.setBounds(180, 260, 120, 25);
        botonDescomprimir.setText("Descomprimir");

        botonEstadisticas = new JButton();
        botonEstadisticas.setBounds(320, 260, 110, 25);
        botonEstadisticas.setText("Estadísticas");

        labelTitulo = new JLabel();
        labelTitulo.setText("Weissman Compressor");
        labelTitulo.setForeground(Color.green);
        labelTitulo.setBounds(45, 20, 380, 30);
        labelTitulo.setFont(new java.awt.Font("Verdana", 1, 15));

        labelSeleccion = new JLabel();
        labelSeleccion.setText("Escoja que operacion desea realizar");
        labelSeleccion.setForeground(Color.green);
        labelSeleccion.setBounds(45, 220, 250, 25);

        textoIntroduccion = "Esta aplicación permite comprimir archivos de texto e imagen." + "\n\n"
                             + "La aplicación permite comprimir y descomprimir archivos de forma automática o manual. "
                             + "Puede también consultar las estadísticas de sus compresiones\n\n";

        areaIntroduccion = new JTextArea();
        areaIntroduccion.setBackground(Color.lightGray);
        areaIntroduccion.setBounds(45, 70, 380, 140);
        areaIntroduccion.setEditable(false);
        areaIntroduccion.setFont(new java.awt.Font("Verdana", 0, 14));
        areaIntroduccion.setLineWrap(true);
        areaIntroduccion.setText(textoIntroduccion);
        areaIntroduccion.setWrapStyleWord(true);
        areaIntroduccion.setBorder(javax.swing.BorderFactory.createBevelBorder(
                javax.swing.border.BevelBorder.LOWERED, null, null, null,
                new java.awt.Color(0, 0, 0)));

        getContentPane().setBackground(Color.darkGray);

        botonComprimir.addActionListener(this);
        botonDescomprimir.addActionListener(this);
        botonEstadisticas.addActionListener(this);
        add(botonComprimir);
        add(botonDescomprimir);
        add(botonEstadisticas);
        add(labelSeleccion);
        add(labelTitulo);
        add(areaIntroduccion);

        setSize(480, 345);
        setTitle("PROP: Compresor de Archivos");
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(null);

    }

/** @brief Establece un enlace con su controlador de vistas
      \pre El controlador de vistas, Coordinador, existe 
      \post La función permite establecer una conexión entre la 
            clase y el controlador de vistas
  */
    public void setControlador(ControladorVistas Coordinador) {
        this.Coordinador=Coordinador;
    }

    @Override
    /** @brief Lógica de los botones
      \pre 
      \post Mandamos a nuestro controlador que ejecuta la acción
            que pertoque según el botón que ha pulsado el usuario
  */
    public void actionPerformed(ActionEvent e) {
        if (e.getSource()==botonComprimir) {
            Coordinador.mostrarSubmenuTipoArchivo();
        }
        if (e.getSource()==botonDescomprimir) {
            Coordinador.descomprimir();
        }
        if (e.getSource()==botonEstadisticas) {
            Coordinador.mostraSubmenuEstadisticas();
        }
    }
}