package com.psoft.scrumboard.controller;

import com.psoft.scrumboard.dto.AtribuiUserStoryDTO;
import com.psoft.scrumboard.dto.MudaStatusDTO;
import com.psoft.scrumboard.dto.UserStoryDTO;
import com.psoft.scrumboard.exception.*;
import com.psoft.scrumboard.model.UserStory;
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
    public ResponseEntity<?> cadastraUserStory(@PathVariable Integer projectKey, @RequestParam String username, @RequestBody UserStoryDTO userStoryDTO) {
        String titulo;

        try {
            titulo = this.userStoryService.criaUserStory(projectKey, userStoryDTO, username);
        }catch (ProjetoNotFoundException e) {
            return new ResponseEntity<String>("Projeto não está cadastrado no sistema - nome inválido.", HttpStatus.CONFLICT);
        } catch (UserStoryAlreadyExistsException e) {
            return new ResponseEntity<String>("UserStory já cadastrada no projeto - número não disponível", HttpStatus.CONFLICT);
        } catch (UsuarioNotAllowedException e) {
            return new ResponseEntity<String>("Usuário não tem permissão para criar UserStory.", HttpStatus.FORBIDDEN);
        } catch (Exception e) {
            return new ResponseEntity<String>("Número da UserStory inválido - insira um número maior que zero.", HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<String>("UserStory cadastrada com título '" + titulo + "'.", HttpStatus.CREATED);
    }

    @RequestMapping(value = "/userstory/{projectKey}/{idUserStory}", method = RequestMethod.GET)
    public ResponseEntity<?> acessaInfoUserStory(@PathVariable Integer projectKey, @PathVariable Integer idUserStory, @RequestParam String username) {
        String info;

        try {
            info = this.userStoryService.getInfoUserStory(projectKey, idUserStory, username);
        }catch (ProjetoNotFoundException e) {
            return new ResponseEntity<String>("Projeto não está cadastrado no sistema - nome inválido.", HttpStatus.CONFLICT);
        } catch (UserStoryNotFoundException e) {
            return new ResponseEntity<String>("UserStory não está cadastrada neste projeto.", HttpStatus.CONFLICT);
        } catch (UsuarioNotAllowedException e) {
            return new ResponseEntity<String>("Usuário não tem permissão para visualizar esta UserStory.", HttpStatus.FORBIDDEN);
        }


        return new ResponseEntity<String>(info, HttpStatus.OK);
    }

    @RequestMapping(value = "/userstory/{projectKey}/{idUserStory}", method = RequestMethod.DELETE)
    public ResponseEntity<?> removeUserStory(@PathVariable Integer projectKey, @PathVariable Integer idUserStory, @RequestParam String username) {
        String info;

        try {
            info = this.userStoryService.deletaUserStory(projectKey, idUserStory, username);
        }catch (ProjetoNotFoundException e) {
            return new ResponseEntity<String>("Projeto não está cadastrado no sistema - nome inválido.", HttpStatus.CONFLICT);
        } catch (UserStoryNotFoundException e) {
            return new ResponseEntity<String>("UserStory não está cadastrada neste projeto.", HttpStatus.CONFLICT);
        } catch (UsuarioNotAllowedException e) {
            return new ResponseEntity<String>("Usuário não tem permissão para remover esta UserStory.", HttpStatus.FORBIDDEN);
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
}
