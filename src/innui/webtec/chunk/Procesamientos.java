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
import static innui.webtec.Ejecutores.k_mapa_nombre_plantilla;
import java.io.File;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Clase para procesar las plantillas Chunk conforme a los condicionantes de webtec_lib
 */
public class Procesamientos {
    /**
     * Extensión de los archivos conteniendo los datos que utilizar.
     */
    public static String k_datos_extension = ".json"; //NOI18N
    /**
     * Prefijo de la carpeta con los recursos, y que no se menciona en los nombres de las plantillas.
     */
    public static String k_ruta_plantillas = "/recursos/"; //NOI18N
    /**
     * Extension de las plantillas de texto que mezclan texto: .c (chunk) .html (HTML)
     */
    public static String k_extension_archivo = "c.html"; //NOI18N
    /**
     * Nombre por defecto de los layers de chunk
     */
    public static String k_layer_names = ""; //NOI18N
    /**
     * Codificaciń de texto por defecto de las plantillas
     */
    public static String k_codificacion_texto = "UTF-8"; //NOI18N
    public static String k_mapa_in_ = "in_"; //NOI18N
    public static String k_mapa_in__dato = "<script type='text/javascript'>in_traducir('"; //NOI18N
    public static String k_mapa__in = "_in"; //NOI18N
    public static String k_mapa__in_dato = "');</script>"; //NOI18N
    public static String k_mapa_in_idioma_array = "in_idioma_array"; //NOI18N
    public static String k_mapa_in_parcial = "in_parcial"; //NOI18N
    public static String k_infijo_in_ = "_in_"; //NOI18N
    
    /**
     * Ruta de la última plantilla. Se utiliza internamente para evitar creaciones consecutivas.
     */
    public Map<String, Theme> ultimas_rutas_mapa = new LinkedHashMap(); //NOI18N
    /**
     * Plantillas. Se utiliza internamente para evitar creaciones consecutivas.
     */
    public Map<String, Chunk> plantillas_mapa = new LinkedHashMap(); //NOI18N
    /**
     * Referencia a los datos de contexto.
     */
    public contextos contexto = null;
    /**
     * Asigna el contexto a la clase.
     * @param con Contexto que asignar
     * @return true si tiene éxito, false si hay algún error
     */
    public boolean configurar(contextos con) {
        contexto = con;
        return true;
    }
    /**
     * Convierte el nombre de una plantilla a la ruta absoluta donde encontrarla, usando el classpath de Java
     * @param nombre_plantilla Nombre de la plantilla
     * @param error Mensaje de error, si hay
     * @return true si tiene éxito, o false si hay error
     */
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
    /**
     * Incluye, en el mapa de objetos, las variables necesarias para la internacionalización de la plantilla
     * Para internacionalizar hay que incorporar un codigo HTML semejante al definido en: innui/webtec/in.c.html 
     * Y un ejemplo de traducción al inglés en: innui/webtec/in.in.en.json
     * @param objects_mapa Mapa de objetos con los datos a utilizar en la plantilla chunk.html
     * @param error Mensaje de error, si hay
     * @return true si tiene éxito, o false si hay error
     */
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
            objects_mapa.put(k_mapa_in_, k_mapa_in__dato);
            objects_mapa.put(k_mapa__in, k_mapa__in_dato);
            locale = Locale.getDefault();
            idioma = locale.getLanguage();      
            nombre_plantilla = (String) objects_mapa.get(k_mapa_nombre_plantilla);
            ruta = k_ruta_plantillas + nombre_plantilla + k_infijo_in_ + idioma + k_datos_extension;
            recurso_url = getClass().getResource(ruta);
            if (recurso_url != null) {
                file = new File(recurso_url.toURI());
                ruta = file.getAbsolutePath();
                in_idioma = Utf8.leer(ruta, error);
            } else {
                in_idioma = "[]"; //NOI18N
            }
            objects_mapa.put(k_mapa_in_idioma_array, in_idioma);
            in_parcial = nombre_plantilla.replace("/", "_"); //NOI18N
            objects_mapa.put(k_mapa_in_parcial, in_parcial);     
        } catch (Exception e) {
            error[0] = e.getMessage();
            if (error[0] == null) {
                error[0] = ""; //NOI18N
            }
            error[0] = java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("in/innui/webtec/chunk/in").getString("ERROR EN PONER_DATOS_IN EN PROCESAMIENTOS. {0}"), new Object[] {error[0]});
            ret = false;
        }
        return ret;
    }
    /**
     * Realiza las operaciones necesarias para procesar una plantilla a partir de su nombre.
     * @param nombre Nombre de la plantilla
     * @param objects_mapa Mapa con los datos que utilizar
     * @param error Mensaje de error, si hay
     * @return true si tiene éxito, o false si hay error
     */   
    public String procesar_plantilla(String nombre, Map<String, Object> objects_mapa, String [] error) {
        String resultado = null;
        boolean ret = true;
        Chunk chunk = null;
        String ruta_plantilla;
        Theme theme = null;        
        try {
            ruta_plantilla = leer_ruta_base(nombre, error);
            ret = (ruta_plantilla != null);
            if (ret) {
                ret = poner_datos_in(objects_mapa, error);
            }
            if (ret) {
                if (ultimas_rutas_mapa.containsKey(ruta_plantilla) == false) {
                    theme = new Theme(ruta_plantilla, k_layer_names);
                    theme.setDefaultFileExtension(k_extension_archivo);
                    theme.setEncoding(k_codificacion_texto);
                    ultimas_rutas_mapa.put(ruta_plantilla, theme);
                } else {
                    theme = ultimas_rutas_mapa.get(ruta_plantilla);
                }
                ruta_plantilla = aumentar_ruta(ruta_plantilla, nombre, error);
                ret = (ruta_plantilla != null);
            }
            if (ret) {
                chunk = plantillas_mapa.get(ruta_plantilla);
                if (chunk == null) {
                    chunk = theme.makeChunk(nombre);
                    plantillas_mapa.put(ruta_plantilla, chunk);
                } else {
                    chunk.clear();
                }
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
