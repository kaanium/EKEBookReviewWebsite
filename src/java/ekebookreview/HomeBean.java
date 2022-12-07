package ekebookreview;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

@ManagedBean(name = "HomeBean")
@ViewScoped
public class HomeBean implements Serializable{

    private ArrayList<Book> books;
    private ArrayList<String> categoriesToDisplay = new ArrayList<String>();
    
    public HomeBean(){
        categoriesToDisplay.add("fantasy");
        categoriesToDisplay.add("romance");
        categoriesToDisplay.add("mystery");
        categoriesToDisplay.add("horror");
        categoriesToDisplay.add("biography");

    }

    public ArrayList<String> getCategoriesToDisplay() {
        return categoriesToDisplay;
    }

    public void setCategoriesToDisplay(ArrayList<String> categoriesToDisplay) {
        this.categoriesToDisplay = categoriesToDisplay;
    }

    public ArrayList<Book> getBooks() {
        return books;
    }

    public void setBooks(ArrayList<Book> books) {
        this.books = books;
    }
    
    public ArrayList<ArrayList<Object>> getBooksByGenre(String genre){
        List<Map<String,Object>> results;

        results = DBConnection.executeQuery("SELECT id FROM genre WHERE name='"+genre+"'");
        int genreId=(Integer)results.get(0).get("ID");
        results = DBConnection.executeQuery("SELECT id FROM book WHERE genreId="+genreId);
        ArrayList<ArrayList<Object>> books = new ArrayList<ArrayList<Object>>();
        results.forEach((rs) -> {
            Book b = new Book((Integer)rs.get("ID"));
            ArrayList<Object> n = new ArrayList<Object>();
            n.add(b);
            books.add(n);
        });
        return books;
    }
    public ArrayList<ArrayList<Object>> getBooksByLastReview(){
        List<Map<String,Object>> results;

        results = DBConnection.executeQuery("SELECT id,bookId FROM review ORDER BY id DESC FETCH FIRST 4 ROWS ONLY");
        ArrayList<ArrayList<Object>> books = new ArrayList<ArrayList<Object>>();
        results.forEach((rs) -> {
            Book b = new Book((Integer)rs.get("BOOKID"));
            ArrayList<Object> n = new ArrayList<Object>();
            n.add(b);
            n.add(new Review((Integer)rs.get("ID")));
            books.add(n);
        });
        return books;
    }
    public String categoryString(){
        String str;
        str = "[";
        for(String c : categoriesToDisplay){
            str += "'#" + c + "'";
            if(c!=categoriesToDisplay.get(categoriesToDisplay.size()-1)){
                str +=",";
            }
        }
        str += "]";
        return str;
    }
}
