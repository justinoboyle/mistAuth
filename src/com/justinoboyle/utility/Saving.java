package com.justinoboyle.utility;

import java.io.UnsupportedEncodingException;

import javax.xml.bind.DatatypeConverter;

import com.google.gson.Gson;

public class Saving {

    public static final Gson GSON = new Gson();

    public static String fromBase64(String str) {
        try {
            return new String(DatatypeConverter.parseBase64Binary(str), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String toBase64(String str) {
        try {
            return DatatypeConverter.printBase64Binary(str.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
        
    }

}
