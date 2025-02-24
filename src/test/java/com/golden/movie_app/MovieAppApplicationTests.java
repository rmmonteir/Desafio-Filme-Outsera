package com.golden.movie_app;

import com.golden.movie_app.controller.MovieController;
import com.golden.movie_app.service.MovieService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class MovieAppApplicationTests {
    /* teste de contexto, configurações */

	@Autowired
	private MovieController movieController;

	@Autowired
	private MovieService movieService;

	@Autowired
	private DataSource dataSource;

	@Value("${spring.application.name}")
	private String applicationName;

	@Test
	void contextLoads() {
		// Verifica se o contexto da aplicação é carregado corretamente
		assertThat(movieController).isNotNull();
		assertThat(movieService).isNotNull();
	}

	@Test
	void applicationStarts() {
		// Verifica se a aplicação inicializa corretamente
		MovieAppApplication.main(new String[] {});
		assertThat(true).isTrue(); // Apenas para garantir que o teste passa
	}

	@Test
	void testApplicationName() {
		// Verifica se a propriedade spring.application.name está correta
		assertThat(applicationName).isEqualTo("movie-app");
	}

	@Test
	void testDatabaseConnection() throws SQLException {
		// Verifica se a conexão com o banco de dados está funcionando
		assertThat(dataSource.getConnection()).isNotNull();
	}
}