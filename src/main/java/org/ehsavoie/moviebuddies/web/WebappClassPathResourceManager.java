/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ehsavoie.moviebuddies.web;

import io.undertow.server.handlers.resource.ClassPathResourceManager;
import io.undertow.server.handlers.resource.Resource;
import io.undertow.server.handlers.resource.ResourceChangeListener;
import io.undertow.server.handlers.resource.ResourceManager;
import java.io.IOException;

/**
 *
 * @author Emmanuel Hugonnet (ehsavoie) <emmanuel.hugonnet@gmail.com>
 */
public class WebappClassPathResourceManager implements ResourceManager {

    private final String resourcePrefix;
    private final ClassPathResourceManager manager;

    public WebappClassPathResourceManager(String resourcePrefix, ClassLoader loader) {
        this.resourcePrefix = resourcePrefix;
        this.manager = new ClassPathResourceManager(loader);
    }

    @Override
    public Resource getResource(String path) throws IOException {
        if (path.startsWith(resourcePrefix)) {
            return manager.getResource(path.substring(resourcePrefix.length()));
        }
        return manager.getResource(path);
    }

    @Override
    public boolean isResourceChangeListenerSupported() {
        return manager.isResourceChangeListenerSupported();
    }

    @Override
    public void registerResourceChangeListener(ResourceChangeListener listener) {
        manager.registerResourceChangeListener(listener);
    }

    @Override
    public void removeResourceChangeListener(ResourceChangeListener listener) {
        manager.removeResourceChangeListener(listener);
    }

    @Override
    public void close() throws IOException {
        manager.close();
    }

}
