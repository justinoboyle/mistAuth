package com.justinoboyle.listeners.user;

import java.util.Map;
import java.util.Random;

import main.java.Listener;
import main.java.SuccessResponse;
import spark.Request;
import spark.Response;

import com.justinoboyle.user.User;

public class UserListener extends Listener {

    @Override
    public Object process(Request req, Response res, String listen, Map<String, String> data) {
        if (listen.equals("newkey")) {
            
            if (!data.containsKey("uuid"))
                return new SuccessResponse(false, "error", "No uuid was specified.");
            
            String uuid = data.get("uuid");

            if (!User.uuidExists(uuid))
                return new SuccessResponse(false, "error", "Login is invalid.");

            if (!data.containsKey("password"))
                return new SuccessResponse(false, "error", "No password was specified.");

            String password = data.get("password");
            User usr = User.getByUUID(uuid);

            if (usr == null)
                return new SuccessResponse(false, "error", "Login is invalid.");

            if (!usr.checkPassword(password))
                return new SuccessResponse(false, "error", "Login is invalid.");

            return new SuccessResponse(true, "success", usr.generateNewKey(req));

        }
        if (listen.equals("newuser")) {

            if (!data.containsKey("email"))
                return new SuccessResponse(false, "error", "No email was specified.");
            String email = data.get("email");

            if (!email.contains("@") || !email.contains("."))
                return new SuccessResponse(false, "error", "Invalid email.");

            if (User.emailExists(email))
                return new SuccessResponse(false, "error", "A user with that email already exists.");

            if (!data.containsKey("password"))
                return new SuccessResponse(false, "error", "No password was specified.");

            String password = data.get("password");
            if (password.length() < 8) {
                return new SuccessResponse(false, "error", "Password must be at least 8 characters.");
            }

            String username = email.substring(0, email.indexOf("@"));

            if (data.containsKey("username"))
                username = data.get("username");

            User user = new User(email, password, false);
            try {
                user.setCurrentUsername(username);
            } catch (SecurityException ex) {
                return new SuccessResponse(false, "error", ex.getMessage());
            }
            user.save();
            UserListenerResponse response = new UserListenerResponse();
            response.success = true;
            response.resultUser = user.getUniqueId();
            return response;
        }
        return null;
    }

}
