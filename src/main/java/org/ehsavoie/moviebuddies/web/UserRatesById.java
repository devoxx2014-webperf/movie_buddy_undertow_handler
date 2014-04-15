/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ehsavoie.moviebuddies.web;

import io.undertow.server.HttpServerExchange;
import io.undertow.util.StatusCodes;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import static org.ehsavoie.moviebuddies.web.User.findUserById;

/**
 *
 * @author Emmanuel Hugonnet (ehsavoie) <emmanuel.hugonnet@gmail.com>
 */
public class UserRatesById implements Runnable {

    private final HttpServerExchange exchange;
    private final int userId;
    private final List<User> allUsers;

    public UserRatesById(HttpServerExchange exchange, int userId, List<User> allUsers) {
        this.exchange = exchange;
        this.allUsers = allUsers;
        this.userId = userId;
    }

    @Override
    public void run() {
        User user = findUserById(userId, allUsers);
        if (user == null) {
            exchange.setResponseCode(StatusCodes.NOT_FOUND);
        } else {
            List<String> result = new LinkedList<>();
            result.add(("{"));
            if (user.rates != null) {
                for (Entry<Movie, Integer> rate : user.rates.entrySet()) {
                    result.add("'" + rate.getKey().id + "':" + rate.getValue());
                }
            }
            result.add(("}"));
            exchange.getResponseSender().send(user.toString());
        }
        exchange.endExchange();
    }

}
