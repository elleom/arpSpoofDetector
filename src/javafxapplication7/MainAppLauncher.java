/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxapplication7;

import java.io.IOException;
import java.net.URL;
import java.util.Timer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javax.imageio.ImageIO;

/**
 *
 * @author el_le
 */
public class MainAppLauncher extends Application {

    // one icon location is shared between the application tray icon and task bar icon.
    // you could also use multiple icons to allow for clean display of tray icons on hi-dpi devices.
    private static final String iconImageLoc = "https://img.icons8.com/cute-clipart/16/000000/search.png";

    // application stage is stored so that it can be shown and hidden based on system tray icon operations.
    private Stage stage;

    // a timer allowing the tray icon to provide a periodic notification event.
    private Timer notificationTimer = new Timer();

    // format used to display the current time in a tray icon notification.
    //private DateFormat timeFormat = SimpleDateFormat.getTimeInstance();
    // sets up the javafx application.
    // a tray icon is setup for the icon, but the main stage remains invisible until the user
    // interacts with the tray icon.
    @Override
    public void start(Stage stage) throws Exception {
        System.out.println("mehtod.ARP_spoof.start()");

        Parent root = FXMLLoader.load(MainAppLauncher.class.getResource("/javafxapplication7/FXMLDocument.fxml"));
        Scene scene = new Scene(root);
        scene.getStylesheets().add("/javafxapplication7/style.css");
        stage.setScene(scene);
        stage.setTitle("ARP Spoof Detector v1.0b");
        stage.setResizable(false);
        stage.getIcons().add(new Image("/javafxapplication7/images/icono.png"));

        // stores a reference to the stage.
        this.stage = stage;

        // instructs the javafx system not to exit implicitly when the last application window is shut.
        Platform.setImplicitExit(false);

        OsValidator os = new OsValidator();

        if (os.isWindows()) {

            // sets up the tray icon (using awt code run on the swing thread).
            javax.swing.SwingUtilities.invokeLater(this::addAppToTray);

            // out stage will be translucent, so give it a transparent style.
            //stage.initStyle(StageStyle.TRANSPARENT);
            // create the layout for the javafx stage.
            StackPane layout = new StackPane(createContent());
            layout.setStyle(
                    "-fx-background-color: rgba(255, 255, 255, 0.5);"
            );
            layout.setPrefSize(300, 200);

            // this dummy app just hides itself when the app screen is clicked.
            // a real app might have some interactive UI and a separate icon which hides the app window.
            layout.setOnMouseClicked(event -> stage.hide());

            // a scene with a transparent fill is necessary to implement the translucent app window.
            Scene sceneTray = new Scene(layout);
            sceneTray.setFill(Color.TRANSPARENT);

        }

        stage.setScene(scene);
        stage.show();

    }

    /**
     * @return the main window application content.
     */
    private Node createContent() {
        Label hello = new Label("hello, world");
        hello.setStyle("-fx-font-size: 40px; -fx-text-fill: forestgreen;");
        Label instructions = new Label("(click to hide)");
        instructions.setStyle("-fx-font-size: 12px; -fx-text-fill: orange;");

        VBox content = new VBox(10, hello, instructions);
        content.setAlignment(Pos.CENTER);

        return content;
    }

    /**
     * Sets up a system tray icon for the application.
     */
    private void addAppToTray() {
        try {
            // ensure awt toolkit is initialized.
            java.awt.Toolkit.getDefaultToolkit();

            // app requires system tray support, just exit if there is no support.
            if (!java.awt.SystemTray.isSupported()) {
                System.out.println("No system tray support, application exiting.");
                Platform.exit();
            }

            // set up a system tray icon.
            java.awt.SystemTray tray = java.awt.SystemTray.getSystemTray();
            URL imageLoc = new URL(
                    iconImageLoc
            );
            java.awt.Image image = ImageIO.read(imageLoc);
            java.awt.TrayIcon trayIcon = new java.awt.TrayIcon(image);

            // if the user double-clicks on the tray icon, show the main app stage.
            trayIcon.addActionListener(event -> Platform.runLater(this::showStage));

            // if the user selects the default menu item (which includes the app name), 
            // show the main app stage.
            java.awt.MenuItem openItem = new java.awt.MenuItem("Show arpSD");
            openItem.addActionListener(event -> Platform.runLater(this::showStage));

            // the convention for tray icons seems to be to set the default icon for opening
            // the application stage in a bold font.
            java.awt.Font defaultFont = java.awt.Font.decode(null);
            java.awt.Font boldFont = defaultFont.deriveFont(java.awt.Font.BOLD);
            openItem.setFont(boldFont);

            // to really exit the application, the user must go to the system tray icon
            // and select the exit option, this will shutdown JavaFX and remove the
            // tray icon (removing the tray icon will also shut down AWT).
            java.awt.MenuItem exitItem = new java.awt.MenuItem("Close");
            exitItem.addActionListener(event -> {
                notificationTimer.cancel();
                tray.remove(trayIcon);
                System.exit(0);
            });

            // setup the popup menu for the application.
            final java.awt.PopupMenu popup = new java.awt.PopupMenu();
            popup.add(openItem);
            popup.addSeparator();
            popup.add(exitItem);
            trayIcon.setPopupMenu(popup);

            tray.add(trayIcon);
        } catch (java.awt.AWTException | IOException e) {
            System.out.println("Unable to init system tray");
            e.printStackTrace();
        }
    }

    private void showStage() {
        if (stage != null) {
            stage.show();
            stage.toFront();
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);

    }

}
