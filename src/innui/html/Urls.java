/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package innui.html;

import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Clase para manejar URLs
 */
public class Urls {
    /**
     * Protocolo por defecto 
     */
    public static String k_protocolo_por_defecto = "https"; //NOI18N
    /**
     * Obtiene los parámetros de la consulta de una url
     * @param url URL de donde extraerlos.
     * @param objects_mapa Mapa donde almacenar las claves y los valores extraidos.
     * @param error mensaje de error, si lo hay.
     * @return true si tiene éxito, false si hay algún error
     */
    public static boolean extraer_parametros_query(URL url, Map<String, Object> objects_mapa, String [] error)
    {
        boolean ret = true;
        String [] partes_array = { "" }; //NOI18N
        String mensaje;
        String clave;
        Map<String, List<String>> listas_mapa = new LinkedHashMap();
        List <String> lista;
        try {
            mensaje = url.getQuery();
            if (mensaje != null && mensaje.isEmpty() == false) {
                String [] parametros_array = mensaje.split("&"); //NOI18N
                for (String cadena : parametros_array) {
                    partes_array = cadena.split("="); //NOI18N
                    if (partes_array.length == 2) {
                        partes_array[0] = URLDecoder.decode(partes_array[0], "UTF-8"); //NOI18N
                        partes_array[1] = URLDecoder.decode(partes_array[1], "UTF-8"); //NOI18N
                    } else if (partes_array.length == 1) {
                        clave = partes_array[0];
                        partes_array = new String [2];
                        partes_array[0] = URLDecoder.decode(clave, "UTF-8"); //NOI18N
                        partes_array[1] = ""; //NOI18N
                    } else {
                        ret = false;
                        error[0] = java.util.ResourceBundle.getBundle("in/innui/html/in").getString("NO SE HAN RECONOCIDO LOS PARÁMETROS. ");
                        break;
                    }
                    if (ret) {
                        lista = listas_mapa.get(partes_array[0]);
                        if (lista == null) {
                            lista = new ArrayList();
                            lista.add(partes_array[1]);
                            listas_mapa.put(partes_array[0], lista);
                        } else {
                            lista.add(partes_array[1]);
                        }
                    }
                }
                if (ret) {
                    for (Entry<String, List<String>> entrada: listas_mapa.entrySet()) {
                        if (entrada.getValue().size() != 1) {
                            int i = 0;
                            for (String entrada_lista: entrada.getValue()) {
                                objects_mapa.put(entrada.getKey()+"["+i+"]", entrada_lista); //NOI18N
                                i = i + 1;
                            }
                        } else {
                            objects_mapa.put(entrada.getKey(), entrada.getValue().get(0));
                        }
                    }
                }
            }
        } catch (Exception e) {
            error [0] = e.getMessage();
            if (error[0] == null) {
                error[0] = ""; //NOI18N
            }
            error[0] = java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("in/innui/html/in").getString("ERROR AL EXTRAER LOS PARAMETROS DE LA QUERY. {0}"), new Object[] {error[0]});
            ret = false;
        }
        return ret;
    }
    /**
     * Extrae
     * @param url_texto
     * @param error
     * @return 
     */
    public static String extraer_anclaje(String url_texto, String [] error)
    {
        String resto = null; 
        return resto;
    }
    /**
     * Extrae la parte de la ruta de la url que sigue al macador indicado.
     * @param url_texto Texto con la URlL de la que extraer la ruta
     * @param marcador Marcdor que buscar en la ruta, a partir del que extraer.
     * @param error mensaje de error, si lo hay.
     * @return El path dentro de la URL; o null si no encuentra el marcador
     */
    public static String extraer_path(String url_texto, String marcador, String [] error)
    {
        String resto = null; 
        int pos_fin;
        int pos_inicio = url_texto.indexOf(marcador);
        if (pos_inicio >= 0) {
            pos_inicio = pos_inicio + marcador.length();
            pos_fin = url_texto.indexOf("?"); //NOI18N
            if (pos_fin >= 0) {
                resto = url_texto.substring(pos_inicio, pos_fin);
            } else {
                resto = url_texto.substring(pos_inicio);
            }
            pos_fin = url_texto.indexOf("#"); //NOI18N
            if (pos_fin >= 0) {
                resto = resto.substring(pos_inicio, pos_fin);
            }
            pos_inicio = resto.indexOf("://"); //NOI18N
            if (pos_inicio >= 0) {
                resto = resto.substring(pos_inicio + "://".length()); //NOI18N
            }           
        }
        return resto;
    }
    /**
     * Extrae el protocolo de un texto con una URL
     * @param url_texto Texto con la URlL de la que extraer el protocolo.
     * @param error mensaje de error, si lo hay.
     * @return El protocolo si tiene éxito, null si hay algún error
     */
    public static String extraer_protocolo(String url_texto, String [] error)
    {
        String retorno = null; 
        int pos_inicio;
        pos_inicio = url_texto.indexOf("://"); //NOI18N
        if (pos_inicio >= 0) {
            retorno = url_texto.substring(0, pos_inicio);
        }            
        return retorno;
    }
    /**
     * Extrae las subcarpetas de una ruta de url.
     * @param ruta Ruta de url de la que extraer
     * @param url_fragmentos_path_lista Lista conteniendo las subcarpetas.
     * @param error mensaje de error, si lo hay.
     * @return true si tiene éxito, false si hay algún error
     */
    public static boolean extraer_fragmentos_path(String ruta, List <String> url_fragmentos_path_lista, String [] error)
    {
        boolean ret = true;
        String [] resto_partes_array = ruta.split("/"); //NOI18N
        int i = 0;
        for (String parte: resto_partes_array) {
            if (parte.isEmpty() == false) {
                url_fragmentos_path_lista.add(parte);
                i = i + 1;
            }
        }
        return ret;
    }
    /**
     * Añade el protocolo a un texto con una url, si no lo tiene.
     * @param url_texto Texto con la URL
     * @param protocolo_si_falta Protocolo que poner, si falta.
     * @param error mensaje de error, si lo hay.
     * @return la URL coimpletada con el protocolo, null si hay error.
     */
    public static URL completar_URL(String url_texto, String protocolo_si_falta, String [] error) {
        URL retorno = null;
        String texto;
        String url_path = ""; //NOI18N
        try {
            url_path = extraer_path(url_texto, "", error); //NOI18N
            if (url_path != null) {
                if (url_path.contains("://") == false) { //NOI18N
                    if (protocolo_si_falta.isEmpty()) {
                        texto = k_protocolo_por_defecto + "://" + url_texto; //NOI18N
                    } else {
                        texto = protocolo_si_falta + "://" + url_texto; //NOI18N
                    }
                    retorno = new URL(texto);
                } else {
                    retorno = new URL(url_texto);
                }
            }
        } catch (Exception e) {
            error [0] = e.getMessage();
            if (error[0] == null) {
                error[0] = ""; //NOI18N
            }
            error[0] = java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("in/innui/html/in").getString("ERROR EN COMPLETAR_URL. {0}"), new Object[] {error[0]});
            retorno = null;
        }
        return retorno;
    }
            
}
