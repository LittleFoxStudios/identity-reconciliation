package com.littlefoxstudios.identityreconciliation.controller;

import com.littlefoxstudios.identityreconciliation.Constants;
import com.littlefoxstudios.identityreconciliation.exceptionhandler.ResourceNotFoundException;
import com.littlefoxstudios.identityreconciliation.persistence.FluxKartContact;
import com.littlefoxstudios.identityreconciliation.persistence.FluxKartContactRepo;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ControllerHelper {


    FluxKartContactRepo repo;

    ControllerHelper(FluxKartContactRepo repo){
        this.repo = repo;
    }

    //method to find the id of the contact which is at the end of the linked node
    private Long findLastLinkedContactId(Long currentContactID) {
        FluxKartContact contact = repo.findByLinkedId(currentContactID);
        if(contact != null && contact.getLinkedId() != null){
            return findLastLinkedContactId(contact.getId());
        }
        return currentContactID;
    }

    public Identity handleEmailAndPhoneNumberCase(ContactInput contactInput) {
        Date date = new Date();

        //check whether existing data is present for the given email and phone numberA
        ArrayList<FluxKartContact> emailContacts = repo.findByEmail(contactInput.getEmail());
        ArrayList<FluxKartContact> phoneContacts = repo.findByPhoneNumber(contactInput.getPhoneNumber());


        //even if contacts are not present, the arraylist will be empty and not null.
        if(!emailContacts.isEmpty() && !phoneContacts.isEmpty()){
            //both email and phone contacts present
            //find if any records have the same email and phone number
            for(FluxKartContact contact : emailContacts){
                if(contact.getPhoneNumber().equals(contactInput.getPhoneNumber())){
                    //if the input phone number and email are in same record, update it and exit
                    contact.setUpdatedAt(date);
                    repo.save(contact);
                    return getIdentity(false, contact);
                }
            }
            //now the email and phone number are in different records.
            //we have to link those together

            //first see which record is at first in id
            //the fist node will be primary
            long emailContactID = emailContacts.get(0).getId();
            long phoneContactID = phoneContacts.get(0).getId();
            Long appendingID;
            Long newRecordAppendingID;
            FluxKartContact alteredContact;
            if(emailContactID < phoneContactID){
                //link phone to email
                appendingID = findLastLinkedContactId(emailContactID);
                alteredContact = phoneContacts.get(0);
                newRecordAppendingID = findLastLinkedContactId(phoneContactID);
            }else{
                //link email to phone
                appendingID = findLastLinkedContactId(phoneContactID);
                alteredContact = emailContacts.get(0);
                newRecordAppendingID = findLastLinkedContactId(emailContactID);
            }
            alteredContact.setUpdatedAt(date);
            alteredContact.setLinkPrecedence(FluxKartContact.SECONDARY);
            alteredContact.setLinkedId(appendingID);
            //now update the altered contact (primary -> secondary with link)
            this.repo.save(alteredContact);
            //now add a new contact record
            FluxKartContact newContact = createNewContact(contactInput, newRecordAppendingID);
            //method needs to scan existing link. so using new contact as false.
            return getIdentity(false, newContact);
        }

        if(emailContacts.isEmpty() && phoneContacts.isEmpty()){
            //both email and phone contacts are empty. create a new contact
            FluxKartContact contact = createNewContact(contactInput);
            return getIdentity(true, contact);
        }

        //either phone number contacts or email contacts is present
        Long firstID;
        Long appendingID;
        FluxKartContact newContact;
        if(!emailContacts.isEmpty()){
            firstID = emailContacts.get(0).getId();
        }else{
            firstID = phoneContacts.get(0).getId();
        }
        appendingID = findLastLinkedContactId(firstID);
        newContact = createNewContact(contactInput, appendingID);
        return getIdentity(false, newContact);
    }
    private FluxKartContact createNewContact(ContactInput contactInput) {
        return createNewContact(contactInput, null);
    }

    private FluxKartContact createNewContact(ContactInput contactInput, Long linkedID) {
        FluxKartContact contact = new FluxKartContact();
        if(contactInput.getEmail() != null){
            contact.setEmail(contactInput.getEmail());
        }
        if(contactInput.getPhoneNumber() != null){
            contact.setPhoneNumber(contactInput.getPhoneNumber());
        }
        Date date = new Date();
        contact.setCreatedAt(date);
        contact.setUpdatedAt(date);
        if(linkedID == null){
            contact.setLinkPrecedence(FluxKartContact.PRIMARY);
        }else{
            contact.setLinkedId(linkedID);
            contact.setLinkPrecedence(FluxKartContact.SECONDARY);
        }
        repo.save(contact);
        return contact;
    }

    public Identity handleEmailAloneCase(ContactInput contactInput) {
        //check if a contact exists
        ArrayList<FluxKartContact> contacts = repo.findByEmail(contactInput.getEmail());
        if(!contacts.isEmpty()){
            //contact exists. update the date for all existing contact nodes
            Date date = new Date();
            for(FluxKartContact contact : contacts){
                contact.setUpdatedAt(date);
                this.repo.save(contact);
            }
            return getIdentity(false, contacts.get(0));
        }
        //contact does not exist - add a new contact
        FluxKartContact newContact = createNewContact(contactInput);
        return getIdentity(true, newContact);
    }

    public Identity handlePhoneNumberAloneCase(ContactInput contactInput) {
        //check if a contact exists
        ArrayList<FluxKartContact> contacts = repo.findByPhoneNumber(contactInput.phoneNumber);
        if(!contacts.isEmpty()){
            //contact exists. update the date for all existing contact nodes
            Date date = new Date();
            for(FluxKartContact contact : contacts){
                contact.setUpdatedAt(date);
                this.repo.save(contact);
            }
            return getIdentity(false, contacts.get(0));
        }
        //contact does not exist - add a new contact
        FluxKartContact newContact = createNewContact(contactInput);
        return getIdentity(true, newContact);
    }


    private Identity getIdentity(boolean isNewContact, FluxKartContact contact) {
        if(isNewContact){ //new contact - no need to search for linked contacts
            return new Identity().createNewIdentity(contact);
        }
        Identity identity = new Identity();
        //chain data with linked id
        chainDataBasedOnLinkedId(identity, contact);
        //chain data with id
        chainDataBasedOnId(identity, contact);
        return identity;
    }

    private void chainDataBasedOnId(Identity identity, FluxKartContact contact) {
        FluxKartContact linkedContact = repo.findByLinkedId(contact.getId()); //current contact id will be the next linked id
        if(linkedContact == null){
            //no other linked contact for the given id is present
            return;
        }
        Identity.addEmailToList(identity, linkedContact.getEmail(), false);
        Identity.addPhoneNumberToList(identity, linkedContact.getPhoneNumber(), false);
        Identity.addSecondaryIdToList(identity, contact.getLinkedId(), false);
        chainDataBasedOnId(identity, linkedContact);
    }

    private void chainDataBasedOnLinkedId(Identity identity, FluxKartContact contact) {
        Identity.addEmailToList(identity, contact.getEmail(), true);
        Identity.addPhoneNumberToList(identity, contact.getPhoneNumber(), true);
        if(FluxKartContact.PRIMARY.equals(contact.getLinkPrecedence())){
            //primary reached
            Identity.addPrimaryContactId(identity, contact.getId());
            return;
        }
        FluxKartContact linkedContact = getContactByID(contact.getLinkedId());
        Identity.addSecondaryIdToList(identity, contact.getLinkedId(), true);
        chainDataBasedOnLinkedId(identity, linkedContact);
    }

    public FluxKartContact getContactByID(long id) {
        Optional<FluxKartContact> contactData = repo.findById(id);
        if(contactData.isEmpty()) {
            throw new ResourceNotFoundException(Constants.NO_DATA_FOUND_FOR_THE_PROVIDED_ID);
        }
        return contactData.get();
    }

}
