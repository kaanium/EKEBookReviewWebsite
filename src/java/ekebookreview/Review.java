package ekebookreview;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Review {
    private int id;
    private User user;
    private String comment;
    private float rating;
    private ArrayList<Boolean> reviewStars;

    public Review(int id, User user, String comment, float rating) {
        this.id = id;
        this.user = user;
        this.comment = comment;
        this.rating = rating;
        
        reviewStars = new ArrayList<Boolean>();
        for(int i=0;i<5;i++){
            if(i<this.rating)
                reviewStars.add(true);
            else
                reviewStars.add(false);
        }
    }
    public Review(int id){
        this.id = id;
        List<Map<String,Object>> result;
        result = DBConnection.executeQuery("SELECT id,userId,comment,rating FROM review WHERE id=" + id);
        Map<String,Object> rs = result.get(0);
        this.id = (Integer) rs.get("ID");
        this.user = new User((Integer)rs.get("USERID"));
        this.comment = (String) rs.get("COMMENT");
        BigDecimal bg = (BigDecimal)rs.get("RATING");
        this.rating = bg.floatValue();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User userId) {
        this.user = user;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public ArrayList<Boolean> getReviewStars() {
        return reviewStars;
    }

    public void setReviewStars(ArrayList<Boolean> reviewStars) {
        this.reviewStars = reviewStars;
    }
    
}
