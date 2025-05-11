package com.multiservice.AppService.services;

import com.multiservice.AppService.dto.ServiceRequestDTO;
import com.multiservice.AppService.dto.ServiceResponseDTO;
import com.multiservice.AppService.entities.Category;
import com.multiservice.AppService.entities.Provider;
import com.multiservice.AppService.entities.Services;
import com.multiservice.AppService.entities.ServiceStatus;
import com.multiservice.AppService.exceptions.ServiceAccessDeniedException;
import com.multiservice.AppService.exceptions.service.ServiceNotFoundException;
import com.multiservice.AppService.repositories.CategoryRepository;
import com.multiservice.AppService.repositories.ProviderRepository;
import com.multiservice.AppService.repositories.ServiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ServiceServiceImpl implements ServiceService {

    private final ServiceRepository serviceRepository;
    private final ProviderRepository providerRepository;
    private final CategoryRepository categoryRepository;
    private final FileStorageService fileStorageService;

    @Override
    public ServiceResponseDTO createService(ServiceRequestDTO dto) {
        Provider provider = providerRepository.findById(dto.getProviderId())
                .orElseThrow(() -> new RuntimeException("Provider not found: " + dto.getProviderId()));
        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found: " + dto.getCategoryId()));

        List<String> imageUrls = null;
        if (dto.getImages() != null && !dto.getImages().isEmpty()) {
            try {
                imageUrls = dto.getImages().stream()
                        .map(file -> {
                            try {
                                return fileStorageService.storeFile(file);
                            } catch (IOException e) {
                                throw new RuntimeException("Error uploading image", e);
                            }
                        })
                        .collect(Collectors.toList());
            } catch (Exception e) {
                throw new RuntimeException("Error processing images", e);
            }
        }

        Services entity = new Services();
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setPrice(dto.getPrice());
        entity.setCity(dto.getCity());
        entity.setImages(imageUrls);
        entity.setProvider(provider);
        entity.setCategory(category);
        entity.setStatus(ServiceStatus.ACTIVE);
        entity.setRating(0.0);

        Services saved = serviceRepository.save(entity);
        return mapToResponse(saved);
    }

    @Override
    public ServiceResponseDTO updateService(Long id, ServiceRequestDTO dto) {
        Services s = serviceRepository.findById(id)
                .orElseThrow(() -> new ServiceNotFoundException(id));
        return applyUpdateAndMap(s, dto);
    }

    @Override
    public ServiceResponseDTO updateServiceByProvider(Long id, ServiceRequestDTO dto, Long providerId) {
        Services s = serviceRepository.findById(id)
                .orElseThrow(() -> new ServiceNotFoundException(id));
        if (!s.getProvider().getId().equals(providerId)) {
            throw new ServiceAccessDeniedException(id, providerId);
        }
        return applyUpdateAndMap(s, dto);
    }

    private ServiceResponseDTO applyUpdateAndMap(Services s, ServiceRequestDTO dto) {
        s.setName(dto.getName());
        s.setDescription(dto.getDescription());
        s.setPrice(dto.getPrice());
        s.setCity(dto.getCity());

        if (dto.getCategoryId() != null && !dto.getCategoryId().equals(s.getCategory().getId())) {
            Category newCat = categoryRepository.findById(dto.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Category not found: " + dto.getCategoryId()));
            s.setCategory(newCat);
        }

        if (dto.getImages() != null && !dto.getImages().isEmpty()) {
            try {
                List<String> newImageUrls = dto.getImages().stream()
                        .map(file -> {
                            try {
                                return fileStorageService.storeFile(file);
                            } catch (IOException e) {
                                throw new RuntimeException("Error uploading image", e);
                            }
                        })
                        .collect(Collectors.toList());
                s.setImages(newImageUrls);
            } catch (Exception e) {
                throw new RuntimeException("Error processing images", e);
            }
        }

        Services updated = serviceRepository.save(s);
        return mapToResponse(updated);
    }

    @Override
    public ServiceResponseDTO getServiceById(Long id) {
        Services s = serviceRepository.findById(id)
                .orElseThrow(() -> new ServiceNotFoundException(id));
        return mapToResponse(s);
    }

    @Override
    public List<ServiceResponseDTO> getAllServices() {
        return serviceRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<ServiceResponseDTO> getServicesByProvider(Long providerId) {
        providerRepository.findById(providerId)
                .orElseThrow(() -> new RuntimeException("Provider not found: " + providerId));
        return serviceRepository.findByProviderId(providerId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteService(Long id) {
        if (!serviceRepository.existsById(id))
            throw new ServiceNotFoundException(id);
        serviceRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void deleteServiceByProvider(Long id, Long providerId) {
        int deleted = serviceRepository.deleteByIdAndProviderId(id, providerId);
        if (deleted == 0) {
            if (!serviceRepository.existsById(id))
                throw new ServiceNotFoundException(id);
            throw new ServiceAccessDeniedException(id, providerId);
        }
    }

    @Override
    public List<ServiceResponseDTO> getServicesByCity(String city) {
        return serviceRepository.findByCity(city).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<ServiceResponseDTO> getServicesByPriceRange(Double minPrice, Double maxPrice) {
        return serviceRepository.findByPriceBetween(minPrice, maxPrice).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<ServiceResponseDTO> searchServices(String query) {
        return serviceRepository.findByNameContainingOrDescriptionContaining(query, query).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ServiceResponseDTO updateServiceRating(Long id, Double rating) {
        Services service = serviceRepository.findById(id)
                .orElseThrow(() -> new ServiceNotFoundException(id));
        service.setRating(rating);
        Services updated = serviceRepository.save(service);
        return mapToResponse(updated);
    }

    @Override
    public ServiceResponseDTO updateServiceStatus(Long id, ServiceStatus status) {
        Services service = serviceRepository.findById(id)
                .orElseThrow(() -> new ServiceNotFoundException(id));
        service.setStatus(status);
        Services updated = serviceRepository.save(service);
        return mapToResponse(updated);
    }

    @Override
    public List<ServiceResponseDTO> getServicesByCategory(Long categoryId) {
        return serviceRepository.findByCategoryId(categoryId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<ServiceResponseDTO> getServicesByStatus(ServiceStatus status) {
        return serviceRepository.findByStatus(status).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private ServiceResponseDTO mapToResponse(Services s) {
        List<String> imageUrls = null;
        if (s.getImages() != null) {
            imageUrls = s.getImages().stream()
                    .map(filename -> ServletUriComponentsBuilder
                            .fromCurrentContextPath()
                            .path("/uploads/")
                            .path(filename)
                            .toUriString())
                    .collect(Collectors.toList());
        }

        return new ServiceResponseDTO(
                s.getId(),
                s.getName(),
                s.getDescription(),
                s.getPrice(),
                s.getCity(),
                s.getRating(),
                s.getStatus(),
                imageUrls,
                s.getProvider().getId(),
                s.getProvider().getUser().getName(),
                s.getProvider().getUser().getUsername(), // Using username as avatar for now
                s.getProvider().getDisponibilites() ? "Available" : "Busy",
                s.getCategory().getId(),
                s.getCategory().getName()
        );
    }
}
