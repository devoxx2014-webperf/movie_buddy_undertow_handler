/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.ehsavoie.moviebuddies.web;

import static java.util.Collections.binarySearch;
import java.util.List;

/**
 *
 * @author Emmanuel Hugonnet (ehsavoie) <emmanuel.hugonnet@gmail.com>
 */
public class Movie  implements Comparable<Movie>{
  final int id;
  final String title;
  final String actors;
  final String genre;
  final String json;

  private Movie(int id, String title, String actors, String genre, String json) {
    this.id = id;
    this.title = title;
    this.actors = actors;
    this.genre = genre;
    this.json = json;
  }

  @Override
  public int compareTo(Movie movie) {
    return Integer.compare(id, movie.id);
  }

  @Override
  public String toString() {
    return json;
  }

  static Movie parse(JsonItem item) {
    return new Movie(
        item.getInt("_id"),
        item.getString("Title").toLowerCase(),
        item.getString("Actors").toLowerCase(),
        item.getString("Genre").toLowerCase(),
        item.toJson());
  }

  static Movie findMovieById(int id, List<Movie> movies) {
    int index = binarySearch(movies, new Movie(id, "", "", "", ""));
    return (index < 0)? null: movies.get(index);
  }
}
