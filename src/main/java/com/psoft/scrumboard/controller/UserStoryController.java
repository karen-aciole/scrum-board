package com.psoft.scrumboard.controller;

import com.psoft.scrumboard.dto.UserStoryDTO;
import com.psoft.scrumboard.service.UserStoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/api")
@CrossOrigin

public class UserStoryController {

    @Autowired
    private UserStoryService userStoryService;

    @RequestMapping(value = "/userstory/", method = RequestMethod.POST)
    public ResponseEntity<?> cadastraUserStory(@RequestParam String nomeProjeto, @RequestBody UserStoryDTO userStoryDTO, UriComponentsBuilder ucBuilder) {

        if (this.userStoryService.contemUserStory(nomeProjeto, userStoryDTO.getNumero())) {
            return new ResponseEntity<String>("UserStory já cadastrada no sistema - número não disponível", HttpStatus.CONFLICT);
        }

        String titulo = this.userStoryService.criaUserStory(nomeProjeto, userStoryDTO);

        return new ResponseEntity<String>("UserStory cadastrada com título '" + titulo + "'.", HttpStatus.CREATED);
    }

    @RequestMapping(value = "/userstory/{nomeProjeto}/{numero}", method = RequestMethod.GET)
    public ResponseEntity<?> acessaInfoUserStory(@PathVariable String nomeProjeto, @PathVariable Integer numero) {

        if (!(this.userStoryService.contemUserStory(nomeProjeto, numero))) {
            return new ResponseEntity<String>("UserStory não está cadastrada neste projeto.", HttpStatus.CONFLICT);
        }

        String info = this.userStoryService.getInfoUserStory(nomeProjeto, numero);

        return new ResponseEntity<String>(info, HttpStatus.OK);
    }

    @RequestMapping(value = "/userstory/{nomeProjeto}/{numero}", method = RequestMethod.DELETE)
    public ResponseEntity<?> removeUserStory(@PathVariable String nomeProjeto, @PathVariable Integer numero) {

        if (!(this.userStoryService.contemUserStory(nomeProjeto, numero))) {
            return new ResponseEntity<String>("UserStory não está cadastrada neste projeto.", HttpStatus.CONFLICT);
        }

        String info = this.userStoryService.deletaUserStory(nomeProjeto, numero);

        return new ResponseEntity<String>(info, HttpStatus.OK);
    }

}
