package com.littlefoxstudios.identityreconciliation.resource;

import com.littlefoxstudios.identityreconciliation.Constants;
import com.littlefoxstudios.identityreconciliation.controller.ContactInput;
import com.littlefoxstudios.identityreconciliation.controller.FluxKartContactController;
import com.littlefoxstudios.identityreconciliation.controller.Identity;
import com.littlefoxstudios.identityreconciliation.persistence.FluxKartContact;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;

@RestController
public class FluxKartContactResource {

    FluxKartContactController controller;

    public FluxKartContactResource(FluxKartContactController controller){
        this.controller = controller;
    }

    @GetMapping(value = "/api/contacts")
    public List<FluxKartContact> getAllContacts() {
        return controller.getAllContacts();
    }

    @PostMapping(value = Constants.IDENTITY, consumes = Constants.JSON_APPLICATION)
    public ResponseEntity<HashMap<String,Identity>> addOrUpdateContact(@RequestBody ContactInput contactInput) {
        HashMap<String, Identity> map = new HashMap<>();
        map.put("contact", controller.addOrUpdateContact(contactInput));
        return ResponseEntity.ok(map);
    }

}
