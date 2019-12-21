/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package innui.webtec;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 *
 * @author daw
 */
public class Ejecutores extends A_ejecutores {
    public static String k_mapa_nombre_clase = "innui_webtec_ejecutores_nombre_clase";
    public static String k_mapa_nombre_plantilla = "innui_webtec_ejecutores_nombre_plantilla";
    public static String k_mapa_ruta_base = "innui_webtec_ejecutores_ruta_base";
    public Map<String, A_ejecutores> a_ejecutores_mapa = new HashMap();
    
    @Override
    public boolean ejecutar(Map <String, Object> datos_mapa, String [] error)
    {
        boolean ret = true;
        String nombre_clase;
        Class clase = null;
        ClassLoader classLoader = null;
        Class clase_cargada = null;
        A_ejecutores a_ejecutor = null;
        Object objeto = null;
        try {
            nombre_clase = (String) datos_mapa.get(k_mapa_nombre_clase);
            a_ejecutor = a_ejecutores_mapa.get(nombre_clase);
            if (a_ejecutor == null) {
                clase = Ejecutores.class;
                classLoader = clase.getClassLoader();
                clase_cargada = classLoader.loadClass(nombre_clase);
                objeto = clase_cargada.newInstance();
                if (objeto instanceof A_ejecutores) {
                    a_ejecutor = (A_ejecutores) objeto;
                    a_ejecutor.configurar(contexto);
                } else {
                    error[0] = "La clase indicada no implementa la clase abstracta A_ejecutores. ";
                    ret = false;
                }
                if (ret) {
                    a_ejecutores_mapa.put(nombre_clase, a_ejecutor);
                    ret = a_ejecutor.iniciar(datos_mapa, error);
                }
            }
            if (ret) {
                ret = a_ejecutor.ejecutar(datos_mapa, error);
            }
        } catch (Exception e) {
            error [0] = e.getMessage();
            if (error[0] == null) {
                error[0] = "";
            }
            error[0] = "Error al ejecutar. " + error[0];
            ret = false;
        }
        return ret;
    }

    @Override
    public boolean terminar(Map<String, Object> objects_mapa, String[] error) {
        boolean ret = true;
        boolean ret_1 = true;
        A_ejecutores a_ejecutor = null;
        String nombre = "";
        error[0] = "";
        String [] error_1 = { "" };
        for (Entry<String, A_ejecutores> nodo : a_ejecutores_mapa.entrySet()) {
            a_ejecutor = nodo.getValue();
            ret_1 = a_ejecutor.terminar(objects_mapa, error_1);
            if (ret_1 == false) {
                nombre = nodo.getKey();
                error[0] += " " + nombre + " { " + error_1[0] + " } ";
                ret = false;
            }
        }
        return ret;
    }
    
}