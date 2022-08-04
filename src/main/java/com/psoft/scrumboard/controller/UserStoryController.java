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
    public ResponseEntity<?> cadastraUserStory(@RequestBody UserStoryDTO userStoryDTO, UriComponentsBuilder ucBuilder) {

        if (this.userStoryService.contemUserStory(userStoryDTO.getTitulo())) {
            return new ResponseEntity<String>("UserStory já cadastrado no sistema - titulo não disponível", HttpStatus.CONFLICT);
        }

        String titulo = this.userStoryService.criaUserStory(userStoryDTO);

        return new ResponseEntity<String>("UserStory cadastrada com título '" + titulo + "'.", HttpStatus.CREATED);
    }



}
