package com.littlefoxstudios.identityreconciliation.controller;

import com.littlefoxstudios.identityreconciliation.persistence.FluxKartContact;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import java.util.ArrayList;

@Builder
@Data
@AllArgsConstructor
public class Identity {
    long primaryContactId;
    ArrayList<String> emails = new ArrayList<>();
    ArrayList<String> phoneNumbers = new ArrayList<>();
    ArrayList<Long> secondaryContactIds = new ArrayList<>();

    public Identity(){

    }


    public static void addPrimaryContactId(Identity identity, long id) {
        identity.primaryContactId = id;
    }

    public static void addEmailToList(Identity identity, String email, boolean basedOnLinkedId) {
        if(email == null || email.length() == 0){
            return;
        }
        if(identity.emails.contains(email)){
            return;
        }
        if(basedOnLinkedId){
            identity.emails.add(0, email);
            return;
        }
        identity.emails.add(email);
    }

    public static void addPhoneNumberToList(Identity identity, String phoneNumber, boolean basedOnLinkedId) {
        if(phoneNumber == null || phoneNumber.length() == 0){
            return;
        }
        if(identity.phoneNumbers.contains(phoneNumber)){
            return;
        }
        if(basedOnLinkedId){
            identity.phoneNumbers.add(0, phoneNumber);
            return;
        }
        identity.phoneNumbers.add(phoneNumber);
    }

    public static void addSecondaryIdToList(Identity identity, Long secondaryId, boolean basedOnLikedId) {
        if(secondaryId == null){
            return;
        }
        if(identity.secondaryContactIds.contains(secondaryId)){
            return;
        }
        if(basedOnLikedId){
            identity.secondaryContactIds.add(0, secondaryId);
            return;
        }
        identity.secondaryContactIds.add(secondaryId);
    }

    public Identity createNewIdentity(FluxKartContact contact) {
        this.primaryContactId = contact.getId();
        ArrayList<String> list = new ArrayList<>();
        if(contact.getEmail() != null){
            list.add(contact.getEmail());
            this.emails = list;
            list = new ArrayList<>();
        }
        if(contact.getPhoneNumber() != null){
            list.add(contact.getPhoneNumber());
            this.phoneNumbers = list;
        }
        this.secondaryContactIds = new ArrayList<>();
        return this;
    }

}
