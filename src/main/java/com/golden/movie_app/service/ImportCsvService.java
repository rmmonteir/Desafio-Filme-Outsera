package com.golden.movie_app.service;

import com.golden.movie_app.entity.Movie;
import com.golden.movie_app.exception.ProcessingException;
import com.golden.movie_app.repository.MovieRepository;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.exceptions.CsvValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.MessageSource;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.golden.movie_app.util.Contants;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

@Service
public class ImportCsvService {

    @Autowired
    private MovieRepository movieRepository;
    @Autowired
    private MessageSource messageSource;

    @EventListener(ApplicationReadyEvent.class)
    public void loadCsvData() {
        String csvFile = Contants.CAMINHO_CSV;
        csvLoadProcessing(csvFile);
    }

    public void csvLoadProcessing(MultipartFile arquivoCsv) {
        try (Reader reader = new InputStreamReader(arquivoCsv.getInputStream())) {
            movieRepository.deleteAll();
            csvLoadProcessing(reader);
        } catch (IOException e) {
            throw new ProcessingException(messageSource.getMessage("error.csv.api.read",new Object[]{e.getMessage()}, null));
        }
    }

    private void csvLoadProcessing(String caminhoArquivo) {
        try (Reader reader = new java.io.FileReader(caminhoArquivo)) {
            csvLoadProcessing(reader);
        } catch (IOException e) {
            throw new ProcessingException(messageSource.getMessage("error.csv.read",new Object[]{e.getMessage()}, null));
        }
    }

    private void csvLoadProcessing(Reader reader) {
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
                        throw new ProcessingException(messageSource.getMessage("error.csv.line",null, null));
                    }

                    Movie csvFileEntity = importarCsvParaObjeto(line);
                    if (csvFileEntity != null) {
                        validRecords.add(csvFileEntity);
                    }
                } catch (IllegalArgumentException e) {
                    throw new ProcessingException(messageSource.getMessage("error.csv.line.process",new Object[]{String.join(";", line)}, null));
                }
            }

            movieRepository.saveAll(validRecords);

        } catch (IOException | CsvValidationException e) {
            throw new ProcessingException(messageSource.getMessage("error.csv.file.process",new Object[]{e.getMessage()}, null));
        }
    }

    private Movie importarCsvParaObjeto(String[] linhaCsv) {
        try {
            int releaseYear = Integer.parseInt(linhaCsv[0]);
            String title = linhaCsv[1];
            if (title == null || title.isEmpty()) {
                throw new ProcessingException(messageSource.getMessage("error.invalid.title",null, null));
            }
            String studio = linhaCsv[2];
            if (studio == null || studio.isEmpty()) {
                throw new ProcessingException(messageSource.getMessage("error.invalid.studios",null, null));
            }
            String produtor = linhaCsv[3];
            if (produtor == null || produtor.isEmpty()) {
                throw new ProcessingException(messageSource.getMessage("error.invalid.producers",null, null));
            }
            Integer winner = linhaCsv.length > 4 ? ("yes".equalsIgnoreCase(linhaCsv[4]) ? 1 : 0) : 0;

            return new Movie(releaseYear, title, studio, produtor, winner);
        } catch (NumberFormatException e) {
            throw new ProcessingException(messageSource.getMessage("error.invalid.year",new Object[]{linhaCsv[0]}, null));
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new ProcessingException(messageSource.getMessage("error.malformed.line",null, null));
        }
    }
}