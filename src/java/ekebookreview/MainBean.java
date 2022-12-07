package ekebookreview;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean(name="Main")
@SessionScoped
public class MainBean {

    private UserBean user;

    public UserBean getUser() {
        return user;
    }

    public void setUser(UserBean user) {
        this.user = user;
    }
    
    public MainBean() {
        this.user = new UserBean(0);
    }
    
    public String logout(){
        if(this.getUser().getId() != 0){
            this.user = new UserBean(0);
        }
        return "index";
    }
}
