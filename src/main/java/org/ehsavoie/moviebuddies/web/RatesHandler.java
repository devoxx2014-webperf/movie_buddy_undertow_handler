/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ehsavoie.moviebuddies.web;

import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;
import io.undertow.util.Methods;
import io.undertow.util.StatusCodes;
import java.util.List;
import static org.ehsavoie.moviebuddies.web.StartMovieBuddy.MYAPP;

/**
 *
 * @author Emmanuel Hugonnet (ehsavoie) <emmanuel.hugonnet@gmail.com>
 */
public class RatesHandler implements HttpHandler {

    private static final String PREFIX = MYAPP + "/rates";

    public RatesHandler() {
    }

    protected void doGet(HttpServerExchange exchange) throws Exception {
        final String[] params = URLParser.parse(PREFIX, exchange);
        String result = RateService.INSTANCE.findRateByUser(Integer.parseInt(params[0]));
        if (result == null) {
            exchange.setResponseCode(StatusCodes.NOT_FOUND);
        } else {
            exchange.getResponseHeaders().add(Headers.CONTENT_TYPE, "application/json");
            exchange.getResponseSender().send(result);
        }
        exchange.endExchange();
    }

    protected void doPost(HttpServerExchange exchange) throws Exception {
        exchange.startBlocking();
        List<JsonItem> items = JsonLoader.load(exchange.getInputStream());
        exchange.setResponseCode(StatusCodes.MOVED_PERMENANTLY);
        exchange.getResponseHeaders().put(Headers.LOCATION, "http://" + exchange.getHostAndPort()
                + MYAPP + RateService.INSTANCE.rateMovie(items.get(0)));
        exchange.endExchange();
    }

    @Override
    public void handleRequest(HttpServerExchange exchange) throws Exception {
        if (Methods.POST.equals(exchange.getRequestMethod())) {
            doPost(exchange);
        } else {
            doGet(exchange);
        }
    }

}
