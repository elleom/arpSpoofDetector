/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxapplication7;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author el_le
 */
public class Local_MacAddress {
    
    private String ip;
    private String mac;
    private String dato = "";
    private Matcher mtch;
    private String regexMAC = "([0-9a-fA-F]{2}[\\\\.:-]){5}([0-9A-Fa-f]{2})"; //patron MAC (!= CISCO)
    private Pattern ptnMAC;
    private OsValidator os = new OsValidator();
    
    public Local_MacAddress(String ip){
        System.out.println("App.Local_MacAddress.<init>()");
        this.ip = ip;
        
        try {
            Runtime rt = Runtime.getRuntime();
            
            /**
             * establece una consulta en el WINDOWS MANAGEMENT INSTRUMENTATION CONTROLLER
             * devuelve todas las interfaces activas instaladas.
             */
            if (os.isWindows()) {
                Process pr = rt.exec("wmic nicconfig Where IPEnabled=true get Description,MacAddress,IPAddress");
                
                BufferedReader br = new BufferedReader(new InputStreamReader(pr.getInputStream()));
            
            while ((dato = br.readLine()) !=null) {
                if (dato.contains(ip)) {
                 ptnMAC = Pattern.compile(regexMAC, Pattern.CASE_INSENSITIVE);
                 mtch = ptnMAC.matcher(dato);
                    while (mtch.find()) {                        
                        mac = mtch.group();
                        System.out.println(mac);
                    }
                 
                }                
            }
            
            }
            
            //*******************OUTPUT LINUX*************************
            /**
             * enp0s3: flags=4163<UP,BROADCAST,RUNNING,MULTICAST>  mtu 1500
             inet 192.168.0.84  netmask 255.255.255.0  broadcast 192.168.0.255
             inet6 fe80::ef50:9210:2730:78d8  prefixlen 64  scopeid 0x20<link>
             ether 00:00:00:00:00 :bd  txqueuelen 1000  (Ethernet)
             RX packets 6342  bytes 3254575 (3.2 MB)
             RX errors 0  dropped 0  overruns 0  frame 0
             TX packets 4454  bytes 602761 (602.7 KB)
             TX errors 0  dropped 0 overruns 0  carrier 0  collisions 0
             */
            
            if (os.isUnix()) {
                Process pr = rt.exec("ifconfig");
                
                BufferedReader br = new BufferedReader(new InputStreamReader(pr.getInputStream()));
                
                while ((dato = br.readLine()) != null) {
                    if (dato.contains(ip)) {
                        while (!dato.contains("ether")) {
                            dato = br.readLine();
                        }
                        ptnMAC = Pattern.compile(regexMAC, Pattern.CASE_INSENSITIVE);
                        mtch = ptnMAC.matcher(dato);
                        while (mtch.find()) {
                            mac = mtch.group();
                            System.out.println(mac);
                        }
                    }
                }
                
            }
            
                        
            
        } catch (IOException ex) {
            Logger.getLogger(Local_MacAddress.class.getName()).log(Level.SEVERE, null, ex);
            
        }
    
    }
    
    public String getMac() {
        return mac;
    }

}
