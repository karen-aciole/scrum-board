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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;


@Service
public class UserStoryService {

    @Autowired
    private EstagioDesenvolvimentoRepository estagioDesenvolvimentoRepository;

    @Autowired
    private ProjetoRepository projetoRepository;

    @Autowired
    private ProjetoService projetoService;

    public String criaUserStory(Integer projectKey, UserStoryDTO userStoryDTO) throws UserStoryAlreadyExistsException, ProjetoNotFoundException {

        if (!this.projetoRepository.containsProjectKey(projectKey)) {
            throw new ProjetoNotFoundException("Projeto não está cadastrado no sistema - nome inválido.");
        } else if (this.contemUserStory(projectKey, userStoryDTO.getId())) {
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
        } else if (!this.contemUserStory(projectKey, idUserStory)) {
            throw new UserStoryNotFoundException("UserStory não encontrada no projeto.");
        }

        return this.projetoRepository.getProjeto(projectKey).getUserStoryRepository().getUserStory(idUserStory).toString();
    }

    public String updateInfoUserStory(Integer projectKey, String username, UserStoryDTO userStoryDTO)
            throws UserStoryNotFoundException, UsuarioNotAllowedException, ProjetoNotFoundException {

        if (!this.projetoRepository.containsProjectKey(projectKey)) {
            throw new ProjetoNotFoundException("Projeto não cadastrado no sistema - nome inválido.");
        } else if (!contemUserStory(projectKey, userStoryDTO.getId())) {
            throw new UserStoryNotFoundException("UserStory não encontrada no projeto.");
        } else if (!this.projetoService.contemIntegrante(projectKey, username)) {
            throw new UsuarioNotAllowedException("Usuário não é integrante deste projeto.");
        }

        Projeto projeto = this.projetoRepository.getProjeto(projectKey);
        UserStory us = projeto.getUserStoryRepository().getUserStory(userStoryDTO.getId());
        boolean scrumMaster = projetoService.getIntegranteByUserName(projectKey, username).getPapel().getTipo().equals(PapelEnum.SCRUM_MASTER);
        boolean productOwner = projetoService.getIntegranteByUserName(projectKey, username).getPapel().getTipo().equals(PapelEnum.PRODUCT_OWNER);

        if (!(integranteParticipaDeUserStory(projectKey, userStoryDTO.getId(), username) || productOwner || scrumMaster)) {
            throw new UsuarioNotAllowedException("Usuário não tem permissão para atualizar esta UserStory.");
        }

        us.setDescricao(userStoryDTO.getDescricao());
        us.setTitulo(userStoryDTO.getTitulo());

        this.projetoRepository.getProjeto(projectKey).getUserStoryRepository().addUserStory(us);

        return "UserStory atualizado com titulo '" + us.getTitulo() + "'";

    }

