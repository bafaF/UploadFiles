package fr.upload.uploadfiles.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;

import java.io.File;
import java.util.Date;

@Entity
public class UploadFile {

    @Id
    private String id;

    @NotBlank
    @JsonIgnore
    private String filename;

    @NotBlank
    private String originalFilename;

    private Date uploadDate;

    private Date deletingDate;

    @JsonIgnore
    private File path;

    private String size;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    public UploadFile() {
    }

    public UploadFile(String id, String filename, String originalFilename, Date uploadDate, Date deletingDate, File path, String size, User user) {
        this.id = id;
        this.filename = filename;
        this.originalFilename = originalFilename;
        this.uploadDate = uploadDate;
        this.deletingDate = deletingDate;
        this.path = path;
        this.size = size;
        this.user = user;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getOriginalFilename() {
        return originalFilename;
    }

    public void setOriginalFilename(String originalFilename) {
        this.originalFilename = originalFilename;
    }

    public Date getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(Date uploadDate) {
        this.uploadDate = uploadDate;
    }

    public Date getDeletingDate() {
        return deletingDate;
    }

    public void setDeletingDate(Date deletingDate) {
        this.deletingDate = deletingDate;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public File getPath() {
        return path;
    }

    public void setPath(File path) {
        this.path = path;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
