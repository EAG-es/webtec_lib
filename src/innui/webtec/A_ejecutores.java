/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package innui.webtec;

import innui.contextos.contextos;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Clase Abstracta con métodos para la ejecución de acciones.
 */
public abstract class A_ejecutores {
    /**
     * Atributo conteniendo una referencia a los datos de contexto del ejecutor
     */
    public contextos contexto = null;
    /**
     * Asignar el contexto al ejecutor.
     * @param con Contexto que asignar
     * @return true si tiene éxito, o false si hay error
     */
    public boolean configurar(contextos con) {
        contexto = con;
        return true;
    }
    /**
     * Crea un mapa a partir de los parámetros pasados.
     * Las posiciones pares corresponden a los nombres de las claves, las impares al valor.
     * @param error mensaje de error, si hay
     * @param objetos_array datos con los que crear el mapa
     * @return el mapa enlazado o nulo, si hay error
     */
    public Map<String, Object> crear_mapa(String [] error, Object ... objetos_array) 
    {
        return crear_mapa(objetos_array, error);
    }
    
    /**
     * Crea un mapa a partir de los parámetros pasados.
     * Las posiciones pares corresponden a los nombres de las claves, las impares al valor.
     * @param objetos_array datos con los que crear el mapa
     * @param error mensaje de error, si hay
     * @return el mapa enlazado o nulo, si hay error
     */
    public Map<String, Object> crear_mapa(Object [] objetos_array, String [] error) 
    {
        boolean es_clave = true;
        String clave = null;
        Map<String, Object> objects_mapa = new LinkedHashMap();
        for (Object objeto: objetos_array) {
            if (es_clave) {
                es_clave = false;
                if (objeto instanceof String) {
                    clave = (String) objeto;
                } else {
                    error[0] = java.util.ResourceBundle.getBundle("in/innui/webtec/in").getString("LAS POSICIONES PARES CORRESPONDEN A LOS NOMBRES DE LAS CLAVES. ");
                    objects_mapa = null;
                    break;
                }
            } else {
                es_clave = true;
                objects_mapa.put(clave, objeto);
            }
        }
        return objects_mapa;
    }
    /**
     * Método que debe ser implementado, para ejecutarse.
     * @param objects_mapa datos con nombre que están disponibles
     * @param error mensaje de error, si lo hay.
     * @return true si tiene éxito, false si hay algún error
     */
    public abstract boolean ejecutar(Map<String, Object> objects_mapa, String [] error);    
    /**
     * Método que se debería llamar antes de llamar a ejecutar (al menos al primera vez)
     * @param objects_mapa datos con nombre que están disponibles
     * @param error mensaje de error, si lo hay.
     * @return true si tiene éxito, false si hay algún error
     */   
    public boolean iniciar(Map<String, Object> objects_mapa, String [] error)
    {
        return true;
    }
    /**
     * Método que se debería llamar antes de llamar a ejecutar (al menos tras la última vez)
     * @param objects_mapa datos con nombre que están disponibles
     * @param error mensaje de error, si lo hay.
     * @return true si tiene éxito, false si hay algún error
     */       
    public boolean terminar(Map<String, Object> objects_mapa, String [] error) 
    {
        return true;
    }
}
