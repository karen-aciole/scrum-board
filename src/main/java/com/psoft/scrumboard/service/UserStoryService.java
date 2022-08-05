package com.psoft.scrumboard.service;

import com.psoft.scrumboard.dto.UserStoryDTO;
import com.psoft.scrumboard.model.Projeto;
import com.psoft.scrumboard.model.UserStory;
import com.psoft.scrumboard.model.estagiodesenvolvimento.EstagioDesenvolvimento;
import com.psoft.scrumboard.repository.EstagioDesenvolvimentoRepository;
import com.psoft.scrumboard.repository.ProjetoRepository;
import com.psoft.scrumboard.repository.UserStoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserStoryService {
	
	@Autowired
    private EstagioDesenvolvimentoRepository estagioDesenvolvimentoRepository;
	
	@Autowired
	private ProjetoRepository projetoRepository;

    public String criaUserStory(String nomeProjeto, UserStoryDTO userStoryDTO) {
    	
    	EstagioDesenvolvimento estagioDesenvolvimento =
    			this.estagioDesenvolvimentoRepository.getEstagioDesenvolvimentoByID(1); // chave 1 está relacionada a ToDo
    			
    	UserStory userStory = new UserStory(userStoryDTO.getNumero(), userStoryDTO.getTitulo(),
                userStoryDTO.getDescricao(),
                estagioDesenvolvimento);
    	
    	Projeto projeto = this.projetoRepository.getProjeto(nomeProjeto);

        projeto.getUserStoryRepository().addUserStory(userStory);

        return userStory.getTitulo();
    }

    public boolean contemUserStory(String nomeProjeto, Integer numero) {
        if (this.projetoRepository.containsProjectname(nomeProjeto)) {
        	return this.projetoRepository.getProjeto(nomeProjeto).getUserStoryRepository().containsUserStory(numero);
        } else {
        	return false;
        }
    }

    public String getInfoUserStory(String nomeProjeto, Integer numero) {
    	return this.projetoRepository.getProjeto(nomeProjeto).getUserStoryRepository().getUserStory(numero).toString();
    }

    public String updateInfoUserStory(String nomeProjeto, UserStoryDTO userStoryDTO) {
        UserStory userStory;

        if (!contemUserStory(nomeProjeto, userStoryDTO.getNumero())) {
            return "UserStory não encontrada";
        } else {
            userStory = new UserStory(userStoryDTO.getNumero(), userStoryDTO.getTitulo(),
                    userStoryDTO.getDescricao(),
                    null); // futuramente remover essa linha do UserStoryDTO

            this.projetoRepository.getProjeto(nomeProjeto).getUserStoryRepository().addUserStory(userStory);

            return "UserStory atualizado com titulo '" + userStory.getTitulo() + "'";
        }
    }

    public String deletaUserStory(String nomeProjeto, Integer numero) {
        
    	UserStoryRepository userStories = this.projetoRepository.getProjeto(nomeProjeto).getUserStoryRepository();
    	String titulo = userStories.getUserStory(numero).getTitulo();
    	userStories.delUserStory(numero);
    	
    	return "UserStory com titulo '" + titulo + "' removida";
    }

}


