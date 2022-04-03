package com.phoenix.auctionsystem.dto.request;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;

@Data
public class ItemRequestDto {
    @NotNull(message = "name of item must be provided")
    private String name;

    @NotNull(message = "provide description for your item")
    private String description;

    @NotNull(message = "provide price for your item")
    private Double price;

    @NotNull(message = "enter the quantity for your item")
    private Integer quantity;

    @NotNull(message = "provide app user id")
    private Long appUserId;

    @NotNull(message = "provide the image of the item")
    private MultipartFile image;
}
