package mri.socket.tcp.chat;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ServeurTCP {

    /*Liste static des printers vers les sockets actives */
    private static List<PrintWriter> printerSocketActives = new ArrayList<PrintWriter>();

    /**
     * Creer un serveur TCP ouvert sur le port 9999
     * @param args
     */
    public static void main(String[] args) {
        int socketPort = 9999;
        ServerSocket serverSocket = null;
        Executor pool = Executors.newFixedThreadPool(2);//Pool de threads


        try {
            serverSocket = new ServerSocket(socketPort);
        } catch (IOException e) {
            e.printStackTrace();
        }
        boolean run = true;
        while (run) {
            try {

                Socket socket = serverSocket.accept();
                TraiteUnClient traiteUnClient = new TraiteUnClient(socket);
                //new Thread(traiteUnClient).start(); Exercice 4 - Question 3
                pool.execute(traiteUnClient);//Exercice 4 - Question 5
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

    /**
     * Gère l'envoie et la réception de message depuis le client
     * @param socket
     */
    public static void traiterSocketCliente(Socket socket) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(socket.getOutputStream());
            ajouterPrinterSocketActives(writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (reader != null && writer != null) {
            String clientName = null;
            try {
                clientName = avoirNom(reader);
                if (clientName == null) {
                    envoyerMessage(writer, "BAD NAME");
                    return;
                } else {
                    envoyerMessage(writer, "NAME OK");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            String s;
            try {
                while ((s = recevoirMessage(reader)) != null) {
                    String post = clientName + " > " + s;
                    System.out.println(post);
                    envoyerATouteLesSocketsActive(post);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        enleverPrinterSocketActives(writer);
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
     *  @return
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
    public static void envoyerMessage(PrintWriter printWriter, String message) {
        printWriter.println(message);
        printWriter.flush();
    }

    /**
     * envoie le message à toutes les sockets actives
     * @param message
     */
    public static synchronized void envoyerATouteLesSocketsActive(String message) {
        for (PrintWriter writer : printerSocketActives) {
            writer.println(message);
            writer.flush();
        }
    }

    public static String avoirNom(BufferedReader reader) throws IOException {
        String s = reader.readLine();
        if (!s.matches("NAME:.+"))
            return null;
        return s.substring(5);
    }

    /**
     *ajouter le printer à la liste
     * @param printWriter
     */
    public static synchronized void ajouterPrinterSocketActives(PrintWriter printWriter) {
        printerSocketActives.add(printWriter);
    }

    /**
     * enlever le printer à la liste
     * @param printWriter
     */
    public static synchronized void enleverPrinterSocketActives(PrintWriter printWriter) {
        printerSocketActives.remove(printWriter);
    }
}
