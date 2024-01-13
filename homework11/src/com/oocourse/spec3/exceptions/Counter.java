package com.oocourse.spec3.exceptions;

import java.util.HashMap;

public class Counter {
    private HashMap<Integer, Integer> counter; // id, cnt

    public Counter() {
        this.counter = new HashMap<>();
    }

    public void addCnt(int id) {
        if (counter.get(id) == null) {
            counter.put(id, 1);
        } else {
            counter.put(id, counter.get(id) + 1);
        }
    }

    public void addDouble(int id1, int id2) {
        if (counter.get(id1) != null) {
            counter.put(id1, counter.get(id1) + 1);
        }
        if (counter.get(id2) != null) {
            counter.put(id2, counter.get(id2) + 1);
        }
        if (counter.get(id1) == null) {
            counter.put(id1, 1);
        }
        if (counter.get(id2) == null) {
            counter.put(id2, 1);
        }
    }

    public int getAllCnt(int id) {
        return counter.get(id);
    }
}
