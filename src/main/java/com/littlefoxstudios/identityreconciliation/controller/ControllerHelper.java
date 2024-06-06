package com.littlefoxstudios.identityreconciliation.controller;

import com.littlefoxstudios.identityreconciliation.Constants;
import com.littlefoxstudios.identityreconciliation.exceptionhandler.BadRequestException;
import com.littlefoxstudios.identityreconciliation.persistence.FluxKartContact;
import com.littlefoxstudios.identityreconciliation.persistence.FluxKartContactDataFetcher;
import com.littlefoxstudios.identityreconciliation.persistence.FluxKartContactRepo;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.littlefoxstudios.identityreconciliation.Utilities.invalidEmail;
import static com.littlefoxstudios.identityreconciliation.Utilities.invalidPhoneNumber;

@Service
public class ControllerHelper {

    FluxKartContactDataFetcher dataHandler;
    FluxKartContactRepo repository;

    ControllerHelper(FluxKartContactDataFetcher dataHandler, FluxKartContactRepo repository){
        this.dataHandler = dataHandler;
        this.repository = repository;
    }

    //method to find the id of the contact which is at the end of the linked node
    private Long findLastLinkedContactId(Long currentContactID) {
        return findLastLinkedContactId(currentContactID, true);
    }

    private Long findLastLinkedContactId(Long currentContactID, boolean excludeDeletedContact) {
        FluxKartContact contact = dataHandler.getContactByLinkedID(currentContactID, excludeDeletedContact);
        if(contact != null && contact.getLinkedId() != null){
            return findLastLinkedContactId(contact.getId(), excludeDeletedContact);
        }
        return currentContactID;
    }

