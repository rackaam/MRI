package mri.socket.tcp.charset;


import java.io.*;
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
            socketClient = new Socket(adresseDuServeur, portDuServeur);//Création socket
        } catch (IOException e) {
            e.printStackTrace();
        }
        PrintWriter printWriter = null;
        BufferedReader bufferedReader = null;

        String charset;
        if (args.length > 1) {
            charset = args[1];
        } else {
            charset = "UTF-8";//Par défaut UTF_8
        }

        try {
            bufferedReader = creerReader(socketClient, charset);//Création du reader utlisant le bon charset
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            printWriter = creerWriter(socketClient, charset);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (args.length > 0) {
            envoyerNom(printWriter, args[0]);
        } else {
            envoyerNom(printWriter, "Provencal le gaulois");
        }
        String res = null;
        try {
            res = recevoirMessage(bufferedReader);
            System.out.println("Réponse serveur: " + res);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String s = null;
        while (!(s = lireMessageAuClavier()).equals("fin")) {
            envoyerMessage(printWriter, s);
            try {
                res = recevoirMessage(bufferedReader);
                System.out.println("Réponse serveur: " + res);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * Recupere la saisi d'un utilisateur sur l'entree standard
     * @return s
     */
    public static String lireMessageAuClavier() {
        Scanner scanner = new Scanner(System.in);
        String s = scanner.nextLine();
        return s;
    }

    /**
     * Création d'un reader associé à une socket utilisant le bon charset
     *@param socket
     * @param charset
     * @return
     * @throws IOException
     */
    public static BufferedReader creerReader(Socket socket, String charset) throws IOException {
        return new BufferedReader(new InputStreamReader(socket.getInputStream(), charset));
    }

    /**
     * Création d'un writer associé à une socket
     * @param socket
     * @param charset
     * @return
     * @throws IOException
     */
    public static PrintWriter creerWriter(Socket socket, String charset) throws IOException {
        return new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), charset));
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
    public static void envoyerMessage(PrintWriter printWriter, String message) {
        printWriter.println(message);
        printWriter.flush();
    }

    public static void envoyerNom(PrintWriter printWriter, String nom) {
        printWriter.println("NAME:" + nom);
        printWriter.flush();
    }
}