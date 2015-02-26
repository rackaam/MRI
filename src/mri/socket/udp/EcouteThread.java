package mri.socket.udp;

import java.net.MulticastSocket;

class EcouteThread extends Thread {

    private MulticastSocket socket;

    EcouteThread(MulticastSocket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        while (true) {
            String m = ChatMulticast.recevoirMessage(socket);
            System.out.println(m);
        }
    }
}