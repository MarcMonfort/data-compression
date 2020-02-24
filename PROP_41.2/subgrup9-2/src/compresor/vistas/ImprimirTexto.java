/** @file ImprimirTexto.java
 * @author Daniel Donate
    @brief Especificación del previsualizador de textos
*/


package compresor.vistas;

import compresor.dominio.Coordinador;

import java.io.*;
import javax.swing.*;
import java.awt.*;

/** @class imprimirTexto
    @brief Vista de la previsualización de textos
    Contiene las funciones para visualizar un archivo individual 
    después de que este se haya comprimido o descomprimido
*/

class ImprimirTexto {
    JFrame frame = new JFrame("Texto");
    JPanel panel1;
    JTextArea txtHelp;
    JScrollPane scrollHelp;
    public ImprimirTexto(String path_final) {
        panel1 = new JPanel();
        panel1.setLayout(new GridBagLayout());
        txtHelp = new JTextArea(30, 50);
        txtHelp.setLineWrap(true);
        try {
            Coordinador coordinador = new Coordinador();
            coordinador.leerTexto(txtHelp, path_final);
        }
        catch (IOException ex){
            ex.printStackTrace();
        }
        txtHelp.setEditable(false);
        scrollHelp = new JScrollPane(txtHelp);
        scrollHelp.setEnabled(false);
        panel1.add(scrollHelp, getConstraints(0, 4, 2, 1, GridBagConstraints.EAST));

        frame.getContentPane().add(panel1);
        frame.getContentPane().add(BorderLayout.CENTER, panel1);
        frame.pack();
        frame.setVisible(true);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
    }

/** @brief Función auxiliar para la visualización del texto
  */

    private GridBagConstraints getConstraints(int gridx,int gridy, int gridwidth,int gridheight,int anchor) {
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