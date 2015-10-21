package com.justinoboyle.main;

import java.util.ArrayList;
import java.util.List;

import com.justinoboyle.utility.Saving;

public class SuccessResponse {

    public boolean success = false;

    public List<String> errorCodes = new ArrayList<String>();

    public static SuccessResponse create(String json) {
        return Saving.GSON.fromJson(json.replace("error-codes", "errorCodes"), SuccessResponse.class);
    }

    public SuccessResponse() {

    }

    public SuccessResponse(boolean success) {
        this.success = success;
    }

    public SuccessResponse(boolean success, String... reasons) {
        this.success = success;
        for (String s : reasons)
            errorCodes.add(s);
    }

    public String toString() {
        return Saving.GSON.toJson(this);
    }
}
