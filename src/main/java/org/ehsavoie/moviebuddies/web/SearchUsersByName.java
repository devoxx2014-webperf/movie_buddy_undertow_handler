/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ehsavoie.moviebuddies.web;

import io.undertow.server.HttpServerExchange;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

/**
 *
 * @author Emmanuel Hugonnet (ehsavoie) <emmanuel.hugonnet@gmail.com>
 */
public class SearchUsersByName implements Runnable {

    private final HttpServerExchange exchange;
    private final Pattern name;
    private final List<User> allUsers;
    private final int limit;

    public SearchUsersByName(HttpServerExchange exchange, String name, List<User> allUsers, int limit) {
        this.exchange = exchange;
        this.name = Pattern.compile(name.toLowerCase());
        this.allUsers = allUsers;
        this.limit = limit;
    }

    @Override
    public void run() {
        List<String> result = new LinkedList<>();
        int count = 0;
        for (User user : allUsers) {
            if (isLimit(count, limit)) {
                break;
            }
            if (name.matcher(user.name).find()) {
                count++;
                result.add(user.toString());
            }
        }
        exchange.getResponseSender().send("[" + String.join(", ", result) + "]");
        exchange.endExchange();
    }

    protected boolean isLimit(int count, int limit) {
        return limit > 0 && count >= limit;
    }

}
