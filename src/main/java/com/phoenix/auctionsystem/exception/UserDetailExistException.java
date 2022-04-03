package com.phoenix.auctionsystem.exception;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

public class UserDetailExistException extends Throwable {
    public UserDetailExistException(@NotNull(message = "email address can't be null") @Email(regexp = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$",
            message = "provide a valid email address") String message) {
        super(message);
    }
}
