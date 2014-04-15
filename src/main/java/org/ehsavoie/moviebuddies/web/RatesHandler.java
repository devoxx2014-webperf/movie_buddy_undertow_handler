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
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import static org.ehsavoie.moviebuddies.web.Movie.findMovieById;
import static org.ehsavoie.moviebuddies.web.StartMovieBuddy.MYAPP;
import static org.ehsavoie.moviebuddies.web.User.findUserById;

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
         User user = findUserById(Integer.parseInt(params[0]), users);
        if (user == null) {
            exchange.setResponseCode(StatusCodes.NOT_FOUND);
        } else {
            List<String> result = new LinkedList<>();
            if (user.rates != null) {
                for (Map.Entry<Movie, Integer> rate : user.rates.entrySet()) {
                    result.add("'" + rate.getKey().id + "':" + rate.getValue());
                }
            }
            exchange.getResponseSender().send("[" + String.join(", ", result) + "]");
        }
        exchange.endExchange();
    }

    protected void doPost(HttpServerExchange exchange) throws Exception {
        exchange.startBlocking();
        List<JsonItem> items = JsonLoader.load(exchange.getInputStream());
        exchange.setResponseCode(StatusCodes.MOVED_PERMENANTLY);
        exchange.getResponseHeaders().put(Headers.LOCATION, "http://" + exchange.getHostAndPort()
                + MYAPP + rate(items.get(0)));
        exchange.endExchange();
    }

    private String rate(JsonItem item) {
        User user = findUserById(item.getInt("userId"), users);
        Movie movie = findMovieById(item.getInt("movieId"), movies);
        if (user.rates == null) {
            user.rates = new HashMap<>();
        }
        user.rates.put(movie, item.getInt("rate"));
        return "/rates/" + user.id;
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
