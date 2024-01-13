package com.oocourse.spec3.main;

import java.util.Objects;

public class MyPair {
    private Person first;

    public Person getFirst() {
        return first;
    }

    public void setFirst(Person first) {
        this.first = first;
    }

    public Person getSecond() {
        return second;
    }

    public void setSecond(Person second) {
        this.second = second;
    }

    private Person second;

    public MyPair(Person first, Person second) {
        this.first = first;
        this.second = second;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MyPair myPair = (MyPair) o;
        return first.equals(myPair.first) && second.equals(myPair.second);
    }

    @Override
    public int hashCode() {
        return Objects.hash(first.getId(), second.getId());
    }

    @Override
    public String toString() {
        return "(" + first.getId() + ", " + second.getId() + ")";
    }
}
