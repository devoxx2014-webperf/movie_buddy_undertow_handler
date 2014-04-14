/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ehsavoie.moviebuddies.model;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import static org.ehsavoie.moviebuddies.web.StartMovieBuddy.LOADED_MOVIES;
import static org.ehsavoie.moviebuddies.web.StartMovieBuddy.LOADED_USERS;

/**
 * Web application lifecycle listener.
 *
 * @author Emmanuel Hugonnet (ehsavoie) <emmanuel.hugonnet@gmail.com>
 */
public class LoadData  {


    public static void initializeData() {
        try (InputStream in = new BufferedInputStream(LoadData.class.getClassLoader().getResourceAsStream("movies.json"))) {
            List<JsonItem> items = JsonLoader.load(in);
            for(JsonItem item : items) {
                 LOADED_MOVIES.add(Movie.parse(item));
            }
        } catch(IOException ioex){
            throw new RuntimeException(ioex);
        }

        try (InputStream in = new BufferedInputStream(LoadData.class.getClassLoader().getResourceAsStream("users.json"))) {
            List<JsonItem> items = JsonLoader.load(in);
            for(JsonItem item : items) {
                LOADED_USERS.add(User.parse(item));
            }
        }catch(IOException ioex){
            throw new RuntimeException(ioex);
        }

    }
}
