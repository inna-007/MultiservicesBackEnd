package com.multiservice.AppService.repositories;

import com.multiservice.AppService.entities.Services;
import com.multiservice.AppService.entities.ServiceStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServiceRepository extends JpaRepository<Services, Long> {
    // Lister les services par provider
    List<Services> findByProviderId(Long providerId);
    List<Services> findByCity(String city);
    List<Services> findByPriceBetween(Double minPrice, Double maxPrice);
    List<Services> findByNameContainingOrDescriptionContaining(String name, String description);
    List<Services> findByCategoryId(Long categoryId);
    List<Services> findByStatus(ServiceStatus status);
    int deleteByIdAndProviderId(Long id, Long providerId);
}

