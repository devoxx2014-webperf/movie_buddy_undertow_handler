/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ehsavoie.moviebuddies.model;

import java.util.HashMap;
import java.util.List;
import static org.ehsavoie.moviebuddies.model.Movie.findMovieById;

/**
 *
 * @author Emmanuel Hugonnet (ehsavoie) <emmanuel.hugonnet@gmail.com>
 */
public class RateMovie {

    private final JsonItem item;
    private final List<User> allUsers;
    private final List<Movie> allMovies;

    public RateMovie(JsonItem item, List<User> allUsers, List<Movie> allMovies) {
        this.item = item;
        this.allUsers = allUsers;
        this.allMovies = allMovies;
    }

    public String rate() {
        User user = User.findUserById(item.getInt("userId"), allUsers);
        Movie movie = findMovieById(item.getInt("movieId"), allMovies);
        if (user.rates == null) {
            user.rates = new HashMap<>();
        }
        user.rates.put(movie, item.getInt("rate"));
        return "/rates/" + user.id;
    }

}
