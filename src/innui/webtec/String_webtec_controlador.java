/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package innui.webtec;

/**
 *
 * @author emilio
 */
public class String_webtec_controlador extends Webtec_controlador {
    public String contenido; 
    
    @Override
    public boolean cargar_contenido(String contenido, String tipo_contenido, String [] error) {
        boolean ret = true;
        this.contenido = contenido;
        return ret;
    }
    
    @Override
    public boolean poner_error(String mensaje) {
        boolean ret = true;
        this.contenido = mensaje;
        return ret;
    }

}
