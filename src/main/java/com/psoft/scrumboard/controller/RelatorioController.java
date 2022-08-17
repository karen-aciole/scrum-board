package com.psoft.scrumboard.controller;

import com.psoft.scrumboard.exception.ProjetoNotFoundException;
import com.psoft.scrumboard.exception.UserStoryNotFoundException;
import com.psoft.scrumboard.exception.UsuarioNotAllowedException;
import com.psoft.scrumboard.exception.UsuarioNotFoundException;
import com.psoft.scrumboard.service.RelatorioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@CrossOrigin

public class RelatorioController {

    @Autowired
    private RelatorioService relatorioService;

    @RequestMapping(value = "/relatorioDoUsuario/{projectKey}/{username}", method = RequestMethod.GET)
    public ResponseEntity<?> relatorioDescritosDeUserStoriesAtribuidasAUsuario(@PathVariable Integer projectKey, @PathVariable String username) {
        String info;

        try {
            info = this.relatorioService.listaRelatorioDeUsersStoriesDeUmUsuario(projectKey, username);
        } catch (ProjetoNotFoundException e) {
            return new ResponseEntity<String>("Projeto não está cadastrado no sistema - nome inválido.", HttpStatus.CONFLICT);
        } catch (UsuarioNotFoundException e) {
            return new ResponseEntity<String>("Usuário não é integrante deste projeto.", HttpStatus.CONFLICT);
        }

        return new ResponseEntity<>(info, HttpStatus.OK);
    }

    @RequestMapping(value = "/relatorioUserStories/{projectKey}/{productOwner}", method = RequestMethod.GET)
    public ResponseEntity<?> relatorioDescritosDeUserStories(@PathVariable Integer projectKey, @PathVariable String productOwner) {

        String info;

        try {
            info = this.relatorioService.listaRelatorioDeUsersStories(projectKey, productOwner);
        } catch (ProjetoNotFoundException e) {
            return new ResponseEntity<String>("Projeto não está cadastrado no sistema - nome inválido.", HttpStatus.CONFLICT);
        } catch (UsuarioNotFoundException e) {
            return new ResponseEntity<String>("Usuário não é integrante deste projeto.", HttpStatus.CONFLICT);
        } catch (UsuarioNotAllowedException e) {
            return new ResponseEntity<String>("Apenas Product Owners podem requisitar este relatório.", HttpStatus.FORBIDDEN);
        }

        return new ResponseEntity<>(info, HttpStatus.OK);
    }

    @RequestMapping(value = "/relatorioGeral/{projectKey}/{scrumMaster}", method = RequestMethod.GET)
    public ResponseEntity<?> relatorioDescritivosDeTodoOProjeto(@PathVariable Integer projectKey, @RequestParam String scrumMaster) {

        String info;

        try {
            info = this.relatorioService.listaRelatorioDeUsersStoriesDeUmProjeto(projectKey, scrumMaster);
        } catch (ProjetoNotFoundException e) {
            return new ResponseEntity<String>("Projeto não está cadastrado no sistema - nome inválido.", HttpStatus.CONFLICT);
        } catch (UsuarioNotFoundException e) {
            return new ResponseEntity<String>("Usuário não é integrante deste projeto.", HttpStatus.CONFLICT);
        } catch (UsuarioNotAllowedException e) {
            return new ResponseEntity<>("Usuário não tem permissão pra acessar relatório geral.", HttpStatus.FORBIDDEN);
        } catch (UserStoryNotFoundException e) {
            return new ResponseEntity<String>("Não há UserStories neste projeto.", HttpStatus.CONFLICT);
        }

        return new ResponseEntity<>(info, HttpStatus.OK);
    }
}
