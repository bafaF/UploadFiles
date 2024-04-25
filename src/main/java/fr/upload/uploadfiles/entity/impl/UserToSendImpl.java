package fr.upload.uploadfiles.entity.impl;

import fr.upload.uploadfiles.entity.User;
import fr.upload.uploadfiles.entity.interfaces.UserToSend;

public class UserToSendImpl implements UserToSend {

    private String pseudo;

    private String email;

    public UserToSendImpl(String pseudo, String email) {
        this.pseudo = pseudo;
        this.email = email;
    }

    public static UserToSendImpl build(User user){
        return new UserToSendImpl(user.getPseudo(), user.getEmail());
    }

    @Override
    public String getId() {
        return null;
    }

    @Override
    public String getPseudo() {
        return pseudo;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    @Override
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
