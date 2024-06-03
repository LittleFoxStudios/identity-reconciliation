package com.littlefoxstudios.identityreconciliation;

import com.littlefoxstudios.identityreconciliation.persistence.FluxKartContact;


public class Utilities {

    public static String getStandardizedPhoneNumber(FluxKartContact contact){
        return (contact.getPhoneNumber() == null) ? null : contact.getPhoneNumber().replaceAll("[+\\-()\\s]", "");
    }


}
