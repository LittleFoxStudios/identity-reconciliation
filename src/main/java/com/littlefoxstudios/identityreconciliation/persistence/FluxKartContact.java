package com.littlefoxstudios.identityreconciliation.persistence;

import com.littlefoxstudios.identityreconciliation.Constants;
import jakarta.persistence.*;
import lombok.Data;
import jakarta.validation.constraints.Pattern;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@Entity
@Table(name = "contact")
public class FluxKartContact {

    public static final String PRIMARY = "primary";
    public static final String SECONDARY = "secondary";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Pattern(regexp = "^[+]?[a-zA-Z0-9()\\-\\s]{7,32}$", message = Constants.INVALID_PHONE_NUMBER_WARN) //HARDCODED
    private String phoneNumber;

    private String email;

    @Column(unique = true)
    private Integer linkedId;

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

}

