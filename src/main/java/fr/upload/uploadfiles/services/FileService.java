package fr.upload.uploadfiles.services;

import fr.upload.uploadfiles.entity.UploadFile;
import fr.upload.uploadfiles.entity.User;
import fr.upload.uploadfiles.repository.DownloadedFileRepository;
import fr.upload.uploadfiles.repository.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import static java.lang.String.format;


@Service
public class FileService {

    @Value("${uploadFiles.app.fileLocation}")
    private String fileLocation;

    @Autowired
    FileRepository fileRepository;

    @Autowired
    DownloadedFileRepository downloadedFileRepository;

    @Autowired
    GetCurrentUserService getCurrentUserService;

    public String save(MultipartFile file, User user){
        try {
            double fileInKo = file.getSize() / 1024;
            double fileInMo = fileInKo / 1024;
            double fileInGo = fileInMo / 1024;
            UploadFile uploadFile = new UploadFile();
            Path filePath = Paths.get(fileLocation);
            Resource resource;
            do {
                Path existingFile = filePath.resolve(file.getOriginalFilename());
                resource = new UrlResource(existingFile.toUri());
                uploadFile.setUploadDate(new Date());
                if (fileInGo > 50){
                    uploadFile.setDeletingDate(new Date(uploadFile.getUploadDate().getTime() + 86400000));
                } else {
                    uploadFile.setDeletingDate(new Date(uploadFile.getUploadDate().getTime() + (86400000 * 7)));
                }
            }while (resource.exists() && resource.isReadable());

            if (fileInGo >= 1){
                uploadFile.setSize(Math.round(fileInGo * 100.0) / 100.0 + " Go");
            }else if(fileInMo >= 1){
                uploadFile.setSize(Math.round(fileInMo * 100.0) / 100.0 + " Mo");
            }else {
                uploadFile.setSize(Math.round(fileInKo * 100.0) / 100.0 + " Ko");
            }

            if (user != null){
                uploadFile.setUser(user);
            }

            uploadFile.setOriginalFilename(file.getOriginalFilename());
            var destination = new File(fileLocation, format("%s-%s", UUID.randomUUID(), ""));
            uploadFile.setFilename(destination.getName());
            uploadFile.setPath(destination);
            file.transferTo(destination);
            fileRepository.create(uploadFile);
            return uploadFile.getId();

        }catch (Exception e){
            throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
        }
    }

    public Optional<UploadFile> load(String id){

            Optional<UploadFile> optFile = fileRepository.findById(id);

            return optFile;
    }

    public List<UploadFile> myFiles(String userId){
        List<UploadFile>myFiles = fileRepository.getUploadFileByUserIdOrderByUploadDateDesc(userId);

        return myFiles;
    }

    public String delete(String id, User user){

        try {
            UploadFile uploadFile = fileRepository.findById(id).get();
            String filename = uploadFile.getFilename();
            Path filePath= Paths.get(fileLocation);
            Path file = filePath.resolve(filename);

            if (uploadFile.getUser() != null){
                if (user == null || !user.equals(uploadFile.getUser())){
                    return "You dont have the authorization";
                }
            }

            Resource resource = new UrlResource(file.toUri());
            if (resource.exists()){

                File deleteFile = new File(file.toUri());
                deleteFile.delete();
                fileRepository.delete(uploadFile);
                return "File deleted";

            }else {
                return "The file does not exist";
            }

        }catch (Exception e){
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

    public void deleteAllUserFiles(){

        try {
            User user = getCurrentUserService.getUser();
            Path filePath = Paths.get(fileLocation);
            List<UploadFile>files = fileRepository.getUploadFileByUserIdOrderByUploadDateDesc(user.getId());

            for (int i = 0; i<files.size();i++){
                Path file = filePath.resolve(files.get(i).getFilename());
                Resource resource = new UrlResource(file.toUri());

                if (resource.exists()){
                    File deleteFile = new File(file.toUri());
                    deleteFile.delete();
                }
            }
            fileRepository.deleteAllByUserId(user.getId());

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void deleteAllUserDownloadedFiles(){
        try {
            User user = getCurrentUserService.getUser();
            downloadedFileRepository.deleteAllByUserId(user.getId());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public List<String> autoDelete(){
        try{
            if (fileLocation == null) {
                return Collections.EMPTY_LIST;
            }
            Path filePath = Paths.get(fileLocation);
            List<String> fileList = new ArrayList<>();
            List<UploadFile> uploadFiles = fileRepository.findAll();
            for (UploadFile element : uploadFiles) {

                Path file = filePath.resolve(element.getFilename());
                Resource resource = new UrlResource(file.toUri());

                if (new Date().after(element.getDeletingDate())){
                    fileList.add(element.getFilename());
                    if (resource.exists()){
                        File deletedFile = new File(resource.getURI());
                        deletedFile.delete();
                        fileRepository.delete(element);
                    }
                }
            }
            return fileList;
        }catch (Exception e){
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

}