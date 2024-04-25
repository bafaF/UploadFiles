package fr.upload.uploadfiles.entity.impl;

import fr.upload.uploadfiles.entity.UploadFile;

import java.util.Date;

public class FileToSendImpl {

    private String id;

    private String originalFilename;

    private Date downloadDate;

    private Date deletingDate;

    private String size;

    private String owner;

    public FileToSendImpl(String id, String originalFilename, Date downloadDate, Date deletingDate, String size, String owner) {
        this.id = id;
        this.originalFilename = originalFilename;
        this.downloadDate = downloadDate;
        this.deletingDate = deletingDate;
        this.size = size;
        this.owner = owner;
    }

    public static FileToSendImpl build(UploadFile file){
        if (file.getUser() != null){
            return new FileToSendImpl(file.getId(), file.getOriginalFilename(), file.getUploadDate(), file.getDeletingDate(), file.getSize(), file.getUser().getPseudo());
        }else {
            return new FileToSendImpl(file.getId(), file.getOriginalFilename(), file.getUploadDate(), file.getDeletingDate(), file.getSize(), null);
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOriginalFilename() {
        return originalFilename;
    }

    public void setOriginalFilename(String originalFilename) {
        this.originalFilename = originalFilename;
    }

    public Date getDownloadDate() {
        return downloadDate;
    }

    public void setDownloadDate(Date downloadDate) {
        this.downloadDate = downloadDate;
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

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }
}