    public String deletaUserStory(Integer projectKey, Integer idUserStory, String username) throws UserStoryNotFoundException, ProjetoNotFoundException, UsuarioNotAllowedException {

        if (!this.projetoRepository.containsProjectKey(projectKey)) {
            throw new ProjetoNotFoundException("Projeto não está cadastrado no sistema - nome inválido.");
        } else if (!this.contemUserStory(projectKey, idUserStory)) {
            throw new UserStoryNotFoundException("UserStory não encontrada no projeto.");
        } else if (!this.projetoService.contemIntegrante(projectKey, username)) {
            throw new UsuarioNotAllowedException("Usuário não é integrante deste projeto.");
        }

        Projeto projeto = this.projetoRepository.getProjeto(projectKey);
        UserStory us = projeto.getUserStoryRepository().getUserStory(idUserStory);
        boolean scrumMaster = projetoService.getIntegranteByUserName(projectKey, username).getPapel().getTipo().equals(PapelEnum.SCRUM_MASTER);
        boolean productOwner = projetoService.getIntegranteByUserName(projectKey, username).getPapel().getTipo().equals(PapelEnum.PRODUCT_OWNER);

        if (!(integranteParticipaDeUserStory(projectKey, idUserStory, username) || productOwner || scrumMaster)) {
            throw new UsuarioNotAllowedException("Usuário não tem permissão para atualizar esta UserStory.");
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
            throws ProjetoNotFoundException, UserStoryNotFoundException, UsuarioNotFoundException, UsuarioAlreadyExistsException, StatusException {

        Integer projectKey = atribuiUserStory.getProjectKey();
        Integer userStoryId = atribuiUserStory.getIdUserStory();
        String username = atribuiUserStory.getUsername();

        if (!this.projetoRepository.containsProjectKey(projectKey)) {
            throw new ProjetoNotFoundException("Projeto não está cadastrado no sistema - nome inválido.");
        } else if (!this.contemUserStory(projectKey, userStoryId)) {
            throw new UserStoryNotFoundException("UserStory não encontrada no projeto.");
        } else if (!(this.projetoService.contemIntegrante(projectKey, username))) {
            throw new UsuarioNotFoundException("Usuário não é integrante deste projeto");
        } else if (integranteParticipaDeUserStory(projectKey, userStoryId, username)) {
            throw new UsuarioAlreadyExistsException("Usuário já participa desta User Story");
        } else if ((getUserStoryState(projectKey, userStoryId).equals(EstagioDesenvolvimentoEnum.DONE))) {
            throw new StatusException("User Story já está finalizada");
        }

        Integrante integrante = this.projetoRepository.getProjeto(projectKey)
                .getIntegranteRepository()
                .getIntegrante(username);

        if (!usuarioTemPapelPermitido(integrante))
            return "Usuário não possui um tipo de papel permitido";

        this.projetoRepository.getProjeto(projectKey)
                .getUserStoryRepository()
                .getUserStory(userStoryId)
                .getResponsaveis()
                .addIntegrante(integrante);

        this.mudaStatusToDoParaWorkInProgress(new MudaStatusDTO(projectKey, userStoryId, username));

        return integrante.getUsuario().getUsername() + " recebeu a atribuição com sucesso!";
    }

    public String scrumMasterAtribuiUsuarioUserStory(AtribuiUserStoryDTO atribuiUserStory, String scrumMaster)
            throws ProjetoNotFoundException, UserStoryNotFoundException, UsuarioNotFoundException, UsuarioNotAllowedException, UsuarioAlreadyExistsException, StatusException {

        Integer projectKey = atribuiUserStory.getProjectKey();
        Integer userStoryId = atribuiUserStory.getIdUserStory();
        String username = atribuiUserStory.getUsername();

        if (!this.projetoRepository.containsProjectKey(projectKey)) {
            throw new ProjetoNotFoundException("Projeto não está cadastrado no sistema - nome inválido.");
        } else if (!this.contemUserStory(projectKey, userStoryId)) {
            throw new UserStoryNotFoundException("UserStory não encontrada no projeto.");
        } else if (!(this.projetoService.contemIntegrante(projectKey, username))) {
            throw new UsuarioNotFoundException("Usuário não é integrante deste projeto");
        } else if (!(this.projetoService.getScrumMasterName(projectKey).equals(scrumMaster))) {
            throw new UsuarioNotAllowedException("O Scrum Master informado não possui autorização para atribuir User Storys aos integrantes desse projeto");
        } else if (integranteParticipaDeUserStory(projectKey, userStoryId, username)) {
            throw new UsuarioAlreadyExistsException("Usuário já participa desta User Story");
        } else if ((getUserStoryState(projectKey, userStoryId).equals(EstagioDesenvolvimentoEnum.DONE))) {
            throw new StatusException("User Story já está finalizada");
        }

        this.mudaStatusToDoParaWorkInProgress(new MudaStatusDTO(projectKey, userStoryId, username));

        return atribuiUsuarioUserStory(atribuiUserStory);
    }

    public void mudaStatusWorkInProgressParaToVerify(MudaStatusDTO mudaStatus)
            throws ProjetoNotFoundException, UserStoryNotFoundException, UsuarioNotFoundException, UsuarioNotAllowedException, StatusException {

        Projeto projeto = this.projetoRepository.getProjeto(mudaStatus.getProjectKey());
        UserStory us = projeto.getUserStoryRepository().getUserStory(mudaStatus.getIdUserStory());


        if (!this.projetoRepository.containsProjectKey(mudaStatus.getProjectKey())) {
            throw new ProjetoNotFoundException("Projeto não está cadastrado no sistema - nome inválido.");
        } else if (!this.contemUserStory(mudaStatus.getProjectKey(), mudaStatus.getIdUserStory())) {
            throw new UserStoryNotFoundException("UserStory não encontrada no projeto.");
        } else if (!(this.projetoService.getScrumMasterName(mudaStatus.getProjectKey()).equals(mudaStatus.getUsername())
                || us.getResponsaveis().containsUsername(mudaStatus.getUsername()))) {
            throw new UsuarioNotAllowedException("Username informado não possui autorização para mudar status nesse projeto.");
        }

        EstagioDesenvolvimento statusAtual = this.estagioDesenvolvimentoRepository
                .getEstagioDesenvolvimentoByEnum(us.getEstagioDesenvolvimento());

        if (!statusAtual.equals(estagioDesenvolvimentoRepository
                .getEstagioDesenvolvimentoByEnum(EstagioDesenvolvimentoEnum.WORK_IN_PROGRESS))) {
            throw new StatusException("A US não se encontra no estágio de desenvolvimento 'work in progress'");
        }

        this.mudaStatus(mudaStatus, EstagioDesenvolvimentoEnum.TO_VERIFY);
    }

    private EstagioDesenvolvimentoEnum getUserStoryState(Integer projectKey, Integer userStoryId) {

        return this.projetoRepository.getProjeto(projectKey)
                .getUserStoryRepository()
                .getUserStory(userStoryId)
                .getEstagioDesenvolvimento();
    }

    private void mudaStatus(MudaStatusDTO mudaStatus, EstagioDesenvolvimentoEnum estagio) {

        Projeto projeto = this.projetoRepository.getProjeto(mudaStatus.getProjectKey());
        UserStory us = projeto.getUserStoryRepository().getUserStory(mudaStatus.getIdUserStory());

        EstagioDesenvolvimento estagioDesenvolvimento = this.estagioDesenvolvimentoRepository
                .getEstagioDesenvolvimentoByEnum(estagio);

        us.setEstagioDesenvolvimentoEnum(estagioDesenvolvimento);
    }

    public void mudaStatusToVerifyParaDone(MudaStatusDTO mudaStatus)
            throws ProjetoNotFoundException, UserStoryNotFoundException, UsuarioNotAllowedException, StatusException {

        if (!this.projetoRepository.containsProjectKey(mudaStatus.getProjectKey())) {
            throw new ProjetoNotFoundException("Projeto não está cadastrado no sistema - nome inválido.");
        } else if (!this.contemUserStory(mudaStatus.getProjectKey(), mudaStatus.getIdUserStory())) {
            throw new UserStoryNotFoundException("UserStory não encontrada no projeto.");
        } else if (!(this.projetoService.getScrumMasterName(mudaStatus.getProjectKey()).equals(mudaStatus.getUsername()))) {
            throw new UsuarioNotAllowedException("O Scrum Master informado não possui autorização para mudar status nesse projeto.");
        }

        Projeto projeto = this.projetoRepository.getProjeto(mudaStatus.getProjectKey());
        UserStory us = projeto.getUserStoryRepository().getUserStory(mudaStatus.getIdUserStory());

        EstagioDesenvolvimento statusAtual = this.estagioDesenvolvimentoRepository
                .getEstagioDesenvolvimentoByEnum(us.getEstagioDesenvolvimento());

        if (!statusAtual.equals(estagioDesenvolvimentoRepository.getEstagioDesenvolvimentoByEnum(EstagioDesenvolvimentoEnum.TO_VERIFY))) {
            throw new StatusException("A US não se encontra no estágio de desenvolvimento 'To Verify'");
        }

        this.mudaStatus(mudaStatus, EstagioDesenvolvimentoEnum.DONE);
    }

    private void mudaStatusToDoParaWorkInProgress(MudaStatusDTO mudaStatus) {

        Projeto projeto = this.projetoRepository.getProjeto(mudaStatus.getProjectKey());
        UserStory us = projeto.getUserStoryRepository().getUserStory(mudaStatus.getIdUserStory());

        this.estagioDesenvolvimentoRepository.getEstagioDesenvolvimentoByEnum(us.getEstagioDesenvolvimento());

        this.mudaStatus(mudaStatus, EstagioDesenvolvimentoEnum.WORK_IN_PROGRESS);
    }

    private boolean integranteParticipaDeUserStory(Integer projectKey, Integer userStoryId, String username) {
        Projeto projeto = this.projetoRepository.getProjeto(projectKey);
        UserStory us = projeto.getUserStoryRepository().getUserStory(userStoryId);
        return us.getResponsaveis().containsUsername(username);
    }

    public String listaRelatorioDeUsersStoriesDeUmUsuario(Integer projectKey, String username) throws ProjetoNotFoundException, UsuarioNotFoundException {

        if (!this.projetoRepository.containsProjectKey(projectKey)) {
            throw new ProjetoNotFoundException("Projeto não está cadastrado no sistema - nome inválido.");
        } else if (!(this.projetoService.contemIntegrante(projectKey, username))) {
            throw new UsuarioNotFoundException("Usuário não é integrante deste projeto");
        }

        int userStoriesTotal = getTotalDeUserStoriesByProject(projectKey);
        int userStoriesByUsuario = getTotalDeUserStoriesByIntegrante(projectKey, username);
        float percentualUserStoriesByUsuario = ((float) userStoriesByUsuario / userStoriesTotal) * 100;
        String percentualUserStoriesByUsuarioFormatado = String.format("%.2f", percentualUserStoriesByUsuario);

        int totalUserStoriesWorkInProgressByUsuario = getTotalUserStoriesFromUserByStatusWorkInProgress(projectKey, username);
        int totalUserStoriesToVerifyByUsuario = getTotalUserStoriesFromUserByStatusToVerify(projectKey, username);
        int totalUserStoriesDoneByUsuario = getTotalUserStoriesFromUserByStatusDone(projectKey, username);

        String percentualUserStoriesWorkInProgress = String.format("%.2f", ((float) totalUserStoriesWorkInProgressByUsuario / userStoriesByUsuario) * 100);
        String percentualUserStoriesToVerify = String.format("%.2f", ((float) totalUserStoriesToVerifyByUsuario / userStoriesByUsuario) * 100);
        String percentualUserStoriesDone = String.format("%.2f", ((float) totalUserStoriesDoneByUsuario / userStoriesByUsuario) * 100);


        if (percentualUserStoriesByUsuario == 0)
            return "Não há User Stories atribuídas para o usuário: " + username;

        return "Percentual de User Stories do usuário " + username + ": " + percentualUserStoriesByUsuarioFormatado + "%\n" +
                "Total de User Stories atribuídas ao usuário: " + userStoriesByUsuario + "/" + userStoriesTotal + "\n" +
                "Percentual de User Stories do usuário em cada estágio de desenvolvimento: \n" +
                "Work In Progress: " + percentualUserStoriesWorkInProgress + "% esse percentual representa um total de: " + totalUserStoriesWorkInProgressByUsuario + " User Storys\n" +
                "To Verify: " + percentualUserStoriesToVerify + "% esse percentual representa um total de: " + totalUserStoriesToVerifyByUsuario + " User Storys\n" +
                "Done: " + percentualUserStoriesDone + "% esse percentual representa um total de: " + totalUserStoriesDoneByUsuario + " User Storys\n";
    }

    private int getTotalUserStoriesFromUserByStatusWorkInProgress(Integer projectKey, String username) {
        return listAllUserStoriesFromUserByEstagioDesenvolvimento(projectKey, EstagioDesenvolvimentoEnum.WORK_IN_PROGRESS, username).size();
    }

    private int getTotalUserStoriesFromUserByStatusToVerify(Integer projectKey, String username) {
        return listAllUserStoriesFromUserByEstagioDesenvolvimento(projectKey, EstagioDesenvolvimentoEnum.TO_VERIFY, username).size();
    }

    private int getTotalUserStoriesFromUserByStatusDone(Integer projectKey, String username) {
        return listAllUserStoriesFromUserByEstagioDesenvolvimento(projectKey, EstagioDesenvolvimentoEnum.DONE, username).size();
    }

    private Collection<UserStory> listAllUserStoriesByEstagioDesenvolvimento(Integer projectKey, EstagioDesenvolvimentoEnum estagio) {
        List<UserStory> userStoriesByEstagioDesenvolvimento = new ArrayList<>();

        for (UserStory us : this.getUsersStoriesByProject(projectKey)) {
            if (us.getEstagioDesenvolvimento().equals(estagio))
                userStoriesByEstagioDesenvolvimento.add(us);
        }
        return userStoriesByEstagioDesenvolvimento;
    }


    private Collection<UserStory> listAllUserStoriesFromUserByEstagioDesenvolvimento(Integer projectKey, EstagioDesenvolvimentoEnum estagio, String username) {
        Collection<UserStory> listaDeUserStoriesPorStatus = listAllUserStoriesByEstagioDesenvolvimento(projectKey, estagio);
        Collection<UserStory> listaDeUserStoriesDoUsuarioPorStatus = new ArrayList<>();

        for (UserStory us : listaDeUserStoriesPorStatus) {
            if (us.getResponsaveis().containsUsername(username))
                listaDeUserStoriesDoUsuarioPorStatus.add(us);
        }
        return listaDeUserStoriesDoUsuarioPorStatus;
    }

    private Collection<String> listaIntegrantesDeUmaUserStory(Integer projectKey, Integer userStoryId) {
        return this.projetoRepository.getProjeto(projectKey)
                .getUserStoryRepository().getUserStory(userStoryId).getResponsaveis().getIntegrantes();
    }

    private Collection<UserStory> getUsersStoriesByProject(Integer projectKey) {
        return projetoRepository.getProjeto(projectKey).getUserStoryRepository().getAll();
    }

    private int getTotalDeUserStoriesByProject(Integer projectKey) {
        return projetoRepository.getProjeto(projectKey).getUserStoryRepository().getAll().size();
    }

    private int getTotalDeUserStoriesByIntegrante(Integer projectKey, String username) {
        Collection<UserStory> listaDeUserStories = getUsersStoriesByProject(projectKey);
        List<UserStory> listaDeUserStoriesDoUsuario = new ArrayList<>();

        for (UserStory userStory : listaDeUserStories) {
            if (listaIntegrantesDeUmaUserStory(projectKey, userStory.getId()).contains(username)) {
                listaDeUserStoriesDoUsuario.add(userStory);
            }
        }
        return listaDeUserStoriesDoUsuario.size();
    }


    public String listaRelatorioDeUsersStories(Integer projectKey, String username) throws ProjetoNotFoundException, UsuarioNotFoundException, UsuarioNotAllowedException {
        if (!this.projetoRepository.containsProjectKey(projectKey)) {
            throw new ProjetoNotFoundException("Projeto não está cadastrado no sistema - nome inválido.");
        } else if (!(this.projetoService.contemIntegrante(projectKey, username))) {
            throw new UsuarioNotFoundException("Usuário não é integrante deste projeto");
        } else if (!this.projetoService.getIntegranteByUserName(projectKey, username).getPapel().getTipo().equals(PapelEnum.PRODUCT_OWNER)) {
            throw new UsuarioNotAllowedException("Apenas Product Owners podem requisitar este relatório");
        }

        int userStoriesTotal = getTotalDeUserStoriesByProject(projectKey);

        int totalUserStoriesToDo = listAllUserStoriesByEstagioDesenvolvimento(projectKey, EstagioDesenvolvimentoEnum.TO_DO).size();
        int totalUserStoriesWorkInProgress = listAllUserStoriesByEstagioDesenvolvimento(projectKey, EstagioDesenvolvimentoEnum.WORK_IN_PROGRESS).size();
        int totalUserStoriesToVerify = listAllUserStoriesByEstagioDesenvolvimento(projectKey, EstagioDesenvolvimentoEnum.TO_VERIFY).size();
        int totalUserStoriesDone = listAllUserStoriesByEstagioDesenvolvimento(projectKey, EstagioDesenvolvimentoEnum.DONE).size();

        String percentualUserStoriesToDo = String.format("%.2f", ((float) totalUserStoriesToDo / userStoriesTotal) * 100);
        String percentualUserStoriesWorkInProgress = String.format("%.2f", ((float) totalUserStoriesWorkInProgress / userStoriesTotal) * 100);
        String percentualUserStoriesToVerify = String.format("%.2f", ((float) totalUserStoriesToVerify / userStoriesTotal) * 100);
        String percentualUserStoriesDone = String.format("%.2f", ((float) totalUserStoriesDone / userStoriesTotal) * 100);

        if (userStoriesTotal == 0)
            return "Não há User Stories atribuídas a esse projeto.";

        return "Percentual e total de User Stories em cada estágio de desenvolvimento: \n\n" +
                "To Do: " + percentualUserStoriesToDo + "% esse percentual representa um total de: " + totalUserStoriesToDo + " User Storys\n" +
                "Work In Progress: " + percentualUserStoriesWorkInProgress + "% esse percentual representa um total de: " + totalUserStoriesWorkInProgress + " User Storys\n" +
                "To Verify: " + percentualUserStoriesToVerify + "% esse percentual representa um total de: " + totalUserStoriesToVerify + " User Storys\n" +
                "Done: " + percentualUserStoriesDone + "% esse percentual representa um total de: " + totalUserStoriesDone + " User Storys\n";
    }

    public String listaRelatorioDeUsersStoriesDeUmProjeto(Integer projectKey, String username) throws ProjetoNotFoundException, UsuarioNotAllowedException, UsuarioNotFoundException {
        if (!this.projetoRepository.containsProjectKey(projectKey)) {
            throw new ProjetoNotFoundException("Projeto não está cadastrado no sistema - nome inválido.");
        } else if (!(this.projetoService.getScrumMasterName(projectKey).equals(username))) {
            throw new UsuarioNotAllowedException("Usuário não é o Scrum Master deste projeto");
        }

        String relatorio = "";

        for(String integrante: this.projetoRepository.getProjeto(projectKey).getIntegranteRepository().getIntegrantes()){
            relatorio += listaRelatorioDeUsersStoriesDeUmUsuario(projectKey, integrante) + "\n";
        }

        return relatorio;

    }
}
