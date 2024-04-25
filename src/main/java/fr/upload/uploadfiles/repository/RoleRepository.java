package fr.upload.uploadfiles.repository;

import fr.upload.uploadfiles.entity.ERole;
import fr.upload.uploadfiles.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByName(ERole name);

}
