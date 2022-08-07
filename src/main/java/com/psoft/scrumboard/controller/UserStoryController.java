package com.psoft.scrumboard.controller;

import com.psoft.scrumboard.dto.UserStoryDTO;
import com.psoft.scrumboard.service.ProjetoService;
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
    
    @Autowired
    private ProjetoService projetoService;

    @RequestMapping(value = "/userstory/", method = RequestMethod.POST)
    public ResponseEntity<?> cadastraUserStory(@RequestParam String nomeProjeto, @RequestBody UserStoryDTO userStoryDTO, UriComponentsBuilder ucBuilder) {

        if (this.userStoryService.contemUserStory(nomeProjeto, userStoryDTO.getId())) {
            return new ResponseEntity<String>("UserStory já cadastrada no sistema - número não disponível", HttpStatus.CONFLICT);
        }

        String titulo = this.userStoryService.criaUserStory(nomeProjeto, userStoryDTO);

        return new ResponseEntity<String>("UserStory cadastrada com título '" + titulo + "'.", HttpStatus.CREATED);
    }

    @RequestMapping(value = "/userstory/{nomeProjeto}/{idUserStory}", method = RequestMethod.GET)
    public ResponseEntity<?> acessaInfoUserStory(@PathVariable String nomeProjeto, @PathVariable Integer idUserStory) {

        if (!(this.userStoryService.contemUserStory(nomeProjeto, idUserStory))) {
            return new ResponseEntity<String>("UserStory não está cadastrada neste projeto.", HttpStatus.CONFLICT);
        }

        String info = this.userStoryService.getInfoUserStory(nomeProjeto, idUserStory);

        return new ResponseEntity<String>(info, HttpStatus.OK);
    }

    @RequestMapping(value = "/userstory/{nomeProjeto}/{idUserStory}", method = RequestMethod.DELETE)
    public ResponseEntity<?> removeUserStory(@PathVariable String nomeProjeto, @PathVariable Integer idUserStory) {

        if (!(this.userStoryService.contemUserStory(nomeProjeto, idUserStory))) {
            return new ResponseEntity<String>("UserStory não está cadastrada neste projeto.", HttpStatus.CONFLICT);
        }

        String info = this.userStoryService.deletaUserStory(nomeProjeto, idUserStory);

        return new ResponseEntity<String>(info, HttpStatus.OK);
    }

    @RequestMapping(value = "/userstory/{nomeProjeto}/{idUserStory}", method = RequestMethod.PUT)
    public ResponseEntity<?> atualizaUserStory(@PathVariable String nomeProjeto, @PathVariable Integer idUserStory, @RequestBody UserStoryDTO userStoryDTO) {

        if (!(this.userStoryService.contemUserStory(nomeProjeto, idUserStory))) {
            return new ResponseEntity<String>("UserStory não está cadastrada neste projeto.", HttpStatus.CONFLICT);
        }

        String info = this.userStoryService.updateInfoUserStory(nomeProjeto, userStoryDTO);

        return new ResponseEntity<String>(info, HttpStatus.OK);
    }
    
    @RequestMapping(value = "/userstory/{nomeProjeto}/{idUserStory}/responsaveis/", method = RequestMethod.POST)
    public ResponseEntity<?> participaDesenvolvimentoUserStory(@PathVariable String nomeProjeto, @PathVariable Integer idUserStory, @RequestParam String responsavelUsername) {
    	
    	if (!(this.projetoService.contemProjectname(nomeProjeto))) {
            return new ResponseEntity<String>("Projeto não está cadastrado no sistema - nome inválido", HttpStatus.CONFLICT);
        }
    	
    	if (!(this.userStoryService.contemUserStory(nomeProjeto, idUserStory))) {
            return new ResponseEntity<String>("UserStory não está cadastrada neste projeto", HttpStatus.CONFLICT);
        }
    	
    	if (!(this.projetoService.contemIntegrante(nomeProjeto, responsavelUsername))) {
            return new ResponseEntity<String>("Usuário não é integrante deste projeto", HttpStatus.CONFLICT);
        }
    	
    	String info = this.userStoryService.atribuiUsuarioUserStory(nomeProjeto, idUserStory, responsavelUsername);
    	
    	return new ResponseEntity<String>(info, HttpStatus.OK);
    }

}
