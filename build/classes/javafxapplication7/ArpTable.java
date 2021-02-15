/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxapplication7;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author el_le
 */
public class ArpTable {

    String ifaceIP;
    List<String> ips = new ArrayList();
    HashMap<String, String> macs = new HashMap<>();
    private OsValidator os = new OsValidator();

    public ArpTable() {

        //EMPTY 
    }

    public void calcArpTable(String ifaceIP) {

        System.out.println("ArpTable.calcArpTable()");
        System.out.println("[+]clearing ip table");
        ips.clear();
        System.out.println("[+]clearing mac table");
        macs.clear();

        try {
            this.ifaceIP = ifaceIP;
            pingNetwork(ifaceIP);
            Matcher mtch;
            String regexIP = "(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})"; //patron IPV4
            String regexMAC = "([0-9a-fA-F]{2}[\\\\.:-]){5}([0-9A-Fa-f]{2})"; //patron MAC (!= CISCO)
            Pattern ptnIP;
            Pattern ptnMAC;
            String datoRecibido;
            Runtime rt = Runtime.getRuntime();
            try {

                Process pr = null;
                BufferedReader br = null;

                if (os.isWindows()) {

                    Process prFlush = rt.exec("arp -d"); //flush //requires elevated Privileges
                    System.out.println("WARNING: Only admin/root HAS clear access ");

                    pr = rt.exec("arp -a -N " + ifaceIP); //call
                    br = new BufferedReader(new InputStreamReader(pr.getInputStream()));

                    for (int i = 0; i < 3; i++) {
                        br.readLine(); //omite el Header
                    }

                    /**
                     * FLUSH ARP TABLE TO AVOID INCORRECT DATA FIRST CALLS IN
                     * ARP TABLE COMMAND AFTER TO GET RENEWED DATA
                     */
                }

                if (os.isUnix()) {
                    pr = rt.exec("arp -a"); //callvv

                    br = new BufferedReader(new InputStreamReader(pr.getInputStream()));

                }

                while ((datoRecibido = br.readLine()) != null) {

                    String ipFound = null;
                    String macFound = null;

                    ptnIP = Pattern.compile(regexIP);
                    ptnMAC = Pattern.compile(regexMAC, Pattern.CASE_INSENSITIVE);

                    mtch = ptnIP.matcher(datoRecibido);
                    while (mtch.find()) {

                        ipFound = mtch.group();
                        ips.add(ipFound);
                    }
                    mtch = ptnMAC.matcher(datoRecibido);
                    while (mtch.find()) {
                        macFound = mtch.group();

                    }
                    if (null != ipFound & null != macFound) {
                        macs.put(ipFound, macFound);
                    }

                }

            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }

        } catch (IOException ex) {
            Logger.getLogger(ArpTable.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public HashMap<String, String> getTable() {

        return macs;
    }

    public List<String> getIpList() {
        return ips;
    }

    public final void pingNetwork(String iface) throws IOException {

        Matcher matcher;
        String IPcut = "(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})\\."; //patron IPV4
        Pattern pattern;
        pattern = Pattern.compile(IPcut);
        matcher = pattern.matcher(iface);

        while (matcher.find()) {
            iface = matcher.group();
        }

        System.out.println("[+] pingNetwork() + SUBNET = "+ iface);
        for (int i = 1; i <= 255; i++) {
            String address = iface + i;
            new Thread(new PingService(address)).start();

        }
        System.out.println("[+] app.ArpTable.pingNetwork() finished");

    }
}
