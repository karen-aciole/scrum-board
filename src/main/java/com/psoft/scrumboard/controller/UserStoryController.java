package com.psoft.scrumboard.controller;

import com.psoft.scrumboard.dto.AtribuiUserStoryDTO;
import com.psoft.scrumboard.dto.MudaStatusDTO;
import com.psoft.scrumboard.dto.UserStoryDTO;
import com.psoft.scrumboard.exception.*;
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

    @RequestMapping(value = "/userstory/{projectKey}", method = RequestMethod.POST)
    public ResponseEntity<?> cadastraUserStory(@PathVariable Integer projectKey, @RequestBody UserStoryDTO userStoryDTO) {
        String titulo;

        try {
            titulo = this.userStoryService.criaUserStory(projectKey, userStoryDTO);
        } catch (ProjetoNotFoundException e) {
            return new ResponseEntity<String>("Projeto não está cadastrado no sistema - nome inválido.", HttpStatus.CONFLICT);
        } catch (UserStoryAlreadyExistsException e) {
            return new ResponseEntity<String>("UserStory já cadastrado no sistema - nome inválido.", HttpStatus.CONFLICT);
        }

        return new ResponseEntity<String>("UserStory cadastrada com título '" + titulo + "'.", HttpStatus.CREATED);
    }

    @RequestMapping(value = "/userstory/{projectKey}/{idUserStory}", method = RequestMethod.GET)
    public ResponseEntity<?> acessaInfoUserStory(@RequestParam Integer projectKey, @PathVariable Integer idUserStory) {
        String info;

        try {
            info = this.userStoryService.getInfoUserStory(projectKey, idUserStory);
        } catch (ProjetoNotFoundException e) {
            return new ResponseEntity<String>("Projeto não está cadastrado no sistema - nome inválido.", HttpStatus.CONFLICT);
        } catch (UserStoryNotFoundException e) {
            return new ResponseEntity<String>("UserStory não encontrada no projeto.", HttpStatus.CONFLICT);
        }

        return new ResponseEntity<String>(info, HttpStatus.OK);
    }

    @RequestMapping(value = "/userstory/{projectKey}/{idUserStory}", method = RequestMethod.DELETE)
    public ResponseEntity<?> removeUserStory(@PathVariable Integer projectKey, @PathVariable Integer idUserStory) {
        String info;

        try {
            info = this.userStoryService.deletaUserStory(projectKey, idUserStory);
        } catch (ProjetoNotFoundException e) {
            return new ResponseEntity<String>("Projeto não está cadastrado no sistema - nome inválido.", HttpStatus.CONFLICT);
        } catch (UserStoryNotFoundException e) {
            return new ResponseEntity<String>("UserStory não encontrada no projeto.", HttpStatus.CONFLICT);
        }

        return new ResponseEntity<String>(info, HttpStatus.OK);
    }

    @RequestMapping(value = "/userstory/{projectKey}/", method = RequestMethod.PUT)
    public ResponseEntity<?> atualizaUserStory(@PathVariable Integer projectKey, @RequestParam String username, @RequestBody UserStoryDTO userStoryDTO) {
        String info;

        try {
            info = this.userStoryService.updateInfoUserStory(projectKey, username, userStoryDTO);
        }catch (ProjetoNotFoundException e) {
            return new ResponseEntity<String>("Projeto não está cadastrado no sistema - nome inválido.", HttpStatus.CONFLICT);
        } catch (UserStoryNotFoundException e) {
            return new ResponseEntity<String>("UserStory não está cadastrada neste projeto.", HttpStatus.CONFLICT);
        } catch (UsuarioNotAllowedException e) {
            return new ResponseEntity<String>("Usuário não tem permissão para alterar esta UserStory.", HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<String>(info, HttpStatus.OK);
    }
    
    @RequestMapping(value = "/userstory/participaDesenvolvedor", method = RequestMethod.POST)
    public ResponseEntity<?> participaDesenvolvimentoUserStory(@RequestBody AtribuiUserStoryDTO atribuiUserStoryDTO) {
        String info;

    	try {
            info = this.userStoryService.atribuiUsuarioUserStory(atribuiUserStoryDTO);
        } catch (ProjetoNotFoundException e) {
            return new ResponseEntity<String>("Projeto não está cadastrado no sistema - nome inválido.", HttpStatus.CONFLICT);
        } catch (UserStoryNotFoundException e) {
            return new ResponseEntity<String>("UserStory não encontrada no projeto.", HttpStatus.CONFLICT);
        } catch (UsuarioNotFoundException e) {
            return new ResponseEntity<String>("Usuário não é integrante deste projeto.", HttpStatus.CONFLICT);
        } catch (UsuarioAlreadyExistsException e) {
            return new ResponseEntity<String>("Usuário já é integrante desta UserStory.", HttpStatus.CONFLICT);
        } catch (StatusException e) {
            return new ResponseEntity<String>("UserStory já foi finalizada.", HttpStatus.CONFLICT);
        }

        return new ResponseEntity<String>(info, HttpStatus.OK);
    }

    @RequestMapping(value = "/userstory/scrumMasterAtribuiUserStory", method = RequestMethod.POST)
    public ResponseEntity<?> scrumMasterAtribuiUserStoryAIntegrante(@RequestBody AtribuiUserStoryDTO atribuiUserStoryDTO) {
        String info;

        try {

            info = this.userStoryService.scrumMasterAtribuiUsuarioUserStory(atribuiUserStoryDTO, atribuiUserStoryDTO.getScrumMasterName());

        } catch (ProjetoNotFoundException e) {
            return new ResponseEntity<String>("Projeto não está cadastrado no sistema - nome inválido.", HttpStatus.CONFLICT);
        } catch (UserStoryNotFoundException e) {
            return new ResponseEntity<String>("UserStory não encontrada no projeto.", HttpStatus.CONFLICT);
        } catch (UsuarioNotFoundException e) {
            return new ResponseEntity<String>("Usuário não é integrante deste projeto.", HttpStatus.CONFLICT);
        } catch (UsuarioNotAllowedException e) {
            return new ResponseEntity<String>("O Scrum Master informado não possui autorização para atribuir User Storys aos integrantes desse projeto.", HttpStatus.CONFLICT);
        } catch (UsuarioAlreadyExistsException e) {
            return new ResponseEntity<String>("Usuário já é integrante desta UserStory.", HttpStatus.CONFLICT);
        } catch (StatusException e) {
            return new ResponseEntity<String>("UserStory já foi finalizada.", HttpStatus.CONFLICT);
        }

        return new ResponseEntity<String>(info, HttpStatus.OK);
    }

    @RequestMapping(value = "/userstory/mudaStatusWorkInProgressparaToVerify", method = RequestMethod.PUT)
    public ResponseEntity<?> mudaStatusWorkInProgressparaToVerify(@RequestBody MudaStatusDTO mudaStatusDTO) {

        try {
            this.userStoryService.mudaStatusWorkInProgressParaToVerify(mudaStatusDTO);

        } catch (ProjetoNotFoundException e) {
            return new ResponseEntity<String>("Projeto não está cadastrado no sistema - nome inválido.", HttpStatus.CONFLICT);
        } catch (UserStoryNotFoundException e) {
            return new ResponseEntity<String>("UserStory não encontrada no projeto.", HttpStatus.CONFLICT);
        } catch (UsuarioNotFoundException e) {
            return new ResponseEntity<String>("Usuário não é integrante deste projeto.", HttpStatus.CONFLICT);
        } catch (UsuarioNotAllowedException e) {
            return new ResponseEntity<String>("O Scrum Master informado não possui autorização para mudar status nesse projeto.", HttpStatus.CONFLICT);
        } catch (StatusException e) {
            return new ResponseEntity<String>("A US não se encontra no estágio de desenvolvimento 'Work In Progress'.", HttpStatus.CONFLICT);
        }

        return new ResponseEntity<String>("Status alterado com sucesso!", HttpStatus.OK);
    }

    @RequestMapping(value = "/userstory/mudaStatusToVerifyParaDone", method = RequestMethod.PUT)
    public ResponseEntity<?> mudaStatusToVerifyParaDone(@RequestBody MudaStatusDTO mudaStatusDTO) {

        try {
            this.userStoryService.mudaStatusToVerifyParaDone(mudaStatusDTO);
        } catch (ProjetoNotFoundException e) {
            return new ResponseEntity<String>("Projeto não está cadastrado no sistema - nome inválido.", HttpStatus.CONFLICT);
        } catch (UserStoryNotFoundException e) {
            return new ResponseEntity<String>("UserStory não encontrada no projeto.", HttpStatus.CONFLICT);
        } catch (UsuarioNotAllowedException e) {
            return new ResponseEntity<String>("O Scrum Master informado não possui autorização para mudar status nesse projeto.", HttpStatus.CONFLICT);
        } catch (StatusException e) {
            return new ResponseEntity<String>("A US não se encontra no estágio de desenvolvimento 'To Verify'.", HttpStatus.CONFLICT);
        }

        return new ResponseEntity<String>("Status alterado com sucesso!", HttpStatus.OK);
    }

    @RequestMapping(value = "/userStory/{projectKey}/{username}", method = RequestMethod.GET)
    public ResponseEntity<?> relatorioDescritosDeUserStoriesAtribuidasAUsuario(@PathVariable Integer projectKey, @PathVariable String username) {
        String info;

        try {
            info = this.userStoryService.listaRelatorioDeUsersStoriesDeUmUsuario(projectKey, username);
        } catch (ProjetoNotFoundException e) {
            return new ResponseEntity<String>("Projeto não está cadastrado no sistema - nome inválido.", HttpStatus.CONFLICT);
        } catch (UsuarioNotFoundException e) {
            return new ResponseEntity<String>("Usuário não é integrante deste projeto.", HttpStatus.CONFLICT);
        }

        return new ResponseEntity<>(info, HttpStatus.OK);
    }

    @RequestMapping(value = "/userStory/relatorio/{projectKey}/{productOwnerName}", method = RequestMethod.GET)
    public ResponseEntity<?> relatorioDescritosDeUserStories(@PathVariable Integer projectKey, @PathVariable String productOwnerName) {
        String info;

        try {
            info = this.userStoryService.listaRelatorioDeUsersStories(projectKey, productOwnerName);
        } catch (ProjetoNotFoundException e) {
            return new ResponseEntity<String>("Projeto não está cadastrado no sistema - nome inválido.", HttpStatus.CONFLICT);
        } catch (UsuarioNotFoundException e) {
            return new ResponseEntity<String>("Usuário não é integrante deste projeto.", HttpStatus.CONFLICT);
        } catch (UsuarioNotAllowedException e) {
            return new ResponseEntity<String>("Apenas Product Owners podem requisitar este relatório.", HttpStatus.CONFLICT);
        }

        return new ResponseEntity<>(info, HttpStatus.OK);
    }

    @RequestMapping(value = "/userStory/{projectKey}/{username}/relatorioGeral", method = RequestMethod.GET)
    public ResponseEntity<?> relatorioDescritivosDeTodoOProjeto(@PathVariable Integer projectKey, @PathVariable String username) {
        String info;

        try {
            info = this.userStoryService.listaRelatorioDeUsersStoriesDeUmProjeto(projectKey, username);
        } catch (ProjetoNotFoundException e) {
            return new ResponseEntity<String>("Projeto não está cadastrado no sistema - nome inválido.", HttpStatus.CONFLICT);
        } catch (UsuarioNotFoundException e) {
            return new ResponseEntity<String>("Usuário não é integrante deste projeto.", HttpStatus.CONFLICT);
        } catch (UsuarioNotAllowedException e) {
            throw new RuntimeException(e);
        }

        return new ResponseEntity<>(info, HttpStatus.OK);
    }
}
