package com.littlefoxstudios.identityreconciliation;

public class Constants {
    //api validation
    public static final String INVALID_PHONE_NUMBER_WARN = "Invalid Phone Number format provided";
    public static final String INVALID_EMAIL_WARN = "Invalid Email format provided";
    public static final String EMAIL_LENGTH_REACHED = "Maximum length of Email exceeded";
    public static final String INVALID_INPUT = "At least email or phoneNumber must be provided!";

    //query error
    public static final String NO_DATA_FOUND_FOR_THE_PROVIDED_ID = "There are no data present for the provided ID";
    public static final String INTERNAL_ERROR_OCCURRED = "Internal Error occurred";


    //misc
    public static final String JSON_APPLICATION = "application/json";

    //api
    public static final String GET_ALL_CONTACTS = "/api/contacts";
    public static final String GET_ALL_LOGS = "/api/logs";
    public static final String RESET_CONTACTS = "/api/reset/contacts";
    public static final String RESET_LOGS = "/api/reset/logs";
    public static final String IDENTITY = "/api/identity";
}
