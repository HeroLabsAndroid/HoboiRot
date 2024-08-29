package com.example.hoboirot;

import java.io.Serializable;

public class HoboiSave implements Serializable {
    int id;
    String name, catID;

    public HoboiSave(int id, String name, String catID) {
        this.id = id;
        this.name = name;
        this.catID = catID;
    }
}
