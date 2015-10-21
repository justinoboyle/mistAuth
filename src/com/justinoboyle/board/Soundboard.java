package com.justinoboyle.board;

import java.util.ArrayList;
import java.util.List;

public class Soundboard {
    
    private List<Sound> sounds = new ArrayList<Sound>();
    
    public List<Sound> getSounds() {
        return sounds;
    }
    
    public void setSounds(List<Sound> sounds) {
        this.sounds = sounds;
    }

}
