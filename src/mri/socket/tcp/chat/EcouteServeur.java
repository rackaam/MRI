package mri.socket.tcp.chat;

import java.io.BufferedReader;
import java.io.IOException;

public class EcouteServeur implements Runnable {


    private BufferedReader reader;

    public EcouteServeur(BufferedReader reader) {
        this.reader = reader;
    }

    @Override
    public void run() {
        while (true) {
            String res = null;
            try {
                res = ClientTCP.recevoirMessage(reader);//Réception message
                System.out.println(res);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
