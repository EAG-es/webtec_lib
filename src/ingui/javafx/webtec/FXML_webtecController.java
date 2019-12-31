/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ingui.javafx.webtec;

import innui.webtec.Webtec_controlador;
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
 *
 * @author daw
 */
public class FXML_webtecController extends Webtec_controlador implements Initializable {

    @FXML
    public WebView webView;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        boolean ret = true;
        String [] error = { "" }; //NOI18N
        ret = poner_escuchador_cambios_url(error);
        if (ret == false) {
            poner_error(error[0]);
        }
    } 
    
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
    
    @Override
    public boolean poner_error(String mensaje) {
        String [] error = { "" }; //NOI18N
        return cargar_contenido(mensaje, "text/html", error); //NOI18N
    }
    
    @Override
    public boolean cargar_contenido(String contenido, String tipo_contenido, String [] error) {
        boolean ret = true;
        final String contenido_final = contenido;
        final String tipo_contenido_final = tipo_contenido;
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
               WebEngine webEngine =  webView.getEngine();
               webEngine.loadContent(contenido_final, tipo_contenido_final);
            }
            
        });
        return ret;
    }

}
