package mri.socket.tcp;


import java.io.*;
import java.net.*;
import java.util.Enumeration;

public class ServeurTCP {

    public static void main(String[] args) {
        int socketPort = 9999;
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(socketPort);
        } catch (IOException e) {
            e.printStackTrace();
        }
        boolean run = true;
        while(run){
            try {
                Socket socket = serverSocket.accept();
                System.out.println("Client connecté");
                traiterSocketCliente(socket);
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("Client déconnecté");
        }

    }

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
            String s;
            try {
                while ((s = recevoirMessage(reader)) != null){
                    System.out.println("New message:" + s);
                    envoyerMessage(writer, s);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static BufferedReader creerReader(Socket socket) throws IOException {
        return new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    public static PrintWriter creerWriter(Socket socket) throws IOException {
        return new PrintWriter(socket.getOutputStream());
    }

    public static String recevoirMessage(BufferedReader reader) throws IOException {
        return reader.readLine();
    }

    public static void envoyerMessage(PrintWriter printWriter, String message){
        printWriter.println(message);
        printWriter.flush();
    }
}
