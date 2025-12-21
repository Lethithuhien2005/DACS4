package main.Server;

import main.Server.Controller.CentralHandler;
import main.Server.Controller.MeetingServiceImplement;
import main.Server.DAO.MongoChatRepository;
import shared.MeetingService;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ServerMain {
    public static void main(String[] args) throws Exception {
        int port = 5555;

        MongoChatRepository repo = new MongoChatRepository();
        // Ket noi tcp
        CentralHandler TCPserver = new CentralHandler(port, repo);
        TCPserver.start();

        // ket noi RMI
        new Thread(() -> {
            try {
                Registry registry = LocateRegistry.createRegistry(2005);
                MeetingService meetingServiceSkeleton = new MeetingServiceImplement();
                registry.rebind("MeetingService", meetingServiceSkeleton);
                System.out.println("Server RMI is running...");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

    }
}
