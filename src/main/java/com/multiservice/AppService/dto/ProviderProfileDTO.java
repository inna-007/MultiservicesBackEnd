package com.multiservice.AppService.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class ProviderProfileDTO {
    private String city;
    private String phoneNumber;
    private String cin;
    private Boolean disponibilites;
}
