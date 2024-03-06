package com.example.hoboirot;

public class Hoboi {
    private int id;
    private String name;
    private boolean doneToday;

    public Hoboi(int id, String name) {
        this.id = id;
        this.name = name;
        this.doneToday = false;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isDoneToday() {
        return doneToday;
    }

    public void setDoneToday(boolean doneToday) {
        this.doneToday = doneToday;
    }
}
