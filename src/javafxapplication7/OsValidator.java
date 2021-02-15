/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxapplication7;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 *
 * @author el_le
 */
public class OsValidator {
    
    @FXML
    private ImageView iv_OS;
    
    
    private String OS = System.getProperty("os.name");

	public OsValidator(){
            
            //System.out.println(OS);

        }
        
        public String getOS(){
            return OS;
        }
	

	public boolean isWindows() {

		return (OS.toLowerCase().indexOf("win") >= 0);

	}

	public boolean isMac() {

		return (OS.toLowerCase().indexOf("mac") >= 0);

	}

	public boolean isUnix() {

		return (OS.toLowerCase().indexOf("nix") >= 0 || OS.indexOf("nux") >= 0 || OS.indexOf("aix") > 0 );

	}

	public boolean isSolaris() {

		return (OS.toLowerCase().indexOf("sunos") >= 0);

	}

}
    

