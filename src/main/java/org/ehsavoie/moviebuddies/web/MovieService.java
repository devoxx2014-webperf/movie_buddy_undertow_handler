/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ehsavoie.moviebuddies.web;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import static java.util.Collections.binarySearch;
import java.util.List;
import java.util.regex.Pattern;
import static java.util.stream.Collectors.joining;

/**
 *
 * @author Emmanuel Hugonnet (ehsavoie) <emmanuel.hugonnet@gmail.com>
 */
public class MovieService {

    public static final MovieService INSTANCE = new MovieService();
    private List<Movie> movies;

    private MovieService() {
        try (InputStream in = new BufferedInputStream(UserService.class.getClassLoader().getResourceAsStream("movies.json"))) {
            List<JsonItem> items = JsonLoader.load(in);
            movies = new ArrayList<>(items.size());
            items.stream().forEach((item) -> {
                movies.add(Movie.parse(item));
            });
        } catch (IOException ioex) {
            throw new RuntimeException(ioex);
        }
    }

    public String searchMovieByActor(String name, int limit) {
        Pattern pattern = Pattern.compile(name.toLowerCase());
        return movies.stream()
                .filter(movie -> pattern.matcher(movie.actors).find())
                .map(Movie::toString)
                .limit(limit)
                .collect(joining(",", "[", "]"));
    }

    public String searchMovieByTitle(String title, int limit) {
        Pattern pattern = Pattern.compile(title.toLowerCase());
        return movies.stream()
                .filter(movie -> pattern.matcher(movie.title).find())
                .map(Movie::toString)
                .limit(limit)
                .collect(joining(",", "[", "]"));
    }

    public String searchMovieByGenre(String genre, int limit) {
        Pattern pattern = Pattern.compile(genre.toLowerCase());
        return movies.stream()
                .filter(movie -> pattern.matcher(movie.genre).find())
                .map(Movie::toString)
                .limit(limit)
                .collect(joining(",", "[", "]"));
    }

    public Movie findMovieById(int id) {
        int index = binarySearch(movies, new Movie(id, "", "", "", ""));
        return (index < 0) ? null : movies.get(index);
    }
}
