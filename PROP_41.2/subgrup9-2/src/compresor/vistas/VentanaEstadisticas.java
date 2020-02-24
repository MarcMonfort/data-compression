/** @file ventanaEstadisticas.java
 * @author Daniel Donate
    @brief Especificación del menú de estadísticas
*/


package compresor.vistas;

import compresor.dominio.ControladorVistas;
import compresor.dominio.Coordinador;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/** @class ventanaEstadisticas
    @brief Vista del menú de estadísticas
    Contiene las funciones para visualizar el menú de estadísticas,
    que permite al usuario seleccionar entre consultar las estadísticas
    de un fichero y el historial de estadísticas
*/

public class VentanaEstadisticas extends JFrame implements ActionListener {

    private ControladorVistas controladorVistas;
    private JLabel labelTitulo;
    private JTextArea areaIntroduccion;
    private JButton botonHistorialEstadisticas, botonEstadisticasFichero;

    /** @brief Constructor de la clase donde se inicializan todos los componentes
      * del submenú
    */
    public VentanaEstadisticas() {

        botonHistorialEstadisticas = new JButton();
        botonHistorialEstadisticas.setBounds(110, 210, 120, 25);
        botonHistorialEstadisticas.setText("Historial");

        botonEstadisticasFichero = new JButton();
        botonEstadisticasFichero.setBounds(250, 210, 120, 25);
        botonEstadisticasFichero.setText("Individual");

        labelTitulo = new JLabel();
        labelTitulo.setText("Menú de Estadísticas");
        labelTitulo.setForeground(Color.green);
        labelTitulo.setBounds(130, 20, 380, 30);
        labelTitulo.setFont(new java.awt.Font("Verdana", 1, 18));

        String textoIntroduccion = "Haga click en el botón de la izquierda para mostrar el registro de todas las compresiones hechas por el usuario." + "\n\n"
                + "Haga click en el botón de la derecha para consultar las estadísticas asociadas a un fichero en concreto.\n\n";
        areaIntroduccion = new JTextArea();
        areaIntroduccion.setBackground(Color.lightGray);
        areaIntroduccion.setBounds(50, 70, 380, 120);
        areaIntroduccion.setEditable(false);
        areaIntroduccion.setFont(new java.awt.Font("Verdana", 0, 14));
        areaIntroduccion.setLineWrap(true);
        areaIntroduccion.setText(textoIntroduccion);
        areaIntroduccion.setWrapStyleWord(true);
        areaIntroduccion.setBorder(javax.swing.BorderFactory.createBevelBorder(
                javax.swing.border.BevelBorder.LOWERED, null, null, null,
                new java.awt.Color(0, 0, 0)));

        getContentPane().setBackground(Color.darkGray);
        botonHistorialEstadisticas.addActionListener(this);
        botonEstadisticasFichero.addActionListener(this);
        add(botonHistorialEstadisticas);
        add(botonEstadisticasFichero);
        add(labelTitulo);
        add(areaIntroduccion);
        setSize(480, 300);
        setTitle("PROP: Menú de Estadísticas");
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(null);
    }

/** @brief Establece un enlace con su controlador de vistas
      \pre El controlador de vistas, controladorVistas, existe 
      \post La función permite establecer una conexión entre la 
            clase y el controlador de vistas
  */

    public void setControlador(ControladorVistas Coordinador) {
        this.controladorVistas = Coordinador;
    }

/** @brief Lógica de los botones
      \pre 
      \post Mandamos a realizar la acción que pertoque según 
            el botón que ha pulsado el usuario
  */
    public void actionPerformed(ActionEvent e) {
        try {
            Coordinador coordinador = new Coordinador();
            coordinador.setControladorEstadisticas();

            if (e.getSource() == botonHistorialEstadisticas) {
                coordinador.consultarRegistro();
            }

            if (e.getSource() == botonEstadisticasFichero) {
                coordinador.imprimirEstadisticasFichero();
            }

            setVisible(false);

        } catch (Exception ex) {
            System.out.println("Error consultando estadísticas");
        }
    }

}