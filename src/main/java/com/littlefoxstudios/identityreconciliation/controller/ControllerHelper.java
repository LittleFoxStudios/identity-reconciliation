package com.littlefoxstudios.identityreconciliation.controller;

import com.littlefoxstudios.identityreconciliation.Utilities;
import com.littlefoxstudios.identityreconciliation.persistence.FluxKartContact;
import com.littlefoxstudios.identityreconciliation.persistence.FluxKartContactRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ControllerHelper {

    @Autowired
    FluxKartContactRepo repo;

    ControllerHelper(FluxKartContactRepo repo){
        this.repo = repo;
    }

    public FluxKartContact addNewContact(FluxKartContact fluxkartcontact) {
        Date currentDate = new Date();
        fluxkartcontact.setCreatedAt(currentDate);
        fluxkartcontact.setUpdatedAt(currentDate);
        fluxkartcontact.setLinkPrecedence(FluxKartContact.PRIMARY);
        fluxkartcontact.setDeletedAt(null);
        fluxkartcontact.setLinkedId(null);
        // Optional.ofNullable(fluxKartContact.getEmail()).ifPresent(oldData::setEmail);
        return repo.save(fluxkartcontact);
    }

    public List<Identity> handleEmailOnly(FluxKartContact fluxkartcontact) {
        Optional<FluxKartContact> existingContact = findByEmail(fluxkartcontact.getEmail());
        if(existingContact.isEmpty()){
            //new contact
            addNewContact(fluxkartcontact);
        }
        return new ArrayList<>();
    }

    public List<Identity> handlePhoneNumberOnly(FluxKartContact fluxkartcontact) {
        return new ArrayList<>();
    }

    public List<Identity> handleEmailAndPhoneNumber(FluxKartContact fluxkartcontact) {
        return new ArrayList<>();
    }

    public Optional<FluxKartContact> findByPhoneNumber(String phoneNumber) {
        return Optional.ofNullable(repo.findByPhoneNumber(phoneNumber));
    }

    public Optional<FluxKartContact> findByEmail(String email) {
        return Optional.ofNullable(repo.findByEmail(email));
    }


}
