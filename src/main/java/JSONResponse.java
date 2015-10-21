package main.java;

import com.justinoboyle.utility.Saving;

public class JSONResponse {
    
    public String toString() {
        return Saving.GSON.toJson(this);
    }

}
