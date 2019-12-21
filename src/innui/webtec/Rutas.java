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
 *
 * @author daw
 */
public class Rutas {
    
    public static boolean encontrar_rutas_posibles(String ruta_buscada
            , Map <String, String> rutas_mapa
            , Map <String, String> rutas_posibles_mapa, String [] error)
    {
        boolean ret = true;
        String ruta;
        String clase = "";
        if (rutas_mapa != null) {
            for (Map.Entry<String, String> entrada: rutas_mapa.entrySet()) {
                ruta = entrada.getKey();
                if (ruta.contains("{")) {
                    ruta = ruta.replace("/", "\\/");
                    ruta = ruta.replaceAll("\\{[^}]*\\}", "[^/]*");
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
    
    public static Map.Entry<String, String> seleccionar_ruta_mayor(String ruta_buscada
            , Map <String, String> rutas_posibles_mapa, String [] error)
    {
        Map.Entry<String, String> entrada_seleccionada = null;
        int profundidad = -1;
        int profundidad_nueva = 0;
        String ruta;
        String [] rutas_array;
        for (Map.Entry<String, String> entrada: rutas_posibles_mapa.entrySet()) {
            ruta = entrada.getKey();
            rutas_array = ruta.split("/");
            profundidad_nueva = rutas_array.length;
            if (profundidad_nueva > profundidad) {
                profundidad = profundidad_nueva;
                entrada_seleccionada = entrada;
            }
        }
        return entrada_seleccionada;
    }
    
    public static boolean asociar_parametros_en_path(String ruta_seleccionada
            , List <String> url_fragmentos_path_lista
            , Map<String, Object> objetos_mapa, String [] error)
    {
        boolean ret = true;
        String clave;
        List<String> lista = null;
        String [] ruta_seleccionada_array = ruta_seleccionada.split("\\{");
        int i = 0;
        int pos;
        for (String ruta_parte: ruta_seleccionada_array) {
            if (ruta_parte.isEmpty() == false) {
                pos = ruta_parte.indexOf("}");
                if (pos >= 0) {
                    clave = ruta_parte.substring(0, pos);
                    objetos_mapa.put(clave, url_fragmentos_path_lista.get(i));
                }
                i = i + 1;
            }
        }
        return ret;
    }

    public static String convertir_nombre_clase_a_ruta(String nombre_clase, String [] error)
    {
        nombre_clase = nombre_clase.toLowerCase();
        return nombre_clase.replace(".", "/");
    }
    
    public static String convertir_nombre_ruta_a_clase(String nombre_clase, String [] error)
    {
        nombre_clase = nombre_clase.toLowerCase();
        nombre_clase = nombre_clase.replace("/", ".");
        if (nombre_clase.startsWith(".")) {
            nombre_clase = nombre_clase.substring(1);
        }
        return nombre_clase;
    }        

}
