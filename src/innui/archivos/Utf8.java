/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package innui.archivos;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;

/**
 * Clase con métodos para manejar archivos UTF-8
 */
public class Utf8 {
    /**
     * Leer un archivo con texto UTF-8
     * @param ruta_archivo Ruta del archivo
     * @param error mensaje de error, si lo hay.
     * @return true si tiene éxito, false si hay algún error
     */
    public static String leer(String ruta_archivo, String [] error)
    {
        File file = new File(ruta_archivo);
        String retorno = leer(file, error);
        return retorno;
    }    
    /**
     * Leer un archivo con texto UTF-8
     * @param file Representación del archivo
     * @param error mensaje de error, si lo hay.
     * @return true si tiene éxito, false si hay algún error
     */
    public static String leer(File file, String [] error)
    {
        boolean ret = true;  
        String retorno = null;
        FileInputStream fileInputStream;
        InputStreamReader inputStreamReader; //NOI18N
        BufferedReader bufferedReader;
        try {
            if (! file.exists()) {
                ret = false;
                error[0] = java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("in/innui/archivos/in").getString("NO SE HA ENCONTRADO EL ARCHIVO: {0}"), new Object[] {file.getAbsolutePath()});
            }
            if (ret) {
                char[] char_array = new char[100];
                int tam;
                fileInputStream = new FileInputStream(file);
                inputStreamReader = new InputStreamReader(fileInputStream, "UTF-8"); //NOI18N
                bufferedReader = new BufferedReader(inputStreamReader);
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
    /**
     * Escribir un archivo con texto UTF-8
     * @param ruta_archivo Ruta del archivo
     * @param texto Texto que escribir
     * @param error mensaje de error, si lo hay.
     * @return true si tiene éxito, false si hay algún error
     */
    public static boolean escribir(String ruta_archivo, String texto, String [] error)
    {
        boolean ret = true;  
        File file;        
        file = new File(ruta_archivo);
        ret = escribir(file, texto, error);
        return ret;
    }    

    /**
     * Escribir un archivo con texto UTF-8
     * @param file Representación del archivo
     * @param texto Texto que escribir
     * @param error mensaje de error, si lo hay.
     * @return true si tiene éxito, false si hay algún error
     */
    public static boolean escribir(File file, String texto, String [] error)
    {
        boolean ret = true;  
        FileOutputStream fileOutputStream;
        PrintStream printStream; 
        try {
            fileOutputStream = new FileOutputStream(file);
            printStream = new PrintStream(fileOutputStream, true, "UTF-8"); //NOI18N
            printStream.print(texto);
            printStream.close();
        } catch (Exception e) {
            error[0] = e.getMessage();
            if (error[0] == null) {
                error[0] = ""; //NOI18N
            }
            error[0] = java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("in/innui/archivos/in").getString("ERROR EN ESCRIBIR_ARCHIVO_UTF8. {0}"), new Object[] {error[0]});
            ret = false;
        }
        return ret;
    }    
}
