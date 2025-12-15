package main.Client;

import javafx.application.Platform;
import org.bson.Document;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class ChatClient {
    public interface Listener {
        void onOpenPrivateOk(String conversationId, String withUserId, java.util.List<Document> messages);
        void onNewMessage(String conversationId, Document message);
        void onError(String message);

        default void onListGroupsOk(java.util.List<Document> groups) {}
        default void onHelloOk() {}
    }

    private static ChatClient instance;
    public static ChatClient getInstance() {
        if (instance == null) instance = new ChatClient();
        return instance;
    }

    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private Listener listener;

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    private volatile boolean connected = false;

    public boolean isConnected() {
        return connected;
    }

    public void connect(String host, int port, String userIdHex, String email) throws Exception {
        socket = new Socket(host, port);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
        out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8), true);

        connected = true; // ✅ sau khi out != null

        send(new Document("type", "HELLO")
                .append("userId", userIdHex)
                .append("email", email));

        Thread t = new Thread(this::listenLoop);
        t.setDaemon(true);
        t.start();
    }

    private void listenLoop() {
        try {
            String line;
            while ((line = in.readLine()) != null) {
                System.out.println("RECV: " + line); // ✅ thêm dòng này

                Document res = Document.parse(line);
                String type = res.getString("type");

                if ("OPEN_PRIVATE_OK".equals(type)) {
                    String conversationId = res.getString("conversationId");
                    String withUserId = res.getString("withUserId");

//                    @SuppressWarnings("unchecked")
//                    java.util.List<Document> msgs = (java.util.List<Document>) res.get("messages");
                    java.util.List<Document> tmp = res.getList("messages", Document.class);
                    final java.util.List<Document> msgs = (tmp != null) ? tmp : java.util.List.of();

                    if (listener != null) Platform.runLater(() ->
                            listener.onOpenPrivateOk(conversationId, withUserId, msgs));
                }
                if ("OPEN_GROUP_OK".equals(type)) {
//                    String conversationId = res.getString("conversationId");
//
//                    @SuppressWarnings("unchecked")
//                    java.util.List<Document> msgs = (java.util.List<Document>) res.get("messages");
//
//                    if (listener != null) Platform.runLater(() ->
//                            listener.onOpenPrivateOk(conversationId, res.getString("groupName"), msgs)
//                    );
//                    String conversationId = res.getString("conversationId");
//                    String groupName = res.getString("groupName");
//
//                    java.util.List<Document> msgs = res.getList("messages", Document.class);
//                    if (msgs == null) msgs = java.util.List.of();
//
//                    if (listener != null) Platform.runLater(() ->
//                            listener.onOpenPrivateOk(conversationId, groupName, msgs));
                    final String conversationId = res.getString("conversationId");
                    final String groupName = res.getString("groupName");

                    java.util.List<Document> tmp = res.getList("messages", Document.class);
                    final java.util.List<Document> msgs = (tmp != null) ? tmp : java.util.List.of();

                    if (listener != null) {
                        Platform.runLater(() -> listener.onOpenPrivateOk(conversationId, groupName, msgs));
                    }
                }
                else if ("NEW_MESSAGE".equals(type)) {
                    String conversationId = res.getString("conversationId");
                    Document msg = (Document) res.get("message");

                    if (listener != null) Platform.runLater(() ->
                            listener.onNewMessage(conversationId, msg));
                }
                else if ("ERROR".equals(type)) {
                    String msg = res.getString("message");
                    if (listener != null) Platform.runLater(() -> listener.onError(msg));
                }
                else if ("HELLO_OK".equals(type)) {
                    connected = true;
                    if (listener != null) Platform.runLater(() -> listener.onHelloOk());
                }
                else if ("LIST_GROUPS_OK".equals(type)) {
                    @SuppressWarnings("unchecked")
                    java.util.List<Document> groups = (java.util.List<Document>) res.get("groups");
                    if (listener != null) Platform.runLater(() -> listener.onListGroupsOk(groups));
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void openPrivate(String withUserIdHex) {
        if (!connected || out == null) { // ✅ chặn null out
            if (listener != null) Platform.runLater(() ->
                    listener.onError("Client chưa connect xong (out=null). Hãy đợi TCP Connected!"));
            return;
        }

        send(new Document("type", "OPEN_PRIVATE").append("withUserId", withUserIdHex));
    }

    public void listGroups() {
        if (!connected || out == null) {
            if (listener != null) Platform.runLater(() -> listener.onError("Client chưa connect xong."));
            return;
        }
        send(new Document("type", "LIST_GROUPS"));
    }

    public void openGroup(String groupName) {
        if (!connected || out == null) {
            if (listener != null) Platform.runLater(() -> listener.onError("Client chưa connect xong."));
            return;
        }
        send(new Document("type", "OPEN_GROUP").append("groupName", groupName));
    }


    public void sendMessage(String conversationId, String content) {
        if (!connected || out == null) {
            if (listener != null) Platform.runLater(() ->
                    listener.onError("Client chưa connect xong (out=null)."));
            return;
        }

        send(new Document("type", "SEND")
                .append("conversationId", conversationId)
                .append("content", content));
    }

    private void send(Document doc) {
        synchronized (out) {
            out.println(doc.toJson());
            out.flush();
        }
    }

    public void createGroup(String groupName, java.util.List<String> memberIds) {
        if (!connected || out == null) {
            if (listener != null) Platform.runLater(() -> listener.onError("Client chưa connect xong."));
            return;
        }
        send(new Document("type", "CREATE_GROUP")
                .append("groupName", groupName)
                .append("memberIds", memberIds));
    }

}
