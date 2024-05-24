package ru.elfin.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.elfin.models.ClientAssessment;
import ru.elfin.requests.ClientDataScoringDto;
import ru.elfin.services.ClientService;

@RestController
@RequestMapping(path = "client")
public class ClientController {

    private final ClientService clientService;

    @Autowired
    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @PostMapping("/assess")
    public ClientAssessment assessClient(@RequestBody ClientDataScoringDto clientDataScoringDto) {
        return clientService.assessClient(clientDataScoringDto);
    }
}
