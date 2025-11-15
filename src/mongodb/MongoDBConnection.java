package mongodb;

import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;

public class MongoDBConnection {
    public static void main(String[] args) {
        // Tạo kết nối MongoDB
        try (MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017")) {

            // Chọn database
            MongoDatabase database = mongoClient.getDatabase("testdb");

            // Kiểm tra connection
            System.out.println("Kết nối MongoDB thành công đến database: " + database.getName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
