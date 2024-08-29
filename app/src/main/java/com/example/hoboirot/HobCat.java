package com.example.hoboirot;

import java.util.ArrayList;

public class HobCat {

    public ArrayList<HoboiLog> hob;
    public String name;
    public boolean open = true;

    public HobCat(ArrayList<HoboiLog> hob, String name) {
        this.hob = hob;
        this.name = name;
    }
}
