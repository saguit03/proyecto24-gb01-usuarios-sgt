package es.unex.asee.gb01.contents.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.unex.asee.gb01.contents.clients.ContentsClient;
import es.unex.asee.gb01.contents.dto.CategoryDTO;
import es.unex.asee.gb01.contents.dto.MovieDTO;
import es.unex.asee.gb01.contents.dto.SeasonDTO;
import es.unex.asee.gb01.contents.dto.SeriesDTO;

@Service
public class ContentService {

    @Autowired private final ContentsClient contentsClient;
    
    public ContentService(ContentsClient contentsClient) {
        this.contentsClient = contentsClient;
    }

    public MovieDTO getMovie(int movieId) {
        return contentsClient.getMovie(movieId);
    }

    public CategoryDTO fetchCategory(int categoryId) {
        return contentsClient.getCategory(categoryId);
    }

    public SeriesDTO fetchSeries(int seriesId) {
        return contentsClient.getSeries(seriesId);
    }

    public SeasonDTO fetchSeason(int seasonId) {
        return contentsClient.getSeason(seasonId);
    }
}
