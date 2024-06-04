package com.littlefoxstudios.identityreconciliation.persistence;

import com.littlefoxstudios.identityreconciliation.Constants;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.Data;
import jakarta.validation.constraints.Pattern;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.Objects;

@Data
@Entity
@Table(name = "contact")
public class FluxKartContact {

    public static final String PRIMARY = "primary";
    public static final String SECONDARY = "secondary";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Pattern(regexp = "^[+]?[a-zA-Z0-9()\\-\\s]{7,32}$", message = Constants.INVALID_PHONE_NUMBER_WARN) //HARDCODED
    private String phoneNumber;

    @Email
    private String email;

    @Column(unique = true)
    private Long linkedId;

    private String linkPrecedence;

    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS'Z'")
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS'Z'")
    private Date updatedAt;

    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS'Z'")
    private Date deletedAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FluxKartContact that = (FluxKartContact) o;
        return id.equals(that.id) &&
                Objects.equals(phoneNumber, that.phoneNumber) &&
                Objects.equals(email, that.email) &&
                Objects.equals(linkedId, that.linkedId) &&
                Objects.equals(linkPrecedence, that.linkPrecedence) &&
                Objects.equals(createdAt, that.createdAt) &&
                Objects.equals(updatedAt, that.updatedAt) &&
                Objects.equals(deletedAt, that.deletedAt);
    }

}

