/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package innui.archivos;

import java.io.File;
import java.net.URI;
import java.net.URL;

/**
 *
 * @author daw
 */
public class Rutas {
    /**
     * Para conseguir que los archivos de assets se copien a la carpeta de distribución (dist)
     * se debe modificar el archivo: built.xml
     * <pre>{@code 
     * <target name="-post-compile"> 
     *     <copy todir="${dist.dir}/assets">
     *         <fileset dir="assets" includes="**" />
     *     </copy>
     *  </target> 
     * }</pre>
     * @param nombre
     * @param error
     * @return 
     */
    public static String leer_ruta_base(Class clase, String [] error)
    {
        String resultado = ""; //NOI18N
        File file;
        try {
            URL url = clase.getResource(""); //NOI18N
            String forma = url.toExternalForm();
            String path = url.getPath();
            if (forma.startsWith("jar:")) { //NOI18N
                url = new URL(path);
                path = url.getPath();
                int pos = path.indexOf("!"); //NOI18N
                if (pos >= 0) {
                    path = path.substring(0, pos);
                }
                file = new File(path);
                file = file.getParentFile();
            } else {
                String ruta_paquete;
                Package package_o =clase.getPackage();
                ruta_paquete = package_o.getName();
                ruta_paquete = ruta_paquete.replace(".", "/"); //NOI18N
                int pos = path.indexOf(ruta_paquete);
                if (pos >= 0) {
                    path = path.substring(0, pos);
                }
                file = new File(path);
            }
            URI uri = file.toURI();
            resultado = uri.getPath();
//            resultado = file.getAbsolutePath();
        } catch (Exception e) {
            error[0] = e.getMessage();
            if (error[0] == null) {
                error[0] = ""; //NOI18N
            }
            error[0] = java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("in/innui/archivos/in").getString("ERROR EN LEER_ARCHIVO_TEXTO. {0}"), new Object[] {error[0]});
            resultado = null;
        }
        return resultado;
    }
    
    public static String aumentar_ruta(String ruta, String nombre, String [] error)
    {
        String resultado = null;
        if (ruta != null) {
            if (ruta.endsWith("/")) { //NOI18N
                if (nombre.startsWith("/")) { //NOI18N
                    nombre = nombre.substring(1);
                }
                ruta = ruta + nombre;
            } else {
                if (nombre.startsWith("/") == false) { //NOI18N
                    nombre = "/" + nombre; //NOI18N
                }
                ruta = ruta + nombre;
            }
            resultado = ruta;
        }
        return resultado;
    }
    
}
