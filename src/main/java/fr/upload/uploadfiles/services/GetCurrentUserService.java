package fr.upload.uploadfiles.services;

import fr.upload.uploadfiles.entity.User;
import fr.upload.uploadfiles.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GetCurrentUserService {

    @Autowired
    UserRepository userRepository;

    public User getUser() throws ClassCastException {
        try{

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            Optional<User>optUser = userRepository.findByPseudo(authentication.getName());

            if(optUser.isEmpty()){
                return null;
            }else {
                User user = optUser.get();
                return user;
            }

        }catch (Exception e){
         e.printStackTrace();
        }
        return null;
    }
}