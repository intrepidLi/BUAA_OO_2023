package com.oocourse.spec3.main;

import com.oocourse.spec3.exceptions.EmojiIdNotFoundException;
import com.oocourse.spec3.exceptions.EqualMessageIdException;
import com.oocourse.spec3.exceptions.EqualPersonIdException;
import com.oocourse.spec3.exceptions.MyEmojiIdNotFoundException;
import com.oocourse.spec3.exceptions.MyEqualPersonIdException;
import com.oocourse.spec3.exceptions.MyPersonIdNotFoundException;
import com.oocourse.spec3.exceptions.MyRelationNotFoundException;
import com.oocourse.spec3.exceptions.PersonIdNotFoundException;
import com.oocourse.spec3.exceptions.RelationNotFoundException;
import com.oocourse.spec3.exceptions.PathNotFoundException;
import com.oocourse.spec3.exceptions.MyPathNotFoundException;
import com.oocourse.spec3.exceptions.MyEqualMessageIdException;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Objects;
import java.util.Comparator;

public class MyNetwork2 {
    // 全局建图
    private static int max = 0x3f3f3f3f;

    /*public static int findPath(HashMap<Integer, Person> people, int id) {
        if (people.size() < 3) {
            return -1;
        }
        if (((MyPerson)people.get(id)).getAcquaintance().size() == 0) {
            return -1;
        }
        // 建图
        int max = Integer.MAX_VALUE; // 无法到达时距离设为Max
        HashMap<MyPair, Integer> adjMatrix = new HashMap<>();
        for (int i : people.keySet()) {
            for (int j : people.keySet()) {
                MyPair myPair = new MyPair(people.get(i), people.get(j));
                if (i == j) {
                    adjMatrix.put(myPair, 0);
                } else {
                    adjMatrix.put(myPair, max);
                }
            }
        }

        for (int i : people.keySet()) {
            MyPerson personi = (MyPerson)people.get(i);
            if (personi.getId() != id) {
                for (int j = 0; j < personi.getAcquaintance().size(); j++) {
                    MyPerson person = (MyPerson) personi.getAcquaintance().get(j);
                    MyPair myPair1 = new MyPair(person, personi);
                    MyPair myPair2 = new MyPair(personi, person);
                    adjMatrix.put(myPair1, personi.getValue().get(j));
                    adjMatrix.put(myPair2, personi.getValue().get(j));
                }
            }
        }

        HashMap<Integer, Integer> dis = new HashMap<>();
        HashMap<Integer, Integer> book = new HashMap<>();
        HashMap<Integer, Integer> path = new HashMap<>();

        int minRound = Integer.MAX_VALUE;
        for (Person person : ((MyPerson)people.get(id)).getAcquaintance()) {
            MyPair tempPair = new MyPair(people.get(id), person);
            MyPair tempPair1 = new MyPair(person, people.get(id));
            adjMatrix.put(tempPair, max);
            adjMatrix.put(tempPair1, max);
            dijkstra(dis, book, path, adjMatrix, people, id, person.getId());
            if (dis.get(person.getId()) != Integer.MAX_VALUE) {
                int distance = dis.get(person.getId()) + people.get(id).queryValue(person);
                // int personId = person.getId();
                // while (path.get(personId) != -1) {
                //     // System.out.println("pass " + personId);
                //     personId = path.get(personId);
                //     // System.out.println("pass " + personId);
                // }
                if (distance < minRound) {
                    minRound = distance;
                }
            }
            adjMatrix.put(tempPair, people.get(id).queryValue(person));
            adjMatrix.put(tempPair1, people.get(id).queryValue(person));
        }
        if (minRound == max) {
            minRound = -1;
        }
        return minRound;
    }*/

