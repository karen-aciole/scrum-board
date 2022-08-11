package com.psoft.scrumboard.controller;

import com.psoft.scrumboard.dto.AtribuiUserStoryDTO;
import com.psoft.scrumboard.service.ProjetoService;
import com.psoft.scrumboard.service.UserStoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@CrossOrigin

public class ScrumMasterController {
    @Autowired
    private UserStoryService userStoryService;

    @Autowired
    private ProjetoService projetoService;

    @RequestMapping(value = "/scrummaster/papeisDisponiveis", method = RequestMethod.GET)
    public ResponseEntity<?> listaPapeis() {
        String papeis = "Papéis que podem ser assumidos por usuários associados a projetos:\n\n" +
                "- Product Owner\n" +
                "- Pesquisador\n" +
                "- Desenvolvedor\n" +
                "- Estagiário";

        return new ResponseEntity<String>(papeis, HttpStatus.OK);
    }
    @RequestMapping(value = "/scrummaster/estagiosDesenvolvimentoDisponiveis", method = RequestMethod.GET)
    public ResponseEntity<?> listaEstagiosDeDesenvolvimento() {
        String estagiosDesenvolvimento = "Estágios de desenvolvimento que uma User Story pode assumir:\n\n" +
                "- To Do\n" +
                "- Work in Progress\n" +
                "- To Verify\n" +
                "- Done";

        return new ResponseEntity<String>(estagiosDesenvolvimento, HttpStatus.OK);
    }

    @RequestMapping(value = "/scrumMaster/", method = RequestMethod.POST)
    public ResponseEntity<?> atribuiUserStory(@RequestBody AtribuiUserStoryDTO atribuiUserStoryDTO) {

        if (!(this.projetoService.contemProjectKey(atribuiUserStoryDTO.getProjectKey()))) {
            return new ResponseEntity<String>("Projeto não está cadastrado no sistema - nome inválido", HttpStatus.CONFLICT);
        }

        if (!(this.userStoryService.contemUserStory(atribuiUserStoryDTO.getProjectKey(), atribuiUserStoryDTO.getIdUserStory()))) {
            return new ResponseEntity<String>("UserStory não está cadastrada neste projeto", HttpStatus.CONFLICT);
        }

        if (!(this.projetoService.contemIntegrante(atribuiUserStoryDTO.getProjectKey(), atribuiUserStoryDTO.getUsername()))) {
            return new ResponseEntity<String>("Usuário não é integrante deste projeto", HttpStatus.CONFLICT);
        }

        String info = this.userStoryService.atribuiUsuarioUserStory(atribuiUserStoryDTO);

        return new ResponseEntity<String>(info, HttpStatus.OK);
    }



}
