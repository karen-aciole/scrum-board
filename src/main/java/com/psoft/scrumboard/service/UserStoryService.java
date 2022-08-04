package com.psoft.scrumboard.service;

import com.psoft.scrumboard.dto.UserStoryDTO;
import com.psoft.scrumboard.model.UserStory;
import com.psoft.scrumboard.repository.UserStoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserStoryService {

    @Autowired
    private UserStoryRepository userStoryRepository;

    public String criaUserStory(UserStoryDTO userStoryDTO) {
        UserStory userStory = new UserStory(userStoryDTO.getTitulo(),
                userStoryDTO.getDescricao(),
                userStoryDTO.getEstagioDesenvolvimento()); // futuramente remover essa linha do UserStoryDTO

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
                    userStoryDTO.getEstagioDesenvolvimento()); // futuramente remover essa linha do UserStoryDTO

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

        return "UserStory com titulo " + titulo + " removida";
    }

}


