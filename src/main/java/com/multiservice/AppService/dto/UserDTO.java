package com.multiservice.AppService.dto;

import jakarta.validation.constraints.*;
import java.util.List;

public class UserDTO {

    // Validation groups for different operations
    public interface CreateValidation {}
    public interface UpdateValidation {}

    // User fields
    @NotBlank(groups = CreateValidation.class, message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;

    @NotBlank(groups = CreateValidation.class, message = "Username is required")
    @Size(min = 3, max = 20, message = "Username must be between 3 and 20 characters")
    private String username;

    @NotBlank(groups = CreateValidation.class, message = "Password is required")
    @Size(min = 6, groups = CreateValidation.class, message = "Password must be at least 6 characters")
    @Null(groups = UpdateValidation.class, message = "Password cannot be changed this way")
    private String password;

    @NotBlank(message = "Name is required")
    private String name;

    private Boolean enabled;
    private List<Long> roleIds;
    private ProviderDetails providerDetails;

    // Nested ProviderDetails class
    public static class ProviderDetails {
        @Size(max = 100, message = "Address cannot exceed 100 characters")
        private String address;

        @Pattern(regexp = "^[0-9]{10}$", message = "Phone number must be 10 digits")
        private String telephone;

        @Size(min = 5, max = 20, message = "CIN must be between 5 and 20 characters")
        private String cin;

        private Boolean verificationCnie;
        private Boolean disponibilites;

        // Getters and Setters
        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getTelephone() {
            return telephone;
        }

        public void setTelephone(String telephone) {
            this.telephone = telephone;
        }

        public String getCin() {
            return cin;
        }

        public void setCin(String cin) {
            this.cin = cin;
        }

        public Boolean getVerificationCnie() {
            return verificationCnie;
        }

        public void setVerificationCnie(Boolean verificationCnie) {
            this.verificationCnie = verificationCnie;
        }

        public Boolean getDisponibilites() {
            return disponibilites;
        }

        public void setDisponibilites(Boolean disponibilites) {
            this.disponibilites = disponibilites;
        }
    }

    // Getters and Setters
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public List<Long> getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(List<Long> roleIds) {
        this.roleIds = roleIds;
    }

    public ProviderDetails getProviderDetails() {
        return providerDetails;
    }

    public void setProviderDetails(ProviderDetails providerDetails) {
        this.providerDetails = providerDetails;
    }
}