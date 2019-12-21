/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package innui.webtec;

import ingui.javafx.webtec.Webtec;
import innui.contextos.a_eles;
import innui.contextos.contextos;
import innui.contextos.filas;
import innui.webtec.chunk.Procesamientos;
import innui.html.Urls;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author daw
 */
public class Webtec_controlador {    
    public static int k_urls_fila_max_tam = 100;
    public static String k_urls_fila_nombre = "historial_urls_fila";
    
    public Procesamientos procesamiento = new Procesamientos();
    public Ejecutores ejecutor = new Ejecutores();
    public Map <String, String> rutas_mapa = null;
    public contextos con_su = null;
    public filas urls_fila = null;
    
    public boolean configurar(contextos con, Boolean mantener_historial, String [] error) {
        boolean ret = true;
        con_su = con;
        ejecutor.configurar(con);
        procesamiento.poner_contexto(con);
        urls_fila = con.leer(k_urls_fila_nombre).dar();
        if (urls_fila == null || mantener_historial == false) {
            urls_fila = new filas();
            urls_fila.poner_max_tam(k_urls_fila_max_tam);
            con_su.superponer(k_urls_fila_nombre, urls_fila);
        }
        return ret;
    }

    public boolean poner_error(String mensaje) {
        String [] error = { "" };
        return cargar_contenido(mensaje, "text/html", error);
    }
    
    public boolean cargar_contenido(String contenido, String tipo_contenido, String [] error) {
        boolean ret = true;
        return ret;
    }

    public boolean procesar_url(URL url, Map <String, Object> datos_mapa, String [] error)
    {
        boolean ret = true;
        Map <String, String> rutas_posibles_mapa = null;
        List <String> url_fragmentos_path_lista = null;
        Map.Entry<String, String> entrada_seleccionada = null;
        String nombre_clase = null;
        String nombre_plantilla = null;
        String contenido = null;
        String ruta_buscada = null;
        String ruta_base= null;
        String url_texto;
        a_eles elem = null;
        try {
            url_texto = url.toExternalForm();
            if (url_texto != null && url_texto.isEmpty() == false) {                
                elem = a_eles.crear(url_texto);
                urls_fila.addLast(elem);
                ruta_buscada = Urls.extraer_path(url_texto, Webtec.k_prefijo_url, error);
                if (ruta_buscada != null) {
                    url_fragmentos_path_lista = new ArrayList();
                    ret = Urls.extraer_fragmentos_path(ruta_buscada, url_fragmentos_path_lista, error);
                    if (ret) {                                
                        rutas_posibles_mapa = new LinkedHashMap ();
                        ret = Rutas.encontrar_rutas_posibles(ruta_buscada, rutas_mapa, rutas_posibles_mapa, error);
                    }
                    if (ret) {
                        entrada_seleccionada = Rutas.seleccionar_ruta_mayor(ruta_buscada, rutas_posibles_mapa, error);
                        ret = (entrada_seleccionada != null);
                    }
                    if (ret) {
                        ret = Rutas.asociar_parametros_en_path(entrada_seleccionada.getKey()
                            , url_fragmentos_path_lista
                            , datos_mapa, error);
                    }
                    if (ret) {
                        ret = Urls.extraer_parametros_query(url, datos_mapa, error);
                    }
                    if (ret) {
                        nombre_clase = entrada_seleccionada.getValue();
                        nombre_plantilla = Rutas.convertir_nombre_clase_a_ruta(nombre_clase, error);
                        ret = (nombre_plantilla != null);
                    }
                    if (ret) {
                        ruta_base = Procesamientos.leer_ruta_base(nombre_plantilla, error);
                        ret = (ruta_base != null);
                    }
                    if (ret) {
                        datos_mapa.put(Ejecutores.k_mapa_nombre_clase, nombre_clase);
                        datos_mapa.put(Ejecutores.k_mapa_nombre_plantilla, nombre_plantilla);
                        datos_mapa.put(Ejecutores.k_mapa_ruta_base, ruta_base);
                        ret = ejecutor.ejecutar(datos_mapa, error);
                    }
                    if (ret) {
                        contenido = procesamiento.procesar_plantilla(nombre_plantilla, datos_mapa, error);
                        ret = (contenido != null);
                    }
                    if (ret) {
                        ret = cargar_contenido(contenido, "text/html", error);
                    }
                }
            }
        } catch (Exception e) {
            error [0] = e.getMessage();
            if (error[0] == null) {
                error[0] = "";
            }
            error[0] = "Error al analizar el cambio de URL. " + error[0];
            ret = false;
        }
        return ret;
    }

    public static boolean reiniciar_mapa(Map <String, Object> datos_mapa, String [] error) {
        String nombre_clase = (String) datos_mapa.get(Ejecutores.k_mapa_nombre_clase);
        String nombre_plantilla = (String) datos_mapa.get(Ejecutores.k_mapa_nombre_plantilla);
        String ruta_base = (String) datos_mapa.get(Ejecutores.k_mapa_ruta_base);
        datos_mapa.clear();
        datos_mapa.put(Ejecutores.k_mapa_nombre_clase, nombre_clase);
        datos_mapa.put(Ejecutores.k_mapa_nombre_plantilla, nombre_plantilla);
        datos_mapa.put(Ejecutores.k_mapa_ruta_base, ruta_base); 
        return true;
    }
    
}
