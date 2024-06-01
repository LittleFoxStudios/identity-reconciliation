package com.littlefoxstudios.identityreconciliation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.littlefoxstudios.identityreconciliation.persistence.FluxKartContact;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;


public class Utilities {

    @Autowired
    private ObjectMapper objectMapper;

    public String convertPersistenceObjectToJson(Object object) throws Exception {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (Exception e) {
            throw new Exception(Constants.INTERNAL_ERROR_OCCURRED);
        }
    }

}
