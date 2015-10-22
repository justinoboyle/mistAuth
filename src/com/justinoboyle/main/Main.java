package com.justinoboyle.main;

import static spark.Spark.get;
import static spark.SparkBase.port;
import static spark.SparkBase.staticFileLocation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import spark.Request;
import spark.Response;
import spark.Route;

import com.justinoboyle.listeners.user.UserListener;

public class Main {

    private static List<Listener> listeners = new ArrayList<Listener>();

    public static void main(String[] args) {

        listeners.add(new UserListener());

        try {
            try {
                port(Integer.valueOf(System.getenv("PORT")));
            } catch (Exception ex) {
                boolean started = false;
                int startPort = 8080;
                while (!started) {
                    try {
                        port(startPort);
                        started = true;
                        System.out.println("Started on port " + startPort);
                    } catch (Exception ex2) {
                        ++startPort;
                    }
                }

            }
            staticFileLocation("/public");
            get("*", new Route() {
                public Object handle(Request req, Response res) {
                    return test(req, res);
                }
            });

        } catch (Exception ex) {
            System.err.println("Port already in use. Shutting down.");
            System.exit(0);
            return;
        }
    }

    private static Object test(Request req, Response res) {
        Map map = req.queryMap().toMap();
        Map<String, String> data = new HashMap<String, String>();
        try {
            for (Object s : map.keySet())
                data.put(s.toString(), (String) ((Object[]) map.get(s))[0].toString());
        } catch (Exception ex) {
        }
        try {
            for (Listener l : listeners) {
                Object response = l.process(req, res, req.splat()[0], data);
                if (response != null)
                    return response;
            }
        } catch (Exception ex) {
        }
        return new SuccessResponse(false, "error", "method not found");
    }

}
