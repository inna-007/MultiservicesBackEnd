package com.multiservice.AppService.dto;

import com.multiservice.AppService.entities.ServiceStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServiceResponseDTO {
    private Long id;
    private String name;
    private String description;
    private Double price;
    private String city;
    private Double rating;
    private ServiceStatus status;
    private List<String> images;
    private Long providerId;
    private String providerName;
    private String providerAvatar;
    private String providerStatus;
    private Long categoryId;
    private String categoryName;
}
