package com.psoft.scrumboard.model;

import com.psoft.scrumboard.model.enums.EstagioDesenvolvimentoEnum;
import com.psoft.scrumboard.model.estagiodesenvolvimento.EstagioDesenvolvimento;
import com.psoft.scrumboard.repository.IntegranteRepository;
import com.psoft.scrumboard.repository.TaskRepository;

import java.util.Collection;

public class UserStory {
	
    private Integer id;
    
    private String titulo;
    
    private String descricao;
    
    private EstagioDesenvolvimento estagioDesenvolvimento;
    
    private IntegranteRepository responsaveis;
    private TaskRepository tasks;

    public UserStory(Integer id, String titulo, String descricao, EstagioDesenvolvimento estagioDesenvolvimento) {
        this.id = id;
        this.titulo = titulo;
        this.descricao = descricao;
        this.estagioDesenvolvimento = estagioDesenvolvimento;
        this.responsaveis = new IntegranteRepository();
        this.tasks = new TaskRepository();
    }

    public Integer getId() {
    	return this.id;
    }
    
    public String getTitulo() {
        return titulo;
    }
    
    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public EstagioDesenvolvimentoEnum getEstagioDesenvolvimento() {
        return this.estagioDesenvolvimento.getTipo();
    }

    public void setEstagioDesenvolvimentoEnum (EstagioDesenvolvimento estagioDesenvolvimento) {
        this.estagioDesenvolvimento = estagioDesenvolvimento;
    }
    
    public IntegranteRepository getResponsaveis() {
    	return this.responsaveis;
    }

    public TaskRepository getTasks() {
        return tasks;
    }

    private StringBuilder listaTasksFormatada() {
        Collection<Task> listaDeTasks = this.getTasks().getAllTasksByIdUserStory(this.getId());
        StringBuilder listaDeTasksFormatada = new StringBuilder();
        for(Task task: listaDeTasks) {
            listaDeTasksFormatada.append(task.getTitulo())
                    .append(" - ").append(task.getDescricao())
                    .append(" - ").append(task.getStatus())
                    .append("\n");
        }
        return listaDeTasksFormatada;
    }

    public String toString() {
        return "Informações da UserStory '" + this.titulo + "' - US" + this.getId() + "\n"
                + "Descrição: " + this.getDescricao() + "\n"
                + "Estágio de desenvolvimento: " + getEstagioDesenvolvimento() + "\n"
                + "Tasks: \n" + this.listaTasksFormatada();
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

}
