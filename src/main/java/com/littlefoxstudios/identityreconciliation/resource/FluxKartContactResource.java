package com.littlefoxstudios.identityreconciliation.resource;

import com.littlefoxstudios.identityreconciliation.Constants;
import com.littlefoxstudios.identityreconciliation.controller.ContactInput;
import com.littlefoxstudios.identityreconciliation.controller.FluxKartContactController;
import com.littlefoxstudios.identityreconciliation.controller.Identity;
import com.littlefoxstudios.identityreconciliation.persistence.FluxKartContact;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Valid;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

@RestController
public class FluxKartContactResource {

    FluxKartContactController controller;

    public FluxKartContactResource(FluxKartContactController controller){
        this.controller = controller;
    }

    @GetMapping(value = Constants.GET_ALL_CONTACTS)
    public List<FluxKartContact> getAllContacts() {
        return controller.getAllContacts();
    }

    @Validated
    @PostMapping(value = Constants.IDENTITY, consumes = Constants.JSON_APPLICATION)
    public ResponseEntity<HashMap<String,Identity>> addOrUpdateContact(@Valid @RequestBody ContactInput contactInput) {
        HashMap<String, Identity> map = new HashMap<>();
        map.put("contact", controller.addOrUpdateContact(contactInput));
        return ResponseEntity.ok(map);
    }

    @DeleteMapping(value = Constants.IDENTITY+"/{id}")
    public ResponseEntity<HashMap<String,FluxKartContact>> deleteContact(@PathVariable Long id){
        HashMap<String, FluxKartContact> map = new HashMap<>();
        map.put("deletedContact", controller.deleteContact(id));
        return ResponseEntity.ok(map);
    }

    @GetMapping(value = Constants.IDENTITY+"/{id}")
    public ResponseEntity<HashMap<String, Identity>> getIdentity(@PathVariable Long id){
        HashMap<String, Identity> map = new HashMap<>();
        map.put("deletedContact", controller.getIdentity(id));
        return ResponseEntity.ok(map);
    }

    @DeleteMapping(value = Constants.IDENTITY)
    public ResponseEntity<HashMap<String,List<FluxKartContact>>> flushDB(){
        HashMap<String, List<FluxKartContact>> map = new HashMap<>();
        map.put("deletedDB", controller.flushDB());
        return ResponseEntity.ok(map);
    }

}
