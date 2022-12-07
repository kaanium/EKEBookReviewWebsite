package ekebookreview;

import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Base64;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

@ManagedBean(name = "settings")
@ViewScoped
public class SettingsBean {

    @ManagedProperty(value = "#{Main}")
    MainBean main;

    private String oldPassword;
    private String newPassword;
    private String newPasswordAgain;
    private String hashed;
    private String salt;
    private String oldHash;
    private String oldSalt;
    private boolean successful;
    private boolean error;

    public SettingsBean() {
        this.successful = false;
        this.error = false;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public boolean isSuccessful() {
        return successful;
    }

    public void setSuccessful(boolean successful) {
        this.successful = successful;
    }

    public MainBean getMain() {
        return main;
    }

    public void setMain(MainBean main) {
        this.main = main;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getNewPasswordAgain() {
        return newPasswordAgain;
    }

    public void setNewPasswordAgain(String newPasswordAgain) {
        this.newPasswordAgain = newPasswordAgain;
    }

    private void hashPassword() {
        try {
            SecureRandom random = new SecureRandom();
            byte[] saltTemp = new byte[16];
            random.nextBytes(saltTemp);

            KeySpec spec = new PBEKeySpec(newPassword.toCharArray(), saltTemp, 65536, 128);
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");

            byte[] hash = factory.generateSecret(spec).getEncoded();
            hashed = Base64.getEncoder().encodeToString(hash);
            salt = Base64.getEncoder().encodeToString(saltTemp);
        } catch (Exception e) {

        }
    }

    private boolean hashOldPassword() {
        try {
            SecureRandom random = new SecureRandom();
            byte[] saltTemp;
            if (oldSalt == null) {
                saltTemp = new byte[16];
                random.nextBytes(saltTemp);
            } else {
                saltTemp = Base64.getDecoder().decode(oldSalt);
            }

            KeySpec spec = new PBEKeySpec(oldPassword.toCharArray(), saltTemp, 65536, 128);
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");

            byte[] hash = factory.generateSecret(spec).getEncoded();
            if (oldHash.equals(Base64.getEncoder().encodeToString(hash))) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    public void changePassword() {
        if (oldPassword.isEmpty() || newPassword.isEmpty() || newPasswordAgain.isEmpty() || !newPassword.equals(newPasswordAgain)) {
            error = true;
            return;
        }
        hashPassword();
        getHashSalt();
        if (hashOldPassword()) {
            DBConnection.execute("UPDATE users SET password='" + hashed + "',salt='" + salt + "' WHERE id=" + main.getUser().user.getId());
            successful = true;
            error = false;
        }
        else{
            successful = false;
            error = true;
        }
    }

    private void getHashSalt() {
        try {
            DBConnection db = new DBConnection();
            db.stmt = db.conn.createStatement();
            ResultSet results = db.stmt.executeQuery("SELECT password,salt FROM users WHERE id=" + main.getUser().user.getId());
            results.next();
            oldHash = results.getString("password");
            oldSalt = results.getString("salt");
            results.close();
            db.stmt.close();
        } catch (SQLException sqlExcept) {
            sqlExcept.printStackTrace();
        }
    }
}
