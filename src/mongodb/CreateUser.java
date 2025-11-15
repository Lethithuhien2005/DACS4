package mongodb;

import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;
import org.bson.Document;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class CreateUser {

    // Hàm hash password với SHA-256
    public static String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedHash = digest.digest(password.getBytes(StandardCharsets.UTF_8));

            // Chuyển mảng byte sang chuỗi hex
            StringBuilder hexString = new StringBuilder();
            for (byte b : encodedHash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        try (MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017")) {

            MongoDatabase db = mongoClient.getDatabase("halomeet");
            MongoCollection<Document> users = db.getCollection("users");

            // Hash password trước khi lưu
            String hashedPassword = hashPassword("123");

            Document user = new Document("username", "quynhanh")
                    .append("fullname", "Nguyen Quynh Anh")
                    .append("email", "quynhanh@example.com")
                    .append("password", hashedPassword)
                    .append("female", true)
                    .append("dateOfBirth", LocalDate.parse("2005-03-15", DateTimeFormatter.ISO_DATE));

            users.insertOne(user);

            System.out.println("User đã được tạo thành công với password đã hash!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
