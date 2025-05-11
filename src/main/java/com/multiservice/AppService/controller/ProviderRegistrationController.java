package com.multiservice.AppService.controller;

import com.multiservice.AppService.entities.Provider;
import com.multiservice.AppService.entities.User;
import com.multiservice.AppService.repositories.ProviderRepository;
import com.multiservice.AppService.repositories.UserRepository;
import com.multiservice.AppService.services.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class ProviderRegistrationController {

    private final ProviderRepository providerRepository;
    private final UserRepository userRepository;
    private final FileStorageService fileStorageService;

    @PostMapping(value = "/become-provider", consumes = {"multipart/form-data"})
    @PreAuthorize("hasRole('ROLE_CLIENT')")
    public ResponseEntity<?> becomeProvider(
            @RequestParam("city") String city,
            @RequestParam("phoneNumber") String phoneNumber,
            @RequestParam("idCardFront") MultipartFile idCardFront,
            @RequestParam("idCardBack") MultipartFile idCardBack
    ) throws IOException {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Save ID card images
        String frontImagePath = fileStorageService.storeFile(idCardFront);
        String backImagePath = fileStorageService.storeFile(idCardBack);

        // Create provider record
        Provider provider = new Provider();
        provider.setUser(user);
        provider.setCity(city);
        provider.setPhoneNumber(phoneNumber);
        provider.setIdCardFrontPath(frontImagePath);
        provider.setIdCardBackPath(backImagePath);
        provider.setStatus("PENDING"); // Provider needs to be approved by admin

        providerRepository.save(provider);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Provider registration successful. Awaiting approval.");
        response.put("status", "PENDING");

        return ResponseEntity.ok(response);
    }
} 