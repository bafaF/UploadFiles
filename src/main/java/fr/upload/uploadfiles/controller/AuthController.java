package fr.upload.uploadfiles.controller;

import fr.upload.uploadfiles.entity.ERole;
import fr.upload.uploadfiles.entity.Role;
import fr.upload.uploadfiles.entity.User;
import fr.upload.uploadfiles.entity.impl.UserToSendImpl;
import fr.upload.uploadfiles.payload.LoginRequest;
import fr.upload.uploadfiles.payload.RegisterRequest;
import fr.upload.uploadfiles.repository.RoleRepository;
import fr.upload.uploadfiles.repository.UserRepository;
import fr.upload.uploadfiles.security.jwt.JwtUtils;
import fr.upload.uploadfiles.security.service.UserDetailsImpl;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/auth/")
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    //@Autowired
   // private JavaMailSender javaMailSender;


    // TODO: Profile page, emailsend

    @PostMapping("register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest registerRequest){

        try {
          if (registerRequest.getPseudo() == null||
                  registerRequest.getEmail() == null ||
                  registerRequest.getPassword() == null)
              {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erreur: Un input est vide");
            }

            if (userRepository.existsByPseudo(registerRequest.getPseudo())){
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Erreur: Pseudo déjà utilisé");
            }
            if (userRepository.existsByEmail(registerRequest.getEmail())){
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Erreur: Email déjà utilisé");
            }

            User user = new User();
            user.setPseudo(registerRequest.getPseudo());
            user.setPassword(encoder.encode(registerRequest.getPassword()));
            user.setEmail(registerRequest.getEmail());
            user.setRegisterDate(new Date());
            Set<Role> roles = new HashSet<>();

            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Erreur: Role non trouvé"));
            roles.add(userRole);

            user.setRoles(roles);

            userRepository.save(user);

            //sendMail(user.getEmail(), user.getPseudo());

            UserToSendImpl userToSend = UserToSendImpl.build(user);
            return ResponseEntity.ok().body(userToSend);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }

    }

    @PostMapping("login")
    public ResponseEntity<?>login(@Valid @RequestBody LoginRequest loginRequest){

        try{
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getPseudo(), loginRequest.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);

            String jwt = jwtUtils.generateJwtToken(authentication);

            User user = userRepository.findByPseudo(loginRequest.getPseudo()).get();

            if (!user.getPseudo().equals(loginRequest.getPseudo())){
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Erreur : pseudo ou mot de passe incorrect");
            }

            ResponseCookie resCookie = ResponseCookie.from("user-token", jwt)
                    .httpOnly(false)
                    .secure(true)
                    .path("/")
                    .maxAge(1 * 24 * 60 * 60)
                    .domain("localhost")
                    .build();
            return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, resCookie.toString()).build();

        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("disconnect")
    public ResponseEntity<?> deleteCookie(HttpServletResponse response) {

        Cookie cookie = new Cookie("user-token", null);
        cookie.setPath("/");
        cookie.setHttpOnly(false);
        cookie.setMaxAge(0);
        response.addCookie(cookie);

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, response.toString()).build();
    }

    public void sendMail(String email, String pseudo){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Welcome to My Application");
        message.setText("Dear " + pseudo + ",\n\nThank you for registering with My Application.\n\nBest regards,\nThe My Application Team");
       // javaMailSender.send(message);
    }

}