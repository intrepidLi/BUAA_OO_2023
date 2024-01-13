package com.oocourse.spec3.main;

public class Pair {
    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    private int key;
    private int value;

    public Pair(int k, int v) {
        key = k;
        value = v;
    }
}
