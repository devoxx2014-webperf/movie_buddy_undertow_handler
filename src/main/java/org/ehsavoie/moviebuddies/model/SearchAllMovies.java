/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ehsavoie.moviebuddies.model;

import io.undertow.server.HttpServerExchange;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 *
 * @author Emmanuel Hugonnet (ehsavoie) <emmanuel.hugonnet@gmail.com>
 */
public class SearchAllMovies implements Runnable {

    private final HttpServerExchange exchange;

    public SearchAllMovies(final HttpServerExchange exchange) {
        this.exchange = exchange;
    }

    @Override
    public void run() {
        exchange.startBlocking();
        try (InputStream in = new BufferedInputStream(SearchAllMovies.class.getClassLoader().getResourceAsStream("movies.json"))) {
            byte[] buffer = new byte[8];
            int length = 8;
            while ((length = in.read(buffer, 0, length)) > 0) {
                exchange.getOutputStream().write(buffer, 0, length);
            }
            exchange.endExchange();
        } catch (IOException ioex) {
            throw new RuntimeException(ioex);
        }
    }
}
