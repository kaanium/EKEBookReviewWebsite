package ekebookreview;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

@ManagedBean(name = "search")
@ViewScoped
public class SearchBean implements Serializable {

    public class CheckItem {

        private String name;
        private boolean value;

        public CheckItem(String name, boolean value) {
            this.name = name;
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public boolean getValue() {
            return value;
        }

        public void setValue(boolean value) {
            this.value = value;
        }
    }

    private ArrayList<CheckItem> genres;
    private String searchString;
    private ArrayList<Book> resultBooks;
    private int searchRating;
    private String orderColumn;
    private String orderType;

    public String getOrderColumn() {
        return orderColumn;
    }

    public void setOrderColumn(String orderColumn) {
        this.orderColumn = orderColumn;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public int getSearchRating() {
        return searchRating;
    }

    public void setSearchRating(int searchRating) {
        this.searchRating = searchRating;
    }

    public ArrayList<Book> getResultBooks() {
        return resultBooks;
    }

    public void setResultBooks(ArrayList<Book> resultBooks) {
        this.resultBooks = resultBooks;
    }

    public String getSearchString() {
        return searchString;
    }

    public void setSearchString(String searchString) {
        this.searchString = searchString;
    }

    public ArrayList<CheckItem> getGenres() {
        return genres;
    }

    public void setGenres(ArrayList<CheckItem> genres) {
        this.genres = genres;
    }

    public void getGenresDB() {
        List<Map<String, Object>> results = DBConnection.executeQuery("SELECT name FROM genre");
        genres = new ArrayList<CheckItem>();

        for (int i = 0; i < results.size(); i++) {
            genres.add(new CheckItem((String) results.get(i).get("NAME"), false));
        }
    }

    public void onLoad() {
        getGenresDB();
        searchRating = 0;
    }

    public void filter() {
        List<Map<String, Object>> results;
        resultBooks = new ArrayList<Book>();
        String query = "";
        String genreStr = "";
        
        boolean genreFlag = false;
        for (int i = 0; i < genres.size(); i++) {
            if (genres.get(i).value == true) {
                if (genreFlag == false) {
                    genreFlag = true;
                    query = "SELECT book.id FROM book INNER JOIN genre ON book.genreId=genre.id WHERE book.genreId IN ( SELECT id FROM genre WHERE name IN ( %s )) AND UPPER(book.name) LIKE UPPER('%%%s%%')";
                }
                genreStr += "'"+genres.get(i).name + "',";
            }
        }
        
        if(!genreFlag)
            query = "SELECT id FROM book WHERE UPPER(name) LIKE UPPER('%" + searchString + "%')";
        else{
            genreStr = genreStr.substring(0,genreStr.length()-1);
            query = String.format(query, genreStr,searchString);
        }
        query += " AND rating>="+searchRating+" ORDER BY "+orderColumn+" "+orderType;
        results = DBConnection.executeQuery(query);
        if(results.isEmpty())
            return;
        results.forEach((r) -> {
            resultBooks.add(new Book((Integer) r.get("ID")));
        });
    }
}
