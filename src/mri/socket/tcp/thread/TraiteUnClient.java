package mri.socket.tcp.thread;


import mri.socket.tcp.charset.*;

import java.net.Socket;

public class TraiteUnClient implements Runnable {

    private Socket socket;

    public TraiteUnClient(Socket socket){
        this.socket = socket;
    }

    @Override
    public void run() {
        mri.socket.tcp.thread.ServeurTCP.traiterSocketCliente(socket);
    }
}
