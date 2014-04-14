/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ehsavoie.moviebuddies.model;

import io.undertow.server.HttpServerExchange;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

/**
 *
 * @author Emmanuel Hugonnet (ehsavoie) <emmanuel.hugonnet@gmail.com>
 */
public class SearchMoviesByTitle implements Runnable {

    private final HttpServerExchange exchange;
    private final Pattern title;
    private final List<Movie> allMovies;
    private final int limit;

    public SearchMoviesByTitle(HttpServerExchange exchange, String title, List<Movie> allMovies, int limit) {
        this.exchange = exchange;
        this.title = Pattern.compile(title.toLowerCase());
        this.allMovies = allMovies;
        this.limit = limit;
    }

    @Override
    public void run() {
        List<String> result = new LinkedList<>();
        int count = 0;
        for (Movie movie : allMovies) {
            if (isLimit(count, limit)) {
                break;
            }
            if (title.matcher(movie.title).find()) {
                count++;
                result.add(movie.toString());
            }
        }
        exchange.getResponseSender().send("[" + String.join(", ", result) + "]");
        exchange.endExchange();
    }

    protected boolean isLimit(int count, int limit) {
        return limit > 0 && count >= limit;
    }

}
