package com.multiservice.AppService.controller;

import com.multiservice.AppService.dto.ApiResponse;
import com.multiservice.AppService.dto.ClientProfileDTO;
import com.multiservice.AppService.dto.ProviderProfileDTO;
import com.multiservice.AppService.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.Authentication;

import static java.lang.System.*;


@RestController
@RequestMapping("/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final UserService userService;

    @PutMapping("/client")
    @PreAuthorize("hasRole('ROLE_CLIENT')")
    public ResponseEntity<?> updateClient(@RequestBody ClientProfileDTO dto, Authentication auth) {
        userService.updateClientProfile(auth.getName(), dto);
        return ResponseEntity.ok(new ApiResponse("Success", "Client profile updated", 200));

    }

    @PutMapping("/provider")
    @PreAuthorize("hasRole('ROLE_PROVIDER')")
    public ResponseEntity<?> updateProvider(@RequestBody ProviderProfileDTO dto, Authentication auth) {
        userService.updateProviderProfile(auth.getName(), dto);
        return ResponseEntity.ok(new ApiResponse("Success", "Provider profile updated", 200));
    }

}
