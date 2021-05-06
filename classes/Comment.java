package classes;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Comment {
    private String author;
    private String comment;
    private String date;
    private SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    private Date d = new Date();

    public Comment(String author, String comment, String date) {
        this.author = author;
        this.comment = comment;
        this.date = date;
    }

    public Comment(String author, String comment) {
        this.author = author;
        this.comment = comment;
        this.date = formatter.format(date);
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
