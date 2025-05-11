package com.multiservice.AppService.repositories;

import com.multiservice.AppService.entities.Provider;
import com.multiservice.AppService.entities.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProviderRepository extends JpaRepository<Provider, Long> {
  Optional<Provider> findByUserEmail(String email);

}
