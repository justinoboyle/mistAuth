package main.java;

import java.util.Map;

import spark.Request;
import spark.Response;

public abstract class Listener {
    
    public abstract Object process(Request req, Response res, String listen, Map<String, String> data);

}
