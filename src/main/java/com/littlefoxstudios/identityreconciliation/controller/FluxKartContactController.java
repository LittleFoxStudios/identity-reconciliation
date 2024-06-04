package com.littlefoxstudios.identityreconciliation.controller;

import com.littlefoxstudios.identityreconciliation.Constants;
import com.littlefoxstudios.identityreconciliation.exceptionhandler.BadRequestException;
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

    public Identity addOrUpdateContact(ContactInput contactInput) {
        if(contactInput.getPhoneNumber() != null && contactInput.getEmail() != null){
            //both email and phone number provided
            return helper.handleEmailAndPhoneNumberCase(contactInput);
        }else if(contactInput.getEmail() != null){
            //only email provided
            return helper.handleEmailAloneCase(contactInput);
        }else if(contactInput.getPhoneNumber() != null){
            return helper.handlePhoneNumberAloneCase(contactInput);
        }
        throw new BadRequestException(Constants.INVALID_INPUT);
    }

    /*
    public Identity addOrUpdateContact(FluxKartContact fluxkartcontact) {
        if(!(fluxkartcontact.getEmail() != null && fluxkartcontact.getPhoneNumber() != null)){
            //only email or phone number is present
            if(fluxkartcontact.getEmail() != null){
                return helper.handleEmailOnly(fluxkartcontact);
            }
            return helper.handlePhoneNumberOnly(fluxkartcontact);
        }
        return helper.handleEmailAndPhoneNumber(fluxkartcontact);
    }

     */

}
