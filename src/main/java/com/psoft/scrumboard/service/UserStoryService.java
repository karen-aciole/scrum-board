package com.psoft.scrumboard.service;

import com.psoft.scrumboard.dto.UserStoryDTO;
import com.psoft.scrumboard.model.Integrante;
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

    public String criaUserStory(Integer projectKey, UserStoryDTO userStoryDTO) {
    	
    	EstagioDesenvolvimento estagioDesenvolvimento =
    			this.estagioDesenvolvimentoRepository.getEstagioDesenvolvimentoByID(1); // chave 1 está relacionada a ToDo
    			
    	UserStory userStory = new UserStory(userStoryDTO.getId(), userStoryDTO.getTitulo(),
                userStoryDTO.getDescricao(),
                estagioDesenvolvimento);
    	
    	Projeto projeto = this.projetoRepository.getProjeto(projectKey);

        projeto.getUserStoryRepository().addUserStory(userStory);

        return userStory.getTitulo();
    }

    public boolean contemUserStory(Integer projectKey, Integer idUserStory) {
        if (this.projetoRepository.containsProjectKey(projectKey)) {
        	return this.projetoRepository.getProjeto(projectKey).getUserStoryRepository().containsUserStory(idUserStory);
        } else {
        	return false;
        }
    }

    public String getInfoUserStory(Integer projectKey, Integer idUserStory) {
    	return this.projetoRepository.getProjeto(projectKey).getUserStoryRepository().getUserStory(idUserStory).toString();
    }

    public String updateInfoUserStory(Integer projectKey, UserStoryDTO userStoryDTO) {
        UserStory userStory;

        if (!contemUserStory(projectKey, userStoryDTO.getId())) {
            return "UserStory não encontrada";
        } else {
            userStory = new UserStory(userStoryDTO.getId(), userStoryDTO.getTitulo(),
                    userStoryDTO.getDescricao(),
                    null); // futuramente remover essa linha do UserStoryDTO

            this.projetoRepository.getProjeto(projectKey).getUserStoryRepository().addUserStory(userStory);

            return "UserStory atualizado com titulo '" + userStory.getTitulo() + "'";
        }
    }

    public String deletaUserStory(Integer projectKey, Integer idUserStory) {
        
    	UserStoryRepository userStories = this.projetoRepository.getProjeto(projectKey).getUserStoryRepository();
    	String titulo = userStories.getUserStory(idUserStory).getTitulo();
    	userStories.delUserStory(idUserStory);
    	
    	return "UserStory com titulo '" + titulo + "' removida";
    }
    
    private boolean usuarioTemPapelPermitido(Integrante integrante) {
    	
    	if (integrante.getPapel().getTipo().equals("Pesquisador")
    			|| integrante.getPapel().getTipo().equals("Desenvolvedor")
    			|| integrante.getPapel().getTipo().equals("Estagiario")) {
    		return true;
    	} else {
    		return false;
    	}
    	
    }
    
    public String atribuiUsuarioUserStory(Integer projectKey, Integer idUserStory, String responsavelUsername) {
    	Integrante integrante = this.projetoRepository.getProjeto(projectKey).getIntegranteRepository().getIntegrante(responsavelUsername);
    	
    	if (usuarioTemPapelPermitido(integrante)) {
    		this.projetoRepository.getProjeto(projectKey).getUserStoryRepository().getUserStory(idUserStory).getResponsaveis().addIntegrante(integrante);
    		return integrante.getUsuario().getUsername();
    	} else {
    		return "Usuário não possui um tipo de papel permitido";
    	}
    	
    }
    
}
