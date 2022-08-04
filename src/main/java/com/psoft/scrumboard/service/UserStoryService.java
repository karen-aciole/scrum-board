package com.psoft.scrumboard.service;

import com.psoft.scrumboard.dto.UserStoryDTO;
import com.psoft.scrumboard.model.UserStory;
import com.psoft.scrumboard.model.estagioDesenvolvimento.EstagioDesenvolvimento;
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

}


