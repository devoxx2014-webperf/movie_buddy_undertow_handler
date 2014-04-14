/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ehsavoie.moviebuddies.model;

import io.undertow.server.HttpServerExchange;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

/**
 *
 * @author Emmanuel Hugonnet (ehsavoie) <emmanuel.hugonnet@gmail.com>
 */
public class SearchAllUsers implements Runnable {

    private final HttpServerExchange exchange;

    public SearchAllUsers(final HttpServerExchange exchange) {
        this.exchange = exchange;
    }

    @Override
    public void run() {
        try (InputStream in = new BufferedInputStream(SearchAllUsers.class.getClassLoader().getResourceAsStream("users.json"));
                ByteArrayOutputStream out = new ByteArrayOutputStream(1000000)) {
            byte[] buffer = new byte[8];
            int length = 8;
            while ((length = in.read(buffer, 0, length)) > 0) {
               out.write(buffer, 0, length);
            }
            exchange.getResponseSender().send(new String(out.toByteArray()));
            exchange.endExchange();
        } catch (Exception ioex) {
            throw new RuntimeException(ioex);
        }
    }
}