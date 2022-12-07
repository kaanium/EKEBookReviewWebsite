package ekebookreview;

import java.io.*;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.Part;

@ManagedBean(name = "addbook")
@ViewScoped
public class AddBookBean {

    private Part uploadedFile; // +getter+setter
    private File savedFile;
    private String bookName;
    private String bookAuthor;
    private String bookGenre;
    private String bookSynopsis;
    private ArrayList<String> genres;
    private String tempFileName = "";

    public String getTempFileName() {
        return tempFileName;
    }

    public void setTempFileName(String tempFileName) {
        this.tempFileName = tempFileName;
    }

    public ArrayList<String> getGenres() {
        return genres;
    }

    public void setGenres(ArrayList<String> genres) {
        this.genres = genres;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getBookAuthor() {
        return bookAuthor;
    }

    public void setBookAuthor(String bookAuthor) {
        this.bookAuthor = bookAuthor;
    }

    public String getBookGenre() {
        return bookGenre;
    }

    public void setBookGenre(String bookGenre) {
        this.bookGenre = bookGenre;
    }

    public String getBookSynopsis() {
        return bookSynopsis;
    }

    public void setBookSynopsis(String bookSynopsis) {
        this.bookSynopsis = bookSynopsis;
    }

    public Part getUploadedFile() {
        return uploadedFile;
    }

    public void setUploadedFile(Part uploadedFile) {
        this.uploadedFile = uploadedFile;
    }

    public void upload() {
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();

        String path = externalContext.getRealPath("/resources/book-images");
        path = path.replace("\\build", "");
        bookSynopsis = bookSynopsis.replaceAll("'", "''");
        List<Map<String,Object>> results = DBConnection.executeQuery("SELECT id FROM genre WHERE UPPER(name)=UPPER('"+bookGenre+"')");
        int genreId = (int) results.get(0).get("ID");
        String str = "INSERT INTO book (name,author,synopsis,genreId) VALUES (\'%s\',\'%s\',\'%s\',%d)";
        str = String.format(str, bookName, bookAuthor, bookSynopsis, genreId);
        int bookId = DBConnection.executeUpdate(str);
        String fileName = bookId+".jpg";
        
        savedFile = new File(path, fileName);

        try (InputStream input = uploadedFile.getInputStream()) {
            Files.copy(input, savedFile.toPath());
        } catch (IOException e) {
            // Show faces message?
        }
    }

    public void getGenresDB() {
        List<Map<String, Object>> results = DBConnection.executeQuery("SELECT name FROM genre");
        results.forEach((r) -> {
            genres.add((String) r.get("NAME"));
        });
    }

    public void onLoad() {
        genres = new ArrayList<String>();
        getGenresDB();
    }
    
    public void uploadTempImage() {
        tempFileName = Paths.get(uploadedFile.getSubmittedFileName()).getFileName().toString(); // MSIE fix.
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        String path = externalContext.getRealPath("/resources/book-images");
        
        savedFile = new File(path, tempFileName);

        try (InputStream input = uploadedFile.getInputStream()) {
            Files.copy(input, savedFile.toPath());
        } catch (IOException e) {
            // Show faces message?
        }
    }
}
