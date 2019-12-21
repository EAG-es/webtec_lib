/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ingui.javafx.webtec;

import ingui.javafx.webtec.FXML_webtecController;
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
 *
 * @author daw
 */
public class Webtec extends Application {
    public static String k_prefijo_url="innui/webtec";
    public Map <String, String> rutas_mapa = null;   
    public FXML_webtecController fXML_webtec_jafController = null;
    public contextos contexto = new contextos();
    
    @Override
    public void start(Stage stage) throws Exception {
        boolean ret = true;
        String [] error = { "" };
        String resourcePath = "/ingui/javafx/webtec/FXML_webtec.fxml";
        URL location = getClass().getResource(resourcePath);
        FXMLLoader fXMLLoader = new FXMLLoader(location);
        Parent root = fXMLLoader.load();
//        Parent root = fXMLLoader.load(getClass().getResource("/ingui/javafx/browser_jaf/FXML_browser_jaf.fxml"));
        Initializable initializable = fXMLLoader.getController();
        if (initializable instanceof FXML_webtecController) {
            fXML_webtec_jafController = (FXML_webtecController) initializable;
            fXML_webtec_jafController.rutas_mapa = rutas_mapa;
            fXML_webtec_jafController.configurar(contexto, true, error);
        } else {
            ret = false;
            error[0] = "El controlador no es de la clase FXML_browser_jafController. ";
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

    public boolean configurar(Stage stage, String [] error) {
        boolean ret = true;
        stage.setTitle("Webtec");
        ObservableList<Image> observableList = stage.getIcons();
        InputStream inputStream
                = Webtec.class.getResourceAsStream(
                "/recursos/ingui/javafx/webtec/icono_web_carpeta.png");
        Image image = new Image(inputStream);
        observableList.add(image);
        return ret;
    }
    
    public boolean iniciar(String [] error)
    {
        return true;
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
