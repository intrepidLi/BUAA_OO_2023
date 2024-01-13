package com.oocourse.spec2.main;

import com.oocourse.spec2.exceptions.MyAcquaintanceNotFoundException;
import com.oocourse.spec2.exceptions.MyGroupIdNotFoundException;
import com.oocourse.spec2.exceptions.MyEqualGroupIdException;
import com.oocourse.spec2.exceptions.MyMessageIdNotFoundException;
import com.oocourse.spec2.exceptions.MyEqualMessageIdException;
import com.oocourse.spec2.exceptions.MyRelationNotFoundException;
import com.oocourse.spec2.exceptions.MyEqualPersonIdException;
import com.oocourse.spec2.exceptions.MyPersonIdNotFoundException;
import com.oocourse.spec2.exceptions.MyEqualRelationException;
import com.oocourse.spec2.exceptions.AcquaintanceNotFoundException;
import com.oocourse.spec2.exceptions.GroupIdNotFoundException;
import com.oocourse.spec2.exceptions.EqualGroupIdException;
import com.oocourse.spec2.exceptions.MessageIdNotFoundException;
import com.oocourse.spec2.exceptions.EqualMessageIdException;
import com.oocourse.spec2.exceptions.RelationNotFoundException;
import com.oocourse.spec2.exceptions.EqualPersonIdException;
import com.oocourse.spec2.exceptions.PersonIdNotFoundException;
import com.oocourse.spec2.exceptions.EqualRelationException;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class MyNetwork implements Network {
    private HashMap<Integer, Group> groups = new HashMap<>();
    private HashMap<Integer, Message> messages = new HashMap<>();
    private HashMap<Person, Person> parents = new HashMap<>(); // 存储子节点和父节点
    private HashMap<Integer, Person> people = new HashMap<>(); // id person
    private static boolean flagCou;
    private static int oldcoup;
    private int tripleSum;

    public MyNetwork() {
        flagCou = false;
        oldcoup = 0;
        tripleSum = 0;
    }

    @Override
    public boolean contains(int id) {
        return people.get(id) != null;
    }

    @Override
    public Person getPerson(int id) {
        return people.get(id);
    }

    @Override
    public void addPerson(Person person) throws EqualPersonIdException {
        for (int i : people.keySet()) {
            if (people.get(i).equals(person)) {
                throw new MyEqualPersonIdException(person.getId());
            }
        }
        people.put(person.getId(), person);
        parents.put(person, person); // 新加入的结点父亲是null
    }

    @Override
    public void addRelation(int id1, int id2, int value)
            throws PersonIdNotFoundException, EqualRelationException {
        if (contains(id1) && contains(id2) && !getPerson(id1).isLinked(getPerson(id2))) {
            MyPerson person1 = (MyPerson) getPerson(id1);
            MyPerson person2 = (MyPerson) getPerson(id2);
            if (person1.getAcquaintance().size() < person2.getAcquaintance().size()) {
                tripleSum = person1.addTriple(person2, tripleSum);
            } else {
                tripleSum = person2.addTriple(person1, tripleSum);
            }
            person1.addAcquaintance(person2, value);
            person2.addAcquaintance(person1, value);
            if (!getRoot(person1).equals(getRoot(person2))) { // 如果根不相同，将根连在一起
                parents.put(getRoot(person1), getRoot(person2));
            }
            flagCou = true;
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
    public void modifyRelation(int id1, int id2, int value) throws PersonIdNotFoundException,
            EqualPersonIdException, RelationNotFoundException {
        if (!contains(id1)) {
            throw new MyPersonIdNotFoundException(id1);
        } else if (contains(id1) && !contains(id2)) {
            throw new MyPersonIdNotFoundException(id2);
        } else if (id1 == id2) {
            throw new MyEqualPersonIdException(id1);
        } else if (!getPerson(id1).isLinked(getPerson(id2))) {
            throw new MyRelationNotFoundException(id1, id2);
        } else {
            flagCou = true;
            if (getPerson(id1).queryValue(getPerson(id2)) + value > 0) {
                setDoubleValue(id1, id2, value);
                setDoubleValue(id2, id1, value);
            }
            else {
                delRelation(id2, id1);
                delRelation(id1, id2);
                // TripleSum
                if (((MyPerson)getPerson(id1)).getAcquaintance().size()
                    < ((MyPerson)getPerson(id2)).getAcquaintance().size()) {
                    tripleSum = ((MyPerson)getPerson(id1)).delTriple((MyPerson) getPerson(id2), tripleSum);
                } else {
                    tripleSum = ((MyPerson)getPerson(id2)).delTriple((MyPerson) getPerson(id1), tripleSum);
                }
                // 并查集删边操作？？？
                rebuilddsu();
            }
        }
    }

    private void rebuilddsu() {
        // int cnt = 0;
        parents.clear();
        for (int i : people.keySet()) {
            parents.put(people.get(i), people.get(i));
            // cnt++;
        }
        // System.out.println("cnt = " + cnt);
        // cnt = 0;
        for (int i : people.keySet()) {
            MyPerson person1 = (MyPerson)people.get(i);
            for (int j = 0; j < person1.getAcquaintance().size(); j++) {
                if (!getRoot(person1.getAcquaintance().get(j)).equals(getRoot(person1))) {
                    parents.put(getRoot(person1.getAcquaintance().get(j)), getRoot(person1));
                }
                // cnt++;
            }
            // System.out.println("cnt1 = " + cnt);
            // cnt = 0;
        }
    }

    private void delRelation(int id1, int id2) {
        MyPerson person2 = ((MyPerson)getPerson(id2));
        int delId2 = -1;
        for (int i = 0; i < person2.getAcquaintance().size(); i++) {
            if (person2.getAcquaintance().get(i).equals(getPerson(id1))) {
                delId2 = i;
                break;
            }
        }
        person2.getAcquaintance().remove(delId2);
        person2.getValue().remove(delId2);
    }

    private void setDoubleValue(int id1, int id2, int value) {
        MyPerson person1 = ((MyPerson)getPerson(id1));
        for (int i = 0; i < person1.getAcquaintance().size(); i++) {
            if (person1.getAcquaintance().get(i).equals(getPerson(id2))) {
                int oldValue = person1.getValue().get(i);
                person1.getValue().set(i, oldValue + value);
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

    @Override
    public int queryTripleSum() {
        // 洛谷P1989 O(n^(3/2))算法
        return tripleSum;
    }

    @Override
    public void addGroup(Group group) throws EqualGroupIdException {
        for (int i : groups.keySet()) {
            if (groups.get(i).equals(group)) {
                throw new MyEqualGroupIdException(group.getId());
            }
        }
        groups.put(group.getId(), group);
        // System.out.println("groups add index " + group.getId());
    }

    @Override
    public Group getGroup(int id) {
        if (groups.get(id) != null) {
            return groups.get(id);
        }
        else {
            return null;
        }
    }

    @Override
    public void addToGroup(int id1, int id2) throws GroupIdNotFoundException,
            PersonIdNotFoundException, EqualPersonIdException {
        if (!groups.containsKey(id2)) {
            throw new MyGroupIdNotFoundException(id2);
        } else if (!people.containsKey(id1)) {
            throw new MyPersonIdNotFoundException(id1);
        } else if (getGroup(id2).hasPerson(getPerson(id1))) {
            throw new MyEqualPersonIdException(id1);
        } else {
            if (getGroup(id2).getSize() <= 1111) {
                getGroup(id2).addPerson(getPerson(id1));
            }
        }
    }

    @Override
    public int queryGroupValueSum(int id) throws GroupIdNotFoundException {
        if (groups.get(id) == null) {
            throw new MyGroupIdNotFoundException(id);
        } else {
            return groups.get(id).getValueSum();
        }
    }

    @Override
    public int queryGroupAgeVar(int id) throws GroupIdNotFoundException {
        if (groups.get(id) == null) {
            throw new MyGroupIdNotFoundException(id);
        } else {
            return groups.get(id).getAgeVar();
        }
    }

    @Override
    public void delFromGroup(int id1, int id2) throws GroupIdNotFoundException,
            PersonIdNotFoundException, EqualPersonIdException {
        if (!groups.containsKey(id2)) {
            throw new MyGroupIdNotFoundException(id2);
        } else if (!people.containsKey(id1)) {
            throw new MyPersonIdNotFoundException(id1);
        } else if (!getGroup(id2).hasPerson(getPerson(id1))) {
            throw new MyEqualPersonIdException(id1);
        } else {
            groups.get(id2).delPerson(getPerson(id1));
        }
    }

    @Override
    public boolean containsMessage(int id) {
        return messages.containsKey(id);
    }

    @Override
    public void addMessage(Message message) throws EqualMessageIdException, EqualPersonIdException {
        if (messages.get(message.getId()) != null) {
            throw new MyEqualMessageIdException(message.getId());
        } else if (message.getType() == 0 && message.getPerson1().equals(message.getPerson2())) {
            throw new MyEqualPersonIdException(message.getPerson1().getId());
        } else {
            messages.put(message.getId(), message);
        }

    }

    @Override
    public Message getMessage(int id) {
        return messages.get(id);
    }

    @Override
    public void sendMessage(int id) throws RelationNotFoundException,
            MessageIdNotFoundException, PersonIdNotFoundException {
        if (!messages.containsKey(id)) {
            throw new MyMessageIdNotFoundException(id);
        } else if (getMessage(id).getType() == 0 &&
                !(getMessage(id).getPerson1().isLinked(getMessage(id).getPerson2()))) {
            throw new MyRelationNotFoundException(getMessage(id).getPerson1().getId(),
                    getMessage(id).getPerson2().getId());
        } else if (getMessage(id).getType() == 1 &&
                !(getMessage(id).getGroup().hasPerson(getMessage(id).getPerson1()))) {
            throw new MyPersonIdNotFoundException(getMessage(id).getPerson1().getId());
        } else {
            if (getMessage(id).getType() == 0 && getMessage(id).getPerson1().
                    isLinked(getMessage(id).getPerson2())
                && getMessage(id).getPerson1() != getMessage(id).getPerson2()) {
                getMessage(id).getPerson1().addSocialValue(getMessage(id).getSocialValue());
                getMessage(id).getPerson2().addSocialValue(getMessage(id).getSocialValue());
                ((MyPerson)(getMessage(id).getPerson2())).addMessage(0, getMessage(id));
                messages.remove(id);
            } else if (getMessage(id).getType() == 1 &&
                    getMessage(id).getGroup().hasPerson(getMessage(id).getPerson1())) {
                ((MyGroup) getMessage(id).getGroup()).
                        addSocialValue(getMessage(id).getSocialValue());
                messages.remove(id);
            }
        }
    }

    @Override
    public int querySocialValue(int id) throws PersonIdNotFoundException {
        if (!contains(id)) {
            throw new MyPersonIdNotFoundException(id);
        } else {
            return getPerson(id).getSocialValue();
        }
    }

    @Override
    public List<Message> queryReceivedMessages(int id) throws PersonIdNotFoundException {
        if (!contains(id)) {
            throw new MyPersonIdNotFoundException(id);
        } else {
            return getPerson(id).getReceivedMessages();
        }
    }

    @Override
    public int queryBestAcquaintance(int id) throws
            PersonIdNotFoundException, AcquaintanceNotFoundException {
        if (!contains(id)) {
            throw new MyPersonIdNotFoundException(id);
        } else if (((MyPerson)getPerson(id)).getAcquaintance().size() == 0) {
            throw new MyAcquaintanceNotFoundException(id);
        } else {
            MyPerson person = ((MyPerson)getPerson(id));
            int bestId = person.getAcquaintance().get(0).getId();
            int bestVal = person.getValue().get(0);
            for (int i = 1; i < person.getAcquaintance().size(); i++) {
                if (person.getValue().get(i) > bestVal) {
                    bestId = person.getAcquaintance().get(i).getId();
                    bestVal = person.getValue().get(i);
                } else if (person.getValue().get(i) == bestVal) {
                    if (person.getAcquaintance().get(i).getId() < bestId) {
                        bestId = person.getAcquaintance().get(i).getId();
                    }
                }
            }
            return bestId;
        }
    }

    @Override
    public int queryCoupleSum() {
        if (!flagCou) {
            return oldcoup;
        }
        int sum = 0;
        for (int i : people.keySet()) {
            MyPerson person1 = (MyPerson) people.get(i);
            if (person1.getAcquaintance().isEmpty()) {
                continue;
            }
            int idi;
            try {
                idi = this.queryBestAcquaintance(i);
            } catch (PersonIdNotFoundException | AcquaintanceNotFoundException e) {
                throw new RuntimeException(e);
            }
            if (((MyPerson) getPerson(idi)).getAcquaintance().isEmpty() ||
                idi > i) {
                continue;
            }
            int idj;
            try {
                idj = this.queryBestAcquaintance(idi);
            } catch (PersonIdNotFoundException | AcquaintanceNotFoundException e) {
                throw new RuntimeException(e);
            }
            if (idj == i) {
                sum++;
            }
        }
        oldcoup = sum;
        flagCou = false;
        return sum;
    }

    @Override
    public int modifyRelationOKTest(int id1, int id2, int value,
                                    HashMap<Integer, HashMap<Integer, Integer>> beforeData,
                                    HashMap<Integer, HashMap<Integer, Integer>> afterData) {
        return OkTest.modifyRelationOkTest(id1, id2, value, beforeData, afterData);
    }

    public Person getRoot(Person person) { // 并查集查找根节点
        Person root = person; // 从底向上找根
        while (parents.get(root) != root) {
            // System.out.println("ID " + person.getId() + " is finding...");
            root = parents.get(root);
        }
        if (!root.equals(person)) {
            parents.put(person, root);
        }
        return root;
    }
}
