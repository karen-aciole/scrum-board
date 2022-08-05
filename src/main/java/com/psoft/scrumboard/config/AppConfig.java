package com.psoft.scrumboard.config;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.psoft.scrumboard.repository.ProjetoRepository;
import com.psoft.scrumboard.repository.TaskRepository;
import com.psoft.scrumboard.repository.UserStoryRepository;
import com.psoft.scrumboard.repository.UsuarioRepository;
import com.psoft.scrumboard.repository.EstagioDesenvolvimentoRepository;
import com.psoft.scrumboard.repository.PapelRepository;

@Configuration
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class, DataSourceTransactionManagerAutoConfiguration.class, HibernateJpaAutoConfiguration.class})
public class AppConfig {
	
	@Bean
	public UsuarioRepository usuarioRepository() {
		return new UsuarioRepository();
	}
	
	@Bean
	public PapelRepository PapelRepository() {
		return new PapelRepository();
	}
	
	@Bean
	public ProjetoRepository projetoRepository() {
		return new ProjetoRepository();
	}
	
	@Bean
	public EstagioDesenvolvimentoRepository estagioDesenvolvimentoRepository() {
		return new EstagioDesenvolvimentoRepository();
	}
	
	@Bean
	public EstagioDesenvolvimentoRepository estagioDesenvolvimentoRepository() {
		return new EstagioDesenvolvimentoRepository();
	}
	
}
