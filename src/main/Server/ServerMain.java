package main.Server;

import main.Server.Controller.CentralHandler;
import main.Server.DAO.MongoChatRepository;

public class ServerMain {
    public static void main(String[] args) throws Exception {
        int port = 5555;

        MongoChatRepository repo = new MongoChatRepository();
        CentralHandler server = new CentralHandler(port, repo);

        server.start(); // chặn tại đây
    }
}
