package fr.upload.uploadfiles.services;

import fr.upload.uploadfiles.entity.ERole;
import fr.upload.uploadfiles.entity.Provider;
import fr.upload.uploadfiles.entity.Role;
import fr.upload.uploadfiles.entity.User;
import fr.upload.uploadfiles.repository.RoleRepository;
import fr.upload.uploadfiles.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private RoleRepository roleRepository;

    public void processOAuthPostLogin(String username, String email) {
        Optional<User> existUser = userRepo.findByPseudo(username);

        if (!existUser.isPresent()) {
            User newUser = new User();
            newUser.setPseudo(username);
            newUser.setEmail(email);
            newUser.setRegisterDate(new Date());
            newUser.setProvider(Provider.GOOGLE);
            Set<Role> roles = new HashSet<>();

            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Erreur: Role non trouvé"));
            roles.add(userRole);

            newUser.setRoles(roles);
            userRepo.save(newUser);
        }

    }

}
