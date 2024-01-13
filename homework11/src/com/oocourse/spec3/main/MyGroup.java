package com.oocourse.spec3.main;

import java.util.HashMap;

public class MyGroup implements Group {
    private int id;
    private HashMap<Integer, Person> people;
    private int ageSum;

    public int getAgeSum() {
        return ageSum;
    }

    public void setAgeSum(int ageSum) {
        this.ageSum = ageSum;
    }

    public MyGroup(int id) {
        this.id = id;
        this.ageSum = 0;
        this.people = new HashMap<>();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null && obj instanceof Group) {
            if (((Group) obj).getId() == id) {
                return true;
            }
            else {
                return false;
            }
        }
        else {
            return false;
        }
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void addPerson(Person person) {
        if (!hasPerson(person)) {
            ageSum += person.getAge();
            people.put(person.getId(), person);
        }
    }

    @Override
    public boolean hasPerson(Person person) {
        if (people.get(person.getId()) != null) {
            return true;
        }
        return false;
    }

    @Override
    public int getValueSum() {
        int sum = 0;
        for (int i : people.keySet()) {
            for (int j : people.keySet()) {
                if (people.get(i).isLinked(people.get(j))) {
                    sum += people.get(i).queryValue(people.get(j));
                }
            }
        }
        return sum;
    }

    @Override
    public int getAgeMean() {
        if (people.size() == 0) {
            return 0;
        }
        return ageSum / people.size();
        // return 0;
    }

    @Override
    public int getAgeVar() {
        if (people.size() == 0) {
            return 0;
        }
        else {
            int ageMean = getAgeMean();
            int sum = 0;
            for (int i : people.keySet()) {
                int age1 = people.get(i).getAge();
                int tmp = age1 - ageMean;
                sum += tmp * tmp;
            }
            return sum / people.size();
        }
    }

    @Override
    public void delPerson(Person person) {
        if (hasPerson(person)) {
            for (int i : people.keySet()) {
            }
            ageSum -= person.getAge();
            people.remove(person.getId());
        }
    }

    @Override
    public int getSize() {
        return people.size();
    }

    public void addSocialValue(int num) {
        for (int i : people.keySet()) {
            people.get(i).addSocialValue(num);
        }
    }
}
