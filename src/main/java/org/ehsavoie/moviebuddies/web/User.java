/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.ehsavoie.moviebuddies.web;

import static java.util.Collections.binarySearch;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Emmanuel Hugonnet (ehsavoie) <emmanuel.hugonnet@gmail.com>
 */
public class User implements Comparable<User> {
  final int id;
  final String name;
  final String json;
  Map<Movie, Integer> rates;

  private User(int _id, String name, String json) {
    this.id = _id;
    this.name = name;
    this.json = json;
  }

  @Override
  public int compareTo(User user) {
    return Integer.compare(id, user.id);
  }

  @Override
  public String toString() {
    return json;
  }

  static User parse(JsonItem item) {
    return new User(
        item.getInt("_id"),
        item.getString("name").toLowerCase(),
        item.toJson());
  }

  static User findUserById(int id, List<User> users) {
    int index = binarySearch(users, new User(id, "", ""));
    return(index < 0)? null: users.get(index);
  }
}