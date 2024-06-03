package com.littlefoxstudios.identityreconciliation.resource;

import com.littlefoxstudios.identityreconciliation.Constants;
import com.littlefoxstudios.identityreconciliation.controller.ControllerHelper;
import com.littlefoxstudios.identityreconciliation.controller.FluxKartContactController;
import com.littlefoxstudios.identityreconciliation.persistence.FluxKartContact;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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

    /*
    @PostMapping(value = Constants.IDENTITY, consumes = Constants.JSON_APPLICATION)
    public List<Identity> addOrUpdateContact(@RequestBody FluxKartContact fluxKartContact) {
        return controller.addOrUpdateContact(fluxKartContact);
    }
     */

    @PostMapping(value = Constants.IDENTITY, consumes = Constants.JSON_APPLICATION)
    public FluxKartContact test(@RequestBody FluxKartContact fluxkartcontact) {
        return controller.addNewContact(fluxkartcontact);
    }

}
