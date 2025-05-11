package com.multiservice.AppService.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
//import javax.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotBlank;
//import javax.validation.constraints.NotNull;
import jakarta.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServiceDTO {
    private Long id;

    @NotBlank(message = "Le nom ne peut pas être vide")
    private String name;

    @NotBlank(message = "La description ne peut pas être vide")
    private String description;

    @NotNull
    private Long prix;
    //private MultipartFile poster;
    /** MultipartFile pour l'image du service */
    @NotNull(message = "Le poster est requis")
    private MultipartFile poster;

    private Long providerId;

    @NotNull(message = "L'ID de la catégorie est requis")
    private Long categoryId;
}