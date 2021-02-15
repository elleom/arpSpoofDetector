/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxapplication7;

import java.awt.AWTException;
import java.awt.CheckboxMenuItem;
import java.awt.Menu;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.NodeOrientation;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Accordion;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

/**
 *
 * @author el_le
 */
public class FXMLDocumentController implements Initializable {

    @FXML
    private Button bt_refresh;
    @FXML
    private Button bt_shield;
    @FXML
    private Button bt_settings;
    @FXML
    private Button bt_exit;
    @FXML
    private Pane localPane;
    @FXML
    private Label lb_LocalIfaceIP;
    @FXML
    private Label lb_LocalIfaceMAC;
    @FXML
    private Label lb_LocalIfaceVendor;
    @FXML
    private Accordion accordionPane;
    @FXML
    private Label lb_localOS;
    @FXML
    private Circle circleBlue;
    @FXML
    private ImageView iv_OS;
    @FXML
    private Button bt_arp;
    @FXML
    private ImageView iv_local;
    @FXML
    private AnchorPane anchorRootMain;
    private Stage stage;
    @FXML
    private javafx.scene.control.MenuItem menuItemSettings;
    @FXML
    private javafx.scene.control.MenuItem menuItemRefresh;
    @FXML
    private javafx.scene.control.MenuItem menuItemClose;
    @FXML
    private javafx.scene.control.MenuItem menuItemHide;
    @FXML
    private javafx.scene.control.MenuItem menuItemAbout;

    private String iface_address;
    private Local_IpAddress ip;
    private ArpTable arpTable;
    private String mac;
    private List<String> ips = new ArrayList();
    private HashMap<String, String> macs = new HashMap<>();
    private VendorLookUp vendorName = new VendorLookUp();
    private Image statusOK = new Image("/javafxapplication7/images/bt_statusOK.png");
    private Image statusAlert = new Image("/javafxapplication7/images/bt_statusAlert.png");
    String confInicial = "sendEmail = true\n"
            + "frequency = 10\n"
            + "savedEmail = ";

    private EmailSender emailSender = new EmailSender();

    private boolean windows = false;
    private boolean linux = false;
    private boolean protection = true;
    private boolean threatFound = false;
    public boolean emailService = true;

    private OsValidator oS = new OsValidator();
    private String configData;
    File fWin = new File("D:/arpSpoofSettings/settings.conf");
    File fUnix = new File("/opt/arpSpoofSettings/settings.conf");
    private String startUp;
    private String sendEmail = "el_leo_m@hotmail.com";
    private String frequency;
    private int frequencyValue;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO

        windows = oS.isWindows();

