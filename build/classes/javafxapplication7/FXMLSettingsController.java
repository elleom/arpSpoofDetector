/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxapplication7;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author el_le
 */
public class FXMLSettingsController implements Initializable {

    
    @FXML
    private CheckBox cb_sendEmail;
    @FXML
    private Button bt_SettingAccept;
    @FXML
    private Button bt_settingCancel;
    @FXML
    private Spinner<Integer> spinner_timeMIn;
    @FXML
    private TextField et_newEmail;
    @FXML
    private Text text_email;
    @FXML
    private Button bt_addEmail;
    private String email;
    private Stage stage;
    private String configData;
    private int defaultSpinner;
    File fWin = new File("D:/arpSpoofSettings/settings.conf");
    File fUnix = new File("D:/arpSpoofSettings/settings.conf");

    /**
     * initialises the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        OsValidator os = new OsValidator();
        
        try {
            //spinner  setUp
            SpinnerValueFactory<Integer> numsVF = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10);
            spinner_timeMIn.setValueFactory(numsVF);
            
                           
                if (os.isWindows()) {
                    recoverSettingsInfo(fWin);
                }
                else if (os.isUnix()) {
                    recoverSettingsInfo(fUnix);
                }
            
            recoverSettingsInfo(fWin);

        } catch (IOException ex) {
            Logger.getLogger(FXMLSettingsController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    synchronized public void recoverSettingsInfo(File f) throws FileNotFoundException, IOException {
        
        String sendEmail = "";
        String frequency = "";
        String savedEmail = "";

        BufferedReader br = new BufferedReader(new FileReader(f));
        while ((configData = br.readLine()) != null) {
            sendEmail = configData;
            frequency = (configData = br.readLine());
            savedEmail = (configData = br.readLine());

        }

        br.close();

        
        if (sendEmail.contains("true")) {
            cb_sendEmail.setSelected(true);
        }

        if (frequency.matches(".*\\d.*")) {
            int value = Integer.parseInt(frequency.replaceAll("[^0-9]", ""));
            defaultSpinner = value;
            spinner_timeMIn.getValueFactory().setValue(value);

        }

        savedEmail = savedEmail.substring(13);
        savedEmail = savedEmail.trim();

        text_email.setText(savedEmail);

    }

    @FXML
    synchronized private void onClickAcceptSettings(MouseEvent event) throws IOException {
        writeNewSettings();
        stage = (Stage) bt_SettingAccept.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void onClickCancelSettings(MouseEvent event) {
        stage = (Stage) bt_SettingAccept.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void onCLickAddEmail(MouseEvent event) {
        email = et_newEmail.getText();
        text_email.setText(email);
        et_newEmail.setText("");
    }

    synchronized private void writeNewSettings() {
        
        OsValidator os = new OsValidator();

        boolean start = false;
        boolean sender = false;
        int frequency = spinner_timeMIn.getValue();
        String emailTo = text_email.getText();

        if (cb_sendEmail.isSelected()) {
            sender = true;
        }
        
        String config = "sendEmail = " + sender
                + "\nfrequency = " + frequency
                + "\nsavedEmail = " + emailTo;       
        

        if (!fWin.exists()) {
            return;
        } else {
            
            try {
                FileWriter fw = null;
                
                if (os.isWindows()) {
                    fw = new FileWriter(fWin);
                }
                else if (os.isUnix()) {
                    fw = new FileWriter(fUnix);
                }                
                
                BufferedWriter bw = new BufferedWriter(fw);

                bw.write(config);
                
                bw.close();
            } catch (IOException ex) {
                Logger.getLogger(FXMLSettingsController.class.getName()).log(Level.SEVERE, null, ex);
            } finally {

            }
        }

    }

}
