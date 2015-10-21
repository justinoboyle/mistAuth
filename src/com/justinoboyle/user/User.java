package com.justinoboyle.user;

import java.io.File;
import java.io.FileInputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import spark.Request;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.justinoboyle.board.Soundboard;
import com.justinoboyle.util.Saving;
import com.justinoboyle.util.UtilPassword;

public class User {

    private boolean isAdmin = false;
    private boolean isVerified = false;
    private int level = 0;
    private int soundsPlayed = 0;
    private String email;
    private String currentUsername = "User";
    private String url = currentUsername;
    private List<Soundboard> boards = new ArrayList<Soundboard>();
    private String uuidSalt = UtilPassword.getSalt();
    
    private String salt = UtilPassword.getSalt();
    private String hashedPassword;
    List<String> loginKeys = new ArrayList<String>();
    
    private String uniqueId;

    public User(String email, String password, boolean shouldSave) {
        this.email = email;
        setPassword(password);
        uniqueId = UtilPassword.getSecurePassword(email, uuidSalt);
        if(shouldSave)
        save();
    }

    public static User fromBase64(String b64) {
        return Saving.GSON.fromJson(Saving.fromBase64(b64), User.class);
    }

    public static User fromFile(File f) {
        if (!f.exists())
            return null;
        try {
            Scanner stream = new Scanner(new FileInputStream(f));
            try {
                String temp = stream.nextLine();
                stream.close();
                temp = Saving.fromBase64(temp);
                return Saving.GSON.fromJson(temp, User.class);
            } catch (Exception ex) {
                stream.close();
                return null;
            }
        } catch (Exception ex) {
        }
        return null;
    }

    public static boolean uuidExists(String uuid) {
        File f = new File("./data/storage/users/" + Saving.toBase64(uuid));
        return f.exists();
    }
    
    public static boolean emailExists(String email) {
        File folder = new File("./data/storage/users/");
        if (!folder.exists())
            return false;
        folder.mkdirs();
        for (File f : folder.listFiles()) {
            User temp = fromFile(f);
            if (temp == null)
                continue;
            if (temp.email.equals(email))
                return true;
        }
        return false;
    }

    public static User getByUUID(String uuid) {
        File folder = new File("./data/storage/users/");
        if (!folder.exists())
            return null;
        folder.mkdirs();
        for (File f : folder.listFiles()) {
            User temp = fromFile(f);
            if (temp == null)
                continue;
            if (temp.uniqueId.equals(uuid))
                return temp;
        }
        return null;
    }
    
    public static User getByEmail(String email) {
        File folder = new File("./data/storage/users/");
        if (!folder.exists())
            return null;
        folder.mkdirs();
        for (File f : folder.listFiles()) {
            User temp = fromFile(f);
            if (temp == null)
                continue;
            if (temp.email.equals(email))
                return temp;
        }
        return null;
    }

    public static User getByName(String displayName) {
        File folder = new File("./data/storage/users/");
        if (!folder.exists())
            return null;
        folder.mkdirs();
        for (File f : folder.listFiles()) {
            User temp = fromFile(f);
            if (temp == null)
                continue;
            if (temp.getCurrentUsername().equals(displayName))
                return temp;
        }
        return null;
    }

    public void save() {
        try {
            File f = new File("./data/storage/users/" + Saving.toBase64(uniqueId));
            System.out.println("Saving to " + f.getName());
            f.getParentFile().mkdirs();
            if (f.exists())
                f.delete();
            PrintWriter pw = new PrintWriter(f);
            pw.print(Saving.toBase64(toString()));
            pw.close();
        } catch (Exception ex) {
            System.err.println("COULD NOT SAVE USER FILE! THIS IS BAD!");
        }
    }

    public String toString() {
        return Saving.GSON.toJson(this);
    }

    public String toSafeString() {
        String gson = toString();
        JsonParser parser = new JsonParser();
        JsonObject o = (JsonObject) parser.parse(gson);
        o.remove("salt");
        o.remove("hashedPassword");
        o.remove("loginKeys");
        o.remove("uuidSalt");
        o.remove("email");
        return o.toString();
    }

    public void setPassword(String newPassword) {
        salt = UtilPassword.getSalt();
        hashedPassword = UtilPassword.getSecurePassword(newPassword, salt);
    }
    
    public boolean checkPassword(String check) {
        return UtilPassword.getSecurePassword(check, salt).equals(hashedPassword);
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    public boolean isVerified() {
        return isVerified;
    }

    public void setVerified(boolean isVerified) {
        this.isVerified = isVerified;
    }

    public String getEmail() {
        return email;
    }

    public String getCurrentUsername() {
        return currentUsername;
    }

    public void setCurrentUsername(String currentUsername) throws SecurityException {
        if (getByName(currentUsername) != null)
            throw new SecurityException("That name is already in use.");
        String tempStr = currentUsername.replaceAll("[^a-zA-Z0-9 -]", "");
        if (!tempStr.equals(currentUsername)) {
            throw new SecurityException("That is an invalid username; usernames must be alphanumeric or contain dashes. Allowed characters (RegEx): [^a-zA-Z0-9 -]");
        }
        this.currentUsername = currentUsername;
        url = currentUsername;
    }

    public List<Soundboard> getBoards() {
        return boards;
    }

    public void setBoards(List<Soundboard> boards) {
        this.boards = boards;
    }

    public String getUniqueId() {
        return uniqueId;
    }
    
    public String generateNewKey(Request request) {
        return UtilPassword.getSecurePassword(UtilPassword.getSalt(), UtilPassword.getSalt());
    }

}
