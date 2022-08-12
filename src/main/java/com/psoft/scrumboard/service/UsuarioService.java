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

		if (contemUsername(usuarioDTO.getUsername())) throw new UsuarioAlreadyExistsException("Usuário já cadastrado no sistema - username não disponível");

		Usuario usuario = new Usuario(usuarioDTO.getNomeCompleto(),
				                      usuarioDTO.getUsername(),
				                      usuarioDTO.getEmail(),
				                      usuarioDTO.getSenha());
		
		this.usuarioRepository.addUser(usuario);
		
		return usuario.getUsername();
	}
	
	public boolean contemUsername(String username) {
		return this.usuarioRepository.containsUsername(username);
	}
	
	public String getInfoUsuario(String username, String senha) {
		Usuario usuario = this.usuarioRepository.getUser(username);
		
		if (!(usuario.getSenha().equals(senha))) {
			return "Senha incorreta";
		} else {
			return usuario.toString();
		}
		
	}
	
	public String updateInfoUsuario(String username, String novoNomeCompleto, String novoEmail, String senha) {
		Usuario usuario = this.usuarioRepository.getUser(username);
		
		if (!(usuario.getSenha().equals(senha))) {
			return "Senha incorreta";
		} else {
			usuario.setNomeCompleto(novoNomeCompleto);
			usuario.setEmail(novoEmail);
			
			this.usuarioRepository.updateUser(usuario);
			
			return "Usuário atualizado com username '" + usuario.getUsername() + "'";
		}
		
	}
	
	public String deletaUsuario(String username, String senha) {
		Usuario usuario = this.usuarioRepository.getUser(username);
		
		if (!(usuario.getSenha().equals(senha))) {
			return "Senha incorreta";
		} else {
			this.usuarioRepository.delUser(username);
			return "Usuário removido com username '" + usuario.getUsername() + "'";
		}
	}

}
