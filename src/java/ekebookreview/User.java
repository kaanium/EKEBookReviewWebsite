package ekebookreview;

import java.sql.ResultSet;
import java.sql.SQLException;

public class User {
    private int id;
    private String username;
    private String email;
    private String name;
    private DBConnection db;
    
    public User(int id, String username, String name) {
        this.id = id;
        this.username = username;
        this.name = name;
    }
    public User(int id){
        this.id=id;
        db = new DBConnection();
        try{
            db.stmt = db.conn.createStatement();
            ResultSet results = db.stmt.executeQuery("SELECT name,username,email FROM users WHERE id="+this.id);
            results.next();
            name = results.getString("name");
            username = results.getString("username");
            email = results.getString("email");
            results.close();
            db.stmt.close();
        }
        catch(SQLException sqlExcept){
            sqlExcept.printStackTrace();
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
