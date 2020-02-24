/**
 * @file imprimirEstadisticas.java
 * @author Daniel Donate
 * @brief Contiene los métodos para la visualización de las estadísticas
 */

package compresor.vistas;

import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.border.*;

/**
 * @class imprimirEstadisticas
 * @brief Implementación de los métodos de visualización de estadísticas
 */
public class ImprimirEstadisticas {
    JFrame frame;
    JPanel panel1, panel2, panel3;
    JTextArea txtHelp;
    JScrollPane scrollHelp;

/**
 * @brief Visualización del registro de compresiones 
 */
    public ImprimirEstadisticas() {
        frame = new JFrame("Estadisticas Globales");
        //Scroll Panel
        panel1 = new JPanel();
        panel1.setBackground(Color.BLACK);
        panel1.setLayout(new GridBagLayout());
        //Options Panel
        panel2 = new JPanel();
        panel2.setLayout(new FlowLayout(FlowLayout.RIGHT));
        //Image Panel
        panel3 = new JPanel();
        panel3.setLayout(new GridBagLayout());
        //Help ScrollPane
        txtHelp = new JTextArea(30, 50);
        txtHelp.setBackground(Color.lightGray);
        txtHelp.setLineWrap(true);
        try {
            BufferedReader input =  new BufferedReader(new FileReader(".estadisticas.dat"));
            try {
                String line = null; //not declared within while loop
                while (( line = input.readLine()) != null){
                    txtHelp.append(line+"\n");
                }
            }
            finally {
                input.close();
            }
        }
        catch (IOException ex){
            ex.printStackTrace();
        }
        txtHelp.setEditable(false);
        scrollHelp = new JScrollPane(txtHelp);
        scrollHelp.setEnabled(false);
        panel1.add(scrollHelp, getConstraints(0, 4, 2, 1, GridBagConstraints.EAST));

        frame.getContentPane().add(panel1);
        frame.getContentPane().add(panel2);
        frame.getContentPane().add(panel3);
        frame.getContentPane().add(BorderLayout.CENTER, panel1);
        frame.getContentPane().add(BorderLayout.SOUTH, panel2);
        frame.getContentPane().add(BorderLayout.NORTH, panel3);
        frame.pack();
        //frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
    }

/**
 * * @brief Visualización de las estadísticas de un fichero comprimido 
 */
    public static void imprimirEstadisticas(ArrayList<String> info, String accion) {
        JPanel p = new JPanel();
        JFrame frame = new JFrame("Estadistica Individual" + '\n');

        String S = null;

        if (accion == "default") {
            frame.setSize(550, 250);
            S = "<b>Algoritmo empleado:</b> " + info.get(4) + "<br>" +
                    "<b>Grado compresión:</b> " + info.get(0) + "<br>" +
                    "<b>Velocidad:</b> " + info.get(1) + " [ms]" + "<br>" +
                    "<b>Diferencia Tamaño:</b> " + info.get(2) + " [Bytes]" + "<br>" +
                    "<b>Fiabilidad:</b> " + info.get(3) + "<br>";
            if (info.size() > 5) {
                frame.setSize(1500, 325);
                S +=   "<br>" + "<b>Archivo guardado en:</b> " + info.get(5);
            }
        }
        else if (accion == "descompresion") {
            frame.setSize(1500, 200);
            S = "<b>Algoritmo empleado:</b> " + info.get(0) + "<br>" +
                    "<b>Tiempo de descompresión:</b> " + info.get(1) + " [ms]" + "<br>" +
                    "<br>" + "<b>Archivo guardado en:</b> " + info.get(2);
        }

        else if (accion == "carpeta") {
            frame.setSize(1500, 100);
            S = "<b>Archivo guardado en:</b> " + info.get(0);
        }

        JLabel text = new JLabel("<html><div style='text-align: center;'>" + S + "</div></html>");

        Font font = new Font("SansSerif", Font.PLAIN, 28);
        text.setFont(font);
        p.setBackground(Color.darkGray);
        text.setForeground(Color.green);

        p.add(text, BorderLayout.CENTER);

        frame.add(p);
        frame.show();
    }

/**
 * @brief Función auxiliar para la visualización del registro
 */
    private GridBagConstraints getConstraints(int gridx, int gridy, int gridwidth, int gridheight, int anchor) {
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5,5,5,5);
        c.gridx = gridx;
        c.gridy = gridy;
        c.gridwidth = gridwidth;
        c.gridheight = gridheight;
        c.anchor = GridBagConstraints.WEST;
        return c;
    }
}