package mri.socket.address;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class AfficheInterfaces {

    /**
     * Affiche l'ensemble des interfaces réseau de la machine.
     * Affiche l'adresse associé à l'interface
     * @param args
     */
    public static void main(String[] args) {
        Enumeration<NetworkInterface> interfaces = null;
        try {
            interfaces = NetworkInterface.getNetworkInterfaces();
        } catch (SocketException e) {
            e.printStackTrace();
        }
        if (interfaces != null) {
            while (interfaces.hasMoreElements()) {//pour chaque interface de la machine
                NetworkInterface inter = interfaces.nextElement();
                System.out.println(inter.getDisplayName());//affichage du nom de l'interface
                Enumeration<InetAddress> address = inter.getInetAddresses();
                while(address.hasMoreElements()){
                    InetAddress a = address.nextElement();//affichage de l'adresse associé à l'interface
                    System.out.println("->" + a);
                }
                System.out.println();
            }

        }
    }
}
