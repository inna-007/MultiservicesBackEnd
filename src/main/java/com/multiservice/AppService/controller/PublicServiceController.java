package com.multiservice.AppService.controller;

import com.multiservice.AppService.dto.ServiceDTO;
import com.multiservice.AppService.dto.ServiceResponseDTO;
import com.multiservice.AppService.services.ServiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/services")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class PublicServiceController {

    private final ServiceService serviceService;

    /**
     * GET /services
     * Public endpoint to list all services without authentication
     */
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ServiceResponseDTO> getAllServices() {
        return serviceService.getAllServices();
    }

    /**
     * GET /services/{id}
     * Public endpoint to get a service by ID
     */
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ServiceResponseDTO getServiceById(@PathVariable Long id) {
        return serviceService.getServiceById(id);
    }

    /**
     * GET /services/city/{city}
     * Public endpoint to get services by city
     */
    @GetMapping(value = "/city/{city}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ServiceResponseDTO> getServicesByCity(@PathVariable String city) {
        return serviceService.getServicesByCity(city);
    }

    /**
     * GET /services/price
     * Public endpoint to get services by price range
     */
    @GetMapping(value = "/price", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ServiceResponseDTO> getServicesByPriceRange(
            @RequestParam Double minPrice,
            @RequestParam Double maxPrice) {
        return serviceService.getServicesByPriceRange(minPrice, maxPrice);
    }

    /**
     * GET /services/search
     * Public endpoint to search services by name or description
     */
    @GetMapping(value = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ServiceResponseDTO> searchServices(@RequestParam String query) {
        return serviceService.searchServices(query);
    }

    /**
     * GET /services/category/{categoryId}
     * Public endpoint to get services by category
     */
    @GetMapping(value = "/category/{categoryId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ServiceResponseDTO> getServicesByCategory(@PathVariable Long categoryId) {
        return serviceService.getServicesByCategory(categoryId);
    }

    /**
     * POST /services/{id}/rate
     * Public endpoint to rate a service
     */
    @PostMapping(value = "/{id}/rate", produces = MediaType.APPLICATION_JSON_VALUE)
    public ServiceResponseDTO rateService(
            @PathVariable Long id,
            @RequestParam Double rating) {
        return serviceService.updateServiceRating(id, rating);
    }
}
