package com.psoft.scrumboard.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.psoft.scrumboard.dto.UsuarioDTO;
import com.psoft.scrumboard.model.Usuario;
import com.psoft.scrumboard.repository.UsuarioRepository;

@Service
public class UsuarioService {
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	public String criaUsuario(UsuarioDTO usuarioDTO) {
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
	
	public String updateInfoUsuario(UsuarioDTO usuarioDTO) {
		Usuario usuario = this.usuarioRepository.getUser(usuarioDTO.getUsername());
		
		if (!(usuario.getSenha().equals(usuarioDTO.getSenha()))) {
			return "Senha incorreta";
		} else {
			usuario = new Usuario(usuarioDTO.getNomeCompleto(),
                                  usuarioDTO.getUsername(),
                                  usuarioDTO.getEmail(),
                                  usuarioDTO.getSenha());
			
			this.usuarioRepository.addUser(usuario);
			
			return "Usuário atualizado com username '" + usuario.getUsername() + "'";
		}
		
	}

}
