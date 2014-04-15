/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ehsavoie.moviebuddies.web;

import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;
import java.util.List;
import static org.ehsavoie.moviebuddies.web.StartMovieBuddy.MYAPP;

/**
 *
 * @author Emmanuel Hugonnet (ehsavoie) <emmanuel.hugonnet@gmail.com>
 */
public class SearchMoviesHandler implements HttpHandler {
    /*
     POST /rates: enregistre un vote d'un utilisateur pour un film. Le body est formatté: {userId: u, movieId: m, rate: r}. renvoie vers /rates/:userId
     GET /rates/:userid: retourne les votes de userid
     GET /users/share/:userid1/:userid2: retourne la liste de films communs entre userid1 et userid2
     GET /users/distance/:userid1/:userid2: calcul de la distance entre userid1 et userid2. Remarque: le format de la réponse est de type chaine, je ne suis pas sur que cela soit confirme avec JSON

     */
    private static final String PREFIX = MYAPP + "/movies";
    private final List<Movie> movies;

    public SearchMoviesHandler(List<Movie> movies) {
        this.movies = movies;
    }

    @Override
    public void handleRequest(HttpServerExchange exchange) throws Exception {
        exchange.getResponseHeaders().add(Headers.CONTENT_TYPE, "application/json");
        final String[] params = URLParser.parse(PREFIX, exchange);
        final int limit = params.length > 3 ? Integer.parseInt(params[3]) : -1;
        switch (params[0]) {
            case "search":
                final String criteria = params.length > 1 ? params[1] : "";
                switch (criteria) {
                    case "title": {
                        exchange.dispatch(new SearchMoviesByTitle(exchange, params[2], movies, limit));
                    }
                    break;
                    case "actors": {
                        exchange.dispatch(new SearchMoviesByActors(exchange, params[2], movies, limit));
                    }
                    break;
                    case "genre": {
                        exchange.dispatch(new SearchMoviesByGenre(exchange, params[2], movies, limit));
                    }
                    break;
                    default: {
                        exchange.dispatch(new SearchAllMovies(exchange));
                    }
                    break;
                }
                break;
            default: {
                exchange.dispatch(new SearchAllMovies(exchange));
            }
            break;
        }
    }

    protected boolean isLimit(int count, int limit) {
        return limit > 0 && count >= limit;
    }
}