    public Identity handleEmailAndPhoneNumberCase(ContactInput contactInput) {
        Date date = new Date();

        //check whether existing data is present for the given email and phone numberA
        ArrayList<FluxKartContact> emailContacts = dataHandler.getContactsByEmailID(contactInput.getEmail());
        ArrayList<FluxKartContact> phoneContacts = dataHandler.getContactsByPhoneNumber(contactInput.getPhoneNumber());


        //even if contacts are not present, the arraylist will be empty and not null.
        if(!emailContacts.isEmpty() && !phoneContacts.isEmpty()){
            //both email and phone contacts present
            //find if any records have the same email and phone number
            for(FluxKartContact contact : emailContacts){
                if(contact.getPhoneNumber() != null && contact.getPhoneNumber().equals(contactInput.getPhoneNumber())){
                    //if the input phone number and email are in same record, update it and return identity
                    return updateLinkedContactsAndReturnIdentity(contact);
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
            repository.save(alteredContact);
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
        repository.save(contact);
        return contact;
    }

    public Identity handleEmailAloneCase(ContactInput contactInput) {
        //check if a contact exists
        ArrayList<FluxKartContact> contacts = dataHandler.getContactsByEmailID(contactInput.getEmail());
        if(!contacts.isEmpty()){
            //contact exists. update the date for all existing contact nodes
            return updateLinkedContactsAndReturnIdentity(contacts.get(0));
        }
        //contact does not exist - add a new contact
        FluxKartContact newContact = createNewContact(contactInput);
        return getIdentity(true, newContact);
    }

    private Identity updateLinkedContactsAndReturnIdentity(FluxKartContact contact)
    {
        Date date = new Date();
        Identity identity = getIdentity(false, contact, false); //need all the ids together for updating
        for(Long contactID : identity.secondaryContactIds){
            FluxKartContact con = dataHandler.getContactByID(contactID);
            con.setUpdatedAt(date);
            repository.save(con);
        }
        identity.secondaryContactIds.remove(identity.primaryContactId);
        return identity; //updated date not required - so passing old obj
    }

    public Identity handlePhoneNumberAloneCase(ContactInput contactInput) {
        //check if a contact exists
        ArrayList<FluxKartContact> contacts = dataHandler.getContactsByPhoneNumber(contactInput.getPhoneNumber());
        if(!contacts.isEmpty()){
            //contact exists. update the date for all existing contact nodes
            return updateLinkedContactsAndReturnIdentity(contacts.get(0));
        }
        //contact does not exist - add a new contact
        FluxKartContact newContact = createNewContact(contactInput);
        return getIdentity(true, newContact);
    }

    private Identity getIdentity(boolean isNewContact, FluxKartContact contact){
        return getIdentity(isNewContact, contact, true);
    }

    private Identity getIdentity(boolean isNewContact, FluxKartContact contact, boolean removeDuplicatePrimaryID) {
        if(isNewContact){ //new contact - no need to search for linked contacts
            return new Identity().createNewIdentity(contact);
        }
        Identity identity = new Identity();
        //chain data with linked id
        chainDataBasedOnLinkedId(identity, contact);
        //chain data with id
        chainDataBasedOnContactId(identity, contact);
        if(removeDuplicatePrimaryID){
            identity.secondaryContactIds.remove(identity.primaryContactId);
        }
        return identity;
    }

    private void chainDataBasedOnContactId(Identity identity, FluxKartContact contact) {
        FluxKartContact linkedContact = dataHandler.getContactByLinkedID(contact.getId()); //current contact id will be the next linked id
        if(linkedContact == null){
            //no other linked contact for the given id is present
            return;
        }
        Identity.addEmailToList(identity, linkedContact.getEmail(), false);
        Identity.addPhoneNumberToList(identity, linkedContact.getPhoneNumber(), false);
        Identity.addSecondaryIdToList(identity, contact.getLinkedId(), false);
        chainDataBasedOnContactId(identity, linkedContact);
    }

    private void chainDataBasedOnLinkedId(Identity identity, FluxKartContact contact) {
        Identity.addEmailToList(identity, contact.getEmail(), true);
        Identity.addPhoneNumberToList(identity, contact.getPhoneNumber(), true);
        if(FluxKartContact.PRIMARY.equals(contact.getLinkPrecedence())){
            //primary reached
            Identity.addPrimaryContactId(identity, contact.getId());
            return;
        }
        FluxKartContact linkedContact = dataHandler.getContactByID(contact.getLinkedId());
        Identity.addSecondaryIdToList(identity, contact.getLinkedId(), true);
        chainDataBasedOnLinkedId(identity, linkedContact);
    }


    public void validateInput(ContactInput contactInput) {
        if(contactInput.getPhoneNumber() == null && contactInput.getEmail() == null){
            throw new BadRequestException(Constants.INVALID_INPUT);
        }
        if(contactInput.getPhoneNumber() != null && contactInput.getEmail() != null){
            if(invalidPhoneNumber(contactInput.getPhoneNumber()) || invalidEmail(contactInput.getEmail())){
                throw new BadRequestException(Constants.CHECK_INPUT);
            }
            return;
        }
        if(contactInput.getEmail() != null && invalidEmail(contactInput.getEmail())){
            throw new BadRequestException(Constants.INVALID_EMAIL_WARN);
        }else if(contactInput.getPhoneNumber() != null && invalidPhoneNumber(contactInput.getPhoneNumber())) {
            throw new BadRequestException(Constants.INVALID_PHONE_NUMBER_WARN);
        }
    }


    public FluxKartContact deleteContact(Long id) {
        Date date = new Date();
        FluxKartContact contact = dataHandler.getContactByID(id);
        //check if the contact is linked with another node
        FluxKartContact linkedContact = dataHandler.getContactByLinkedID(contact.getId());
        if(linkedContact == null){
            //no linked node. so just delete the contact
            contact.setDeletedAt(date);
            contact.setUpdatedAt(date);
            repository.save(contact);
            return contact;
        }
        //linked node present
        if(FluxKartContact.PRIMARY.equals(contact.getLinkPrecedence())){
            //this is the primary node. Convert the linked node from secondary to primary and delete the current node
            linkedContact.setLinkedId(null);
            linkedContact.setLinkPrecedence(FluxKartContact.PRIMARY);
            linkedContact.setUpdatedAt(date);
            repository.save(linkedContact);
            contact.setUpdatedAt(date);
            contact.setDeletedAt(date);
            repository.save(contact);
            return contact;
        }

        //current node is not primary node. so need to link the current node's linked id to the next node
        //first remove the current contacts link
        Long linkedID = contact.getLinkedId();
        contact.setLinkedId(null);
        contact.setLinkPrecedence(FluxKartContact.PRIMARY);
        contact.setDeletedAt(date);
        contact.setUpdatedAt(date);
        repository.save(contact);
        linkedContact.setLinkedId(linkedID);
        linkedContact.setUpdatedAt(date);
        repository.save(linkedContact);

        return contact;
    }

    public Identity getIdentityForContactID(Long id) {
        FluxKartContact contact = dataHandler.getContactByID(id);
        return getIdentity(false, contact);
    }
}
