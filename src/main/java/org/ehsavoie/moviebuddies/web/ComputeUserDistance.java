/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ehsavoie.moviebuddies.web;

import io.undertow.server.HttpServerExchange;
import static java.lang.Math.sqrt;
import java.util.List;
import java.util.Map.Entry;
import static org.ehsavoie.moviebuddies.web.User.findUserById;

/**
 *
 * @author Emmanuel Hugonnet (ehsavoie) <emmanuel.hugonnet@gmail.com>
 */
public class ComputeUserDistance implements Runnable {

    private final HttpServerExchange exchange;
    private final int userId1;
    private final int userId2;
    private final List<User> allUsers;

    public ComputeUserDistance(HttpServerExchange exchange, int userId1, int userId2, List<User> allUsers) {
        this.exchange = exchange;
        this.userId1 = userId1;
        this.userId2 = userId2;
        this.allUsers = allUsers;
    }

    @Override
    public void run() {
            User user1 = findUserById(userId1, allUsers);
            User user2 = findUserById(userId2, allUsers);
            String result = "0";
            if (user1 != user2 && user1.rates != null && !user1.rates.isEmpty() && user2.rates != null && !user2.rates.isEmpty()) {
                double sum_of_squares = 0.0D;
                for (Entry<Movie, Integer> rate1 : user1.rates.entrySet()) {
                    if (user2.rates.containsKey(rate1.getKey())) {
                        Integer rate2 = user2.rates.get(rate1.getKey());
                        double diff = rate1.getValue() - rate2;
                        sum_of_squares += diff * diff;
                    }
                }
                result = Double.toString(1.0 / (1.0 + sqrt(sum_of_squares)));
            }
            exchange.getResponseSender().send(result);
            exchange.endExchange();
    }

}
