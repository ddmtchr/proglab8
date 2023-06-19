package server;

import server.Server;

public class ServerApp {
    public static void main(String[] args) {
        try {
            Server server = new Server(Integer.parseInt("1737"));
            server.run();
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Использование: java -jar server.jar {port}");
            System.exit(0);
        }
    }
}
