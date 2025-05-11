package com.multiservice.AppService.controller;

import com.multiservice.AppService.dto.ServiceDTO;
import com.multiservice.AppService.dto.ServiceRequestDTO;
import com.multiservice.AppService.dto.ServiceResponseDTO;
import com.multiservice.AppService.entities.ServiceStatus;
import com.multiservice.AppService.services.ServiceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/services")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class AdminServiceController {

    private final ServiceService serviceService;

    // List all services
    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<ServiceResponseDTO> listAll() {
        return serviceService.getAllServices();
    }

    // Get a service by ID
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ServiceResponseDTO getById(@PathVariable Long id) {
        return serviceService.getServiceById(id);
    }

    // Update any service
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ServiceResponseDTO updateAny(
            @PathVariable Long id,
            @ModelAttribute @Valid ServiceRequestDTO dto
    ) {
        return serviceService.updateService(id, dto);
    }

    // Delete any service
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void deleteAny(@PathVariable Long id) {
        serviceService.deleteService(id);
    }

    // Update service status
    @PutMapping("/{id}/status")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ServiceResponseDTO updateStatus(
            @PathVariable Long id,
            @RequestParam ServiceStatus status
    ) {
        return serviceService.updateServiceStatus(id, status);
    }

    // Get services by status
    @GetMapping("/status/{status}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<ServiceResponseDTO> getServicesByStatus(@PathVariable ServiceStatus status) {
        return serviceService.getServicesByStatus(status);
    }
}
