package main.Server;

//import common.meeting.MeetingService;
import main.Server.Controller.CentralHandler;

import main.Server.Controller.ChatServiceImplement;
import main.Server.Controller.MeetingServiceImplement;
import main.Server.DAO.MongoChatRepository;
import shared.ChatService;
import shared.MeetingService;
//import shared.MeetingService;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ServerMain {

    public static void main(String[] args) throws Exception {
        int tcpPort = 5555;
        int rmiPort = 2005;

        try {
            // ===== 1. START RMI =====
            Registry registry = LocateRegistry.createRegistry(rmiPort);
            MeetingService meetingService = new MeetingServiceImplement();
            ChatService chatService = new ChatServiceImplement();

            registry.rebind("MeetingService", meetingService);
            registry.rebind("ChatService", chatService);

            System.out.println("✅ RMI Services running on port " + rmiPort);
            System.out.println("   - MeetingService");
            System.out.println("   - ChatService");

            // ===== 2. START TCP SERVER =====
            MongoChatRepository repo = new MongoChatRepository();
            CentralHandler tcpServer = new CentralHandler(tcpPort, repo);

            tcpServer.start(); // chặn tại đây

            System.out.println("✅ TCP Server running on port " + tcpPort);

        } catch (Exception e) {
            e.printStackTrace();
        }

//        MongoChatRepository repo = new MongoChatRepository();
//        // Ket noi tcp
//        CentralHandler TCPserver = new CentralHandler(tcpPort, repo);
//        TCPserver.start();
//
//        // ket noi RMI
//        new Thread(() -> {
//            try {
//                Registry registry = LocateRegistry.createRegistry(2005);
//                MeetingService meetingServiceSkeleton = new MeetingServiceImplement();
//                registry.rebind("MeetingService", meetingServiceSkeleton);
//                ChatService chatServiceSkeleton = new ChatServiceImplement();
//                registry.rebind("ChatService", chatServiceSkeleton);
//
//                System.out.println("Server RMI is running...");
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }).start();

    }
}
