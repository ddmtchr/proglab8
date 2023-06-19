package client;

import client.controllers.MainWindowController;
import javafx.collections.FXCollections;
import utility.Request;
import utility.RequestType;
import utility.Response;

import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.Arrays;

public class ConnectionProvider {

    private final DatagramSocket datagramSocket;
    private final DatagramChannel datagramChannel;
    private final InetAddress HOST = InetAddress.getLocalHost();
    private final int PORT = 1737;
    private final int BUFFER_SIZE = 8 * 1024;
    private static ConnectionProvider connectionProvider;

    public ConnectionProvider() throws IOException {
//        this.host = InetAddress.getByName(host);
//        this.port = port;
        datagramChannel = DatagramChannel.open();
        datagramSocket = datagramChannel.socket();
        datagramSocket.setSoTimeout(5000);
//        System.out.println("Подключен к серверу " + HOST + ". Порт: " + PORT);
    }

    public void send(Request request) {
        ObjectOutputStream oos = null;

        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(out);
            oos.writeObject(request);

            DatagramPacket datagramPacket = new DatagramPacket(out.toByteArray(), out.size(), HOST, PORT);

            datagramSocket.send(datagramPacket);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                assert oos != null;
                oos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public synchronized Response receive() throws SocketTimeoutException {
        try {
            Response response;
            boolean received = false;
            byte[] result = new byte[0];

            while(!received) {
                ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
                DatagramPacket packet = new DatagramPacket(buffer.array(), buffer.array().length);
//                SocketAddress address = null;
//                while (address == null) {
//                    address = datagramChannel.receive(buffer);
                    datagramSocket.receive(packet);
//                }
                byte[] data = buffer.array();
                if (data[data.length - 1] == 1) {
                    received = true;
                }
                result = Arrays.copyOf(result, result.length + data.length - 1);
                System.arraycopy(data, 0, result, result.length - data.length + 1, data.length - 1);

            }
            ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(result));
            response = (Response) ois.readObject();
            ois.close();
            return response;

        } catch (SocketTimeoutException e) {
            throw new SocketTimeoutException();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

//    public void loadCollection(Session session) {
//        try {
//            Request request = new Request(null, null, null,
//                    session.getLogin(), session.getPassword(), null, RequestType.GET_COLLECTION);
//            send(request);
//            Response response = receive();
//            ClientCollectionManager.setClientCollection(response.getBody().getCollection());
//            MainWindowController.setCollection(ClientCollectionManager.getClientCollection());
//        } catch (SocketTimeoutException e) {
//            System.out.println("СокетТаймАутЭксепшон");
//        }
//    }

    public static synchronized ConnectionProvider getConnectionProvider() throws IOException {
        if (connectionProvider == null) {
            connectionProvider = new ConnectionProvider();
        }
        return connectionProvider;
    }
}
