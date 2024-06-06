package com.littlefoxstudios.identityreconciliation;

import com.littlefoxstudios.identityreconciliation.persistence.FluxKartContact;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Utilities {

    private static final String EMAIL_PATTERN = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@" + "(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
    private static final Pattern emailPattern = Pattern.compile(EMAIL_PATTERN);


    public static String getStandardizedPhoneNumber(FluxKartContact contact){
        return (contact.getPhoneNumber() == null) ? null : contact.getPhoneNumber().replaceAll("[+\\-()\\s]", "");
    }

    public static boolean invalidEmail(String email) {
        if(email.length() < 5 || email.length() > 320){ //HARDCODED
            return true;
        }
        Matcher matcher = emailPattern.matcher(email);
        return !matcher.matches();
    }

    public static boolean invalidPhoneNumber(String phoneNumber) {
        return (phoneNumber.length() < 7 || phoneNumber.length() > 32); //HARDCODED
    }


}
