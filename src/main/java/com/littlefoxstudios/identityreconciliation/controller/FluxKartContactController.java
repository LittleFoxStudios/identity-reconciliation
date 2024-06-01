package com.littlefoxstudios.identityreconciliation.controller;

import com.littlefoxstudios.identityreconciliation.Constants;
import com.littlefoxstudios.identityreconciliation.persistence.FluxKartContact;
import com.littlefoxstudios.identityreconciliation.persistence.FluxKartContactRepo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FluxKartContactController {

    FluxKartContactRepo repo;

    public FluxKartContactController(FluxKartContactRepo repo) {
        this.repo = repo;
    }

    public List<FluxKartContact> getAllContacts() {
        return repo.findAll();
    }

    public FluxKartContact getContactByID(long id) throws Exception {
        Optional<FluxKartContact> contactData = repo.findById(id);
        if(contactData.isEmpty()) {
            throw new Exception(Constants.NO_DATA_FOUND_FOR_THE_PROVIDED_ID);
        }
        return contactData.get();
    }

    public FluxKartContact addContact(FluxKartContact fluxKartContact) {
        return repo.save(fluxKartContact);
    }

    public FluxKartContact updateContactByID(long id, FluxKartContact fluxKartContact) throws Exception {
        FluxKartContact oldData = getContactByID(id);

        Optional.ofNullable(fluxKartContact.getEmail()).ifPresent(oldData::setEmail);
        Optional.ofNullable(fluxKartContact.getPhoneNumber()).ifPresent(oldData::setPhoneNumber);
        Optional.ofNullable(fluxKartContact.getLinkedId()).ifPresent(oldData::setLinkedId);
        Optional.ofNullable(fluxKartContact.getLinkPrecedence()).ifPresent(oldData::setLinkPrecedence);
        Optional.ofNullable(fluxKartContact.getUpdatedAt()).ifPresent(oldData::setUpdatedAt);
        Optional.ofNullable(fluxKartContact.getDeletedAt()).ifPresent(oldData::setDeletedAt);

       return repo.save(oldData);
    }
}
