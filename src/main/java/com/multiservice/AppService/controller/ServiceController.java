package com.multiservice.AppService.controller;

import com.multiservice.AppService.dto.ServiceDTO;
import com.multiservice.AppService.dto.ServiceRequestDTO;
import com.multiservice.AppService.dto.ServiceResponseDTO;
import com.multiservice.AppService.entities.Provider;
import com.multiservice.AppService.entities.ServiceStatus;
import com.multiservice.AppService.services.ServiceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
//import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import com.multiservice.AppService.repositories.ProviderRepository;
import com.multiservice.AppService.repositories.UserRepository;
import com.multiservice.AppService.entities.User;

import java.util.List;

@RestController
@RequestMapping("/provider/services")

@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class ServiceController {

    private final  ServiceService serviceService;
    private final ProviderRepository providerRepository;
    private final UserRepository userRepository;

    // Créer un service sous le provider authentifié
    @PostMapping(consumes = {"multipart/form-data"})
    @PreAuthorize("hasRole('ROLE_PROVIDER')")
    public ServiceResponseDTO createByToken(
            @ModelAttribute @Valid ServiceRequestDTO dto
    ) {
        // injecte l'ID du provider authentifié
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Long pid = providerRepository.findByUserEmail(email)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Provider non trouvé"))
                .getId();
        dto.setProviderId(pid);

        return serviceService.createService(dto);
    }

    // Lister services du provider authentifié
    @GetMapping
    @PreAuthorize("hasRole('ROLE_PROVIDER')")
    public List<ServiceResponseDTO> listByToken() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Provider provider = providerRepository.findByUserEmail(email)
            .orElseGet(() -> {
                // Auto-create provider record for users with ROLE_PROVIDER if missing
                User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
                Provider newProvider = new Provider();
                newProvider.setUser(user);
                newProvider.setCity("Unknown"); // You may want to improve this
                newProvider.setPhoneNumber("0000000000");
                newProvider.setIdCardFrontPath("");
                newProvider.setIdCardBackPath("");
                newProvider.setStatus("PENDING");
                newProvider.setCin("");
                newProvider.setDisponibilites(false);
                newProvider.setVerificationCnie(false);
                return providerRepository.save(newProvider);
            });
        return serviceService.getServicesByProvider(provider.getId());
    }

    // Afficher un service par ID
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ROLE_PROVIDER')")
    public ServiceResponseDTO getById(@PathVariable Long id) {
        return serviceService.getServiceById(id);
    }


    // Mettre à jour un service (avec vérification)
    @PutMapping(value = "/{id}",
            consumes = {"multipart/form-data"}
    )
    @PreAuthorize("hasRole('ROLE_PROVIDER')")
    public ServiceResponseDTO updateByToken(
            @PathVariable Long id,
            @ModelAttribute @Valid ServiceRequestDTO dto
    ) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Long pid = providerRepository.findByUserEmail(email)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Provider non trouvé"))
                .getId();

        return serviceService.updateServiceByProvider(id, dto, pid);
    }



    // Supprimer un service
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_PROVIDER')")
    public void deleteByToken(@PathVariable Long id) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Long pid = providerRepository.findByUserEmail(email)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Provider non trouvé"))
                .getId();

        serviceService.deleteServiceByProvider(id, pid);
    }

    // Update service status
    @PutMapping("/{id}/status")
    @PreAuthorize("hasRole('ROLE_PROVIDER')")
    public ServiceResponseDTO updateStatus(
            @PathVariable Long id,
            @RequestParam ServiceStatus status
    ) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Long pid = providerRepository.findByUserEmail(email)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Provider not found"))
                .getId();

        // Verify that the service belongs to the provider
        ServiceResponseDTO service = serviceService.getServiceById(id);
        if (!service.getProviderId().equals(pid)) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN, "You don't have permission to update this service");
        }

        return serviceService.updateServiceStatus(id, status);
    }

    private String getEmail() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getName();
    }

    private Long getProviderId(String email) {
        Provider prov = providerRepository.findByUserEmail(email)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Provider not found"));
        return prov.getId();
    }
}
