package fr.upload.uploadfiles.controller;

import fr.upload.uploadfiles.entity.UploadFile;
import fr.upload.uploadfiles.services.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ResolvableType;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


@Controller
@EnableScheduling
public class ViewController {

    @Autowired
    FileService fileService;

    @Autowired
    private ClientRegistrationRepository clientRegistrationRepository;

    private void googleAuth(Model model){

        String authorizationRequestBaseUri
                = "oauth2/authorization";
        Map<String, String> oauth2AuthenticationUrls
                = new HashMap<>();

        Iterable<ClientRegistration> clientRegistrations = null;
        ResolvableType type = ResolvableType.forInstance(clientRegistrationRepository)
                .as(Iterable.class);
        if (type != ResolvableType.NONE &&
                ClientRegistration.class.isAssignableFrom(type.resolveGenerics()[0])) {
            clientRegistrations = (Iterable<ClientRegistration>) clientRegistrationRepository;
        }

        clientRegistrations.forEach(registration ->
                oauth2AuthenticationUrls.put(registration.getClientName(),
                        authorizationRequestBaseUri + "/" + registration.getRegistrationId()));
        model.addAttribute("urls", oauth2AuthenticationUrls);
    }

    @RequestMapping(value = "upload", method = RequestMethod.GET)
    public String reg(){
        return "upload";
    }

    @RequestMapping(value = "register", method = RequestMethod.GET)
    public String register(Model model){

        googleAuth(model);

        return "register";
    }

    @RequestMapping(value = "login", method = RequestMethod.GET)
    public String login(Model model){

        googleAuth(model);

        return "login";
    }

    @RequestMapping(value = "profile", method = RequestMethod.GET)
    public String profile(){
         return "profile";
    }

    @RequestMapping(value = "load/{id}", method = RequestMethod.GET)
    public String load(@PathVariable("id") String id, Model model){
        Optional<UploadFile> optFile = fileService.load(id);
        if (optFile.isPresent()){
            UploadFile uploadFile = optFile.get();
            model.addAttribute("file", uploadFile);
            return "load";
        } else {
            return "notfound";
        }

    }

    @RequestMapping(value = "settings", method = RequestMethod.GET)
    public String settings(){
        return "settings";
    }

    @RequestMapping(value = "*", method = RequestMethod.GET)
    public String notFound(){
        return "notfound";
    }


    @Scheduled(fixedRate = 300000) // 5 minutes in milliseconds
    public void doFunctionEvery5Minutes() {
        fileService.autoDelete();
    }

}