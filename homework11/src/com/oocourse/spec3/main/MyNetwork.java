package com.oocourse.spec3.main;

import com.oocourse.spec3.exceptions.EmojiIdNotFoundException;
import com.oocourse.spec3.exceptions.EqualEmojiIdException;
import com.oocourse.spec3.exceptions.EqualGroupIdException;
import com.oocourse.spec3.exceptions.EqualMessageIdException;
import com.oocourse.spec3.exceptions.EqualPersonIdException;
import com.oocourse.spec3.exceptions.EqualRelationException;
import com.oocourse.spec3.exceptions.GroupIdNotFoundException;
import com.oocourse.spec3.exceptions.MessageIdNotFoundException;
import com.oocourse.spec3.exceptions.PersonIdNotFoundException;
import com.oocourse.spec3.exceptions.RelationNotFoundException;
import com.oocourse.spec3.exceptions.PathNotFoundException;
import com.oocourse.spec3.exceptions.AcquaintanceNotFoundException;
import com.oocourse.spec3.exceptions.MyEmojiIdNotFoundException;
import com.oocourse.spec3.exceptions.MyEqualEmojiIdException;
import com.oocourse.spec3.exceptions.MyEqualGroupIdException;
import com.oocourse.spec3.exceptions.MyEqualPersonIdException;
import com.oocourse.spec3.exceptions.MyEqualRelationException;
import com.oocourse.spec3.exceptions.MyGroupIdNotFoundException;
import com.oocourse.spec3.exceptions.MyMessageIdNotFoundException;
import com.oocourse.spec3.exceptions.MyPersonIdNotFoundException;
import com.oocourse.spec3.exceptions.MyRelationNotFoundException;
import com.oocourse.spec3.exceptions.MyAcquaintanceNotFoundException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class MyNetwork implements Network {
    private HashMap<Integer, Group> groups = new HashMap<>();
    private HashMap<Integer, Message> messages = new HashMap<>();
    private HashMap<Person, Person> parents = new HashMap<>(); // 存储子节点和父节点
    private HashMap<Integer, Person> people = new HashMap<>(); // id person
    private HashMap<Integer, Integer> emojis = new HashMap<>(); // EmojiId, EmojiHeat
    private HashMap<MyPair, Integer> adjMatrix = new HashMap<>(); // qlm全局邻接矩阵
    private static boolean flagCou;
    private static int oldcoup;
    private int tripleSum;
    private int max = 0x3f3f3f3f;

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
        MyNetwork2.addPerson(people, parents, person, adjMatrix);
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
            adjMatrix.put(new MyPair(person1, person2), value);
            person2.addAcquaintance(person1, value);
            adjMatrix.put(new MyPair(person2, person1), value);
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
                MyNetwork2.delRelation(id2, id1, people, adjMatrix);
                MyNetwork2.delRelation(id1, id2, people, adjMatrix);
                // TripleSum
                if (((MyPerson)getPerson(id1)).getAcquaintance().size()
                    < ((MyPerson)getPerson(id2)).getAcquaintance().size()) {
                    tripleSum = ((MyPerson)getPerson(id1)).
                            delTriple((MyPerson) getPerson(id2), tripleSum);
                } else {
                    tripleSum = ((MyPerson)getPerson(id2)).
                            delTriple((MyPerson) getPerson(id1), tripleSum);
                }
                rebuilddsu();
            }
        }
    }

    private void rebuilddsu() {
        parents.clear();
        for (int i : people.keySet()) {
            parents.put(people.get(i), people.get(i));
        }
        for (int i : people.keySet()) {
            MyPerson person1 = (MyPerson)people.get(i);
            for (int j = 0; j < person1.getAcquaintance().size(); j++) {
                if (!getRoot(person1.getAcquaintance().get(j)).equals(getRoot(person1))) {
                    parents.put(getRoot(person1.getAcquaintance().get(j)), getRoot(person1));
                }
            }
        }
    }

    private void setDoubleValue(int id1, int id2, int value) {
        MyPerson person1 = ((MyPerson)getPerson(id1));
        for (int i = 0; i < person1.getAcquaintance().size(); i++) {
            if (person1.getAcquaintance().get(i).equals(getPerson(id2))) {
                int oldValue = person1.getValue().get(i);
                person1.getValue().set(i, oldValue + value);
                adjMatrix.put(new MyPair(person1, getPerson(id2)), oldValue + value);
            }
        }
    }

    @Override
    public int queryValue(int id1, int id2)
            throws PersonIdNotFoundException, RelationNotFoundException {
        return MyNetwork2.queryValue(id1, id2, people);
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
    public void addMessage(Message message) throws EqualMessageIdException,
            EqualPersonIdException, EmojiIdNotFoundException {
        MyNetwork2.addMessage(message, messages, emojis);
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
            Message message = getMessage(id);
            if (message.getType() == 0 && message.getPerson1().
                    isLinked(message.getPerson2())
                && message.getPerson1() != message.getPerson2()) {
                message.getPerson1().addSocialValue(message.getSocialValue());
                message.getPerson2().addSocialValue(message.getSocialValue());
                if (message instanceof RedEnvelopeMessage) {
                    message.getPerson1().addMoney(-1 * ((RedEnvelopeMessage) message).getMoney());
                    message.getPerson2().addMoney(((RedEnvelopeMessage) message).getMoney());
                }
                ((MyPerson)(getMessage(id).getPerson2())).addMessage(0, getMessage(id));
            } else if (message.getType() == 1 &&
                    message.getGroup().hasPerson(message.getPerson1())) {
                ((MyGroup) message.getGroup()).
                        addSocialValue(message.getSocialValue());
                if (message instanceof RedEnvelopeMessage) {
                    int moneyToAdd;
                    moneyToAdd = ((RedEnvelopeMessage) message)
                            .getMoney() / message.getGroup().getSize();
                    message.getPerson1().addMoney(-1 * moneyToAdd
                            * (message.getGroup().getSize() - 1));
                    for (int i : people.keySet()) {
                        if (message.getGroup().hasPerson(people.get(i))) {
                            if (!people.get(i).equals(message.getPerson1())) {
                                people.get(i).addMoney(moneyToAdd);
                            }
                        }
                    }
                }
            }
            if (message instanceof EmojiMessage) {
                int eid = ((EmojiMessage) message).getEmojiId();
                emojis.put(eid, emojis.get(eid) + 1);
            }
            messages.remove(id);
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
    public boolean containsEmojiId(int id) {
        return emojis.containsKey(id);
    }

    @Override
    public void storeEmojiId(int id) throws EqualEmojiIdException {
        if (emojis.containsKey(id)) {
            throw new MyEqualEmojiIdException(id);
        } else {
            emojis.put(id, 0);
        }
    }

    @Override
    public int queryMoney(int id) throws PersonIdNotFoundException {
        if (!contains(id)) {
            throw new MyPersonIdNotFoundException(id);
        }
        return getPerson(id).getMoney();
    }

    @Override
    public int queryPopularity(int id) throws EmojiIdNotFoundException {
        if (!emojis.containsKey(id)) {
            throw new MyEmojiIdNotFoundException(id);
        } else {
            return emojis.get(id);
        }
    }

    @Override
    public int deleteColdEmoji(int limit) {
        ArrayList<Integer> deleteId = new ArrayList<>();
        for (int i : emojis.keySet()) {
            if (emojis.get(i) < limit) {
                deleteId.add(i);
            }
        }
        for (int i : deleteId) {
            emojis.remove(i);
        }
        deleteId.clear();
        for (int i : messages.keySet()) {
            if (messages.get(i) instanceof EmojiMessage) {
                if (!containsEmojiId(((EmojiMessage) messages.get(i)).getEmojiId())) {
                    deleteId.add(i);
                }
            }
        }

        for (int i : deleteId) {
            messages.remove(i);
        }
        return emojis.size();
    }

    @Override
    public void clearNotices(int personId) throws PersonIdNotFoundException {
        if (!contains(personId)) {
            throw new MyPersonIdNotFoundException(personId);
        } else {
            ((MyPerson)people.get(personId)).clearNotices();
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
    public int queryLeastMoments(int id) throws PersonIdNotFoundException, PathNotFoundException {
        return MyNetwork2.queryLeastMoments(id, people, adjMatrix);
    }

    @Override
    public int deleteColdEmojiOKTest(int limit, ArrayList<HashMap<Integer, Integer>> beforeData,
                                     ArrayList<HashMap<Integer, Integer>> afterData, int result) {
        return MyNetwork2.deleteColdEmojiOkTest(limit, beforeData, afterData, result);
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
