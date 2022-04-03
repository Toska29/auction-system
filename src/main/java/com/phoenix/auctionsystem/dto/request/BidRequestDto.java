package com.phoenix.auctionsystem.dto.request;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class BidRequestDto {
    @NotNull(message = "provide valid user id")
    private Long bidderId;

    @NotNull(message = "provide valid item id")
    private Long itemId;

    @NotNull(message = "provide amount")
    private Double bidPrice;
}
