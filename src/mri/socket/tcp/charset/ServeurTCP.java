package mri.socket.tcp.charset;


import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class ServeurTCP {

    public static void main(String[] args) {
        int socketPort = 9999;
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(socketPort);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String charset;
        if (args.length > 0) {
            charset = args[0];
        } else {
            charset = "UTF-8";
        }
        boolean run = true;
        while(run){
            try {
                Socket socket = serverSocket.accept();
                System.out.println("Client connecté");
                traiterSocketCliente(socket, charset);
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("Client déconnecté");
        }

    }

    public static void traiterSocketCliente(Socket socket, String charset){
        BufferedReader reader = null;
        try {
            reader = creerReader(socket, charset);
        } catch (IOException e) {
            e.printStackTrace();
        }
        PrintWriter writer = null;
        try {
            writer = creerWriter(socket, charset);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(reader != null && writer != null){
            String clientName = null;
            try {
                clientName = avoirNom(reader);
                if(clientName == null){
                    envoyerMessage(writer, "BAD NAME");
                    return;
                }else{
                    envoyerMessage(writer, "NAME OK");
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

    public static BufferedReader creerReader(Socket socket, String charset) throws IOException {
        return new BufferedReader(new InputStreamReader(socket.getInputStream(), charset));
    }

    public static PrintWriter creerWriter(Socket socket, String charset) throws IOException {
        return new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), charset));
    }

    public static String recevoirMessage(BufferedReader reader) throws IOException {
        return reader.readLine();
    }

    public static void envoyerMessage(PrintWriter printWriter, String message){
        printWriter.println(message);
        printWriter.flush();
    }

    public static String avoirNom(BufferedReader reader) throws IOException {
        String s = reader.readLine();
        if(!s.matches("NAME:.+"))
            return null;
        return s.substring(5);
    }
}
