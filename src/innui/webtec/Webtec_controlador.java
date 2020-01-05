 /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package innui.webtec;

import static ingui.javafx.webtec.FXML_webtecController.k_contexto_temporal_file;
import ingui.javafx.webtec.Webtec;
import static ingui.javafx.webtec.Webtec.k_contexto_rutas_mapa;
import innui.contextos.a_eles;
import innui.contextos.contextos;
import innui.contextos.filas;
import innui.contextos.i_eles;
import innui.webtec.chunk.Procesamientos;
import innui.html.Urls;
import static innui.html.Urls.extraer_path;
import static innui.webtec.Ejecutores.k_mapa_nombre_clase;
import static innui.webtec.Ejecutores.k_mapa_nombre_plantilla;
import static innui.webtec.Ejecutores.k_mapa_ruta_base;
import static innui.webtec.Ejecutores.k_mapa_url_ref_ancla;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Clase con los métodos necesarios para procesar una URL; obtener su clase A_ejecutores; y procesar su plantilla asociada.
 */
public class Webtec_controlador {
    /**
     * Nombre en el contexto de la lista de redirecciones
     */
    public static String k_contexto_redirecciones_lista = "innui_webtec_redirecciones_lista"; //NOI18N
    /**
     * Nombre en el contexto del archivo temporal
     */
    public static String k_contexto_temporal_file = "innui_webtec_temporal_file"; 
    /**
     * Nombre que utilizar en el contexto para referirse al historial de urls
     */
    public static String k_contexto_urls_fila_nombre = "innui_webtec_historial_urls_fila"; //NOI18N
    /**
     * Valor de la url en cada elemento de la lista de redirecciones
     */
    public static String k_contexto_redirecciones_mapa_url = "innui_webtec_redirecciones_lista_url"; //NOI18N
    /**
     * Valor de lógico, verdad si el contenido previo es descartado al hacer una redirección; false si se concatena a continuación.
     */
    public static String k_contexto_redirecciones_mapa_sustituir_contenido = "innui_webtec_redirecciones_lista_sustituir_contenido"; //NOI18N
    /**
     * Número máximo de entradas en el historial de urls.
     */
    public static int k_urls_fila_max_tam = 100;
    /**
     * Objeto encargado de procesar las plantillas asociadas con cada url procesada.
     */
    public Procesamientos procesamiento = new Procesamientos();
    /**
     * Objeto responsable de cargar los ejecutores correspondiente con cada url procesada
     */
    public Ejecutores ejecutor = new Ejecutores();
    /**
     * Mapa que permite asignar la ruta de una url a una clase Java. La ruta puede tener comodines y subcarpetas variables
     */
    public Map <String, String> rutas_mapa = null;
    /**
     * Referencia al contexto recibido en configurar
     */
    public contextos contexto = null;
    /** 
     * Fila conteniendo las url procesadas
     */
    public filas urls_fila = null;
    /**
     * Archivo temporal
     */
    public File temporal_file = null;
    
    /**
     * Configura los atributos de la clase con los datos del contexto.
     * @param con contexto qeu utilizar
     * @param mantener_historial true, si se continúa con el historial actual del contexto; false si hay qeu crear un historial vacio nuevo.
     * @param error Mensaje de error, si hay.
     * @return true si tiene éxito, o false si hay error
     */
    public boolean configurar(contextos con, Boolean mantener_historial, String [] error) {
        boolean ret = true;
        contexto = con;
        rutas_mapa = con.leer(k_contexto_rutas_mapa).dar();
        ejecutor.configurar(con);
        procesamiento.configurar(con);
        urls_fila = con.leer(k_contexto_urls_fila_nombre).dar();
        if (urls_fila == null || mantener_historial == false) {
            urls_fila = new filas();
            urls_fila.poner_max_tam(k_urls_fila_max_tam);
            contexto.superponer(k_contexto_urls_fila_nombre, urls_fila);
        }
        return ret;
    }
    /**
     * Método llamado si hay que poner un mensaje de error.
     * @param mensaje Mensaje de error
     * @return true si tiene éxito, o false si hay error
     */
    public boolean poner_error(String mensaje) {
        String [] error = { "" }; //NOI18N
        return cargar_contenido(mensaje, "text/html", null, error); //NOI18N
    }
    /**
     * Crea un archivo temporal
     * @param error Posición 0, mensjae de error; si lo hay
     * @return true si tieen éxito, false si hay error
     */
    public boolean crear_archivo_temporal(String [] error) {
        boolean ret = true;
        try {
            temporal_file = File.createTempFile("lala", ".tmp.html");
            temporal_file.deleteOnExit();
            contexto.superponer(k_contexto_temporal_file, temporal_file);
        } catch (Exception e) {
            error [0] = e.getMessage();
            if (error[0] == null) {
                error[0] = ""; //NOI18N
            }
            error[0] = "Error al crear_archivo_temporal. " + error[0];
            ret = false;
        }
        return ret;
    }
    
