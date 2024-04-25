package fr.upload.uploadfiles.controller;

import fr.upload.uploadfiles.entity.User;
import fr.upload.uploadfiles.entity.impl.UserToSendImpl;
import fr.upload.uploadfiles.payload.DeleteProfileRequest;
import fr.upload.uploadfiles.payload.EditProfileRequest;
import fr.upload.uploadfiles.repository.DownloadedFileRepository;
import fr.upload.uploadfiles.repository.FileRepository;
import fr.upload.uploadfiles.repository.UserRepository;
import fr.upload.uploadfiles.services.FileService;
import fr.upload.uploadfiles.services.GetCurrentUserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("api/profile/")
public class ProfilController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    FileRepository fileRepository;

    @Autowired
    FileService fileService;

    @Autowired
    GetCurrentUserService getCurrentUserService;

    @Autowired
    DownloadedFileRepository downloadedFileRepository;

    @Autowired
    BCryptPasswordEncoder encoder;

    @GetMapping("getUserInfo")
    public ResponseEntity<?>getUserInfo(){
        try{

            User user = getCurrentUserService.getUser();
            UserToSendImpl userToSend = UserToSendImpl.build(user);
            return ResponseEntity.ok().body(userToSend);

        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    @PostMapping("edit")
    public ResponseEntity<?>edit(@Valid @RequestBody EditProfileRequest editProfileRequest){

        try {
            User user = getCurrentUserService.getUser();

            if (editProfileRequest.getNewPassword() != null){
                if (!editProfileRequest.getNewPassword().isBlank()){
                    if (encoder.matches(editProfileRequest.getOldPassword(), user.getPassword())){
                        user.setPassword(encoder.encode(editProfileRequest.getNewPassword()));
                    }
                }
            }if(editProfileRequest.getEmail() != null){
                if (!editProfileRequest.getEmail().isBlank()){
                    user.setEmail(editProfileRequest.getEmail());
                }
            }

            userRepository.save(user);
            UserToSendImpl userToSend = UserToSendImpl.build(user);

            return ResponseEntity.ok().body(userToSend);
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    //TODO: settings page

    @DeleteMapping("delete")
    @Transactional
    public ResponseEntity<String>delete(@Valid @RequestBody DeleteProfileRequest deleteProfileRequest){
        try{
            User user = getCurrentUserService.getUser();
            if (encoder.matches(deleteProfileRequest.getPassword(), user.getPassword())){
                fileService.deleteAllUserFiles();
                fileService.deleteAllUserDownloadedFiles();
                userRepository.delete(user);
                return ResponseEntity.ok().body("The user has been deleted");
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Wrong password");
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

}
