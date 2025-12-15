package main.Server;

public class ServerMain {
    public static void main(String[] args) throws Exception {
        int port = 5555;

        MongoChatRepository repo = new MongoChatRepository();
        ChatServer server = new ChatServer(port, repo);

        server.start(); // chặn tại đây
    }
}
