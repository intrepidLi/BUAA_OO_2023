package com.oocourse.spec1.main;

import com.oocourse.spec1.exceptions.EqualRelationException;
import com.oocourse.spec1.exceptions.EqualPersonIdException;
import com.oocourse.spec1.exceptions.RelationNotFoundException;
import com.oocourse.spec1.exceptions.PersonIdNotFoundException;
import com.oocourse.spec1.exceptions.MyRelationNotFoundException;
import com.oocourse.spec1.exceptions.MyEqualPersonIdException;
import com.oocourse.spec1.exceptions.MyPersonIdNotFoundException;
import com.oocourse.spec1.exceptions.MyEqualRelationException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;

public class MyNetwork implements Network {
    private HashMap<Person, Person> parents = new HashMap<>(); // 存储子节点和父节点
    private HashMap<Integer, Person> people = new HashMap<>(); // id person
    private static boolean flag;
    private static int oldtri;

    public MyNetwork() {
        flag = false;
        oldtri = 0;
    }

    @Override
    public boolean contains(int id) {
        return people.get(id) != null;
        /*for (int i = 0; i < people.size(); i++) {
            if (people.get(i).getId() == id) {
                return true;
            }
        }*/
    }

    @Override
    public Person getPerson(int id) {
        return people.get(id);
        /*for (int i = 0; i < people.size(); i++) {
            if (people.get(i).getId() == id) {
                return people.get(i);
            }
        }
        return null;*/
    }

    @Override
    public void addPerson(Person person) throws EqualPersonIdException {
        for (int i : people.keySet()) {
            if (people.get(i).equals(person)) {
                throw new MyEqualPersonIdException(person.getId());
            }
        }
        people.put(person.getId(), person);
        parents.put(person, null); // 新加入的结点父亲是null
    }

    @Override
    public void addRelation(int id1, int id2, int value)
            throws PersonIdNotFoundException, EqualRelationException {
        if (contains(id1) && contains(id2) && !getPerson(id1).isLinked(getPerson(id2))) {
            Person person1 = getPerson(id1);
            Person person2 = getPerson(id2);
            ((MyPerson) person1).addAcquaintance(person2, value);
            ((MyPerson) person2).addAcquaintance(person1, value);
            if (!getRoot(person1).equals(getRoot(person2))) { // 如果根不相同，将根连在一起
                parents.put(getRoot(person1), getRoot(person2));
            }
            flag = true;
        } else {
            if (!contains(id1)) {
                throw new MyPersonIdNotFoundException(id1);
            }
            else if (contains(id1) && !contains(id2)) {
                throw new MyPersonIdNotFoundException(id2);
            }
            else if (contains(id1) && contains(id2) && getPerson(id1).isLinked(getPerson(id2))) {
                throw new MyEqualRelationException(id1, id2);
            }
        }
    }

    @Override
    public int queryValue(int id1, int id2)
            throws PersonIdNotFoundException, RelationNotFoundException {
        if (!contains(id1)) {
            throw new MyPersonIdNotFoundException(id1);
        } else if (contains(id1) && !contains(id2)) {
            throw new MyPersonIdNotFoundException(id2);
        }
        Person person1 = getPerson(id1);
        Person person2 = getPerson(id2);
        if (!person1.isLinked(person2)) {
            throw new MyRelationNotFoundException(id1, id2);
        }
        return person1.queryValue(person2);
    }

    @Override
    public boolean isCircle(int id1, int id2) throws PersonIdNotFoundException {
        if (!contains(id1)) {
            throw new MyPersonIdNotFoundException(id1);
        } else if (contains(id1) && !contains(id2)) {
            throw new MyPersonIdNotFoundException(id2);
        }

        Person person1 = getPerson(id1);
        Person person2 = getPerson(id2);
        return (getRoot(person1).equals(getRoot(person2)));
    }

    @Override
    public int queryBlockSum() {
        HashSet<Person> sum = new HashSet<>();
        for (int i : people.keySet()) {
            sum.add(getRoot(people.get(i)));
        }
        return sum.size();
    }

    private static HashMap<Integer, Integer> deg = new HashMap<>();
    private static HashMap<Integer, Integer> visitime = new HashMap<>();

    // 暴力算法 O(n^3)
    /*int sum = 0;
       for (int i : people.keySet()) {
           for (Person person1 : ((MyPerson)people.get(i)).getAcquaintance()) {
               //if (!people.get(j).equals(people.get(i))) {
               for (Person person2 : ((MyPerson)person1).getAcquaintance()) {
                   // if (!people.get(i).equals(people.get(k)) &&
                   // !people.get(j).equals(people.get(k))) {
                   if (person2 != people.get(i)) {
                       if (person2.isLinked(people.get(i))) {
                           sum++;
                       }
                   }
                   //}
               }
               //}
           }
       }
       sum /= 6;*/
    @Override
    public int queryTripleSum() {
        // 洛谷P1989 O(n^(3/2))算法
        if (!flag) {
            return oldtri;
        }
        int cnt = 0;
        HashMap<Integer, ArrayList<Integer>> e = new HashMap<>();

        for (int i : people.keySet()) {
            deg.put(i, ((MyPerson)people.get(i)).getAcquaintance().size()); // 人作为节点，熟人数量即为度
            e.put(i, new ArrayList<>());
        }
        for (int i : people.keySet()) {
            // Person uperson = people.get(i);
            for (Person person1 : ((MyPerson)people.get(i)).getAcquaintance()) {
                if (((e.get(i)).contains(person1.getId()))
                        || ((e.get(person1.getId())).contains(i))) {
                    continue;
                }
                int flag = 0;
                if (deg.get(i) > deg.get(person1.getId())) {
                    flag = 1;
                }
                else if (i > person1.getId() && Objects.equals(deg.get(i),
                        deg.get(person1.getId()))) {
                    flag = 1;
                }
                if (flag == 1) {
                    (e.get(person1.getId())).add(i);
                }
                else {
                    (e.get(i)).add(person1.getId());
                }
            }
        }

        for (int i : people.keySet()) {
            for (int v : (e.get(i))) {
                visitime.put(v, i); // 所有从i出发边的终点的visitime设为 i
            }
            for (int v : (e.get(i))) {  // 所有从u出发的边
                for (int w : (e.get(v))) {  // 所有从v出发的边
                    if (visitime.get(w) != null && visitime.get(w) == i) {
                        cnt++;
                    }
                }
            }
        }
        oldtri = cnt;
        flag = false;
        return cnt;
    }

    @Override
    public boolean queryTripleSumOKTest(HashMap<Integer, HashMap<Integer, Integer>> beforeData,
                                        HashMap<Integer, HashMap<Integer, Integer>> afterData,
                                        int result) {
        if (!afterData.equals(beforeData)) {
            return false;
        }
        int sum = 0;
        for (int i : beforeData.keySet()) {
            for (int j : beforeData.get(i).keySet()) {
                for (int k : beforeData.get(j).keySet()) {
                    if (beforeData.get(k).containsKey(i)) {
                        sum++;
                    }
                }
            }
        }
        // System.out.println("sum = " + sum);
        sum /= 6;
        return sum == result;
    }

    public Person getRoot(Person person) { // 并查集查找根节点
        Person root = person; // 从底向上找根
        while (parents.get(root) != null) {
            root = parents.get(root);
        }
        if (!root.equals(person)) {
            parents.put(person, root);
        }
        return root;
    }
}
