package com.golden.filme_app.service;

import com.golden.filme_app.entity.Movie;
import com.golden.filme_app.exception.CsvProcessingException;
import com.golden.filme_app.repository.MovieRepository;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.exceptions.CsvValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

@Service
public class ImportCsvService {

    @Autowired
    private MovieRepository movieRepository;

    @EventListener(ApplicationReadyEvent.class)
    public void loadCsvData() {
        String csvFile = "src/main/resources/movielist.csv"; // Caminho do arquivo CSV local
        csvProcessing(csvFile);
    }

    public void csvProcessing(MultipartFile arquivoCsv) {
        try (Reader reader = new InputStreamReader(arquivoCsv.getInputStream())) {
            csvProcessing(reader);
        } catch (IOException e) {
            throw new CsvProcessingException("Erro ao ler o arquivo CSV via API: " + e.getMessage(), e);
        }
    }

    private void csvProcessing(String caminhoArquivo) {
        try (Reader reader = new java.io.FileReader(caminhoArquivo)) {
            csvProcessing(reader);
        } catch (IOException e) {
            throw new CsvProcessingException("Erro ao ler o arquivo CSV: " + e.getMessage(), e);
        }
    }

    private void csvProcessing(Reader reader) {
        CSVParser parser = new CSVParserBuilder()
                .withSeparator(';')
                .build();

        try (CSVReader csvReader = new CSVReaderBuilder(reader)
                .withCSVParser(parser)
                .build()) {

            List<Movie> validRecords = new ArrayList<>();
            String[] line;

            csvReader.readNext(); // Pular cabeçalho

            while ((line = csvReader.readNext()) != null) {
                try {
                    if (line.length < 5) {
                        throw new CsvProcessingException("Linha mal formatada: número insuficiente de colunas.");
                    }

                    Movie csvFileEntity = importarCsvParaObjeto(line);
                    if (csvFileEntity != null) {
                        validRecords.add(csvFileEntity);
                    }
                } catch (IllegalArgumentException e) {
                    throw new CsvProcessingException("Erro ao processar linha: " + String.join(";", line), e);
                }
            }

            movieRepository.saveAll(validRecords);

        } catch (IOException | CsvValidationException e) {
            throw new CsvProcessingException("Erro ao processar o arquivo CSV: " + e.getMessage(), e);
        }
    }

    private Movie importarCsvParaObjeto(String[] linhaCsv) {
        try {
            int releaseYear = Integer.parseInt(linhaCsv[0]);
            String title = linhaCsv[1];
            if (title == null || title.isEmpty()) {
                throw new IllegalArgumentException("Título inválido ou ausente.");
            }
            String studio = linhaCsv[2];
            if (studio == null || studio.isEmpty()) {
                throw new IllegalArgumentException("Estúdios inválidos ou ausentes.");
            }
            String produtor = linhaCsv[3];
            if (produtor == null || produtor.isEmpty()) {
                throw new IllegalArgumentException("Produtores inválidos ou ausentes.");
            }
            String ganhador = linhaCsv.length > 4 ? linhaCsv[4] : null;
            return new Movie(releaseYear, title, studio, produtor, ganhador);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Ano inválido: " + linhaCsv[0], e);
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new IllegalArgumentException("Linha mal formatada: numero insuficiente de colunas.", e);
        }
    }
}