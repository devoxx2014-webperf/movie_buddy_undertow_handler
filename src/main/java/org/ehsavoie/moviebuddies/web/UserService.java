/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ehsavoie.moviebuddies.web;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import static java.lang.Math.sqrt;
import java.util.ArrayList;
import static java.util.Collections.binarySearch;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;
import static java.util.stream.Collectors.joining;

/**
 *
 * @author Emmanuel Hugonnet (ehsavoie) <emmanuel.hugonnet@gmail.com>
 */
public class UserService {

    public static final UserService INSTANCE = new UserService();
    private List<User> users;

    private UserService() {
        try (InputStream in = new BufferedInputStream(UserService.class.getClassLoader().getResourceAsStream("users.json"))) {
            List<JsonItem> items = JsonLoader.load(in);
            users = new ArrayList<>(items.size());
            items.stream().forEach((item) -> {
                users.add(User.parse(item));
            });
        } catch (IOException ioex) {
            throw new RuntimeException(ioex);
        }
    }

    public String searchUserByName(String name, int limit) {
        Pattern pattern = Pattern.compile(name.toLowerCase());
        return users.stream()
                .filter(user -> pattern.matcher(user.name).find())
                .map(User::toString)
                .limit(limit)
                .collect(joining(",", "[", "]"));
    }

    public String computeDistance(int userId1, int userId2) {
        User user1 = findUserById(userId1);
        User user2 = findUserById(userId2);
        String result = "0";
        if (user1 != user2 && user1.rates != null && !user1.rates.isEmpty() && user2.rates != null && !user2.rates.isEmpty()) {
            double sum_of_squares = 0.0D;
            sum_of_squares = user1.rates.entrySet().stream().filter((rate1) -> (user2.rates.containsKey(rate1.getKey()))).map((rate1) -> {
                Integer rate2 = user2.rates.get(rate1.getKey());
                double diff = rate1.getValue() - rate2;
                return diff;
            }).map((diff) -> diff * diff).reduce(sum_of_squares, (accumulator, _item) -> accumulator + _item);
            result = Double.toString(1.0 / (1.0 + sqrt(sum_of_squares)));
        }
        return result;
    }

    public String computeShare(int userId1, int userId2) {
        User user1 = findUserById(userId1);
        User user2 = findUserById(userId2);
        List<String> result = new LinkedList<>();
        if (user1.rates != null && !user1.rates.isEmpty() && user2.rates != null && !user2.rates.isEmpty()) {
            HashSet<Movie> set = new HashSet<>();
            set.addAll(user1.rates.keySet());
            set.retainAll(user2.rates.keySet());
            set.stream().forEach((movie) -> {
                result.add(movie.toString());
            });
        }
        return "[" + String.join(", ", result) + "]";
    }

    public User findUserById(int id) {
        int index = binarySearch(users, new User(id, "", ""));
        return (index < 0) ? null : users.get(index);
    }
}
