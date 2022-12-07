package ekebookreview;

import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.sql.SQLException;
import java.util.Base64;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

@ManagedBean(name="SignUpBean")
@ViewScoped
public class SignUpBean {

    public SignUpBean() {
        this.successful = false;
        this.error = false;
    }

    private String username;
    private String email;
    private String name;
    private String age;
    private String password;
    private String passworda;
    private boolean successful;
    private boolean error;
    private String hashed;
    private String salt;
    private String errorMessage;

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
    public boolean isSuccessful() {
        return successful;
    }

    public void setSuccessful(boolean successful) {
        this.successful = successful;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
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
    public String getAge() {
        return age;
    }
    public void setAge(String age) {
        this.age = age;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getPassworda() {
        return passworda;
    }
    public void setPassworda(String passworda) {
        this.passworda = passworda;
    }
    
    public void signupButtonAction(){
        if(username.isEmpty()||email.isEmpty()||password.isEmpty()||name.isEmpty()||!password.equals(passworda)){
            error=true;
            successful = false;
            return;
        }
        hashPassword();
        successful = signToDB();
        error = !successful;
    }
    private void hashPassword(){
        try{
            SecureRandom random = new SecureRandom();
            byte[] saltTemp = new byte[16];
            random.nextBytes(saltTemp);
            
            KeySpec spec = new PBEKeySpec(password.toCharArray(), saltTemp, 65536, 128);
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            
            byte[] hash = factory.generateSecret(spec).getEncoded();
            hashed = Base64.getEncoder().encodeToString(hash);
            salt = Base64.getEncoder().encodeToString(saltTemp);
        }
        catch(Exception e){
            
        }
    }
    private boolean signToDB(){
        try{
            DBConnection db = new DBConnection();
            db.stmt = db.conn.createStatement();
            db.stmt.execute("INSERT INTO users (username,password,name,email,salt) VALUES ('"+username+"','"+hashed+"','"+name+"','"+email+"','"+salt+"')");
            db.stmt.close();
            return true;
        }
        catch (SQLException sqlExcept)
        {
            sqlExcept.printStackTrace();
            errorMessage=sqlExcept.toString();
            return false;
        }
    }
}
