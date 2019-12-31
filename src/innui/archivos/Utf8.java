/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package innui.archivos;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

/**
 *
 * @author emilio
 */
public class Utf8 {
    
    public static String leer(String ruta_archivo, String [] error)
    {
        boolean ret = true;  
        String retorno = null;
        File file;        
        try {
            file = new File(ruta_archivo);
            if (! file.exists()) {
                ret = false;
                error[0] = java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("in/innui/archivos/in").getString("NO SE HA ENCONTRADO EL ARCHIVO: {0}"), new Object[] {ruta_archivo});
            }
            if (ret) {
                char[] char_array = new char[100];
                int tam;
                FileInputStream fileInputStream = new FileInputStream(file);
                InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, "UTF-8"); //NOI18N
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                retorno = ""; //NOI18N
                while (true) {
                    tam = bufferedReader.read(char_array);
                    if (tam < 0) {
                        break;
                    } else {
                        retorno = retorno + new String(char_array, 0, tam);
                    }
                }
                bufferedReader.close();
            }
        } catch (Exception e) {
            error[0] = e.getMessage();
            if (error[0] == null) {
                error[0] = ""; //NOI18N
            }
            error[0] = java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("in/innui/archivos/in").getString("ERROR EN LEER_ARCHIVO_UTF8. {0}"), new Object[] {error[0]});
            ret = false;
            retorno = null;
        }
        return retorno;
    }    
}
