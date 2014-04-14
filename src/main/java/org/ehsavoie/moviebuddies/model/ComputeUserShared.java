/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ehsavoie.moviebuddies.model;

import io.undertow.server.HttpServerExchange;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import static org.ehsavoie.moviebuddies.model.User.findUserById;

/**
 *
 * @author Emmanuel Hugonnet (ehsavoie) <emmanuel.hugonnet@gmail.com>
 */
public class ComputeUserShared implements Runnable {

    private final HttpServerExchange exchange;
    private final int userId1;
    private final int userId2;
    private final List<User> allUsers;

    public ComputeUserShared(HttpServerExchange exchange, int userId1, int userId2, List<User> allUsers) {
        this.exchange = exchange;
        this.userId1 = userId1;
        this.userId2 = userId2;
        this.allUsers = allUsers;
    }

    @Override
    public void run() {
            User user1 = findUserById(userId1, allUsers);
            User user2 = findUserById(userId2, allUsers);
            List<String> result = new LinkedList<>();
            if (user1.rates != null && !user1.rates.isEmpty() && user2.rates != null && !user2.rates.isEmpty()) {
                HashSet<Movie> set = new HashSet<>();
                set.addAll(user1.rates.keySet());
                set.retainAll(user2.rates.keySet());
                for (Movie movie : set) {
                    result.add(movie.toString());
                }
            }
            exchange.getResponseSender().send("[" + String.join(", ", result) + "]");
            exchange.endExchange();
    }

}
