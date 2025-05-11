package com.multiservice.AppService.services;

import com.multiservice.AppService.dto.ServiceRequestDTO;
import com.multiservice.AppService.dto.ServiceResponseDTO;
import com.multiservice.AppService.entities.ServiceStatus;

import java.util.List;

public interface ServiceService {

    /**
     * Crée un nouveau service (upload multipart via ServiceRequestDTO)
     */
    ServiceResponseDTO createService(ServiceRequestDTO dto);

    /**
     * Met à jour un service sans vérification de provider (admin)
     */
    ServiceResponseDTO updateService(Long id, ServiceRequestDTO dto);

    /**
     * Met à jour un service en s'assurant qu'il appartient au provider donné
     */
    ServiceResponseDTO updateServiceByProvider(Long id,
                                               ServiceRequestDTO dto,
                                               Long providerId);

    /**
     * Récupère un service par son ID
     */
    ServiceResponseDTO getServiceById(Long id);

    /**
     * Liste tous les services
     */
    List<ServiceResponseDTO> getAllServices();

    /**
     * Liste les services d'un provider donné
     */
    List<ServiceResponseDTO> getServicesByProvider(Long providerId);

    /**
     * Supprime un service (admin)
     */
    void deleteService(Long id);

    /**
     * Supprime un service en s'assurant qu'il appartient au provider donné
     */
    void deleteServiceByProvider(Long id, Long providerId);

    /**
     * Filter services by city
     */
    List<ServiceResponseDTO> getServicesByCity(String city);

    /**
     * Filter services by price range
     */
    List<ServiceResponseDTO> getServicesByPriceRange(Double minPrice, Double maxPrice);

    /**
     * Search services by name or description
     */
    List<ServiceResponseDTO> searchServices(String query);

    /**
     * Update service rating
     */
    ServiceResponseDTO updateServiceRating(Long id, Double rating);

    /**
     * Update service status
     */
    ServiceResponseDTO updateServiceStatus(Long id, ServiceStatus status);

    /**
     * Get services by category
     */
    List<ServiceResponseDTO> getServicesByCategory(Long categoryId);

    /**
     * Get services by status
     */
    List<ServiceResponseDTO> getServicesByStatus(ServiceStatus status);
}
