package com.multiservice.AppService.controllers;

import com.multiservice.AppService.dto.AdminUserDTO;
import com.multiservice.AppService.entities.User;
import com.multiservice.AppService.entities.Category;
import com.multiservice.AppService.services.UserService;
import com.multiservice.AppService.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class AdminController {

    private final UserService userService;
    private final CategoryService categoryService;

    @Autowired
    public AdminController(UserService userService, CategoryService categoryService) {
        this.userService = userService;
        this.categoryService = categoryService;
    }

    @GetMapping("/users")
    public ResponseEntity<List<AdminUserDTO>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        List<AdminUserDTO> userDTOs = users.stream()
            .map(user -> {
                AdminUserDTO dto = new AdminUserDTO();
                dto.setId(user.getId());
                dto.setEmail(user.getEmail());
                dto.setUsername(user.getUsername());
                dto.setName(user.getName());
                dto.setEnabled(user.isEnabled());
                dto.setRoles(user.getRoles().stream()
                    .map(role -> role.getName().toString())
                    .collect(Collectors.toList()));
                dto.setProfilePicture(user.getProfilePicture() != null ? user.getProfilePicture() : "assets/images/user.avif");
                return dto;
            })
            .collect(Collectors.toList());
        
        return ResponseEntity.ok(userDTOs);
    }

    @GetMapping("/categories")
    public ResponseEntity<List<Category>> getAllCategories() {
        return ResponseEntity.ok(categoryService.getAllCategories());
    }
} 