    /**
     * Método llamado una vez obtenido el resultado de procesar la plantilla asociada a la url que se procesa.
     * @param contenido Texto coteniendo el resultado de procesar la plantilla asociada a la url que se procesa.
     * @param tipo_contenido El tipo MIME del contenido, por ejemplo: text/html
     * @param ref El ancla que referenciar en el contenido. Puede ser null.
     * @param error mensaje de error, si lo hay
     * @return true si tiene éxito, o false si hay error
     */
    public boolean cargar_contenido(String contenido, String tipo_contenido, String ref, String [] error) {
        boolean ret = true;
        return ret;
    }
    /**
     * Incluye una nueva url que debe ser procesada
     * @param contexto Contexto de la aplicación
     * @param url URL que procesar
     * @param es_sustituir_contenido Si es true se elimina el contenido previo; si es false, se concatena.
     * @param extras_mapa Mapa con parametros adicionales, puede ser null
     * @param error Mensaje de error, si lo hay
     * @return true si tiene éxito, false si hay algún error.
     */
    public static boolean poner_redireccion(contextos contexto, URL url, Boolean es_sustituir_contenido, Map<String, Object> extras_mapa, String [] error) {
        boolean ret = true;
        List<Map<String, Object>> redirecciones_lista;
        Map<String, Object> redirecciones_mapa = new LinkedHashMap(); 
        redirecciones_lista = contexto.leer(k_contexto_redirecciones_lista).dar();
        ret =  (redirecciones_lista != null);
        if (ret) {
            redirecciones_mapa.put(k_contexto_redirecciones_mapa_url, url);
            redirecciones_mapa.put(k_contexto_redirecciones_mapa_sustituir_contenido, es_sustituir_contenido);
            if (extras_mapa != null) {
                redirecciones_mapa.putAll(extras_mapa);
            }
            if (es_sustituir_contenido) {
                redirecciones_lista.clear();
            }
            redirecciones_lista.add(redirecciones_mapa);
        } else {
            error[0] = "No se ha encontrado la lista de redirecciones. ";
        }
        return ret;
    }
    /**
     * Ejecuta el ejecutor correspondiente con la url introducida, y procesa la plantilla correspondiente con dicha url.
     * @param url Contiene la url que lleva a una clase y a una plantilla conforme los requisitos de gui_webtec.
     * @param objects_mapa Mapa con los datos adicionales que utilizar.
     * @param error mensaje de error, si lo hay
     * @return true si tiene éxito, o false si hay error
     */
    public boolean procesar_url(URL url, Map <String, Object> objects_mapa, String [] error)
    {
        boolean ret = true;
        String contenido;
        String contenido_nuevo;
        List<Map<String, Object>> redirecciones_lista;
        Boolean es_sustituir_contenido = false;
        String url_texto;
        boolean es_temporal_file = false;
        Map<String, Object> mapa = null;
        int i = 0;
        int tam = 0;
        URL url_redirigida;
        String ref;
        try {
            contexto.subir();
            url_texto = url.toExternalForm();
            if (url_texto != null && url_texto.isEmpty() == false) {
                if (temporal_file != null) {
                    if (url_texto.contains(temporal_file.getAbsolutePath())) {
                        es_temporal_file = true;
                    }
                }
                if (es_temporal_file == false) {
                    redirecciones_lista = new ArrayList();
                    contexto.superponer(k_contexto_redirecciones_lista, redirecciones_lista);
                    contenido = procesar_una_url(url, objects_mapa, error);
                    ret = (contenido != null);
                    if (ret) {
                        while (true) {
                            if (redirecciones_lista.isEmpty()) {
                                break;
                            }
                            mapa = redirecciones_lista.get(0);
                            es_sustituir_contenido = (Boolean) mapa.get(k_contexto_redirecciones_mapa_sustituir_contenido);
                            if (es_sustituir_contenido) {
                                contenido = "";
                            }
                            url_redirigida = (URL) mapa.get(k_contexto_redirecciones_mapa_url);
                            url_redirigida = new URL(url_redirigida.toExternalForm());
                            redirecciones_lista.remove(0);
                            contenido_nuevo = procesar_una_url(url_redirigida, objects_mapa, error);
                            ret = (contenido_nuevo != null);
                            if (ret == false) {
                                break;
                            }
                            contenido = contenido + contenido_nuevo;
                        }
                        if (ret) {
                            ref = (String) objects_mapa.get(k_mapa_url_ref_ancla);
                            if (ref == null) {
                                ref = url.getRef();
                            }
                            ret = cargar_contenido(contenido, "text/html", ref, error); //NOI18N
                        } else {
                            ret = poner_error(error[0]);
                        }
                    }
                }
            }
        } catch (Exception e) {
            error [0] = e.getMessage();
            if (error[0] == null) {
                error[0] = ""; //NOI18N
            }
            error[0] = "Error en procesar_url. " + error[0];
            ret = false;
        } finally {
            contexto.bajar();
        }
        return ret;
    }
    /**
     * Establece la referencia de la url que queda diaponible para utilizarse al cargar contendo
     * @param ancla Nombre del ancla al que referenciar
     * @param objects_mapa Mapa donde guardar el dato
     * @param error Mensaje de error, si lo hay.
     * @return true si todo es correcto; false si hay error
     */
    public static boolean poner_url_ref_a_contenido(String ancla, Map <String, Object> objects_mapa, String [] error) {
        boolean ret = true;
        objects_mapa.put(k_mapa_url_ref_ancla, ancla);                    
        return ret;
    }
            
