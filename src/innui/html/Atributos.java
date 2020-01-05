/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package innui.html;

/**
 * Clase para los atributos de las etiqueta HTML
 */
public class Atributos {
    /**
     * Dado un nombre de atributo, extrae su valor de una etiqueta.
     * @param atributo Atributo que buscar
     * @param etiqueta Etiqueta HTML donde buscar
     * @param error mensaje de error, si lo hay.
     * @return el valor del atributo, o null si hay error
     */
    public static String extraer_atributo(String atributo, String etiqueta, String [] error) {
        boolean ret = true;
        String resultado = null;
        String etiqueta_origen;
        String atributo_buscado;
        int pos;
        etiqueta_origen = etiqueta.toLowerCase();
        etiqueta_origen = etiqueta_origen.replaceAll("\\s+=\\s+", "="); //NOI18N
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
            error[0] = java.util.ResourceBundle.getBundle("in/innui/html/in").getString("NO SE HA ENCONTRADO EL ATRIBUTO EN LA ETIQUETA. ");
            ret = false;
            resultado = null;
        }
        return resultado;
    }
    
}
