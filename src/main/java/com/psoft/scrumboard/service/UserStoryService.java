package com.psoft.scrumboard.service;

import com.psoft.scrumboard.dto.AtribuiUserStoryDTO;
import com.psoft.scrumboard.dto.MudaStatusDTO;
import com.psoft.scrumboard.dto.UserStoryDTO;
import com.psoft.scrumboard.exception.*;
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
    private UserStoryRepository userStoryRepository;

    @Autowired
    private ProjetoService projetoService;

    public String criaUserStory(Integer projectKey, UserStoryDTO userStoryDTO) throws UserStoryAlreadyExistsException, ProjetoNotFoundException {

        if (!this.projetoRepository.containsProjectKey(projectKey)) {
            throw new ProjetoNotFoundException("Projeto não está cadastrado no sistema - nome inválido.");
        } else if  (this.contemUserStory(projectKey, userStoryDTO.getId())) {
            throw new UserStoryAlreadyExistsException("UserStory já cadastrada no projeto - número não disponível");
        }

        EstagioDesenvolvimento estagioDesenvolvimento = this.estagioDesenvolvimentoRepository
                .getEstagioDesenvolvimentoByEnum(EstagioDesenvolvimentoEnum.TO_DO);
    			
    	UserStory userStory = new UserStory(userStoryDTO.getId(), userStoryDTO.getTitulo(),
                userStoryDTO.getDescricao(),
                estagioDesenvolvimento);
    	
    	Projeto projeto = this.projetoRepository.getProjeto(projectKey);

        projeto.getUserStoryRepository().addUserStory(userStory);

        return userStory.getTitulo();
    }

    private boolean contemUserStory(Integer projectKey, Integer idUserStory) {
        if (this.projetoRepository.containsProjectKey(projectKey))
        	return this.projetoRepository.getProjeto(projectKey).getUserStoryRepository().containsUserStory(idUserStory);

        return false;
    }

    public String getInfoUserStory(Integer projectKey, Integer idUserStory) throws UserStoryNotFoundException, ProjetoNotFoundException {
        if (!this.projetoRepository.containsProjectKey(projectKey)) {
            throw new ProjetoNotFoundException("Projeto não está cadastrado no sistema - nome inválido.");
        } else if  (!this.contemUserStory(projectKey, idUserStory)) {
            throw new UserStoryNotFoundException("UserStory não encontrada no projeto.");
        }

        return this.projetoRepository.getProjeto(projectKey).getUserStoryRepository().getUserStory(idUserStory).toString();
    }

    public String updateInfoUserStory(Integer projectKey, UserStoryDTO userStoryDTO) throws UserStoryNotFoundException {

        if (!contemUserStory(projectKey, userStoryDTO.getId())) throw new UserStoryNotFoundException("UserStory não encontrada no projeto.");

        UserStory userStory = this.projetoRepository.getProjeto(projectKey).getUserStoryRepository().getUserStory(userStoryDTO.getId());

        userStory.setDescricao(userStoryDTO.getDescricao());
        userStory.setTitulo(userStoryDTO.getTitulo());

        this.projetoRepository.getProjeto(projectKey).getUserStoryRepository().addUserStory(userStory);

        return "UserStory atualizado com titulo '" + userStory.getTitulo() + "'";

    }

    public String deletaUserStory(Integer projectKey, Integer idUserStory) throws UserStoryNotFoundException, ProjetoNotFoundException {

        if (!this.projetoRepository.containsProjectKey(projectKey)) {
            throw new ProjetoNotFoundException("Projeto não está cadastrado no sistema - nome inválido.");
        } else if  (!this.contemUserStory(projectKey, idUserStory)) {
            throw new UserStoryNotFoundException("UserStory não encontrada no projeto.");
        }

        UserStoryRepository userStories = this.projetoRepository.getProjeto(projectKey).getUserStoryRepository();
    	String titulo = userStories.getUserStory(idUserStory).getTitulo();
    	userStories.delUserStory(idUserStory);
    	
    	return "UserStory com titulo '" + titulo + "' removida";
    }
    
    private boolean usuarioTemPapelPermitido(Integrante integrante) {
    	
    	return (integrante.getPapel().getTipo().equals(PapelEnum.PESQUISADOR)
    			|| integrante.getPapel().getTipo().equals(PapelEnum.DESENVOLVEDOR)
    			|| integrante.getPapel().getTipo().equals(PapelEnum.ESTAGIARIO));
    }
    
    public String atribuiUsuarioUserStory(AtribuiUserStoryDTO atribuiUserStory)
            throws ProjetoNotFoundException, UserStoryNotFoundException, UsuarioNotFoundException {
        Integer projectKey = atribuiUserStory.getProjectKey();
        Integer userStoryId = atribuiUserStory.getIdUserStory();
        String username = atribuiUserStory.getUsername();

        if (!this.projetoRepository.containsProjectKey(projectKey)) {
            throw new ProjetoNotFoundException("Projeto não está cadastrado no sistema - nome inválido.");
        } else if  (!this.contemUserStory(projectKey, userStoryId)) {
            throw new UserStoryNotFoundException("UserStory não encontrada no projeto.");
        } else if (!(this.projetoService.contemIntegrante(projectKey, username))) {
            throw new UsuarioNotFoundException("Usuário não é integrante deste projeto");
        }

<<<<<<< HEAD
        if (!(contemUserStory(projetoKey, userStoryId))) {
            return "UserStory não está cadastrada neste projeto";
        }

        if (!(this.projetoService.contemIntegrante(projetoKey, username))) {
            return "Usuário não é integrante deste projeto";
        }
        
        Integrante integrante = this.projetoRepository.getProjeto(projetoKey)
=======
        Integrante integrante = this.projetoRepository.getProjeto(projectKey)
>>>>>>> 062e10483a1c75ae5c75017c18cb1cfca2658cbe
                .getIntegranteRepository()
                .getIntegrante(username);

        if (!usuarioTemPapelPermitido(integrante))
            return "Usuário não possui um tipo de papel permitido";

        this.projetoRepository.getProjeto(projectKey)
                .getUserStoryRepository()
                .getUserStory(userStoryId)
                .getResponsaveis()
                .addIntegrante(integrante);

        String info = this.mudaStatusToDoParaWorkInProgress(new MudaStatusDTO(projectKey, userStoryId, username));

        return integrante.getUsuario().getUsername() + " recebeu a atribuição com sucesso!";
    }

    public String scrumMasterAtribuiUsuarioUserStory(AtribuiUserStoryDTO atribuiUserStory, String userStoryName)
            throws ProjetoNotFoundException, UserStoryNotFoundException, UsuarioNotFoundException, UsuarioNotAllowedException {

        Integer projectKey = atribuiUserStory.getProjectKey();
        Integer userStoryId = atribuiUserStory.getIdUserStory();
        String username = atribuiUserStory.getUsername();

        if (!this.projetoRepository.containsProjectKey(projectKey)) {
            throw new ProjetoNotFoundException("Projeto não está cadastrado no sistema - nome inválido.");
        } else if  (!this.contemUserStory(projectKey, userStoryId)) {
            throw new UserStoryNotFoundException("UserStory não encontrada no projeto.");
        } else if (!(this.projetoService.contemIntegrante(projectKey, username))) {
            throw new UsuarioNotFoundException("Usuário não é integrante deste projeto");
        } else if (!(this.projetoService.getScrumMasterName(projectKey).equals(userStoryName))) {
            throw new UsuarioNotAllowedException("O Scrum Master informado não possui autorização para atribuir User Storys aos integrantes desse projeto");
        }

        this.mudaStatusToDoParaWorkInProgress(new MudaStatusDTO(projectKey, userStoryId, username));

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

        if (!(this.projetoService.getScrumMasterName(mudaStatus.getProjectKey()).equals(mudaStatus.getUsername())
                || us.getResponsaveis().containsUsername(mudaStatus.getUsername()))) {
            return "Username informado não possui autorização para mudar status nesse projeto.";
        }
        EstagioDesenvolvimento statusAtual = this.estagioDesenvolvimentoRepository
                .getEstagioDesenvolvimentoByEnum(us.getEstagioDesenvolvimento());

        if (!statusAtual.equals(estagioDesenvolvimentoRepository
                .getEstagioDesenvolvimentoByEnum(EstagioDesenvolvimentoEnum.WORK_IN_PROGRESS))) {
            return "A US não se encontra no estágio de desenvolvimento 'work in progress'";
        }

        return this.mudaStatus(mudaStatus, EstagioDesenvolvimentoEnum.TO_VERIFY);
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

        if (!(this.projetoService.getScrumMasterName(mudaStatus.getProjectKey()).equals(mudaStatus.getUsername()))) {
            return "O Scrum Master informado não possui autorização para mudar status nesse Projeto.";
        }
        EstagioDesenvolvimento statusAtual = this.estagioDesenvolvimentoRepository.getEstagioDesenvolvimentoByEnum(us.getEstagioDesenvolvimento());

        if (!statusAtual.equals(estagioDesenvolvimentoRepository.getEstagioDesenvolvimentoByEnum(EstagioDesenvolvimentoEnum.TO_VERIFY))) {
            return "A US não se encontra no estágio de desenvolvimento 'To Verify'";
        }

        return this.mudaStatus(mudaStatus, EstagioDesenvolvimentoEnum.DONE);
    }

    public String mudaStatusToDoParaWorkInProgress(MudaStatusDTO mudaStatus) {
        if (!(this.projetoService.contemProjectKey(mudaStatus.getProjectKey()))) {
            return "Projeto não está cadastrado no sistema - nome inválido";
        }

        if (!(contemUserStory(mudaStatus.getProjectKey(), mudaStatus.getIdUserStory()))) {
            return "UserStory não está cadastrada neste projeto";
        }

        Projeto projeto = this.projetoRepository.getProjeto(mudaStatus.getProjectKey());
        UserStory us = projeto.getUserStoryRepository().getUserStory(mudaStatus.getIdUserStory());

        EstagioDesenvolvimento statusAtual = this.estagioDesenvolvimentoRepository.getEstagioDesenvolvimentoByEnum(us.getEstagioDesenvolvimento());

        if (!statusAtual.equals(estagioDesenvolvimentoRepository.getEstagioDesenvolvimentoByEnum(EstagioDesenvolvimentoEnum.TO_DO))) {
            return "A US não se encontra no estágio de desenvolvimento 'To Do'";
        }

        return this.mudaStatus(mudaStatus, EstagioDesenvolvimentoEnum.WORK_IN_PROGRESS);
    }

}
