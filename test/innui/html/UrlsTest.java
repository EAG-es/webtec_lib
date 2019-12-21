/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package innui.html;

import java.net.URL;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 *
 * @author emilio
 */
public class UrlsTest {
    /**
     * Test of extraer_parametros_query method, of class Urls.
     */
    @Test
    public void testExtraer_parametros_query() throws Exception {
        System.out.println("extraer_parametros_query");
        Map<String, Object> objects_mapa = new LinkedHashMap();
        String[] error = { "" };
        boolean expResult = true;
        String texto = "https://innui/webtec/gui/formularios?fechas=titulo:Fecha::&lineas=nombre:texto,titulo:Introduzca,, un texto::&multilineas=nombre:opinion,titulo:Su opinión::&enteros=nombre:valor,valor:10,titulo:Escriba un número::&decimales=nombre:peso,valor:0.0,titulo:Escriba el peso,, 3 decimales::&radios=titulo:Sí,nombre:si_no,checked:,valor:1&radios=titulo:No,nombre:si_no,valor:0&checkboxes=titulo:Acepto la LOPD,checked:,nombre:aceptacion,valor:1&error=&accion=https://innui/webtec/gui/probar_formularios";
        URL url = new URL(texto);
//https://innui/webtec/gui/formularios?fechas=titulo:Fecha::&lineas=nombre:texto,titulo:Introduzca,, un texto::&multilineas=nombre:opinion,titulo:Su opinión::&enteros=nombre:valor,valor:10,titulo:Escriba un número::&decimales=nombre:peso,valor:0.0,titulo:Escriba el peso,, 3 decimales::&radios=titulo:Sí,nombre:si_no,checked:,valor:1&radios=titulo:No,nombre:si_no,valor:0&checkboxes=titulo:Acepto la LOPD,checked:,nombre:aceptacion,valor:1&error=&accion=https://innui/webtec/gui/probar_formularios        
        boolean result = Urls.extraer_parametros_query(url, objects_mapa, error);
        assertEquals(expResult, result);
    }

    /**
     * Test of extraer_path method, of class Urls.
     */
    @Test
    public void testExtraer_path() {
        System.out.println("extraer_path");
        String url = "";
        String marcador = "";
        String[] error = { "" };
        String expResult = "https://innui/webtec/gui/formularios";
        url = "https://innui/webtec/gui/formularios?fechas=titulo:Fecha::&lineas=nombre:texto,titulo:Introduzca,, un texto::&multilineas=nombre:opinion,titulo:Su opinión::&enteros=nombre:valor,valor:10,titulo:Escriba un número::&decimales=nombre:peso,valor:0.0,titulo:Escriba el peso,, 3 decimales::&radios=titulo:Sí,nombre:si_no,checked:,valor:1&radios=titulo:No,nombre:si_no,valor:0&checkboxes=titulo:Acepto la LOPD,checked:,nombre:aceptacion,valor:1&error=&accion=https://innui/webtec/gui/probar_formularios";
        String result = Urls.extraer_path(url, marcador, error);
        assertEquals(expResult, result);
    }

    /**
     * Test of extraer_fragmentos_path method, of class Urls.
     */
    @Test
    public void testExtraer_fragmentos_path() {
        System.out.println("extraer_fragmentos_path");
        String ruta = "https://innui/webtec/gui/formularios";
        List<String> url_fragmentos_path_lista = new LinkedList();
        String[] error = { "" };
        boolean expResult = true;
        boolean result = Urls.extraer_fragmentos_path(ruta, url_fragmentos_path_lista, error);
        assertEquals(expResult, result);
        String resutado = url_fragmentos_path_lista.toString();
        String resultado_esperado = "[https:, innui, webtec, gui, formularios]";
        assertEquals(resultado_esperado, resutado);
    }
    
}
