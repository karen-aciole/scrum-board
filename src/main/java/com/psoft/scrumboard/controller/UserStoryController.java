package com.psoft.scrumboard.controller;

import com.psoft.scrumboard.dto.AtribuiUserStoryDTO;
import com.psoft.scrumboard.dto.MudaStatusDTO;
import com.psoft.scrumboard.dto.UserStoryDTO;
import com.psoft.scrumboard.exception.UserStoryNotFoundException;
import com.psoft.scrumboard.service.ProjetoService;
import com.psoft.scrumboard.service.UserStoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@CrossOrigin

public class UserStoryController {

    @Autowired
    private UserStoryService userStoryService;
    
    @Autowired
    private ProjetoService projetoService;

    @RequestMapping(value = "/userstory/{projectKey}", method = RequestMethod.POST)
    public ResponseEntity<?> cadastraUserStory(@PathVariable Integer projectKey, @RequestBody UserStoryDTO userStoryDTO) {

        if (this.userStoryService.contemUserStory(projectKey, userStoryDTO.getId())) {
            return new ResponseEntity<String>("UserStory já cadastrada no sistema - número não disponível", HttpStatus.CONFLICT);
        }

        String titulo = this.userStoryService.criaUserStory(projectKey, userStoryDTO);

        return new ResponseEntity<String>("UserStory cadastrada com título '" + titulo + "'.", HttpStatus.CREATED);
    }

    @RequestMapping(value = "/userstory/{projectKey}/{idUserStory}", method = RequestMethod.GET)
    public ResponseEntity<?> acessaInfoUserStory(@RequestParam Integer projectKey, @RequestParam Integer idUserStory) {

        if (!(this.userStoryService.contemUserStory(projectKey, idUserStory))) {
            return new ResponseEntity<String>("UserStory não está cadastrada neste projeto.", HttpStatus.CONFLICT);
        }

        String info = this.userStoryService.getInfoUserStory(projectKey, idUserStory);

        return new ResponseEntity<String>(info, HttpStatus.OK);
    }

    @RequestMapping(value = "/userstory/{projectKey}/{idUserStory}", method = RequestMethod.DELETE)
    public ResponseEntity<?> removeUserStory(@PathVariable Integer projectKey, @PathVariable Integer idUserStory) {

        if (!(this.userStoryService.contemUserStory(projectKey, idUserStory))) {
            return new ResponseEntity<String>("UserStory não está cadastrada neste projeto.", HttpStatus.CONFLICT);
        }

        String info = this.userStoryService.deletaUserStory(projectKey, idUserStory);

        return new ResponseEntity<String>(info, HttpStatus.OK);
    }

    @RequestMapping(value = "/userstory/{projectKey}/{idUserStory}", method = RequestMethod.PUT)
    public ResponseEntity<?> atualizaUserStory(@PathVariable Integer projectKey, @RequestBody UserStoryDTO userStoryDTO) {
        String info;
        try {
            info = this.userStoryService.updateInfoUserStory(projectKey, userStoryDTO);
        } catch (UserStoryNotFoundException e) {
            return new ResponseEntity<String>("UserStory não está cadastrada neste projeto.", HttpStatus.CONFLICT);
        }

        return new ResponseEntity<String>(info, HttpStatus.OK);
    }
    
    @RequestMapping(value = "/userstory/participaDesenvolvedor", method = RequestMethod.POST)
    public ResponseEntity<?> participaDesenvolvimentoUserStory(@RequestBody AtribuiUserStoryDTO atribuiUserStoryDTO) {

    	String info = this.userStoryService.atribuiUsuarioUserStory(atribuiUserStoryDTO);
    	
    	return new ResponseEntity<String>(info, HttpStatus.OK);
    }

    @RequestMapping(value = "/userstory/scrumMasterAtribuiUserStory", method = RequestMethod.POST)
    public ResponseEntity<?> scrumMasterAtribuiUserStoryAIntegrante(@RequestBody AtribuiUserStoryDTO atribuiUserStoryDTO) {

        String info = this.userStoryService.scrumMasterAtribuiUsuarioUserStory(atribuiUserStoryDTO, atribuiUserStoryDTO.getScrumMasterName());

        return new ResponseEntity<String>(info, HttpStatus.OK);
    }

    @RequestMapping(value = "/userstory/mudaStatusWorkInProgressparaToVerify", method = RequestMethod.PUT)
    public ResponseEntity<?> mudaStatusWorkInProgressparaToVerify(@RequestBody MudaStatusDTO mudaStatusDTO) {

        String info = this.userStoryService.mudaStatusWorkInProgressParaToVerify(mudaStatusDTO);

        return new ResponseEntity<String>(info, HttpStatus.OK);
    }

    @RequestMapping(value = "/userstory/mudaStatusToVerifyParaDone", method = RequestMethod.PUT)
    public ResponseEntity<?> mudaStatusToVerifyParaDone(@RequestBody MudaStatusDTO mudaStatusDTO) {

        String info = this.userStoryService.mudaStatusToVerifyParaDone(mudaStatusDTO);

        return new ResponseEntity<String>(info, HttpStatus.OK);
    }

}