        if (windows) {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    stage = (Stage) anchorRootMain.getScene().getWindow();
                    stage.hide();
                }
            });
        }
        linux = oS.isUnix();

        //startApp();
        try {
            if (windows) {
              recoverSettingsInfo(fWin);  
            }
            else if (linux){
              recoverSettingsInfo(fUnix);   
            }
            
            
        } catch (IOException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    System.out.println(frequencyValue);
                    refresh();
                    startApp();

                    if (threatFound && protection) {
                        messageAlert();
                    }
                });
            }
        }, 0, frequencyValue); //keeps an infiniteLoop calling refhresh/start every n minutes;

    }

    @FXML
    private void onMouseClickExit(MouseEvent event) {
        stage = (Stage) bt_exit.getParent().getScene().getWindow();
        stage.hide();

    }

    @FXML
    private void onMouseExitButton(MouseEvent event) {
        Button entered = (Button) event.getSource();
        entered.setStyle("-fx-background-color: #20bac5;");

    }

    @FXML
    private void onMouseEnterButton(MouseEvent event) {
        Button entered = (Button) event.getSource();
        entered.setStyle("-fx-background-color: #25ccbe;");

    }

    @FXML
    private void onMouseExitPane(MouseEvent event) {
        localPane.setStyle("-fx-background-color: #ffffcc;");
    }

    @FXML
    private void onMouseEnterPane(MouseEvent event) {
        localPane.setStyle("-fx-background-color: #f7f7f7;");
    }

    private void osImageSetter(OsValidator oS) {

        if (oS.isWindows()) {
            iv_OS.setImage(new Image("/javafxapplication7/images/winLogo.png"));
        } else if (oS.isMac()) {
            iv_OS.setImage(new Image("/javafxapplication7/images/applelogo.png"));

        } else if (oS.isUnix()) {
            iv_OS.setImage(new Image("/javafxapplication7/images/linLogo.png"));
        } else if (oS.isSolaris()) {

        } else {
            System.out.println("Your OS is not support!!");
        }
    }

    @FXML
    private void onClickRefresh(MouseEvent event) {
        refreshAction();

    }

    private void refreshAction() {
        refresh(); //removes the info on TitledPane
        startApp(); //call to all methods
        if (threatFound && protection) {
            messageAlert();
        }
    }

    private void startApp() {

        ip = new Local_IpAddress();
        iface_address = ip.getIpInterface();

        Local_MacAddress macAddress = new Local_MacAddress(iface_address);
        mac = macAddress.getMac();

        arpTable = new ArpTable();
        arpTable.calcArpTable(iface_address);

        ips = arpTable.getIpList();
        macs = arpTable.getTable();

        lb_LocalIfaceIP.setText(iface_address);
        lb_LocalIfaceMAC.setText(mac);

        osImageSetter(oS);

        lb_localOS.setText(System.getProperty("os.name"));

        lb_LocalIfaceVendor.setText(vendorName.getVendor(mac));
        HashSet set = new HashSet();

        String found = "";

        ImageView iv_status;

        for (String ipSimple : ips) {

        }

        for (String ipSimple : ips) {
            String macVendor = "N/A";
            //System.out.println(ips.size());
            if (ipSimple.contains(iface_address.substring(0, 6))) {

                if ((macVendor = vendorName.getVendor(macs.get(ipSimple))) != null) {

                    Pane newPane = new Pane();
                    newPane.setMinSize(590, 60);
                    newPane.setMaxSize(590, 60);

                    FlowPane flow = new FlowPane();
                    flow.setMaxSize(590, 50);
                    flow.setHgap(75);
                    flow.setVgap(8);
                    flow.setPrefWrapLength(590);
                    flow.setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
                    flow.setLayoutX(20);
                    flow.setLayoutY(10);

                    ObservableList list = flow.getChildren();

                    Label labelMAC = new Label("MAC: " + macs.get(ipSimple));
                    Label labelVendor = new Label(macVendor);

                    iv_status = new ImageView(statusOK);

                    list.addAll(labelMAC, labelVendor, iv_status);
                    newPane.getChildren().add(flow);

                    TitledPane tp = new TitledPane(ipSimple, new Pane());
                    tp.maxHeight(80);

                    tp.setContent(newPane);
                    tp.autosize();

                    accordionPane.getPanes().add(tp);

                    if (set.add(macs.get(ipSimple)) == false) {
                        // your duplicate element

                        iv_status.setImage(statusAlert);
                        tp.setStyle("-fx-color:#FF584C;");
                        found = ipSimple;

                        threatFound = true;

                        if (emailService) {
                            emailSender.SendMailBySite(sendEmail);
                        }

                    }

                }

            }

        }

    }

    public void messageAlert() {

        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Threat found");
        alert.setContentText("This network may not be safe. For more information visit: \n"
                + " https://en.wikipedia.org/wiki/ARP_spoofing");
        alert.showAndWait();
    }

    private void refresh() {
        //System.out.println("ACC " + accordionPane.getPanes().size());

        for (int i = accordionPane.getPanes().size() - 1; i >= 0; i--) {
            accordionPane.getPanes().remove(i);
        }

        //System.out.println("ACC " + accordionPane.getPanes().size());
    }

    @FXML
    private void onClickShield(MouseEvent event) {
        toggleProtection();

    }

    private void toggleProtection() {
        if (protection) {
            bt_shield.setGraphic(new ImageView("/javafxapplication7/images/bt_protectionOFF.png"));
            protection = false;
        } else {
            bt_shield.setGraphic(null);
            protection = true;
        }

    }

    @FXML
    private void onCLickSettings(MouseEvent event) {
        openSettings();

    }

    private void openSettings() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/javafxapplication7/FXMLSettings.fxml"));
            Parent settingRoot = (Parent) fxmlLoader.load();
            Stage settingsStage = new Stage();
            settingsStage.setResizable(false);
            settingsStage.getIcons().add(new Image("/javafxapplication7/images/icono.png"));
            settingsStage.setScene(new Scene(settingRoot));
            settingsStage.setTitle("Arp SD Settings");
            settingsStage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void recoverSettingsInfo(File f) throws FileNotFoundException, IOException {
               
        if (f.createNewFile()) {

            FileWriter fw = new FileWriter(f);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(confInicial);
            fw.close();
            bw.close();
        }

        
        String sendEmailSetting = "";
        String frequencySetting = "";
        String savedEmail = "";

        BufferedReader br = new BufferedReader(new FileReader(f));
        while ((configData = br.readLine()) != null) {
            sendEmailSetting = configData;
            frequencySetting = (configData = br.readLine());
            savedEmail = (configData = br.readLine());

        }
        br.close();

        if (sendEmailSetting.contains("true")) {
            emailService = true;
        }

        if (frequencySetting.matches(".*\\d.*")) {
            int value = Integer.parseInt(frequencySetting.replaceAll("[^0-9]", ""));

            frequencyValue = 60000 * value;

        }

        savedEmail = savedEmail.substring(13);
        savedEmail = savedEmail.trim();
        sendEmail = savedEmail;

    }

    @FXML
    private void onClickLog(MouseEvent event) {
        OpenAbout();
    }

    private void OpenAbout() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/javafxapplication7/FXMLLog.fxml"));
            Parent settingRoot = (Parent) fxmlLoader.load();
            Stage aboutStage = new Stage();
            aboutStage.setResizable(false);
            aboutStage.setTitle("About...");
            aboutStage.setScene(new Scene(settingRoot));
            aboutStage.getIcons().add(new Image("/javafxapplication7/images/icono.png"));
            aboutStage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void menuItemSettings(ActionEvent event) {
        openSettings();
    }

    @FXML
    private void menuItemRefresh(ActionEvent event) {
        refreshAction();
    }

    @FXML
    private void menuItemClose(ActionEvent event) {
        System.exit(0);
    }

    @FXML
    private void onMenuItemHide(ActionEvent event) {
        Platform.exit();
    }

    @FXML
    private void onMenuItemAbout(ActionEvent event) {
        OpenAbout();
    }

}
