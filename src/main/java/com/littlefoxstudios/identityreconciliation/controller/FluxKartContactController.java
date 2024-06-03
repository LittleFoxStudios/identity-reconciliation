package com.littlefoxstudios.identityreconciliation.controller;

import com.littlefoxstudios.identityreconciliation.Constants;
import com.littlefoxstudios.identityreconciliation.persistence.FluxKartContact;
import com.littlefoxstudios.identityreconciliation.persistence.FluxKartContactRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FluxKartContactController {

    @Autowired
    FluxKartContactRepo repo;
    @Autowired
    ControllerHelper helper;

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

    public FluxKartContact addNewContact(FluxKartContact fluxKartContact){
        return helper.addNewContact(fluxKartContact);
    }



    public List<Identity> addOrUpdateContact(FluxKartContact fluxkartcontact) {
        if(!(fluxkartcontact.getEmail() != null && fluxkartcontact.getPhoneNumber() != null)){
            //only email or phone number is present
            if(fluxkartcontact.getEmail() != null){
                return helper.handleEmailOnly(fluxkartcontact);
            }
            return helper.handlePhoneNumberOnly(fluxkartcontact);
        }
        return helper.handleEmailAndPhoneNumber(fluxkartcontact);
    }
}
