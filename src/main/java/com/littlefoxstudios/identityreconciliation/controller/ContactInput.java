package com.littlefoxstudios.identityreconciliation.controller;

import com.littlefoxstudios.identityreconciliation.Constants;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ContactInput {

    @Email(message = Constants.INVALID_EMAIL_WARN)
    @Size(min = 5, max = 320, message = Constants.EMAIL_LENGTH_REACHED) //HARDCODED
    public String email;
    @Pattern(regexp = "^[+]?[a-zA-Z0-9()\\-\\s]{7,32}$", message = Constants.INVALID_PHONE_NUMBER_WARN) //HARDCODED
    public String phoneNumber;
}
