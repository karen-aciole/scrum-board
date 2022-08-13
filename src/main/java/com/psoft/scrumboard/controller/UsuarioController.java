package com.psoft.scrumboard.controller;

import com.psoft.scrumboard.dto.MudaStatusDTO;
import com.psoft.scrumboard.exception.UsuarioAlreadyExistsException;
import com.psoft.scrumboard.service.ProjetoService;
import com.psoft.scrumboard.service.UserStoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.psoft.scrumboard.dto.UsuarioDTO;
import com.psoft.scrumboard.service.UsuarioService;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class UsuarioController {
	
	@Autowired
	private UsuarioService usuarioService;
	
	@RequestMapping(value = "/usuario/", method = RequestMethod.POST)
	public ResponseEntity<?> cadastraUsuario(@RequestBody UsuarioDTO usuarioDTO) {
		String username;

		try {
			username = this.usuarioService.criaUsuario(usuarioDTO);
		} catch (UsuarioAlreadyExistsException e) {
			return new ResponseEntity<String>("Usuário já cadastrado no sistema - username não disponível", HttpStatus.CONFLICT);
		}

		return new ResponseEntity<String>("Usuário cadastrado com username '" + username + "'", HttpStatus.CREATED);
	}
	
	@RequestMapping(value = "/usuario/{username}", method = RequestMethod.GET)
	public ResponseEntity<?> acessaInfoUsuario(@PathVariable String username) {
		
		if (!(this.usuarioService.contemUsername(username))) {
			return new ResponseEntity<String>("Usuário não está cadastrado no sistema - username inválido", HttpStatus.CONFLICT);
		}
		
		String info = this.usuarioService.getInfoUsuario(username);
		
		return new ResponseEntity<String>(info, HttpStatus.OK);		
	}
	
	@RequestMapping(value = "/usuario/{username}", method = RequestMethod.PUT)
	public ResponseEntity<?> atualizaInfoUsuario(@RequestBody UsuarioDTO usuarioDTO) {

		if (!(this.usuarioService.contemUsername(usuarioDTO.getUsername()))) {
			return new ResponseEntity<String>("Usuário não está cadastrado no sistema - username inválido", HttpStatus.CONFLICT);
		}
		
		String info = this.usuarioService.updateInfoUsuario(usuarioDTO);
		
		return new ResponseEntity<String>(info, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/usuario/{username}", method = RequestMethod.DELETE)
	public ResponseEntity<?> removeUsuario(@PathVariable String username) {
		
		if (!(this.usuarioService.contemUsername(username))) {
			return new ResponseEntity<String>("Usuário não está cadastrado no sistema - username inválido", HttpStatus.CONFLICT);
		}
		
		String info = this.usuarioService.deletaUsuario(username);
		
		return new ResponseEntity<String>(info, HttpStatus.OK);		
	}
	
}
