/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ehsavoie.moviebuddies.web;

import java.util.LinkedHashMap;
import java.util.Map.Entry;

/**
 *
 * @author Emmanuel Hugonnet (ehsavoie) <emmanuel.hugonnet@gmail.com>
 */
public class JsonItem extends LinkedHashMap<String, Object> {

    public String getString(String name) {
        return (String) get(name);
    }

    public int getInt(String name) {
        return (Integer) get(name);
    }

    public String toJson() {
        StringBuilder builder = new StringBuilder();
        builder.append('{');
        for (Entry<String,Object> entry   : entrySet()) {
        builder.append('"').append(entry.getKey()).append("\":");
            if (entry.getValue() instanceof String) {
                builder.append('"').append(entry.getValue()).append('"');
            } else {
                builder.append(entry.getValue());
            }
            builder.append(',');
        }
        if (!isEmpty()) {
            builder.setLength(builder.length() - 1);
        }
        return builder.append('}').toString();
    }
}
