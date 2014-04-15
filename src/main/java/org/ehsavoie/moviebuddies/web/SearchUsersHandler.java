/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ehsavoie.moviebuddies.web;

import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;
import io.undertow.util.StatusCodes;
import java.io.BufferedInputStream;
import java.io.InputStream;
import static java.lang.Integer.parseInt;
import java.util.List;
import static org.ehsavoie.moviebuddies.web.StartMovieBuddy.MYAPP;
import static org.ehsavoie.moviebuddies.web.User.findUserById;

/**
 *
 * @author Emmanuel Hugonnet (ehsavoie) <emmanuel.hugonnet@gmail.com>
 */
public class SearchUsersHandler implements HttpHandler {
    /*
     GET /users: retourne le fichier users.json
     GET /users/:id: retourne l'utilisateur en JSON tel qu'il est dans le fichier ou la chaine vide: devrait retourner 404 dans ce cas
     GET /users/search/:name/:limit: retourne une liste d'utilisateurs qui satisfont name avec un maximum de limit
     POST /rates: enregistre un vote d'un utilisateur pour un film. Le body est formatté: {userId: u, movieId: m, rate: r}.
     GET /rates/:userid: retourne les votes de userid
     GET /users/share/:userid1/:userid2: retourne la liste de films communs entre userid1 et userid2
     GET /users/distance/:userid1/:userid2: calcul de la distance entre userid1 et userid2. Remarque: le format de la réponse est de type chaine, je ne suis pas sur que cela soit confirme avec JSON
     */

    private static final String PREFIX = MYAPP + "/users";
    private final List<User> users;

    public SearchUsersHandler(List<User> users) {
        this.users = users;
    }

    @Override
    public void handleRequest(HttpServerExchange exchange) throws Exception {
        exchange.getResponseHeaders().add(Headers.CONTENT_TYPE, "application/json");
        final String[] params = URLParser.parse(PREFIX, exchange);
        switch (params[0]) {
            case "search": {
                final int limit;
                if (params.length > 2) {
                    limit = Integer.parseInt(params[2]);
                } else {
                    limit = -1;
                }
                exchange.dispatch(new SearchUsersByName(exchange, params[1], users, limit));
            }
            break;
            case "": {
                exchange.startBlocking();
                try (InputStream in = new BufferedInputStream(SearchAllUsers.class.getClassLoader().getResourceAsStream("users.json"))) {
                    byte[] buffer = new byte[16];
                    int length = 16;
                    while ((length = in.read(buffer, 0, length)) > 0) {
                        exchange.getOutputStream().write(buffer, 0, length);
                    }
                    exchange.endExchange();
                } catch (Exception ex) {
                    throw ex;
                }
            }
            break;
            case "share": {
                exchange.dispatch(new ComputeUserShared(exchange, parseInt(params[1]), parseInt(params[2]), users));
            }
            break;
            case "distance": {
                exchange.dispatch(new ComputeUserDistance(exchange, parseInt(params[1]), parseInt(params[2]), users));
            }
            break;
            default: {
                User user = findUserById(parseInt(params[0]), users);
                if (user == null) {
                    exchange.setResponseCode(StatusCodes.NOT_FOUND);
                } else {
                    exchange.getResponseSender().send(user.toString());
                }
                exchange.endExchange();
            }
            break;
        }
    }

    private boolean isLimit(int count, int limit) {
        return limit > 0 && count >= limit;
    }
}
