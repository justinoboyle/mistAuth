package main.java;

import com.justinoboyle.util.Saving;

public class JSONResponse {
    
    public String toString() {
        return Saving.GSON.toJson(this);
    }

}