    /*private static void dijkstra(HashMap<Integer, Integer> dis, HashMap<Integer, Integer> book,
                                 HashMap<Integer, Integer> path, HashMap<MyPair, Integer> adjMatrix,
                                 HashMap<Integer, Person> people, int id, int end) {
        int min; // min存储起点到尚未访问过的人中距离最小的人的距离。
        int u = id;
        for (int i : people.keySet()) {
            path.put(i, -1);
            book.put(i, 0);
            dis.put(i, Integer.MAX_VALUE);
        }
        dis.put(id, 0);
        //优先队列优化：
        PriorityQueue<Integer> pq = new PriorityQueue<>((a, b) -> dis.get(a) - dis.get(b));
        pq.offer(id); // 将源节点放入
        while (!pq.isEmpty()) {
            int u1 = pq.poll(); // 取出距离最小的结点
            if (book.get(u1) == 1) {
                continue;
            }
            book.put(u1, 1); // 标记结点已经被访问过
            for (int i : people.keySet()) {
                if ((id == u1 && end == i) || (id == i && end == u1)) {
                    continue;
                }
                MyPair mypair1 = new MyPair(people.get(u1), people.get(i));
                if (adjMatrix.get(mypair1) < Integer.MAX_VALUE &&
                    dis.get(i) > dis.get(u1) + adjMatrix.get(mypair1)) {
                    dis.put(i, dis.get(u1) + adjMatrix.get(mypair1));
                    path.put(i, u1);
                    pq.offer(i);
                }
            }
        }


        /*for (int i : people.keySet()) {
            min = Integer.MAX_VALUE;
            for (int j : people.keySet()) {
                if (book.get(j) == 0 && dis.get(j) < min) { // ID为j的人未被访问过，且到j的最短距离小于min
                    min = dis.get(j);
                    u = j;
                }
            }
            book.put(u, 1); // u结点被访问过了

            for (int v : people.keySet()) {
                // if ((id == u && end == v) || (id == v && end == u)) {
                //     System.out.println("u1 = " + u);
                //     System.out.println("v1 = " + v);
                //     continue;
                // }
                // System.out.println("u = " + u);
                MyPair myPair1 = new MyPair(people.get(u), people.get(v));
                // System.out.println("myPair = " + myPair1);
                // System.out.println("adj: " + adjMatrix.get(myPair1));
                if (adjMatrix.get(myPair1) < Integer.MAX_VALUE &&
                dis.get(v) > dis.get(u) + adjMatrix.get(myPair1)) {
                    // System.out.println("dis[" + v + "]=" + dis.get(v));
                    // System.out.println(dis.get(u) + adjMatrix.get(myPair1));
                    dis.put(v, dis.get(u) + adjMatrix.get(myPair1));
                    // System.out.println("u2 = " + u);
                    // System.out.println("v2 = " + v);
                    path.put(v, u);
                }
            }
        }
    }*/

    public static int deleteColdEmojiOkTest(int limit,
                                            ArrayList<HashMap<Integer, Integer>> beforeData,
                               ArrayList<HashMap<Integer, Integer>> afterData, int result) {
        HashMap<Integer, Integer> oldEmojiIdList = beforeData.get(0);
        HashMap<Integer, Integer> emojiIdList = afterData.get(0);
        for (int i : oldEmojiIdList.keySet()) {
            if (oldEmojiIdList.get(i) >= limit) {
                if (!emojiIdList.containsKey(i)) {
                    return 1;
                }
            }
        }
        for (int i : emojiIdList.keySet()) {
            if (!oldEmojiIdList.containsKey(i)) {
                return 2;
            }
            if (!oldEmojiIdList.get(i).equals(emojiIdList.get(i))) {
                return 2;
            }
        }
        int len = 0;
        for (int i : oldEmojiIdList.keySet()) {
            if (oldEmojiIdList.get(i) >= limit) {
                len++;
            }
        }
        if (len != emojiIdList.size()) {
            return 3;
        }
        for (int i : emojiIdList.keySet()) {
            if (emojiIdList.get(i) == null) {
                return 4;
            }
        }
        HashMap<Integer, Integer> oldMessageIdList = beforeData.get(1);
        HashMap<Integer, Integer> messageIdList = afterData.get(1);
        if (check5case(oldEmojiIdList, emojiIdList, oldMessageIdList, messageIdList)) {
            return 5;
        }
        for (int i : oldMessageIdList.keySet()) {
            if (!oldEmojiIdList.containsKey(oldMessageIdList.get(i))) {
                if (!messageIdList.containsKey(i) ||
                        !Objects.equals(messageIdList.get(i), oldMessageIdList.get(i))) {
                    return 6;
                }
            }
        }
        if (check7case(oldEmojiIdList, emojiIdList, oldMessageIdList, messageIdList)) {
            return 7;
        }
        if (result != emojiIdList.size()) {
            return 8;
        }
        return 0;
    }

