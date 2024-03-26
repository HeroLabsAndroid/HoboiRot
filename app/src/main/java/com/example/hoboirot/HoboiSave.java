package com.example.hoboirot;

import java.io.Serializable;

public class HoboiSave implements Serializable {
    int id;
    String name;

    public HoboiSave(int id, String name) {
        this.id = id;
        this.name = name;
    }
}
