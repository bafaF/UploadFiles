package fr.upload.uploadfiles.payload;

import jakarta.validation.constraints.NotBlank;

public class LoginRequest {

    @NotBlank
    private String pseudo;

    @NotBlank
    private String password;


    public String getPseudo() {
        return pseudo;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
