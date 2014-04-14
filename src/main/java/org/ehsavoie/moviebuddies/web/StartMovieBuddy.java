/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ehsavoie.moviebuddies.web;

import io.undertow.Handlers;
import io.undertow.Undertow;
import io.undertow.util.Headers;
import java.util.ArrayList;
import java.util.List;
import org.ehsavoie.moviebuddies.model.LoadData;
import org.ehsavoie.moviebuddies.model.Movie;
import org.ehsavoie.moviebuddies.model.User;

/**
 *
 * @author Emmanuel Hugonnet (ehsavoie) <emmanuel.hugonnet@gmail.com>
 */
public class StartMovieBuddy {
    public static final List<Movie> LOADED_MOVIES = new ArrayList<>(10000);
    public static final List<User> LOADED_USERS = new ArrayList<>(10000);

    public static final String MYAPP = "/moviebuddy";

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        startServer(Integer.parseInt(System.getProperty("app.port", "8080")), "0.0.0.0", System.getProperty("user.home"));
    }

    public static void startServer(final int port, final String hostName, final String homeDir) throws Exception {
        /* DeploymentInfo servletBuilder = Servlets.deployment()
         .setClassLoader(SearchMoviesServlet.class.getClassLoader())
         .setContextPath(MYAPP)
         .addWelcomePage("index.html")
         .setResourceManager(new ClassPathResourceManager(SearchMoviesServlet.class.getClassLoader()))
         .setDeploymentName("moviebuddy.war")
         .addListener(new ListenerInfo(LoadData.class))
         .addServlets(
         Servlets.servlet("Movies", SearchMoviesServlet.class)
         .addMapping("/movies")
         .addMapping("/movies/*")
         .setAsyncSupported(true),
         Servlets.servlet("Users", SearchUsersServlet.class)
         .addMapping("/users")
         .addMapping("/users/*")
         .setAsyncSupported(true),
         Servlets.servlet("Rates", RatesServlet.class)
         .addMapping("/rates")
         .addMapping("/rates/*")
         .setAsyncSupported(true));

         DeploymentManager manager = Servlets.defaultContainer().addDeployment(servletBuilder);
         manager.deploy();
         PathHandler path = Handlers.path(Handlers.redirect(MYAPP))
         .addPrefixPath(MYAPP, manager.start());*/

        LoadData.initializeData();
        Undertow server = Undertow.builder()
                .addHttpListener(port, hostName)
                .setIoThreads(Runtime.getRuntime().availableProcessors() * 2)
                .setBufferSize(1024 * 16)
                .setWorkerThreads(50)
                .setHandler(Handlers.header(Handlers.path(Handlers.resource(new WebappClassPathResourceManager(MYAPP, SearchMoviesHandler.class.getClassLoader())))
                        .addPrefixPath(MYAPP + "/rates", new RatesHandler(LOADED_MOVIES, LOADED_USERS))
                        .addPrefixPath(MYAPP + "/users", new SearchUsersHandler(LOADED_USERS))
                        .addPrefixPath(MYAPP + "/movies", new SearchMoviesHandler(LOADED_MOVIES)), Headers.SERVER_STRING, "U-tow")).build();
        server.start();
    }

}
