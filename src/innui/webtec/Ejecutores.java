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
 * Clase que instancia clases descendientes de A_ejecutores, y lanza sus métodos de ejecución.
 */
public class Ejecutores extends A_ejecutores {
    /**
     * Clave para el mapa de objetos con información nombre de la clase relativas a la url que procesar.
     */
    public static String k_mapa_nombre_clase = "innui_webtec_ejecutores_nombre_clase"; //NOI18N
    /**
     * Clave para el mapa de objetos con información del nombre de la plantilla relativas a la url que procesar.
     */
    public static String k_mapa_nombre_plantilla = "innui_webtec_ejecutores_nombre_plantilla"; //NOI18N
    /**
     * Clave para el mapa de objetos con información de la ruta de url, base; desde donde se localiza la clase para la ejecución relativas a la url que procesar.
     */
    public static String k_mapa_ruta_base = "innui_webtec_ejecutores_ruta_base"; //NOI18N
    /**
     * Clave para el mapa de objetos con información de la parte de la ruta de url, indicando la referencia al ancla que localizar.
     */
    public static String k_mapa_url_ref_ancla = "innui_webtec_ejecutores_url_ref_ancla"; //NOI18N
    /**
     * Mapa con los ejecutores ya utilizados, y cuya clave es el nombre de la clase. Se reutilizan los objetos previamente instanciados.
     */
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
        boolean es_clase = true;
        try {
            nombre_clase = (String) datos_mapa.get(k_mapa_nombre_clase);
            a_ejecutor = a_ejecutores_mapa.get(nombre_clase);
            if (a_ejecutor == null) {
                clase = Ejecutores.class;
                classLoader = clase.getClassLoader();
                try {
                    clase_cargada = classLoader.loadClass(nombre_clase);
                } catch (Exception e) {
                    es_clase = false;
                }
                if (es_clase) {
                    objeto = clase_cargada.newInstance();
                    if (objeto instanceof A_ejecutores) {
                        a_ejecutor = (A_ejecutores) objeto;
                        a_ejecutor.configurar(contexto);
                    } else {
                        error[0] = java.util.ResourceBundle.getBundle("in/innui/webtec/in").getString("LA CLASE INDICADA NO IMPLEMENTA LA CLASE ABSTRACTA A_EJECUTORES. ");
                        ret = false;
                    }
                    if (ret) {
                        a_ejecutores_mapa.put(nombre_clase, a_ejecutor);
                        ret = a_ejecutor.iniciar(datos_mapa, error);
                    }
                }
            }
            if (a_ejecutor != null) {
                ret = a_ejecutor.ejecutar(datos_mapa, error);
            }
        } catch (Exception e) {
            error [0] = e.getMessage();
            if (error[0] == null) {
                error[0] = ""; //NOI18N
            }
            error[0] = java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("in/innui/webtec/in").getString("ERROR AL EJECUTAR. {0}"), new Object[] {error[0]});
            ret = false;
        }
        return ret;
    }

    @Override
    public boolean terminar(Map<String, Object> objects_mapa, String[] error) {
        boolean ret = true;
        boolean ret_1 = true;
        A_ejecutores a_ejecutor = null;
        String nombre = ""; //NOI18N
        error[0] = ""; //NOI18N
        String [] error_1 = { "" }; //NOI18N
        for (Entry<String, A_ejecutores> nodo : a_ejecutores_mapa.entrySet()) {
            a_ejecutor = nodo.getValue();
            ret_1 = a_ejecutor.terminar(objects_mapa, error_1);
            if (ret_1 == false) {
                nombre = nodo.getKey();
                error[0] += " " + nombre + " { " + error_1[0] + " } "; //NOI18N
                ret = false;
            }
        }
        return ret;
    }
    
}
