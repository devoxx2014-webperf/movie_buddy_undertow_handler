/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ehsavoie.moviebuddies.model;

import io.undertow.server.HttpServerExchange;
import io.undertow.util.StatusCodes;
import java.util.List;
import static org.ehsavoie.moviebuddies.model.User.findUserById;

/**
 *
 * @author Emmanuel Hugonnet (ehsavoie) <emmanuel.hugonnet@gmail.com>
 */
public class SearchUsersById implements Runnable {

    private final HttpServerExchange exchange;
    private final int userId;
    private final List<User> allUsers;

    public SearchUsersById(HttpServerExchange exchange, int userId, List<User> allUsers) {
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
            exchange.getResponseSender().send(user.toString());
        }
        exchange.endExchange();
    }

}
