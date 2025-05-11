package com.multiservice.AppService.repositories;
import com.multiservice.AppService.entities.Client;
import com.multiservice.AppService.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    Optional<Client> findByUserId(Long userId);
    //Optional<Client> findByEmail(String email);
    //Optional<Client> findByUsername(String username);


}
