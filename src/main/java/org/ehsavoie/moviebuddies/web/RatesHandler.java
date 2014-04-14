/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ehsavoie.moviebuddies.web;

import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;
import io.undertow.util.Methods;
import io.undertow.util.StatusCodes;
import java.util.List;
import org.ehsavoie.moviebuddies.model.JsonItem;
import org.ehsavoie.moviebuddies.model.JsonLoader;
import org.ehsavoie.moviebuddies.model.Movie;
import org.ehsavoie.moviebuddies.model.RateMovie;
import org.ehsavoie.moviebuddies.model.User;
import org.ehsavoie.moviebuddies.model.UserRatesById;
import static org.ehsavoie.moviebuddies.web.StartMovieBuddy.MYAPP;

/**
 *
 * @author Emmanuel Hugonnet (ehsavoie) <emmanuel.hugonnet@gmail.com>
 */
public class RatesHandler implements HttpHandler {

    private static final String PREFIX = MYAPP + "/rates";

    private final List<Movie> movies;
    private final List<User> users;

    public RatesHandler(List<Movie> movies, List<User> users) {
        this.movies = movies;
        this.users = users;
    }

    protected void doGet(HttpServerExchange exchange) throws Exception {
        final String[] params = URLParser.parse(PREFIX, exchange);
        exchange.dispatch(new UserRatesById(exchange, Integer.parseInt(params[0]), users));
    }

    protected void doPost(HttpServerExchange exchange) throws Exception {
        exchange.startBlocking();
        List<JsonItem> items = JsonLoader.load(exchange.getInputStream());
        exchange.setResponseCode(StatusCodes.FOUND);
        exchange.getResponseHeaders().put(Headers.LOCATION, "http://" + exchange.getHostAndPort()
                + MYAPP + new RateMovie(items.get(0), users, movies).rate());
        exchange.endExchange();
    }

    @Override
    public void handleRequest(HttpServerExchange exchange) throws Exception {
        if (Methods.POST.equals(exchange.getRequestMethod())) {
            doPost(exchange);
        } else {
            doGet(exchange);
        }
    }

}
