package com.multiservice.AppService.dto;

import lombok.Data;
import java.util.List;

@Data
public class AdminUserDTO {
    private Long id;
    private String email;
    private String username;
    private String name;
    private boolean enabled;
    private List<String> roles;
    private String profilePicture;
    
    // No provider field to avoid circular references
} 