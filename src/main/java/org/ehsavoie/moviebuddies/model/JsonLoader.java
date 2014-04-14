/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ehsavoie.moviebuddies.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import javax.json.Json;
import javax.json.stream.JsonParser;
import static javax.json.stream.JsonParser.Event.END_OBJECT;
import static javax.json.stream.JsonParser.Event.KEY_NAME;
import static javax.json.stream.JsonParser.Event.START_OBJECT;
import static javax.json.stream.JsonParser.Event.VALUE_FALSE;
import static javax.json.stream.JsonParser.Event.VALUE_NULL;
import static javax.json.stream.JsonParser.Event.VALUE_NUMBER;
import static javax.json.stream.JsonParser.Event.VALUE_STRING;
import static javax.json.stream.JsonParser.Event.VALUE_TRUE;

/**
 *
 * @author Emmanuel Hugonnet (ehsavoie) <emmanuel.hugonnet@gmail.com>
 */
public class JsonLoader {

    public static List<JsonItem> load(File file) throws IOException {
        List<JsonItem> items = new ArrayList<>(1000);
        try (FileInputStream in = new FileInputStream(file)) {
            JsonParser parser = Json.createParser(in);
            while (parser.hasNext()) {
                JsonParser.Event event = parser.next();
                JsonItem item = null;
                String key = null;
                switch (event) {
                    case START_OBJECT:
                        item = new JsonItem();
                        break;
                    case END_OBJECT:
                        items.add(item);
                        break;
                    case VALUE_NULL:
                        break;
                    case KEY_NAME:
                        key = parser.getString();
                        break;
                    case VALUE_FALSE:
                    case VALUE_TRUE:
                    case VALUE_STRING:
                        item.put(key, parser.getString());
                        break;
                    case VALUE_NUMBER:
                        item.put(key, parser.getInt());
                        break;
                    default:
                        break;
                }
            }
        } catch (IOException ioex) {
            throw ioex;
        }
        return items;
    }

    public static List<JsonItem> load(InputStream in) {
        List<JsonItem> items = new ArrayList<>(1000);
        JsonParser parser = Json.createParser(in);
        JsonItem item = null;
        String key = null;
        while (parser.hasNext()) {
            JsonParser.Event event = parser.next();
            switch (event) {
                case START_OBJECT:
                    item = new JsonItem();
                    break;
                case END_OBJECT:
                    items.add(item);
                    break;
                case VALUE_NULL:
                    break;
                case KEY_NAME:
                    key = parser.getString();
                    break;
                case VALUE_FALSE:
                case VALUE_TRUE:
                case VALUE_STRING:
                    item.put(key, parser.getString());
                    break;
                case VALUE_NUMBER:
                    item.put(key, parser.getInt());
                    break;
                default:
                    break;
            }
        }
        return items;
    }

}
