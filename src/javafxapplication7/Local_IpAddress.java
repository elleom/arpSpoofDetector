package javafxapplication7;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 *
 * @author el_le
 */
public class Local_IpAddress {

    private String ip;
    private String mac;
    Local_MacAddress macAddress;

    public Local_IpAddress() {

        /**
         * MULTIPLE NETWORK INTERFACES 
         * It always returns the preferred outbound IP. The destination 8.8.8.8 is not needed to be reachable.
         * Connect on a UDP socket has the following effect: it sets the destination for Send/Recv, 
         * discards all packets from other addresses, and - which is what we use - transfers the socket into "connected" state,
         * settings its appropriate fields. This includes checking the existence of the route to the destination according to the
         * system's routing table and setting the local endpoint accordingly. The last part seems to be undocumented officially 
         * but it looks like an integral trait of Berkeley sockets API (a side effect of UDP "connected" state) that works reliably
         * in both Windows and Linux across versions and distributions.
         * 
         * So, this method will give the local address that would be used to connect to the specified remote host.
         * There is no real connection established, hence the specified remote ip can be unreachable.
         */
        
        try (final DatagramSocket socket = new DatagramSocket()) {
            socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
            ip = socket.getLocalAddress().getHostAddress();
                                   
            System.out.println(ip + "\n");
        } catch (SocketException | UnknownHostException e) {

        }        
    }

    public String getIpInterface() {
        return ip;
    }
    
}
