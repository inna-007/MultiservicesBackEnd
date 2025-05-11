package com.multiservice.AppService.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServiceRequestDTO {
    @NotBlank(message = "Name cannot be empty")
    private String name;

    @NotBlank(message = "Description cannot be empty")
    private String description;

    @NotNull(message = "Price is required")
    private Double price;

    @NotBlank(message = "City is required")
    private String city;

    @NotNull(message = "Category ID is required")
    private Long categoryId;

    /**
     * Images to upload.
     * For update, you can omit this if you don't want to change the images.
     */
    private List<MultipartFile> images;

    // Injected by controller from token
    private Long providerId;
}
