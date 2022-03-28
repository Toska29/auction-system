package com.phoenix.auctionsystem.dto.request;

import javax.validation.constraints.*;

public class AppUserRequestDto {
    @NotNull(message = "Firstname must be provided")
    private String firstName;

    @NotNull(message = "Lastname must be provided")
    private String lastName;

    @NotNull(message = "email address can't be null")
    @Email(regexp = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$",
            message = "provide a valid email address")
    private String emailAddress;

    @NotNull(message = "provide valid phone number")
    @Size(min = 11, max = 14, message = "phone number not valid")
    @Pattern(regexp = "^[\\+]?[(]?[0-9]{3}[)]?[-\\s\\.]?[0-9]{3}[-\\s\\.]?[0-9]{4,6}$",
            message = "provide valid phone number")
    private String contactNumber;

    @NotNull(message = "provide contact address")
    private String address;

    @NotNull(message = "provide password")
    private String password;

}
