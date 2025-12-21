//package main.Server;
//
//import main.Server.Controller.CentralHandler;
//import main.Server.DAO.MongoChatRepository;
//
//public class ServerMain {
//    public static void main(String[] args) throws Exception {
//        int port = 5555;
//
//        MongoChatRepository repo = new MongoChatRepository();
//        CentralHandler server = new CentralHandler(port, repo);
//
//        server.start(); // chặn tại đây
//    }
//}
package main.Server;

import common.meeting.MeetingService;
import main.Server.Controller.CentralHandler;
//import main.Server.Controller.MeetingService;
import main.Server.Controller.MeetingServiceImplement;
import main.Server.DAO.MongoChatRepository;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ServerMain {

    public static void main(String[] args) {
        int tcpPort = 5555;
        int rmiPort = 2005;

        try {
            // ===== 1. START RMI =====
            Registry registry = LocateRegistry.createRegistry(rmiPort);
            MeetingService meetingService = new MeetingServiceImplement();
            registry.rebind("MeetingService", meetingService);
            System.out.println("RMI MeetingService running on port " + rmiPort);

            // ===== 2. START TCP SERVER =====
            MongoChatRepository repo = new MongoChatRepository();
            CentralHandler tcpServer = new CentralHandler(tcpPort, repo);

            tcpServer.start(); // chặn tại đây

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
