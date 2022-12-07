package ekebookreview;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

//this class might not have to be a bean
@ManagedBean(name="User")
@SessionScoped
public class UserBean {
    
    private int id;
    private String name;
    private String username;
    private DBConnection db;
    private boolean isGuest;
    public User user;
    
    public UserBean(int id) {
        db = new DBConnection();
        this.id = id;
        if(id==0){
            isGuest = true;
        }
        else {
            getUserInfo();
            isGuest = false;
        }
        user = new User(id);
    }
    
    private void getUserInfo(){
        try{
            db.stmt = db.conn.createStatement();
            ResultSet results = db.stmt.executeQuery("SELECT name,username,email FROM users WHERE id="+id);
            results.next();
            name = results.getString("name");
            username = results.getString("username");
            results.close();
            db.stmt.close();
        }
        catch(SQLException sqlExcept){
            sqlExcept.printStackTrace();
        }
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public DBConnection getDb() {
        return db;
    }

    public void setDb(DBConnection db) {
        this.db = db;
    }

    public boolean isIsGuest() {
        return isGuest;
    }

    public void setIsGuest(boolean isGuest) {
        this.isGuest = isGuest;
    }
}
