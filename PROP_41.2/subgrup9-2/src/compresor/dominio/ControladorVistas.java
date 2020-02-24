/** @file ControladorVistas.java
 * @author Daniel Donate
 @brief Especificación del controlador de gestión de archivos
 */


package compresor.dominio;

import compresor.vistas.MenuPrincipal;
import compresor.vistas.VentanaCompresion;
import compresor.vistas.VentanaEstadisticas;
import compresor.vistas.VentanaCarpetaArchivo;

/** @class ControladorVistas
 @brief Controlador de Vistas
 Contiene funciones para enlazar la interfície
 gráfica con la lógica del sistema
*/

public class ControladorVistas {
    private static VentanaCarpetaArchivo miVentanaCarpetaArchivo;
    private static VentanaCompresion miVentanaCompresion;
    private static VentanaEstadisticas miVentanaEstadisticas;
    private static MenuPrincipal miVentanaPrinicpal;

    private static MenuPrincipal miVentanaPrincipal;
    private static ControladorVistas miCoordinador;

    public static void iniciar() {
        /* Se instancian las clases */
        miVentanaPrincipal = new MenuPrincipal();
        miVentanaCarpetaArchivo = new VentanaCarpetaArchivo();
        miVentanaCompresion = new VentanaCompresion();
        miVentanaEstadisticas = new VentanaEstadisticas();
        miCoordinador = new ControladorVistas();

        /*Se establecen las relaciones entre clases*/
        miVentanaPrincipal.setControlador(miCoordinador);
        miVentanaCarpetaArchivo.setControlador(miCoordinador);
        miVentanaEstadisticas.setControlador(miCoordinador);

        /*Se establecen relaciones con la clase coordinador*/
        miCoordinador.setMiVentanaPrincipal(miVentanaPrincipal);
        miCoordinador.setMiVentanaCarpetaArchivo(miVentanaCarpetaArchivo);
        miCoordinador.setMiVentanaCompresion(miVentanaCompresion);
        miCoordinador.setMiVentanaEstadisticas(miVentanaEstadisticas);

        miVentanaPrincipal.setVisible(true);
    }

    /** @brief Setter del menú principal
     */ 
    public void setMiVentanaPrincipal(MenuPrincipal miVentanaPrincipal) {
        this.miVentanaPrinicpal = miVentanaPrincipal;
    }

    /** @brief Setter de la ventana de selección entre carpeta/archivo
     */
    public void setMiVentanaCarpetaArchivo(VentanaCarpetaArchivo miVentanaCarpetaArchivo) {
        this.miVentanaCarpetaArchivo = miVentanaCarpetaArchivo;
    }
    
    /** @brief Setter de la ventana de selección entre compresión manual/automática
     */    
    public void setMiVentanaCompresion(VentanaCompresion miVentanaCompresion) {
        this.miVentanaCompresion = miVentanaCompresion;
    }

    /** @brief Setter de la ventana de estadísticas
     */
    public void setMiVentanaEstadisticas(VentanaEstadisticas miVentanaEstadisticas) {
        this.miVentanaEstadisticas = miVentanaEstadisticas;
    }
    
    /** @brief Mostrar la ventana de selección entre carpeta/archivo
     */
    public void mostrarSubmenuTipoArchivo() { 
        miVentanaCarpetaArchivo.setVisible(true); 
        
    }
    
    /** @brief Mostrar la ventana de compresión
     */
    public void mostrarSubmenuComprimir() {
        miVentanaCompresion.setVisible(true);
    }
    
    /** @brief Mostrar la ventana de estadísticas
     */    
    public void mostraSubmenuEstadisticas() {
        miVentanaEstadisticas.setVisible(true);
    }

    /** @brief Llama a la funcionalidad de descompresión del sistema
     */
    public void descomprimir() {
        miVentanaCompresion.descomprimir();
    }
}
