package com.psoft.scrumboard.service;

import com.psoft.scrumboard.dto.UserStoryDTO;
import com.psoft.scrumboard.model.UserStory;
import com.psoft.scrumboard.model.estagiodesenvolvimento.EstagioDesenvolvimento;
import com.psoft.scrumboard.repository.EstagioDesenvolvimentoRepository;
import com.psoft.scrumboard.repository.UserStoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserStoryService {

    @Autowired
    private UserStoryRepository userStoryRepository;
    
    @Autowired
    private EstagioDesenvolvimentoRepository estagioDesenvolvimentoRepository;

    public String criaUserStory(UserStoryDTO userStoryDTO) {
    	EstagioDesenvolvimento estagioDesenvolvimento =
    			this.estagioDesenvolvimentoRepository.getEstagioDesenvolvimentoByID(1); // chave 1 está relacionada a ToDo
    			
    	UserStory userStory = new UserStory(userStoryDTO.getTitulo(),
                userStoryDTO.getDescricao(),
                estagioDesenvolvimento);

        this.userStoryRepository.addUserStory(userStory);

        return userStory.getTitulo();
    }

    public boolean contemUserStory(String titulo) {
        return this.userStoryRepository.containsUserStory(titulo);
    }

    public String getInfoUserStory(String titulo) {
        UserStory userStory = this.userStoryRepository.getUserStory(titulo);

        return userStory.toString();
    }

    public String updateInfoUserStory(UserStoryDTO userStoryDTO) {
        UserStory userStory;

        if (!contemUserStory(userStoryDTO.getTitulo())) {
            return "UserStory não encontrada";
        } else {
            userStory = new UserStory(userStoryDTO.getTitulo(),
                    userStoryDTO.getDescricao(),
                    null); // futuramente remover essa linha do UserStoryDTO

            this.userStoryRepository.addUserStory(userStory);

            return "UserStory atualizado com titulo '" + userStory.getTitulo() + "'";
        }
    }

    public String deletaUserStory(String titulo) {
        if (!contemUserStory(titulo)) {
            return "UserStory não encontrada";
        } else {
            this.userStoryRepository.delUserStory(titulo);
        }

        return "UserStory com titulo '" + titulo + "' removida";
    }

}


