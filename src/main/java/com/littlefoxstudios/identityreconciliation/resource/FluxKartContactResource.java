package com.littlefoxstudios.identityreconciliation.resource;

import com.littlefoxstudios.identityreconciliation.controller.FluxKartContactController;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FluxKartContactResource {

    FluxKartContactController controller;

    public FluxKartContactResource(FluxKartContactController controller){
        this.controller = controller;
    }

}
