/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ehsavoie.moviebuddies.web;

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
    private byte[] buffer;

    public SearchAllMovies(final HttpServerExchange exchange) {
        this.exchange = exchange;
    }

    @Override
    public void run() {
        int length = 1024;
        try (InputStream inputStream = new BufferedInputStream(SearchAllMovies.class.getClassLoader().getResourceAsStream("movies.json"))) {
            if (inputStream != null) {
                buffer = new byte[1024];
                exchange.startBlocking();
                while ((length = inputStream.read(buffer, 0, length)) > 0) {
                    exchange.getOutputStream().write(buffer, 0, length);
                }
                exchange.endExchange();
            }
        } catch (IOException e) {
            if (!exchange.isResponseStarted()) {
                exchange.setResponseCode(500);
            }
            exchange.endExchange();
        }
    }
}
