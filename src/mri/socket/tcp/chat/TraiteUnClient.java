package mri.socket.tcp.chat;


import java.net.Socket;

public class TraiteUnClient implements Runnable {

    private Socket socket;

    public TraiteUnClient(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        ServeurTCP.traiterSocketCliente(socket);
    }
}
