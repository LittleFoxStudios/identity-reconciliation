package com.littlefoxstudios.identityreconciliation.persistence;

import com.littlefoxstudios.identityreconciliation.Constants;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Data;
import jakarta.validation.constraints.Pattern;
import java.time.LocalDateTime;


@Entity
@Data
@Table(name = "fluxkart_contact") //HARDCODED
public class FluxKartContact {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Pattern(regexp = "^[+]?[a-zA-Z0-9()\\-\\s]{7,32}$", message = Constants.INVALID_PHONE_NUMBER_WARN) //HARDCODED
    private String phoneNumber;

    @Email(message = Constants.INVALID_EMAIL_WARN)
    @Size(max = 320, message = Constants.EMAIL_LENGTH_REACHED) //HARDCODED
    private String email;

    private int linkedId;

    @Enumerated(EnumType.STRING)
    private LinkPrecedence linkPrecedence;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private LocalDateTime deletedAt;

    public enum LinkPrecedence {
        PRIMARY,
        SECONDARY
    }

    public FluxKartContact(){

    }
}
