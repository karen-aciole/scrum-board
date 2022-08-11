package com.psoft.scrumboard.service;

import com.psoft.scrumboard.dto.AtribuiUserStoryDTO;
import com.psoft.scrumboard.dto.MudaStatusDTO;
import com.psoft.scrumboard.dto.UserStoryDTO;
import com.psoft.scrumboard.model.Integrante;
import com.psoft.scrumboard.model.Projeto;
import com.psoft.scrumboard.model.UserStory;
import com.psoft.scrumboard.model.enums.EstagioDesenvolvimentoEnum;
import com.psoft.scrumboard.model.enums.PapelEnum;
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

    @Autowired
    private ProjetoService projetoService;

    public String criaUserStory(Integer projectKey, UserStoryDTO userStoryDTO) {
    	
    	EstagioDesenvolvimento estagioDesenvolvimento =
    			this.estagioDesenvolvimentoRepository.getEstagioDesenvolvimentoByEnum(EstagioDesenvolvimentoEnum.TO_DO);
    			
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

        if (!contemUserStory(projectKey, userStoryDTO.getId())) {
            return "UserStory não encontrada";
        } else {

            UserStory userStory = this.projetoRepository.getProjeto(projectKey).getUserStoryRepository().getUserStory(userStoryDTO.getId());

            userStory.setDescricao(userStoryDTO.getDescricao());
            userStory.setTitulo(userStoryDTO.getTitulo());

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
    	
    	if (integrante.getPapel().getTipo().equals(PapelEnum.PESQUISADOR)
    			|| integrante.getPapel().getTipo().equals(PapelEnum.DESENVOLVEDOR)
    			|| integrante.getPapel().getTipo().equals(PapelEnum.ESTAGIARIO)) {
    		return true;
    	} else {
    		return false;
    	}
    	
    }
    
    public String atribuiUsuarioUserStory(AtribuiUserStoryDTO atribuiUserStory) {
        if (!(this.projetoService.contemProjectKey(atribuiUserStory.getProjectKey()))) {
            return "Projeto não está cadastrado no sistema - nome inválido";
        }

        if (!(contemUserStory(atribuiUserStory.getProjectKey(), atribuiUserStory.getIdUserStory()))) {
            return "UserStory não está cadastrada neste projeto";
        }

        if (!(this.projetoService.contemIntegrante(atribuiUserStory.getProjectKey(), atribuiUserStory.getUsername()))) {
            return "Usuário não é integrante deste projeto";
        }

        Integrante integrante = this.projetoRepository.getProjeto(atribuiUserStory.getProjectKey()).getIntegranteRepository().getIntegrante(atribuiUserStory.getUsername());

        if (usuarioTemPapelPermitido(integrante)) {
            this.projetoRepository.getProjeto(atribuiUserStory.getProjectKey()).getUserStoryRepository().getUserStory(atribuiUserStory.getIdUserStory()).getResponsaveis().addIntegrante(integrante);
            return integrante.getUsuario().getUsername() + "recebeu a atribuição com sucesso";
        } else {
            return "Usuário não possui um tipo de papel permitido";
        }
    	
    }

    public String scrumMasterAtribuiUsuarioUserStory(AtribuiUserStoryDTO atribuiUserStory, String userStoryName) {
        if (!(this.projetoService.contemProjectKey(atribuiUserStory.getProjectKey()))) {
            return "Projeto não está cadastrado no sistema - nome inválido";
        }

        if (!(contemUserStory(atribuiUserStory.getProjectKey(), atribuiUserStory.getIdUserStory()))) {
            return "UserStory não está cadastrada neste projeto";
        }

        if (!(this.projetoService.contemIntegrante(atribuiUserStory.getProjectKey(), atribuiUserStory.getUsername()))) {
            return "Usuário não é integrante deste projeto";
        }

        if (!(this.projetoService.getScrumMasterName(atribuiUserStory.getProjectKey()).equals(userStoryName))) {
            return "O Scrum Master informado não possui autorização para atribuir User Storys aos integrantes desse projeto";
        }

        return atribuiUsuarioUserStory(atribuiUserStory);
    }

    public String mudaStatusWorkInProgressParaToVerify(MudaStatusDTO mudaStatus) {

        if (!(this.projetoService.contemProjectKey(mudaStatus.getProjectKey()))) {
            return "Projeto não está cadastrado no sistema - nome inválido";
        }

        if (!(contemUserStory(mudaStatus.getProjectKey(), mudaStatus.getIdUserStory()))) {
            return "UserStory não está cadastrada neste projeto";
        }

        if (!(this.projetoService.contemIntegrante(mudaStatus.getProjectKey(), mudaStatus.getUsername()))) {
            return "Usuário não é integrante deste projeto";
        }

        Projeto projeto = this.projetoRepository.getProjeto(mudaStatus.getProjectKey());

        UserStory us = projeto.getUserStoryRepository().getUserStory(mudaStatus.getIdUserStory());

        if (!(this.projetoService.getScrumMasterName(mudaStatus.getProjectKey()).equals(mudaStatus.getUsername())) || !us.getResponsaveis().containsUsername(mudaStatus.getUsername())) {
            return "O Scrum Master informado não possui autorização para mudar status nesse projeto";
        }

        if (!us.getEstagioDesenvolvimento().equals(estagioDesenvolvimentoRepository.getEstagioDesenvolvimentoByEnum(EstagioDesenvolvimentoEnum.WORK_IN_PROGRESS))) {
            return "A US não se encontra no estágio de desenvolvimento 'work in progess'";
        }

        return mudaStatus(mudaStatus, EstagioDesenvolvimentoEnum.TO_VERIFY);
    }

    private String mudaStatus(MudaStatusDTO mudaStatus, EstagioDesenvolvimentoEnum estagio) {

        Projeto projeto = this.projetoRepository.getProjeto(mudaStatus.getProjectKey());
        UserStory us = projeto.getUserStoryRepository().getUserStory(mudaStatus.getIdUserStory());

        EstagioDesenvolvimento estagioDesenvolvimento = this.estagioDesenvolvimentoRepository.getEstagioDesenvolvimentoByEnum(estagio);

        us.setEstagioDesenvolvimentoEnum(estagioDesenvolvimento);


        return "Status alterado com sucesso";
    }

    public String mudaStatusToVerifyParaDone(MudaStatusDTO mudaStatus) {
        if (!(this.projetoService.contemProjectKey(mudaStatus.getProjectKey()))) {
            return "Projeto não está cadastrado no sistema - nome inválido";
        }

        if (!(contemUserStory(mudaStatus.getProjectKey(), mudaStatus.getIdUserStory()))) {
            return "UserStory não está cadastrada neste projeto";
        }

        Projeto projeto = this.projetoRepository.getProjeto(mudaStatus.getProjectKey());
        UserStory us = projeto.getUserStoryRepository().getUserStory(mudaStatus.getIdUserStory());

        if (!(this.projetoService.getScrumMasterName(mudaStatus.getProjectKey()).equals(mudaStatus.getUsername())) || !us.getResponsaveis().containsUsername(mudaStatus.getUsername())) {
            return "O Scrum Master informado não possui autorização para mudar status nesse Projeto.";
        }

        if (!us.getEstagioDesenvolvimento().equals(estagioDesenvolvimentoRepository.getEstagioDesenvolvimentoByEnum(EstagioDesenvolvimentoEnum.TO_VERIFY))) {
            return "A US não se encontra no estágio de desenvolvimento 'To Verify'";
        }

        return mudaStatus(mudaStatus, EstagioDesenvolvimentoEnum.DONE);
    }
}
