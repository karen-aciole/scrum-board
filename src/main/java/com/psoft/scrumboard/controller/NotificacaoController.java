package com.psoft.scrumboard.controller;

import com.psoft.scrumboard.dto.MudaStatusDTO;
import com.psoft.scrumboard.exception.*;
import com.psoft.scrumboard.service.NotificacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class NotificacaoController {
	
	@Autowired
    private NotificacaoService notificacaoService;
	
	@RequestMapping(value = "/notificacao/usuario", method = RequestMethod.PUT)
    public ResponseEntity<?> notificaUsuarioUserStory(@RequestBody MudaStatusDTO inscricaoDTO) {
    	String info;
    	
    	try {
    		info = this.notificacaoService.addInscricaoUsuario(inscricaoDTO);
    	} catch (ProjetoNotFoundException e) {
            return new ResponseEntity<String>("Projeto não está cadastrado no sistema - id inválido.", HttpStatus.CONFLICT);
        } catch (UserStoryNotFoundException e) {
            return new ResponseEntity<String>("UserStory não encontrada no projeto.", HttpStatus.CONFLICT);
        } catch (UsuarioNotFoundException e) {
            return new ResponseEntity<String>("Usuário não é integrante deste projeto.", HttpStatus.CONFLICT);
        }
    	
    	return new ResponseEntity<>(info, HttpStatus.OK);
    }
    
    @RequestMapping(value = "/notificacao/scrum-master", method = RequestMethod.PUT)
    public ResponseEntity<?> notificaScrumMasterUserStory(@RequestBody MudaStatusDTO inscricaoDTO) {
    	String info;
    	
    	try {
    		info = this.notificacaoService.addInscricaoScrumMaster(inscricaoDTO);
    	} catch (ProjetoNotFoundException e) {
            return new ResponseEntity<String>("Projeto não está cadastrado no sistema - id inválido.", HttpStatus.CONFLICT);
        } catch (UsuarioNotAllowedException e) {
            return new ResponseEntity<String>("O Scrum Master informado não possui autorização para mudar status nesse projeto.", HttpStatus.CONFLICT);
        }
    	
    	return new ResponseEntity<>(info, HttpStatus.OK);
    }
    
    @RequestMapping(value = "/notificacao/product-owner", method = RequestMethod.PUT)
    public ResponseEntity<?> notificaProductOwnerUserStory(@RequestBody MudaStatusDTO inscricaoDTO) {
    	String info;
    	
    	try {
    		info = this.notificacaoService.addInscricaoProductOwner(inscricaoDTO);
    	} catch (ProjetoNotFoundException e) {
            return new ResponseEntity<String>("Projeto não está cadastrado no sistema - id inválido.", HttpStatus.CONFLICT);
        } catch (UsuarioNotFoundException e) {
            return new ResponseEntity<String>("Usuário não é Product Owner neste projeto.", HttpStatus.CONFLICT);
        }
    	
    	return new ResponseEntity<>(info, HttpStatus.OK);
    }

}
