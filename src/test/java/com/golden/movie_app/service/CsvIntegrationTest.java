package com.golden.movie_app.service;

import com.golden.movie_app.dto.AwardIntervalResponse;
import com.golden.movie_app.entity.Movie;
import com.golden.movie_app.repository.MovieRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class CsvIntegrationTest {

    @Autowired
    private ImportCsvService importCsvService;

    @Autowired
    private MovieService movieService;

    @Autowired
    private MovieRepository movieRepository;

    @BeforeEach
    void setUp() {
        movieRepository.deleteAll(); // Limpa o banco de dados antes de cada teste
    }

    @Test
    void testApiResponseMatchesCsvFile() throws IOException {
        // Carrega o arquivo CSV local
        ClassPathResource resource = new ClassPathResource("movielist.csv");
        MultipartFile file = new MockMultipartFile("file", resource.getFilename(), "text/csv", Files.readAllBytes(resource.getFile().toPath()));

        // Verifica a quantidade de colunas no arquivo CSV padrao
        validateCsvColumns(file);
        // Salva dados do arquivo padrao na base h2
        importCsvService.csvLoadProcessing(file);

        // Verifica se os dados foram carregados corretamente pela quantidade
        List<Movie> movies = movieRepository.findAll();
        assertEquals(206, movies.size()); // Verifica se todos os filmes foram carregados

        // Verifica os intervalos de premiação
        AwardIntervalResponse response = movieService.getAwardIntervals();

        // Verifica o intervalo mínimo
        assertEquals(1, response.getMin().size());
        assertEquals("Joel Silver", response.getMin().get(0).getProducer());
        assertEquals(1, response.getMin().get(0).getInterval());
        assertEquals(1990, response.getMin().get(0).getPreviousWin());
        assertEquals(1991, response.getMin().get(0).getFollowingWin());

        // Verifica o intervalo máximo
        assertEquals(1, response.getMax().size());
        assertEquals("Matthew Vaughn", response.getMax().get(0).getProducer());
        assertEquals(13, response.getMax().get(0).getInterval());
        assertEquals(2002, response.getMax().get(0).getPreviousWin());
        assertEquals(2015, response.getMax().get(0).getFollowingWin());
    }
    private void validateCsvColumns(MultipartFile file) throws IOException {
        // Lê apenas a primeira linha do arquivo CSV e verifica se tem exatamente 5 colunas
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String headerLine = reader.readLine(); // Lê a primeira linha (cabeçalho)
            assertNotNull(headerLine, "O arquivo CSV está vazio.");

            String[] columns = headerLine.split(";");
            assertEquals(5, columns.length, "O cabeçalho do arquivo CSV deve ter exatamente 5 colunas.");
        }
    }

}