package fr.upload.uploadfiles.payload;

import jakarta.validation.constraints.NotBlank;

public class DeleteProfileRequest {

    @NotBlank
    private String password;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
