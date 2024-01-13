package com.oocourse.spec1.main;

import java.util.ArrayList;

public class MyPerson implements Person {
    private int id;
    private String name;
    private int age;
    private ArrayList<Person> acquaintance = new ArrayList<>();
    private ArrayList<Integer> value = new ArrayList<>();

    public ArrayList<Person> getAcquaintance() {
        return acquaintance;
    }

    public void setAcquaintance(ArrayList<Person> acquaintance) {
        this.acquaintance = acquaintance;
    }

    public ArrayList<Integer> getValue() {
        return value;
    }

    public void setValue(ArrayList<Integer> value) {
        this.value = value;
    }

    public MyPerson(int id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getAge() {
        return age;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof Person) {
            return (((Person) obj).getId() == id);
        }
        else {
            return false;
        }
    }

    @Override
    public boolean isLinked(Person person) {
        if (person == this) {
            return true;
        }
        for (Person p : acquaintance) {
            if (p.getId() == person.getId()) {
                return true;
            }
        }
        return false;
    }

    // 在给定的Person数组中查找指定person的值，如果找到了person，则返回对应的value值；如果没有找到，则返回0。
    @Override
    public int queryValue(Person person) {
        for (int i = 0; i < acquaintance.size(); i++) {
            if (acquaintance.get(i).equals(person)) {
                return value.get(i);
            }
        }
        return 0;
    }

    @Override
    public int compareTo(Person p2) {
        return name.compareTo(p2.getName());
    }

    public void addAcquaintance(Person person, int value) {
        acquaintance.add(person);
        this.value.add(value);
    }
}
