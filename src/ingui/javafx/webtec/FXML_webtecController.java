/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ingui.javafx.webtec;

import innui.archivos.Utf8;
import innui.webtec.Webtec_controlador;
import java.net.URI;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

/**
 * Controlador JavaFX de Webtec
 */
public class FXML_webtecController extends Webtec_controlador implements Initializable {
    /**
     * Webview de Webtec (Solo contiene una vista Web).
     */
    @FXML
    public WebView webView;
    /**
     * Método que pone el escuchador de cambios de URL
     * Llama a poner_error, si hay error.
     * @param url (No se utiliza)
     * @param rb (No se utiliza)
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        boolean ret = true;
        String [] error = { "" }; //NOI18N
        ret = poner_escuchador_cambios_url(error);
        if (ret == false) {
            poner_error(error[0]);
        }
    } 
    /**
     * Método que añade un escuchador de cambios de url al objeto webView
     * @param error mensaje de error, si lo hay
     * @return true si tiene éxito, o false si hay error
     */
    public boolean poner_escuchador_cambios_url(String [] error) {
        boolean ret = true;
        WebEngine webEngine = webView.getEngine();
        ReadOnlyStringProperty readOnlyStringProperty_location = webEngine.locationProperty();
        readOnlyStringProperty_location.addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                boolean ret = true;
                String [] error = { "" }; //NOI18N
                URL url;
                Map <String, Object> datos_mapa = null;
                try {
                    if (newValue.isEmpty() == false) {
                        url = new URL(newValue.trim());
                        datos_mapa = new LinkedHashMap();
                        ret = procesar_url(url, datos_mapa, error);
                        if (ret == false) {
                            poner_error(error[0]);
                        }
                    }
                } catch (Exception e) {
                    throw new RuntimeException(java.util.ResourceBundle.getBundle("in/ingui/javafx/webtec/in").getString("ERROR EN PONER_ESCUCHADOR_CAMBIOS_URL->CHANGED. "), e);
                }
            }           
        });
        return ret;
    }
    /**
     * Pone un mensaje de error en el objeto webView
     * @param mensaje mensaje de error, si lo hay
     * @return true si tiene éxito, o false si hay error
     */
    @Override
    public boolean poner_error(String mensaje) {
        String [] error = { "" }; //NOI18N
        return cargar_contenido(mensaje, "text/html", null, error); //NOI18N
    }
    /**
     * Pone un texto en el objeto webView
     * @param tipo_contenido Tipo MIME del contenido, por ejemplo: "text/html"
     * @param ref El ancla que referenciar en el contenido. Puede ser null.
     * @param error mensaje de error, si lo hay
     * @return true si tiene éxito, o false si hay error
     */
    @Override
    public boolean cargar_contenido(String contenido, String tipo_contenido, String ref, String [] error) {
        boolean ret = true;
        URI uri;
        String url_texto;
        try {
            if (temporal_file == null) {
                ret = crear_archivo_temporal(error);
            }         
            if (ret) {
                Utf8.escribir(temporal_file, contenido, error);
                uri = temporal_file.toURI();
                url_texto = uri.toURL().toExternalForm();
                if (ref != null) {
                    url_texto = url_texto + "#" + ref;
                }
                final String url_texto_final = url_texto;
                final String ref_final = ref;
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                       WebEngine webEngine =  webView.getEngine();
//                       webEngine.loadContent("");
                       webEngine.load(url_texto_final);
                       if (ref_final != null) {
                           webEngine.reload();
                       }
                    }
                });
            }
        } catch (Exception e) {
            throw new RuntimeException("Error en cargar_contenido. ", e);
        }
        return ret;
    }

}
