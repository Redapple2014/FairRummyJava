package org.fcesur.skillengine.http;

import org.fcesur.skillengine.http.handler.TemplateHandler;
import org.fcesur.skillengine.message.parsers.Jackson;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class APIServer {
    private Jackson jsonparser;

    public APIServer(Jackson jackson) {
        this.jsonparser = jackson;
    }

    public void init() {
        HttpServer server;
        try {
            server = HttpServer.create(new InetSocketAddress(8082), 0);
            server.createContext("/v1/tablecreate", new TemplateHandler(jsonparser));
            server.setExecutor(null);
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Server started at http://18.191.105.81:8082");
    }

}
