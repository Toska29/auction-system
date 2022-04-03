package com.phoenix.auctionsystem.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ItemResponseDto {
    private Long id;
    private String name;
    private String description;
    private Double price;
    private Integer quantity;
    private String imageUrl;
    private Long appUserId;
    private LocalDateTime dateCreated;
}
