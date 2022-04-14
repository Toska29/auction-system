package com.phoenix.auctionsystem.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SoldItemResponseDto {
    private Long soldItemId;
    private String buyerEmail;
    private String sellerEmail;
    private Double soldPrice;
    private Long itemId;
    private LocalDateTime dateSold;
}
