package com.psoft.scrumboard.controller;

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
import org.springframework.web.util.UriComponentsBuilder;

import com.psoft.scrumboard.dto.UsuarioDTO;
import com.psoft.scrumboard.service.UsuarioService;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class UsuarioController {
	
	@Autowired
	private UsuarioService usuarioService;
	
	@RequestMapping(value = "/usuario/", method = RequestMethod.POST)
	public ResponseEntity<?> cadastraUsuario(@RequestBody UsuarioDTO usuarioDTO, UriComponentsBuilder ucBuilder) {
		
		if (this.usuarioService.contemUsername(usuarioDTO.getUsername())) {
			return new ResponseEntity<String>("Usuário já cadastrado no sistema - username não disponível", HttpStatus.CONFLICT);
		}
		
		String username = this.usuarioService.criaUsuario(usuarioDTO);
		
		return new ResponseEntity<String>("Usuário cadastrado com username: " + username, HttpStatus.CREATED);
	}
	
	@RequestMapping(value = "/usuario/{username}", method = RequestMethod.GET)
	public ResponseEntity<?> acessaInfoUsuario(@PathVariable String username, @RequestParam String senha) {
		
		if (!(this.usuarioService.contemUsername(username))) {
			return new ResponseEntity<String>("Usuário não está cadastrado no sistema - username incorreto", HttpStatus.CONFLICT);
		}
		
		String info = this.usuarioService.getInfoUsuario(username, senha);
		
		return new ResponseEntity<String>(info, HttpStatus.OK);
		
	}

}
