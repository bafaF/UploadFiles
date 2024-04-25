package fr.upload.uploadfiles.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.Date;

@Entity
public class DownloadedFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    private Date lastDownloadedDate;

    private Date expirationDate;

    private String owner;

    private String filename;

    private String size;

    private String link;


    public DownloadedFile() {
    }

    public DownloadedFile(Long id, User user, Date lastDownloadedDate, Date expirationDate, String owner, String filename, String size, String link) {
        this.id = id;
        this.user = user;
        this.lastDownloadedDate = lastDownloadedDate;
        this.expirationDate = expirationDate;
        this.owner = owner;
        this.filename = filename;
        this.size = size;
        this.link = link;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public Date getLastDownloadedDate() {
        return lastDownloadedDate;
    }

    public void setLastDownloadedDate(Date lastDownloadedDate) {
        this.lastDownloadedDate = lastDownloadedDate;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

}
