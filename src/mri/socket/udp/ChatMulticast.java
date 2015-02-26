package mri.socket.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.Scanner;

public class ChatMulticast {

    private static InetAddress groupeIP;
    private static final int PORT = 6789;

    public static void main(String[] args) throws IOException, InterruptedException {
        groupeIP = InetAddress.getByName("225.0.4.9");
        MulticastSocket socket = new MulticastSocket(PORT);
        socket.joinGroup(groupeIP);

        String name;
        if (args.length > 0) {
            name = args[0];
        } else {
            name = "Provencal le gaulois"; // default
        }

        new EcouteThread(socket).start();
        int i = 0;
        String s = null;
        while (!(s = lireMessageAuClavier()).equals("fin")) {
            envoyerMessage(socket, name + "> " + s);
        }
    }

    /**
     * Envoyer des messages
     * @param s
     * @param message
     */
    static void envoyerMessage(MulticastSocket s, String message) {
        byte[] contenuMessage = message.getBytes();
        if (contenuMessage.length > 1024)
            return;
        DatagramPacket packet;
        packet = new DatagramPacket(contenuMessage, contenuMessage.length, groupeIP, PORT);
        try {
            s.send(packet);//envoie du datagramme
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Recevoir message
     * Méthode bloquante tant qu'un message n'est pas reçu
     * @param s
     * @return
     */
    static String recevoirMessage(MulticastSocket s) {
        byte[] contenuMessage = new byte[1024];//Taille de message maximale 1024
        DatagramPacket message = new DatagramPacket(contenuMessage, contenuMessage.length);
        try {
            s.receive(message);
            return new String(contenuMessage, message.getOffset(), message.getLength());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Lit un message au clavier
     * @return
     */
    static String lireMessageAuClavier() {
        Scanner scanner = new Scanner(System.in);
        String s = scanner.nextLine();
        return s;
    }

}
