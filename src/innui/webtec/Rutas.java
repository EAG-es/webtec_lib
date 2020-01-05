/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package innui.webtec;

import ingui.javafx.webtec.Webtec;
import java.util.List;
import java.util.Map;

/**
 * Clase que gestiona las rutas de ejecución y procesamiento de plantilla de webtec_lib
 */
public class Rutas {
    
    /**
     * Dado un mapa de rutas, localiza los valores que encajan con el patrón que se describe en la clave del mapa
     * Se permiten subcarpetas variables, con el formato: {nombre_variable}.
     * En caso de que no se encuentre ninguna ruta, se asigna la propia ruta buscada añadiento el prefijo; Webtec.k_prefijo_url, y convirtiéndo en nombre de ruta en un nombre de clase. 
     * @param ruta_buscada ruta de url que se intenta encajar con los patrones del mapa.
     * @param rutas_mapa Mapa con los patrones de las rutas de url (clave) y las nombres de las clases que utilizar.
     * @param rutas_posibles_mapa Mapa con los pares clave-valor cuyo patrón encaja con las claves del mapa de rutas.
     * @param error mensaje de error, si lo hay
     * @return true si tiene éxito, o false si hay error
     */
    public static boolean encontrar_rutas_posibles(String ruta_buscada
            , Map <String, String> rutas_mapa
            , Map <String, String> rutas_posibles_mapa, String [] error)
    {
        boolean ret = true;
        String ruta;
        String clase = ""; //NOI18N
        if (rutas_mapa != null) {
            for (Map.Entry<String, String> entrada: rutas_mapa.entrySet()) {
                ruta = entrada.getKey();
                if (ruta.contains("{")) { //NOI18N
                    ruta = ruta.replace("/", "\\/"); //NOI18N
                    ruta = ruta.replaceAll("\\{[^}]*\\}", "[^/]*"); //NOI18N
                    if (ruta_buscada.matches(ruta)) {
                        rutas_posibles_mapa.put(entrada.getKey(), entrada.getValue());
                    }
                } else {
                    if (ruta_buscada.equals(ruta)) {
                        rutas_posibles_mapa.put(entrada.getKey(), entrada.getValue());
                    }                                    
                }
            }
        }
        if (rutas_posibles_mapa.isEmpty()) {
            clase = Webtec.k_prefijo_url + ruta_buscada;
            clase = convertir_nombre_ruta_a_clase(clase, error);
            ret = (clase != null);
            if (ret) {
                rutas_posibles_mapa.put(ruta_buscada, clase);
            }
        }
        return ret;
    }
    /**
     * Dado un mapa de rutas posibles, selecciona con más carpetas en la url.
     * @param rutas_posibles_mapa Mapa con las rutas posibles.
     * @param error mensaje de error, si lo hay
     * @return true si tiene éxito, o false si hay error
     */
    public static Map.Entry<String, String> seleccionar_ruta_mayor(
            Map <String, String> rutas_posibles_mapa, String [] error)
    {
        Map.Entry<String, String> entrada_seleccionada = null;
        int profundidad = -1;
        int profundidad_nueva = 0;
        String ruta;
        String [] rutas_array;
        for (Map.Entry<String, String> entrada: rutas_posibles_mapa.entrySet()) {
            ruta = entrada.getKey();
            rutas_array = ruta.split("/"); //NOI18N
            profundidad_nueva = rutas_array.length;
            if (profundidad_nueva > profundidad) {
                profundidad = profundidad_nueva;
                entrada_seleccionada = entrada;
            }
        }
        return entrada_seleccionada;
    }
    /**
     * Dada una ruta con subcarpetas variables: {nombre_variable}, da valor a cada variable.
     * @param ruta_seleccionada Ruta de url
     * @param url_fragmentos_path_lista Lista con los fragmentos de la ruta de la url
     * @param objetos_mapa Mapa conteniendo las subcarpetas veriables y el fragmento de la url con el que se corresponden.
     * @param error mensaje de error, si lo hay
     * @return true si tiene éxito, o false si hay error
     */
    public static boolean asociar_parametros_en_path(String ruta_seleccionada
            , List <String> url_fragmentos_path_lista
            , Map<String, Object> objetos_mapa, String [] error)
    {
        boolean ret = true;
        String clave;
        String [] ruta_seleccionada_array = ruta_seleccionada.split("\\{"); //NOI18N
        int i = 0;
        int pos;
        for (String ruta_parte: ruta_seleccionada_array) {
            if (ruta_parte.isEmpty() == false) {
                pos = ruta_parte.indexOf("}"); //NOI18N
                if (pos >= 0) {
                    clave = ruta_parte.substring(0, pos);
                    objetos_mapa.put(clave, url_fragmentos_path_lista.get(i));
                }
                i = i + 1;
            }
        }
        return ret;
    }
    /**
     * Convierte el nombre de una clase Java a un equivalente de ruta de url
     * @param nombre_clase Nombre de la clase (incluye el paquete)
     * @param error mensaje de error, si lo hay
     * @return el nombre de la ruta de url.
     */
    public static String convertir_nombre_clase_a_ruta(String nombre_clase, String [] error)
    {
        nombre_clase = nombre_clase.toLowerCase();
        return nombre_clase.replace(".", "/"); //NOI18N
    }
    /**
     * Convierte el nombre de una clase Java a un equivalente de ruta de url
     * @param nombre_ruta Nombre de la clase (incluye el paquete)
     * @param error mensaje de error, si lo hay
     * @return el nombre de la ruta de url.
     */
    public static String convertir_nombre_ruta_a_clase(String nombre_ruta, String [] error)
    {
        nombre_ruta = nombre_ruta.toLowerCase();
        nombre_ruta = nombre_ruta.replace("/", "."); //NOI18N
        if (nombre_ruta.startsWith(".")) { //NOI18N
            nombre_ruta = nombre_ruta.substring(1);
        }
        return nombre_ruta;
    }        

}