    private static boolean check7case(HashMap<Integer, Integer> oldEmojiIdList,
                                      HashMap<Integer, Integer> emojiIdList,
                                      HashMap<Integer, Integer> oldMessageIdList,
                                      HashMap<Integer, Integer> messageIdList) {
        int len = 0;
        for (int i : oldMessageIdList.keySet()) {
            if (oldEmojiIdList.containsKey(oldMessageIdList.get(i)) &&
                !emojiIdList.containsKey(oldMessageIdList.get(i))) {
                continue;
            }
            len++; //EmojiMessage数量
        }
        return len != messageIdList.size();
    }

    public static boolean check5case(HashMap<Integer, Integer> oldEmojiIdList,
                                     HashMap<Integer, Integer> emojiIdList,
                                     HashMap<Integer, Integer> oldMessageIdList,
                                     HashMap<Integer, Integer> messageIdList) {
        for (int i : oldMessageIdList.keySet()) {
            if (oldEmojiIdList.containsKey(oldMessageIdList.get(i))) {
                if (emojiIdList.containsKey(oldMessageIdList.get(i))) {
                    if (!messageIdList.containsKey(i) ||
                            !Objects.equals(messageIdList.get(i), oldMessageIdList.get(i))) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static void addPerson(HashMap<Integer, Person> people, HashMap<Person, Person> parents,
                                 Person person, HashMap<MyPair, Integer> adjMatrix)
            throws MyEqualPersonIdException {
        for (int i : people.keySet()) {
            if (people.get(i).equals(person)) {
                throw new MyEqualPersonIdException(person.getId());
            }
        }
        for (int i : people.keySet()) {
            adjMatrix.put(new MyPair(person, people.get(i)), max);
            adjMatrix.put(new MyPair(people.get(i), person), max);
        }
        people.put(person.getId(), person);
        parents.put(person, person); // 新加入的结点父亲是null
        adjMatrix.put(new MyPair(person, person), 0);
    }

    public static int queryLeastMoments(int id,
                                        HashMap<Integer, Person> people,
                                        HashMap<MyPair, Integer> adjMatrix)
            throws PersonIdNotFoundException, PathNotFoundException {
        if (!people.containsKey(id)) {
            throw new MyPersonIdNotFoundException(id);
        } else {
            int res = MyNetwork2.findPath2(people, id, adjMatrix);
            if (res == -1) {
                throw new MyPathNotFoundException(id);
            } else {
                return res;
            }
        }
    }

    public static void addMessage(Message message, HashMap<Integer, Message> messages,
                           HashMap<Integer, Integer> emojis)
            throws EqualMessageIdException, EqualPersonIdException, EmojiIdNotFoundException {
        if (messages.get(message.getId()) != null) {
            throw new MyEqualMessageIdException(message.getId());
        } else if (message instanceof EmojiMessage &&
                !emojis.containsKey(((EmojiMessage) message).getEmojiId()))  {
            throw new MyEmojiIdNotFoundException(((EmojiMessage) message).getEmojiId());
        } else if (message.getType() == 0 && message.getPerson1().equals(message.getPerson2())) {
            throw new MyEqualPersonIdException(message.getPerson1().getId());
        }
        else {
            messages.put(message.getId(), message);
        }
    }

    public static int queryValue(int id1, int id2, HashMap<Integer, Person> people)
            throws PersonIdNotFoundException, RelationNotFoundException {
        if (!people.containsKey(id1)) {
            throw new MyPersonIdNotFoundException(id1);
        } else if (people.containsKey(id1) && !people.containsKey(id2)) {
            throw new MyPersonIdNotFoundException(id2);
        }
        Person person1 = people.get(id1);
        Person person2 = people.get(id2);
        if (!person1.isLinked(person2)) {
            throw new MyRelationNotFoundException(id1, id2);
        }
        return person1.queryValue(person2);
    }

    public static void delRelation(int id1, int id2, HashMap<Integer, Person> people,
                                   HashMap<MyPair, Integer> adjMatrix) {
        MyPerson person2 = ((MyPerson)people.get(id2));
        int delId2 = -1;
        for (int i = 0; i < person2.getAcquaintance().size(); i++) {
            if (person2.getAcquaintance().get(i).equals(people.get(id1))) {
                delId2 = i;
                break;
            }
        }
        adjMatrix.remove(new MyPair(person2, people.get(id1)));
        person2.getAcquaintance().remove(delId2);
        person2.getValue().remove(delId2);
    }

    private static HashMap<Integer, Integer> father = new HashMap<>();

    public static int findPath2(HashMap<Integer, Person> people,
                                int id, HashMap<MyPair, Integer> adjMatrix) {
        if (people.size() < 3) {
            return -1;
        }
        if (((MyPerson)people.get(id)).getAcquaintance().size() == 0) {
            return -1;
        }
        HashMap<Integer, Integer> dis = new HashMap<>();
        HashMap<Integer, Integer> book = new HashMap<>();
        HashMap<Integer, Integer> path = new HashMap<>();
        dijkstra2(dis, book, path, adjMatrix, people, id);
        int ans = max;
        for (int i : father.keySet()) {
            if (i == id) {
                continue;
            }
            MyPerson personi = (MyPerson) people.get(i);
            for (Person personj : ((MyPerson) people.get(i)).getAcquaintance()) {
                // MyPerson personj = (MyPerson) personi.getAcquaintance().get(j);
                if (personj.getId() == id) {
                    continue;
                }
                if (!Objects.equals(father.get(personi.getId()), father.get(personj.getId()))) {
                    // System.out.println("1: " + i + " 2: " + personj.getId());
                    ans = Math.min(ans, dis.get(personi.getId()) +
                            dis.get(personj.getId()) +
                            adjMatrix.get(new MyPair(personi, personj)));
                }
            }
        }
        // System.out.println("fuckkk = " + path.get(22));
        MyPerson personSou = (MyPerson) people.get(id);
        for (Person personj : personSou.getAcquaintance()) {
            // MyPerson personj = (MyPerson) personSou.getAcquaintance().get(j);
            if (!Objects.equals(path.get(personj.getId()), id)) {
                // System.out.println("root: " + personj.getId());
                ans = Math.min(ans,
                        dis.get(personj.getId()) + adjMatrix.get(new MyPair(personSou, personj)));
            }
        }
        if (ans == max) {
            return -1;
        }
        father.clear();
        return ans;
    }

    private static void dijkstra2(HashMap<Integer, Integer> dis, HashMap<Integer, Integer> book,
                                 HashMap<Integer, Integer> path, HashMap<MyPair, Integer> adjMatrix,
                                 HashMap<Integer, Person> people, int id) {
        //优先队列优化：
        PriorityQueue<Integer> pq = new PriorityQueue<>(Comparator.comparingInt(dis::get));
        // min存储起点到尚未访问过的人中距离最小的人的距离。
        for (int i : people.keySet()) {
            path.put(i, -1);
            book.put(i, 0);
            dis.put(i, max);
            if (people.get(i).isLinked(people.get(id))) {
                father.put(i, i);
                // dis.put(i, people.get(i).queryValue(people.get(id)));
                // pq.offer(i);
                //path.put(i, id);
            }
        }
        dis.put(id, 0);

        pq.offer(id); // 将源节点放入
        while (!pq.isEmpty()) {
            int u1 = pq.poll(); // 取出距离最小的结点
            if (book.get(u1) == 1) {
                continue;
            }
            book.put(u1, 1); // 标记结点已经被访问过
            MyPerson personu1 = (MyPerson)(people.get(u1));
            for (Person personi : personu1.getAcquaintance()) {
                // MyPair mypair1 = new MyPair(people.get(u1), personi);
                int temp = personu1.queryValue(personi);
                if (temp < max &&
                        dis.get(personi.getId()) > dis.get(u1) + temp) {
                    dis.put(personi.getId(), dis.get(u1) + temp);
                    path.put(personi.getId(), u1);
                    if (u1 != id) {
                        father.put(personi.getId(), father.get(u1));
                    }
                    // pq.remove(personi.getId());
                    pq.offer(personi.getId());
                }
            }
        }
    }
}
