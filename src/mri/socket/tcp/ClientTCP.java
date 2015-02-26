package mri.socket.tcp;


import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class ClientTCP {

    /**
     * Lance un client TCP, qui se connecte au serveur d'adresse 127.0.0.1 sur le port 9999
     * @param args
     */
    public static void main(String[] args) {

        int portDuServeur = 9999;
        String adresseDuServeur = "127.0.0.1";
        Socket socketClient = null;
        try {
            socketClient = new Socket(adresseDuServeur, portDuServeur);//création de la socket
        } catch (IOException e) {
            e.printStackTrace();
        }
        //création du writer et reader associé à la socket
        PrintWriter printWriter = null;
        BufferedReader bufferedReader = null;
        try {
             bufferedReader = creerReader(socketClient);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            printWriter =  creerWriter(socketClient);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String s = null;
        while (!(s = lireMessageAuClavier()).equals("fin")){
            envoyerMessage(printWriter, s);//envoie message
            try {
                String res = recevoirMessage(bufferedReader);//reception message
                System.out.println("Réponse serveur: "+res);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * Recupere la saisi d'un utilisateur sur l'entree standard
     * @return s
     */
    public static String lireMessageAuClavier(){
        Scanner scanner = new Scanner(System.in);
        String s = scanner.nextLine();
        return  s;
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
     * Envoyer message vers le client
     * Ecrit dans le PrintWriter
     * @param printWriter
     * @param message
     */
    public static void envoyerMessage(PrintWriter printWriter, String message){
        printWriter.println(message);
        printWriter.flush();
    }
}