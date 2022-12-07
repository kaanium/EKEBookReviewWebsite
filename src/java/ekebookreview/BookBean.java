package ekebookreview;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

@ManagedBean(name = "BookBean")
@ViewScoped
public class BookBean implements Serializable{

    @ManagedProperty(value = "#{Main}")
    MainBean main;

    public MainBean getMain() {
        return main;
    }

    public void setMain(MainBean main) {
        this.main = main;
    }

    private ArrayList<Review> reviews;
    private int id;
    private Book book;
    private ArrayList<ArrayList<Integer>> reviewCounts;
    private String reviewText;
    private int reviewRating;

    public String getReviewText() {
        return reviewText;
    }

    public void setReviewText(String reviewText) {
        this.reviewText = reviewText;
    }

    public int getReviewRating() {
        return reviewRating;
    }

    public void setReviewRating(int reviewRating) {
        this.reviewRating = reviewRating;
    }

    public ArrayList<ArrayList<Integer>> getReviewCounts() {
        return reviewCounts;
    }

    public void setReviewCounts(ArrayList<ArrayList<Integer>> reviewCounts) {
        this.reviewCounts = reviewCounts;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public ArrayList<Review> getReviews() {
        return reviews;
    }

    public void setReviews(ArrayList<Review> reviews) {
        this.reviews = reviews;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void onLoad() {
        reviewCounts = new ArrayList<ArrayList<Integer>>();
        int[][] temp = new int[5][2];
        for (int[] row : temp) {
            Arrays.fill(row, 0);
        }
        
        getBookDB();
        reviews = new ArrayList<Review>();
        getReviewsDB();
        
        reviews.forEach((r) -> {
            int row = (int) r.getRating() - 1;
            temp[row][0] += 1;
        });
        for (int[] ints : temp) {
            ArrayList<Integer> list = new ArrayList<>();
            for (int i : ints) {
                list.add(i);
            }
            reviewCounts.add(list);
        }
        reviewCounts.forEach((r) -> {
            if(!reviews.isEmpty())
                r.set(1, (r.get(0) * 100) / reviews.size());
        });
        Collections.reverse(reviewCounts);
    }

    private void getBookDB() {
        List<Map<String,Object>> r = DBConnection.executeQuery("SELECT * FROM book WHERE id="+id);
        book = new Book((Integer)r.get(0).get("ID"));
    }

    private void getReviewsDB() {
        try {
            DBConnection db = new DBConnection();
            db.stmt = db.conn.createStatement();
            ResultSet results = db.stmt.executeQuery("SELECT id,userId,comment,rating FROM review WHERE bookId=" + book.getId());
            while (results.next()) {
                reviews.add(new Review(results.getInt("id"),
                        new User(results.getInt("userId")),
                        results.getString("comment"),
                        results.getBigDecimal("rating").floatValue()));
            }

            results.close();
            db.stmt.close();
        } catch (SQLException sqlExcept) {
            sqlExcept.printStackTrace();
        }
    }
    public void submitReview(){
        try {
            DBConnection db = new DBConnection();
            db.stmt = db.conn.createStatement();
            db.stmt.execute("INSERT INTO review (userId,bookId,comment,rating) VALUES("+main.getUser().getId() +","+ book.getId()+",'"+reviewText+"',"+reviewRating+")");
            int count=1;
            for(ArrayList<Integer> a : reviewCounts){
                count += a.get(0);
            }
            float newRating = (book.getRating()+reviewRating)/count;
            db.stmt.execute("UPDATE book SET rating = "+newRating+"WHERE id = "+book.getId());
            db.stmt.close();
            ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
            ec.redirect("book.xhtml?id="+book.getId()+"#review");
        } catch (Exception sqlExcept) {
            sqlExcept.printStackTrace();
        }
    }
}
