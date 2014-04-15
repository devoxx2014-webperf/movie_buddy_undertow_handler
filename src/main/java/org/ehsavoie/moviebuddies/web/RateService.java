/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ehsavoie.moviebuddies.web;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Emmanuel Hugonnet (ehsavoie) <emmanuel.hugonnet@gmail.com>
 */
public class RateService {

    public static final RateService INSTANCE = new RateService();

    private RateService() {
    }

    public String rateMovie(JsonItem item) {
        User user = UserService.INSTANCE.findUserById(item.getInt("userId"));
        Movie movie = MovieService.INSTANCE.findMovieById(item.getInt("movieId"));
        if (user.rates == null) {
            user.rates = new HashMap<>();
        }
        user.rates.put(movie, item.getInt("rate"));
        return "/rates/" + user.id;
    }

    public String findRateByUser(int userId) {
        User user = UserService.INSTANCE.findUserById(userId);
        if (user == null) {
            return null;
        } else {
            List<String> result = new LinkedList<>();
            if (user.rates != null) {
                user.rates.entrySet().stream().forEach((rate) -> {
                    result.add("\"" + rate.getKey().id + "\": " + rate.getValue());
                });
            }
            return "{" + String.join(", ", result) + "}";
        }
    }
}
