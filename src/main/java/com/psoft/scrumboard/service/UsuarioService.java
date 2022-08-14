package com.psoft.scrumboard.service;

import com.psoft.scrumboard.exception.UsuarioAlreadyExistsException;
import com.psoft.scrumboard.exception.UsuarioNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.psoft.scrumboard.dto.UsuarioDTO;
import com.psoft.scrumboard.model.Usuario;
import com.psoft.scrumboard.repository.UsuarioRepository;

@Service
public class UsuarioService {
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	public String criaUsuario(UsuarioDTO usuarioDTO) throws UsuarioAlreadyExistsException {

		if (contemUsername(usuarioDTO.getUsername()))
			throw new UsuarioAlreadyExistsException("Usuário já cadastrado no sistema - username não disponível");

		Usuario usuario = new Usuario(usuarioDTO.getNomeCompleto(),
				                      usuarioDTO.getUsername(),
				                      usuarioDTO.getEmail());
		
		this.usuarioRepository.addUser(usuario);
		
		return usuario.getUsername();
	}
	
	public boolean contemUsername(String username) {
		return this.usuarioRepository.containsUsername(username);
	}
	
	public String getInfoUsuario(String username) throws UsuarioNotFoundException {
		if (!contemUsername(username))
			throw new UsuarioNotFoundException("Usuário não encontrado no sistema");

		Usuario usuario = this.usuarioRepository.getUser(username);
		
		return usuario.toString();
	}
	
	public String updateInfoUsuario(UsuarioDTO usuarioDTO) throws UsuarioNotFoundException {
		if (!contemUsername(usuarioDTO.getUsername()))
			throw new UsuarioNotFoundException("Usuário não encontrado no sistema");

		Usuario usuario = this.usuarioRepository.getUser(usuarioDTO.getUsername());
		
		usuario.setNomeCompleto(!usuarioDTO.getNomeCompleto().isBlank() ? usuarioDTO.getNomeCompleto() : usuario.getNomeCompleto());
		usuario.setEmail(!usuarioDTO.getEmail().isBlank() ? usuarioDTO.getEmail() : usuario.getEmail());
		this.usuarioRepository.updateUser(usuario);
		
		return "Usuário atualizado com username '" + usuario.getUsername() + "'";
	}
	
	public String deletaUsuario(String username) throws UsuarioNotFoundException {
		if (!contemUsername(username))
			throw new UsuarioNotFoundException("Usuário não encontrado no sistema");

		Usuario usuario = this.usuarioRepository.getUser(username);
		
		this.usuarioRepository.delUser(username);
		
		return "Usuário removido com username '" + usuario.getUsername() + "'";
	}

}
