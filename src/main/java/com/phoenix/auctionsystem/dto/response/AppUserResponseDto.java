package com.phoenix.auctionsystem.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AppUserResponseDto {
    private Long appUserId;
    private LocalDateTime dateCreated;
    private String email;
    private String userFirstName;
    private String userLastName;
    private String userAddress;
    private String phoneNumber;
}
