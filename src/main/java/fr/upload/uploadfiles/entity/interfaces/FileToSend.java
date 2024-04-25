package fr.upload.uploadfiles.entity.interfaces;

import java.util.Date;

public interface FileToSend {

    String getId();

    String getOriginalFilename();

    Date getUploadDate();

    Date getDeletingDate();

    String getSize();

    String getOwner();

}
