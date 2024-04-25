package fr.upload.uploadfiles.repository;

import fr.upload.uploadfiles.entity.UploadFile;
import fr.upload.uploadfiles.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByPseudo(String pseudo);

    boolean existsByPseudo(String pseduo);

    boolean existsByEmail(String email);

}
