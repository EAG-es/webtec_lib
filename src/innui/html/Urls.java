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
 *
 * @author daw
 */
public class Urls {
    public static String k_protocolo_por_defecto = "https"; //NOI18N
    
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
        }
        pos_inicio = resto.indexOf("://"); //NOI18N
        if (pos_inicio >= 0) {
            resto = resto.substring(pos_inicio + "://".length()); //NOI18N
        }            
        return resto;
    }
    
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
