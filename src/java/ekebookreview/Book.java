package ekebookreview;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.Map;

public class Book {

    private int id;
    private String name;
    private String author;
    private String synopsis;
    private float rating;

    public Book(int id, String name, String author, String synopsis, float rating) {
        this.id = id;
        this.name = name;
        this.author = author;
        this.synopsis = synopsis;
        DecimalFormat df = new DecimalFormat("#.##");
        DecimalFormatSymbols dfs = new DecimalFormatSymbols();
        dfs.setDecimalSeparator('.');
        df.setDecimalFormatSymbols(dfs);
        this.rating = Float.valueOf(df.format(rating));
    }
    public Book(int id){
        List<Map<String,Object>> result;
        result = DBConnection.executeQuery("SELECT id,name,author,synopsis,rating FROM book WHERE id=" + id);
        Map<String,Object> rs = result.get(0);
        this.id = (Integer) rs.get("ID");
        this.name = (String) rs.get("NAME");
        this.author = (String) rs.get("AUTHOR");
        this.synopsis = (String) rs.get("SYNOPSIS");
        DecimalFormat df = new DecimalFormat("#.##");
        DecimalFormatSymbols dfs = new DecimalFormatSymbols();
        dfs.setDecimalSeparator('.');
        df.setDecimalFormatSymbols(dfs);
        this.rating = Float.valueOf(df.format(rs.get("RATING")));
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

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }
    
    public String getShortSynopsis(){
        String str = synopsis;
        if(synopsis.length()>130)
            str = synopsis.substring(0, 131);
        str += "...";
        return str;
    }
    public String getShortSynopsis(int length){
        String str = synopsis;
        if(synopsis.length()>length)
            str = synopsis.substring(0, length+1);
        str += "...";
        return str;
    }
}
