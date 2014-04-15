/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ehsavoie.moviebuddies.web;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Emmanuel Hugonnet (ehsavoie) <emmanuel.hugonnet@gmail.com>
 */
public class InitializeRates {

    public static void loadRate() {
        try (InputStream in = new BufferedInputStream(InitializeRates.class.getClassLoader().getResourceAsStream("rates.json"))) {
            List<JsonItem> items = JsonLoader.load(in);
            List<Movie> movies = new ArrayList<>(items.size());
            for (JsonItem item : items) {
                RateService.INSTANCE.rateMovie(item);
            }
        } catch (IOException ioex) {
            throw new RuntimeException(ioex);
        }

    }
}
