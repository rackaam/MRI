package mri.socket.address;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class AfficheInterfaces {

    public static void main(String[] args) {
        Enumeration<NetworkInterface> interfaces = null;
        try {
            interfaces = NetworkInterface.getNetworkInterfaces();
        } catch (SocketException e) {
            e.printStackTrace();
        }
        if (interfaces != null) {
            while (interfaces.hasMoreElements()) {
                NetworkInterface inter = interfaces.nextElement();
                System.out.println(inter.getDisplayName());
                Enumeration<InetAddress> address = inter.getInetAddresses();
                while(address.hasMoreElements()){
                    InetAddress a = address.nextElement();
                    System.out.println("->" + a);
                }
                System.out.println();
            }

        }
    }
}
