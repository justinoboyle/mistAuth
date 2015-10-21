package com.justinoboyle.board;

import java.io.File;

public class Sound {

    private File path;
    private String displayName;

    public File getPath() {
        return path;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setPath(File path) {
        this.path = path;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

}
