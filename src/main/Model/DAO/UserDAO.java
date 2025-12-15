package main.Model.DAO;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import main.Model.Entities.User;
import main.Model.MongoDBConnection;
import main.Model.PasswordUtils;
import org.bson.Document;

import java.time.ZoneId;
import java.util.Date;

import static com.mongodb.client.model.Filters.eq;



// Truy xuat MongoDB
public class UserDAO {
    private MongoCollection<Document> users;

    public UserDAO() {
        users = MongoDBConnection.getDatabase().getCollection("users");
    }

    // Check if duplicate email
    public boolean checkEmail(String email) {
        Document document = users.find(Filters.eq("email", email)).first();
        return document != null;
    }

    // Add a user to database
    public void addUser(User user) {
               Document doc = new Document("username", user.getUsername())
                       .append("fullName", user.getFullName())
                       .append("email", user.getEmail())
                        .append("password", user.getPassword())
                        .append("role", user.getRole())
                        .append("created_at", new Date())
                        .append("updated_at", new Date())
                       .append("account_name", user.getAccountName()) // Thêm vào cho thông tin account_name
                       .append("gender", user.getGender())            // Thêm vào cho thông tin gender
                       .append("phone", user.getPhone())              // Thêm vào cho thông tin phone
                       .append("address", user.getAddress())
                       .append("dob", user.getDob() != null ? java.util.Date.from(user.getDob().atStartOfDay(ZoneId.systemDefault()).toInstant()) : null); // dob// Thêm vào cho thông tin address
                users.insertOne(doc);

//               Document doc = new Document("account_name", user.getAccountName())
//                .append("full_name", user.getFullName())
//                .append("email", user.getEmail())
//                .append("password", user.getPassword())
//                .append("role", user.getRole());
//        users.insertOne(doc);
    }

    // login to app
    public User getUserByEmail(String email) {
        Document doc = users.find(eq("email", email)).first();
        if (doc == null) {
            return null;
        }
        // Create object User from document
//        User user = new User(
//                doc.getString("username"),
//                doc.getString("email"),
//                doc.getString("password"),
//                doc.getString("role")
//        );
        User user = new User(
                doc.getString("accountName"),
                doc.getString("fullName"),       // fullname
                doc.getString("email"),
                doc.getString("password"),
                doc.getString("role"),
                doc.getString("gender"),         // gender
                doc.getString("phone"),          // phone
                doc.getString("address"),        // address
                doc.getDate("dob") != null
                        ? doc.getDate("dob").toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
                        : null                       // dob
        );

        user.setUserId(doc.getObjectId("_id"));   // ✅ LẤY ObjectId từ Mongo
        // ⭐ THÊM 2 DÒNG NÀY
        user.setCreatedAt(doc.getDate("created_at"));
        user.setUpdatedAt(doc.getDate("updated_at"));

        return user;
    }

    public boolean updateUser(User user) {
        try {
            Document update = new Document()
                    .append("fullName", user.getFullName())
                    .append("gender", user.getGender())
                    .append("phone", user.getPhone())
                    .append("address", user.getAddress())
                    .append("dob", user.getDob() != null ? java.util.Date.from(user.getDob().atStartOfDay(ZoneId.systemDefault()).toInstant()) : null);

            users.updateOne(eq("email", user.getEmail()), new Document("$set", update));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void updatePassword(String email, String newHashedPassword) {
        users.updateOne(eq("email", email), new Document("$set", new Document("password", newHashedPassword)));
    }

    public void updateUpdatedAt(String email) {
        users.updateOne(
                eq("email", email),
                new Document("$set", new Document("updated_at", new java.util.Date()))
        );
    }



}
