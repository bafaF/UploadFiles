package fr.upload.uploadfiles.repository;

import fr.upload.uploadfiles.controller.IdGenerator;
import fr.upload.uploadfiles.entity.UploadFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FileRepository extends JpaRepository<UploadFile, String> {

    Optional<UploadFile>findByFilename(String a);

    List<UploadFile>getUploadFileByUserIdOrderByUploadDateDesc(String id);

    void deleteAllByUserId(String id);

    default void create(UploadFile file){
        var id = getId();
        file.setId(id);
        save(file);
    }

    default String getId(){
        String id = null;
        do {
            id = IdGenerator.generate(16);
        }while (existsById(id));
        return id;
    }

}
