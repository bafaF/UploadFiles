package fr.upload.uploadfiles.controller;

import fr.upload.uploadfiles.entity.DownloadedFile;
import fr.upload.uploadfiles.entity.UploadFile;
import fr.upload.uploadfiles.entity.User;
import fr.upload.uploadfiles.entity.impl.FileToSendImpl;
import fr.upload.uploadfiles.repository.DownloadedFileRepository;
import fr.upload.uploadfiles.repository.FileRepository;
import fr.upload.uploadfiles.repository.UserRepository;
import fr.upload.uploadfiles.services.FileService;
import fr.upload.uploadfiles.services.GetCurrentUserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.io.FileUtils;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.*;

@RestController
@RequestMapping("api/file/")
public class FileController {

    @Autowired
    FileService fileService;

    @Autowired
    FileRepository repository;

    @Autowired
    GetCurrentUserService getCurrentUserService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    DownloadedFileRepository downloadedFileRepository;

    @PostMapping("upload")
    public ResponseEntity<String> upload(MultipartFile file){
       try{
           User user = getCurrentUserService.getUser();
           String url;
           if (user == null){
               url = fileService.save(file, null);
           } else {
               url = fileService.save(file, user);
           }

           return ResponseEntity.ok().body(url);
       }catch (Exception e){
           return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
       }
   }

    @DeleteMapping("delete/{fileId}")
    public ResponseEntity<String>deleteFile(@PathVariable(value = "fileId") String fileId){
        try {

            User user = getCurrentUserService.getUser();
            String response = fileService.delete(fileId, user);
            return ResponseEntity.ok().body(response);

        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    @GetMapping("load/{id}")
    public ResponseEntity<?> load(@PathVariable("id") String id){
       try {
           Optional<UploadFile> optFile = fileService.load(id);
           if (optFile.isPresent()){
               UploadFile uploadFile = optFile.get();
               FileToSendImpl file = FileToSendImpl.build(uploadFile);
               return ResponseEntity.ok().body(file);
           }

          return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No file");

       }catch (Exception e){
           return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
       }
    }

    @GetMapping("download/{id}")
    public void download(@PathVariable("id") String id, HttpServletResponse response) throws IOException {
        var file = repository.findById(id).get();
        var ins = new FileInputStream(file.getPath());

        response.setHeader("Content-Length", file.getSize());
        response.setHeader("Content-Disposition", "attachment; filename=\"" + file.getOriginalFilename() +"\"");

        IOUtils.copy(ins, response.getOutputStream());
        IOUtils.closeQuietly(ins);
        IOUtils.closeQuietly(response.getOutputStream());
    }

    @GetMapping("setDownloadedUser/{id}")
    public String setDownloadedUser(@PathVariable("id") String id){

       User user = getCurrentUserService.getUser();
       if (user != null){

           UploadFile file = repository.findById(id).get();
           DownloadedFile downloadedFile = new DownloadedFile();
           downloadedFile.setUser(user);
           downloadedFile.setFilename(file.getOriginalFilename());
           downloadedFile.setLastDownloadedDate(new Date());
           downloadedFile.setSize(file.getSize());
           downloadedFile.setLink(file.getId());
           downloadedFile.setExpirationDate(file.getDeletingDate());

           if (file.getUser() != null){
               downloadedFile.setOwner(file.getUser().getPseudo());
           }
           downloadedFileRepository.save(downloadedFile);
       }
       return id;
    }

    @GetMapping("myFiles")
    public ResponseEntity<List>myFiles(){
       User user = getCurrentUserService.getUser();
       List<UploadFile>myFiles = fileService.myFiles(user.getId());
       return ResponseEntity.ok().body(myFiles);
    }

    @GetMapping("filehistory")
    public ResponseEntity<?>fileHistory(){
       User user = getCurrentUserService.getUser();
       if (user == null){
           return ResponseEntity.status(HttpStatus.FORBIDDEN).body("User is not connected");
       }
       List<DownloadedFile> downloadedFiles = downloadedFileRepository.findAllByUserIdOrderByLastDownloadedDateDesc(user.getId());
       for (DownloadedFile downloadedFile : downloadedFiles){
           Optional<UploadFile> optFile = fileService.load(downloadedFile.getLink());
           if (!optFile.isPresent() || new Date().after(downloadedFile.getExpirationDate())){
               downloadedFile.setLink("Expired");
           }
       }
        return ResponseEntity.ok().body(downloadedFiles);
    }

}