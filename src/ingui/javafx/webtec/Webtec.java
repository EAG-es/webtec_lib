/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ingui.javafx.webtec;

import innui.contextos.contextos;
import java.io.InputStream;
import java.net.URL;
import java.util.Map;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * Clase con los métodos que pueden sustituirse por una clase derivada para crear una aplicación Webtec.
 */
public class Webtec extends Application {
    /**
     * Prefijo de la ruta de una url que indica que pertenece la aplicación Webtec, o es una URL no Webtec.
     */
    public static String k_prefijo_url="innui/webtec"; //NOI18N
    /**
     * Nombre que utilizar en el contexto para almacenar el mapa de los patrones de las rutas de url y las clases asociadas que utilizar, distintas de las clases por defecto.
     */
    public static String k_contexto_rutas_mapa="ingui_javafx_webtec_webtec_ruta_mapas"; //NOI18N
    /**
     * Mapa de los patrones de las rutas de url y las clases asociadas que utilizar, distintas de las clases por defecto.
     */
    public Map <String, String> rutas_mapa = null;   
    /**
     * Controlador JavaFX que se construye por defecto
     */
    public FXML_webtecController fXML_webtec_jafController = null;
    /**
     * Contexto de la aplicación Webtec
     */
    public contextos contexto = new contextos();
    /**
     * Método de JavaFX para construir el controlador fXML_webtec_jafController y configurarlo
     * @param stage Escena JavaFX que configurar
     * @throws Exception En caso de error
     */
    @Override
    public void start(Stage stage) throws Exception {
        boolean ret = true;
        String [] error = { "" }; //NOI18N
        String resourcePath = "/ingui/javafx/webtec/FXML_webtec.fxml"; //NOI18N
        URL location = getClass().getResource(resourcePath);
        FXMLLoader fXMLLoader = new FXMLLoader(location);
        Parent root = fXMLLoader.load();
        Initializable initializable = fXMLLoader.getController();
        if (initializable instanceof FXML_webtecController) {
            fXML_webtec_jafController = (FXML_webtecController) initializable;
            contexto.superponer(k_contexto_rutas_mapa, rutas_mapa);
            fXML_webtec_jafController.configurar(contexto, true, error);
        } else {
            ret = false;
            error[0] = java.util.ResourceBundle.getBundle("in/ingui/javafx/webtec/in").getString("EL CONTROLADOR NO ES DE LA CLASE FXML_BROWSER_JAFCONTROLLER. ");
        }
        if (ret) {
            ret = configurar(stage, error);
        }
        if (ret) {
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
            ret = iniciar(error);
        }
        if (ret == false) {
            throw new Exception(error[0]);
        }
    }
    /**
     * Método para configurar el icono de la aplicación. Puede sustituisre para modificar la escena con más detalle.
     * @param stage Escena JavaFX donde aplicar los cambios
     * @param error mensaje de error, si lo hay
     * @return true si tiene éxito, o false si hay error
     */
    public boolean configurar(Stage stage, String [] error) {
        boolean ret = true;
        stage.setTitle(java.util.ResourceBundle.getBundle("in/ingui/javafx/webtec/in").getString("WEBTEC"));
        ObservableList<Image> observableList = stage.getIcons();
        InputStream inputStream
                = Webtec.class.getResourceAsStream(
                "/recursos/ingui/javafx/webtec/icono_web_carpeta.png"); //NOI18N
        Image image = new Image(inputStream);
        observableList.add(image);
        return ret;
    }
    /**
     * Método qeu sustituir para añadir procedimientos de inicio sin relación con la escena JavaFX
     * @param error mensaje de error, si lo hay
     * @return true si tiene éxito, o false si hay error
     */
    public boolean iniciar(String [] error)
    {
        return true;
    }
    /**
     * Método main que lanza la aplicación Webtec por defecto.
     * Solo contiene una llamada a: launch(args);
     * @param args Los argumentos de línea de comando.
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
