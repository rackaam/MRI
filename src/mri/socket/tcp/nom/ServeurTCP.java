package mri.socket.tcp.nom;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class ServeurTCP {

    /**
     * Creer un serveur TCP ouvert sur le port 9999
     * @param args
     */
    public static void main(String[] args) {
        int socketPort = 9999;
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(socketPort);
        } catch (IOException e) {
            e.printStackTrace();
        }
        boolean run = true;
        while(run){//Attente de connexions
            try {
                Socket socket = serverSocket.accept();
                System.out.println("Client connecté");
                traiterSocketCliente(socket);//Traitement d'une socket
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("Client déconnecté");
        }

    }

    /**
     * Gère l'envoie et la réception de message depuis le serveur
     * @param socket
     */
    public static void traiterSocketCliente(Socket socket){
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(reader != null && writer != null){
            String clientName = null;
            try {
                clientName = avoirNom(reader);
                if(clientName == null){
                    envoyerMessage(writer, "BAD NAME");//Nom incorect
                    return;
                }else{
                    envoyerMessage(writer, "NAME OK");//Nom correct
                }
             }
             catch (IOException e) {
                e.printStackTrace();
            }
            String s;
            try {
                while ((s = recevoirMessage(reader)) != null){
                    System.out.println(clientName + " > " + s);
                    envoyerMessage(writer, s);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Création d'un reader associé à une socket
     *@param socket
     * @return
     * @throws IOException
     */
    public static BufferedReader creerReader(Socket socket) throws IOException {
        return new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    /**
     * Création d'un writer associé à une socket
     * @param socket
     * @return
     * @throws IOException
     */
    public static PrintWriter creerWriter(Socket socket) throws IOException {
        return new PrintWriter(socket.getOutputStream());
    }

    /**
     *  Lit le contenu du buffered reader
     * @param reader
     * @return string
     * @throws IOException
     */
    public static String recevoirMessage(BufferedReader reader) throws IOException {
        return reader.readLine();
    }

    /**
     * Ecrit dans le PrintWriter
     * @param printWriter
     * @param message
     */
    public static void envoyerMessage(PrintWriter printWriter, String message){
        printWriter.println(message);
        printWriter.flush();
    }

    /**
     * Récupere le nom du client
     * @param reader
     */
    public static String avoirNom(BufferedReader reader) throws IOException {
        String s = reader.readLine();
        if(!s.matches("NAME:.+"))
            return null;
        return s.substring(5);
    }
}
