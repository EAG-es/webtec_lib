/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package innui.webtec.chunk;

import com.x5.template.Chunk;
import com.x5.template.Theme;
import static innui.archivos.Rutas.aumentar_ruta;
import innui.contextos.contextos;
import java.io.File;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URL;
import java.util.Map;

/**
 *
 * @author daw
 */
public class Procesamientos {
    public static String k_ruta_plantillas = "/recursos/";
    public static String k_extension_archivo = "c.html";
    public String layer_names = "";
    public String codificacion_texto = "UTF-8";
    public String ultima_ruta = "";
    public Theme theme = null;
    public contextos con_su = null;
    
    public boolean poner_contexto(contextos con) {
        con_su = con;
        return true;
    }    
    public static String leer_ruta_base(String nombre_plantilla, String [] error)
    {     
        boolean ret = true;
        String ruta_base = null;
        String ruta_relativa;
        int fin;
        File file;
        URL url;
        try {
            ruta_base = aumentar_ruta(k_ruta_plantillas, nombre_plantilla, error);
            ret = (ruta_base != null);
            if (ret) {
                file = new File(ruta_base);
                file = file.getParentFile();
                ruta_relativa = file.getPath();
                url = Procesamientos.class.getResource(ruta_relativa);
                file = new File(url.toURI());
                ruta_base = file.getAbsolutePath();
                fin = ruta_base.length() - ruta_relativa.length();
                ruta_base = ruta_base.substring(0, fin);
            }
            ruta_base = aumentar_ruta(ruta_base, k_ruta_plantillas, error);
        } catch (Exception e) {
            error[0] = e.getMessage();
            if (error[0] == null) {
                error[0] = "";
            }
            error[0] = "Error en leer_ruta_base. " + error[0];
            ret = false;
            ruta_base = null;
        }            
        return ruta_base;
    }
       
    public String procesar_plantilla(String nombre, Map<String, Object> datos_mapa, String [] error) {
        String resultado = null;
        boolean ret = true;
        Chunk chunk = null;
        String ruta_plantilla;
        try {
            ruta_plantilla = leer_ruta_base(nombre, error);
            ret = (ruta_plantilla != null);
            if (ret) {
                if (ruta_plantilla.equals(ultima_ruta) == false) {
                    ultima_ruta = ruta_plantilla;
                    theme = new Theme(ultima_ruta, layer_names);
                    theme.setDefaultFileExtension(k_extension_archivo);
                    theme.setEncoding(codificacion_texto);
                }
                chunk = theme.makeChunk(nombre);
                chunk.putAll(datos_mapa);
                Writer writer = new StringWriter();
                chunk.render(writer);
                writer.close();
                resultado = writer.toString();
            }
        } catch (Exception e) {
            error[0] = e.getMessage();
            if (error[0] == null) {
                error[0] = "";
            }
            error[0] = "Error en procesar_plantilla. " + error[0];
            ret = false;
            resultado = null;
        }
        return resultado;
    }
        
}
