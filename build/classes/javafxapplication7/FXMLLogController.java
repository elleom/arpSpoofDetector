/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxapplication7;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author el_le
 */
public class FXMLLogController implements Initializable {

    @FXML
    private ImageView iv_about;
    Stage stage;
    @FXML
    private Button bt_closeABout;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO

    }    

    @FXML
    private void bt_onCLoseAbout(MouseEvent event) {
        stage = (Stage) bt_closeABout.getParent().getScene().getWindow();
        stage.close();
    }

    
}