    /**
     * Ejecuta el ejecutor correspondiente con la url introducida, y procesa la plantilla correspondiente con dicha url.
     * @param url Contiene la url que lleva a una clase y a una plantilla conforme los requisitos de gui_webtec.
     * @param objects_mapa Mapa con los datos adicionales que utilizar.
     * @param error mensaje de error, si lo hay
     * @return El texto resultante; null, si no se procesa nada.
     */
    public String procesar_una_url(URL url, Map <String, Object> objects_mapa, String [] error)
    {
        boolean ret = true;
        String retorno = null;
        Map <String, String> rutas_posibles_mapa = null;
        List <String> url_fragmentos_path_lista = null;
        Map.Entry<String, String> entrada_seleccionada = null;
        String nombre_clase = null;
        String nombre_plantilla = null;
        String ruta_buscada = null;
        String ruta_base= null;
        String url_texto;
        i_eles ele = null;
        int tam = 0;
        List<Map<String, Object>> redirecciones_lista = null;
        Map<String, Object> nodo = null;
        try {
            url_texto = url.toExternalForm();
            if (url_texto != null && url_texto.isEmpty() == false) {                
                ruta_buscada = Urls.extraer_path(url_texto, Webtec.k_prefijo_url, error);
                if (ruta_buscada != null) {
                    ele = a_eles.crear(url_texto);
                    urls_fila.addLast(ele);
                    rutas_posibles_mapa = new LinkedHashMap ();
                    ret = Rutas.encontrar_rutas_posibles(ruta_buscada, rutas_mapa, rutas_posibles_mapa, error);
                    if (ret) {
                        entrada_seleccionada = Rutas.seleccionar_ruta_mayor(rutas_posibles_mapa, error);
                        ret = (entrada_seleccionada != null);
                    }
                    if (ret) {
                        url_fragmentos_path_lista = new ArrayList();
                        ret = Urls.extraer_fragmentos_path(ruta_buscada, url_fragmentos_path_lista, error);      
                    }
                    if (ret) {
                        ret = Rutas.asociar_parametros_en_path(entrada_seleccionada.getKey()
                            , url_fragmentos_path_lista
                            , objects_mapa, error);
                    }
                    if (ret) {
                        ret = Urls.extraer_parametros_query(url, objects_mapa, error);
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
                        redirecciones_lista = contexto.leer(k_contexto_redirecciones_lista).dar();
                        objects_mapa.put(k_mapa_nombre_clase, nombre_clase);
                        objects_mapa.put(k_mapa_nombre_plantilla, nombre_plantilla);
                        objects_mapa.put(k_mapa_ruta_base, ruta_base);
                        ret = ejecutor.ejecutar(objects_mapa, error);
                    }
                    if (ret) {
                        tam = redirecciones_lista.size();
                        if (tam > 0) {
                            nodo = redirecciones_lista.get(0);
                            if ((Boolean) nodo.get(k_contexto_redirecciones_mapa_sustituir_contenido) == false) {
                                retorno = procesamiento.procesar_plantilla(nombre_plantilla, objects_mapa, error);
                            } else {
                                retorno = "";
                            }
                        } else {
                            retorno = procesamiento.procesar_plantilla(nombre_plantilla, objects_mapa, error);
                        }
                    }
                }
            }
        } catch (Exception e) {
            error [0] = e.getMessage();
            if (error[0] == null) {
                error[0] = ""; //NOI18N
            }
            error[0] = java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("in/innui/webtec/in").getString("ERROR AL ANALIZAR EL CAMBIO DE URL. {0}"), new Object[] {error[0]});
            ret = false;
            retorno = null;
        }
        return retorno;
    }
    /**
     * Ejecuta el ejecutor correspondiente con la url introducida, y procesa la plantilla correspondiente con dicha url.
     * Pero no añade al histrial las urls procesadas internamente.
     * @param contexto Datos de contexto en donde se encuentra conteniendo el historial
     * @param url Contiene la url que lleva a una clase y a una plantilla conforme los requisitos de gui_webtec.
     * @param objects_mapa Mapa con los datos adicionales que utilizar.
     * @param error mensaje de error, si lo hay
     * @return true si tiene éxito, o false si hay error
     */
    public boolean procesar_url_sin_historial(contextos contexto, URL url, Map <String, Object> objects_mapa, String [] error)
    {   
        boolean ret = true;
        try{
            contexto.subir();
            ret = configurar(contexto, false, error);           
            if (ret) {
                ret = procesar_url(url, objects_mapa, error);
            }
        } catch (Exception e) {
            error [0] = e.getMessage();
            if (error[0] == null) {
                error[0] = ""; //NOI18N
            }
            error[0] = java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("in/innui/webtec/in").getString("ERROR AL EJECUTAR PROCSAR_SIN_HISTORIAL EN WEBTEC_CONTROLADOR. {0}"), new Object[] {error[0]});
            ret = false;
        } finally {
            contexto.bajar();
        }
        return ret;
    }
    /**
     * Quita la última url de texto introducida en el historial.
     * @param contexto Datos de contexto en donde se encuentra conteniendo el historial
     * @param error mensaje de error, si lo hay
     * @return true si tiene éxito, o false si hay error
     */
    public static boolean quitar_ultimo_historial(contextos contexto, String [] error) {
        boolean ret = true;
        filas fila;
        i_eles ele; 
        ele = contexto.leer(k_contexto_urls_fila_nombre);
        fila = ele.dar();
        if (fila == null) {
            error[0] = java.util.ResourceBundle.getBundle("in/innui/webtec/in").getString("NO SE HA CONFIGURADO WEBTEC_CONTROLADOR. ");
            ret = false;
        } else {
            fila.removeLast(); 
        }
        return ret;
    }
    /**
     * Quita la última url de texto introducida en el historial.
     * @param contexto Datos de contexto en donde se encuentra conteniendo el historial
     * @param error mensaje de error, si lo hay
     * @return true si tiene éxito, o false si hay error
     */
    public static boolean reducir_historial(contextos contexto, int tam, String [] error) {
        boolean ret = true;
        int tam_historial;
        filas fila;
        i_eles ele; 
        ele = contexto.leer(k_contexto_urls_fila_nombre);
        fila = ele.dar();
        if (fila == null) {
            error[0] = java.util.ResourceBundle.getBundle("in/innui/webtec/in").getString("NO SE HA CONFIGURADO WEBTEC_CONTROLADOR. ");
            ret = false;
        } else {
            tam_historial = fila.size();
            while (tam < tam_historial) {
                fila.removeLast(); 
                tam_historial = tam_historial - 1;
            }
        }
        return ret;
    }
    /**
     * Lee el tamaño del historial.
     * @param contexto Datos de contexto en donde se encuentra conteniendo el historial
     * @param error mensaje de error, si lo hay
     * @return El tamaño del historial, 0 si hay error o está vacio.
     */
    public static int leer_tam_historial(contextos contexto, String [] error) {
        int retorno = 0;
        filas fila;
        i_eles ele; 
        ele = contexto.leer(k_contexto_urls_fila_nombre);
        fila = ele.dar();
        if (fila == null) {
            error[0] = java.util.ResourceBundle.getBundle("in/innui/webtec/in").getString("NO SE HA CONFIGURADO WEBTEC_CONTROLADOR. ");
        } else {
            retorno = fila.size();
        }
        return retorno;
    }
    /**
     * Lee la penúltima url de texto introducida en el historial.
     * @param contexto Datos de contexto en donde se encuentra conteniendo el historial
     * @param error mensaje de error, si lo hay
     * @return la url de texto encontrada, o null si hay error
     */
    public static String leer_penultimo_historial(contextos contexto, String [] error) {
        String retorno = null;
        filas fila;
        int tam;
        i_eles ele; 
        ele = contexto.leer(k_contexto_urls_fila_nombre);
        fila = ele.dar();
        if (fila == null) {
            error[0] = java.util.ResourceBundle.getBundle("in/innui/webtec/in").getString("NO SE HA CONFIGURADO WEBTEC_CONTROLADOR. ");
            retorno = null;
        } else {
            tam = fila.size();
            tam = tam - 2;
            if (tam >= 0) {
                ele = fila.get(tam);
                retorno = ele.dar();
            }
        }
        return retorno;
    }
    /**
     * Lee la última url de texto introducida en el historial.
     * @param contexto Datos de contexto en donde se encuentra conteniendo el historial
     * @param error mensaje de error, si lo hay
     * @return la url de texto encontrada, o null si hay error
     */
    public static String leer_ultimo_historial(contextos contexto, String [] error) {
        String retorno = null;
        filas fila;
        i_eles ele; 
        ele = contexto.leer(k_contexto_urls_fila_nombre);
        fila = ele.dar();
        if (fila == null) {
            error[0] = java.util.ResourceBundle.getBundle("in/innui/webtec/in").getString("NO SE HA CONFIGURADO WEBTEC_CONTROLADOR. ");
            retorno = null;
        } else {
            if (fila.isEmpty() == false) {
                ele = fila.getLast();
                retorno = ele.dar();
            }
        }
        return retorno;
    }
    /**
     * Lee las urls de texto del historial hasta que encuentra una, cuya ruta sea distinta de la última incorporada en el historial..
     * @param contexto Datos de contexto en donde se encuentra conteniendo el historial
     * @param error mensaje de error, si lo hay
     * @return la url de texto encontrada, o null si hay error
     */
    public static String leer_penultimo_con_distinta_ruta_historial(contextos contexto, String [] error) {
        String retorno = null;
        boolean ret = true;
        filas fila;
        i_eles ele; 
        String url_ultimo_texto;
        String url_penultimo_texto;
        String ruta_ultimo = "";
        url_ultimo_texto = leer_ultimo_historial(contexto, error);
        ret = (url_ultimo_texto != null);
        if (ret) {
            ruta_ultimo = extraer_path(url_ultimo_texto, "", error); //NOI18N
            ret = (url_ultimo_texto != null);
        }
        if (ret) {
            ele = contexto.leer(k_contexto_urls_fila_nombre);
            fila = ele.dar();             
            int tam = fila.size(); 
            tam = tam - 1;
            while (true) {
                tam = tam - 1;
                if (tam < 0) {
                    ret = false;
                    break;
                }
                ele = fila.get(tam);
                if (ele != null) {
                    retorno = ele.dar();
                    url_penultimo_texto = extraer_path(retorno, "", error); //NOI18N
                    ret = (retorno != null);
                    if (ret == false) {
                        break;
                    }
                    if (url_penultimo_texto.startsWith(ruta_ultimo) == false) { // url distinta de la del formulario
                        break;
                    }
                }                
            } 
        }
        if (ret == false) {
            retorno = null;
        }
        return retorno;
    }
    /**
     * Reduce el mapa a los datos mínimos que añade de procesar_url
     * @param objects_mapa mapa con los datos
     * @param error mensaje de error si lo hay
     * @return true si tiene éxito, o false si hay error
     */    
    public static boolean dejar_mapa_minimo(Map <String, Object> objects_mapa, String [] error) {
        String nombre_clase = (String) objects_mapa.get(k_mapa_nombre_clase);
        String nombre_plantilla = (String) objects_mapa.get(k_mapa_nombre_plantilla);
        String ruta_base = (String) objects_mapa.get(k_mapa_ruta_base);
        objects_mapa.clear();
        objects_mapa.put(k_mapa_nombre_clase, nombre_clase);
        objects_mapa.put(k_mapa_nombre_plantilla, nombre_plantilla);
        objects_mapa.put(k_mapa_ruta_base, ruta_base); 
        return true;
    }
    
}
