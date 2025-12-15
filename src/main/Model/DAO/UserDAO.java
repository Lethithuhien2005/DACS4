package main.Model.DAO;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import main.Model.Entities.User;
import main.Model.MongoDBConnection;
import main.Model.PasswordUtils;
import org.bson.Document;
import static com.mongodb.client.model.Filters.eq;


import static javax.management.Query.eq;

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
               Document doc = new Document("account_name", user.getAccount_name())
                .append("full_name", user.getFull_name())
                .append("email", user.getEmail())
                .append("password", user.getPassword())
                .append("role", user.getRole());
        users.insertOne(doc);
    }

    // login to app
    public User getUserByEmail(String email) {
        Document doc = users.find(eq("email", email)).first();
        if (doc == null) {
            return null;
        }
        // Create object User from document
        User user = new User(
                doc.getString("username"),
                doc.getString("email"),
                doc.getString("password"),
                doc.getString("role")
        );
        return user;
    }

}
