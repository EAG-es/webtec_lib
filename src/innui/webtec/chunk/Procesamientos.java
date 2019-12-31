/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package innui.webtec.chunk;

import com.x5.template.Chunk;
import com.x5.template.Theme;
import static innui.archivos.Rutas.aumentar_ruta;
import innui.archivos.Utf8;
import innui.contextos.contextos;
import innui.webtec.Ejecutores;
import static innui.webtec.Ejecutores.k_mapa_nombre_plantilla;
import java.io.File;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URL;
import java.util.Locale;
import java.util.Map;

/**
 *
 * @author daw
 */
public class Procesamientos {
    public static String k_datos_extension = ".json"; //NOI18N
    
    public static String k_ruta_plantillas = "/recursos/"; //NOI18N
    public static String k_extension_archivo = "c.html"; //NOI18N
    public String layer_names = ""; //NOI18N
    public String codificacion_texto = "UTF-8"; //NOI18N
    public String ultima_ruta = ""; //NOI18N
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
                error[0] = ""; //NOI18N
            }
            error[0] = java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("in/innui/webtec/chunk/in").getString("ERROR EN LEER_RUTA_BASE. {0}"), new Object[] {error[0]});
            ret = false;
            ruta_base = null;
        }            
        return ruta_base;
    }
    
    public boolean poner_datos_in(Map<String, Object> objects_mapa, String [] error) {
        boolean ret = true;
        Locale locale;
        String nombre_plantilla;
        String in_idioma;
        String ruta;
        String idioma;
        File file;
        URL recurso_url;
        String in_parcial;
        try {        
            objects_mapa.put("in_", "<script type='text/javascript'>in_traducir('");
            objects_mapa.put("_in", "');</script>");
            locale = Locale.getDefault();
            idioma = locale.getLanguage();      
            nombre_plantilla = (String) objects_mapa.get(k_mapa_nombre_plantilla);
            ruta = k_ruta_plantillas + nombre_plantilla + "_in_"+ idioma + k_datos_extension;
            recurso_url = getClass().getResource(ruta);
            if (recurso_url != null) {
                file = new File(recurso_url.toURI());
                ruta = file.getAbsolutePath();
                in_idioma = Utf8.leer(ruta, error);
            } else {
                in_idioma = "[]";
            }
            objects_mapa.put("in_idioma_array", in_idioma);
            in_parcial = nombre_plantilla.replace("/", "_");
            objects_mapa.put("in_parcial", in_parcial);     
        } catch (Exception e) {
            error[0] = e.getMessage();
            if (error[0] == null) {
                error[0] = ""; //NOI18N
            }
            error[0] = "Error en poner_datos_in en Procesamientos. " + error[0];
            ret = false;
        }
        return ret;
    }
       
    public String procesar_plantilla(String nombre, Map<String, Object> objects_mapa, String [] error) {
        String resultado = null;
        boolean ret = true;
        Chunk chunk = null;
        String ruta_plantilla;
        try {
            ruta_plantilla = leer_ruta_base(nombre, error);
            ret = (ruta_plantilla != null);
            if (ret) {
                ret = poner_datos_in(objects_mapa, error);
            }
            if (ret) {
                if (ruta_plantilla.equals(ultima_ruta) == false) {
                    ultima_ruta = ruta_plantilla;
                    theme = new Theme(ultima_ruta, layer_names);
                    theme.setDefaultFileExtension(k_extension_archivo);
                    theme.setEncoding(codificacion_texto);
                }
                chunk = theme.makeChunk(nombre);
                chunk.putAll(objects_mapa);
                Writer writer = new StringWriter();
                chunk.render(writer);
                writer.close();
                resultado = writer.toString();
            }
        } catch (Exception e) {
            error[0] = e.getMessage();
            if (error[0] == null) {
                error[0] = ""; //NOI18N
            }
            error[0] = java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("in/innui/webtec/chunk/in").getString("ERROR EN PROCESAR_PLANTILLA. {0}"), new Object[] {error[0]});
            ret = false;
            resultado = null;
        }
        return resultado;
    }
        
}
