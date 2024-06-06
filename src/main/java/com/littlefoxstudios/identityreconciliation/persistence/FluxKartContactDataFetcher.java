package com.littlefoxstudios.identityreconciliation.persistence;

import com.littlefoxstudios.identityreconciliation.Constants;
import com.littlefoxstudios.identityreconciliation.exceptionhandler.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class FluxKartContactDataFetcher {
    FluxKartContactRepo repo;

    FluxKartContactDataFetcher(FluxKartContactRepo repo){
        this.repo = repo;
    }

    public FluxKartContact getContactByID(Long id) {
        return getContactByID(id, true);
    }

    public FluxKartContact getContactByID(Long id, boolean excludeDeletedID) {
        Optional<FluxKartContact> contactData = repo.findById(id);
        if(contactData.isEmpty()) {
            throw new ResourceNotFoundException(Constants.NO_DATA_FOUND_FOR_THE_PROVIDED_ID);
        }
        if(excludeDeletedID && contactData.get().getDeletedAt() != null){
            throw new ResourceNotFoundException(Constants.LINKED_DATA_DELETED);
        }
        return contactData.get();
    }

    /*
    Method finds the data row by matching the provided linked id
     */
    public FluxKartContact getContactByLinkedID(Long linkedID){
        return getContactByLinkedID(linkedID, true);
    }

    public FluxKartContact getContactByLinkedID(Long linkedID, boolean excludeDeletedID) {
        FluxKartContact linkedContact = repo.findByLinkedId(linkedID);
        if(linkedContact == null){
            return null;
        }
        if(excludeDeletedID && linkedContact.getDeletedAt() != null){
            //linked contact is deleted
            return null;
        }
        return linkedContact;
    }

    public ArrayList<FluxKartContact> getContactsByPhoneNumber(String phoneNumber) {
        return getContactsByPhoneNumber(phoneNumber, true);
    }


    public ArrayList<FluxKartContact> getContactsByPhoneNumber(String phoneNumber, boolean excludeDeletedID) {
        if(phoneNumber == null){
            return new ArrayList<>();
        }
        ArrayList<FluxKartContact> contacts = repo.findByPhoneNumber(phoneNumber);
        return filterContacts(contacts, excludeDeletedID);
    }


    public ArrayList<FluxKartContact> getContactsByEmailID(String emailID) {
        return getContactsByEmailID(emailID, true);
    }

    public ArrayList<FluxKartContact> getContactsByEmailID(String emailID, boolean excludeDeletedID) {
        if(emailID == null){
            return new ArrayList<>();
        }
        ArrayList<FluxKartContact> contacts = repo.findByEmail(emailID);
        if(contacts.size() == 0){
            return contacts;
        }
        return filterContacts(contacts, excludeDeletedID);
    }

    private ArrayList<FluxKartContact> filterContacts(ArrayList<FluxKartContact> contacts, boolean excludeDeletedID){
        if(contacts.size() == 0){
            return contacts;
        }
        if(!excludeDeletedID){
            return contacts;
        }
        ArrayList<FluxKartContact> copy = new ArrayList<>();
        for(FluxKartContact contact : contacts){
            if(contact.getDeletedAt() == null){
                copy.add(contact);
            }
        }
        return copy;
    }


}
