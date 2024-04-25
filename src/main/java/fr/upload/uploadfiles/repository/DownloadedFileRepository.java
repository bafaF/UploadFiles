package fr.upload.uploadfiles.repository;

import fr.upload.uploadfiles.entity.DownloadedFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DownloadedFileRepository extends JpaRepository<DownloadedFile, Long> {

    List<DownloadedFile> findAllByUserIdOrderByLastDownloadedDateDesc(String userId);

    void deleteAllByUserId(String id);

}
