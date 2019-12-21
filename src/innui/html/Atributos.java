/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package innui.html;

/**
 *
 * @author emilio
 */
public class Atributos {
    
    public static String extraer_atributo(String atributo, String etiqueta, String [] error) {
        boolean ret = true;
        String resultado = null;
        String etiqueta_origen;
        String atributo_buscado;
        int pos;
        etiqueta_origen = etiqueta.toLowerCase();
        etiqueta_origen = etiqueta_origen.replaceAll("\\s+=\\s+", "=");
        atributo_buscado = atributo.toLowerCase();
        pos = etiqueta_origen.indexOf(atributo_buscado);
        if (pos >= 0) {
            pos = pos + atributo_buscado.length();
            char delimitador = etiqueta_origen.charAt(pos);
            int pos_final = etiqueta_origen.indexOf(delimitador, pos + 1);
            if (pos_final >= 0) {
                resultado = etiqueta_origen.substring(pos + 1, pos_final);
            }
        } else {
            error[0] = "No se ha encontrado el atributo en la etiqueta. ";
            ret = false;
            resultado = null;
        }
        return resultado;
    }
    
}
