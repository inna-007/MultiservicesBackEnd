package com.multiservice.AppService.dto;

import lombok.Data;

import java.util.Date;

@Data
public class CreateReservationDTO {
    private Long serviceId;
    private Date date;
    private String notes;
} 