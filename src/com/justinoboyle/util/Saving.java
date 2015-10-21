package com.justinoboyle.util;

import java.util.Base64;

import com.google.gson.Gson;

public class Saving {

    public static final Gson GSON = new Gson();

    public static String fromBase64(String str) {
        return new String(Base64.getDecoder().decode(str.getBytes()));
    }

    public static String toBase64(String str) {
        return new String(Base64.getEncoder().encode(str.getBytes()));
    }

}
