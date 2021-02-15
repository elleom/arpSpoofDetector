/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxapplication7;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author el_le
 */
public class PingService implements Runnable { 
    private String ip;
    private OsValidator os;
    
    public PingService(String ip){
        this.ip = ip;
        
    }

    @Override
    public void run() {
        
        os = new OsValidator();
        
        
        try {
            Runtime rt = Runtime.getRuntime();
            
            if (os.isWindows()) {
                Process pr = rt.exec("ping " + ip);
            }
            else if (os.isUnix()){
                Process pr = rt.exec("ping -c 4 " + ip); //by default ping runs until Ctrl+C
            }                                           //so this command makes it only 4 as in windows;
                                                
        } catch (UnknownHostException ex) {
            Logger.getLogger(PingService.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(PingService.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
}
