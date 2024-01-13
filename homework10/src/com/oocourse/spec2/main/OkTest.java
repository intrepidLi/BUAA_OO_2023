package com.oocourse.spec2.main;

import java.util.HashMap;
import java.util.Objects;

public class OkTest {
    public static int modifyRelationOkTest(int id1, int id2, int value,
                                           HashMap<Integer, HashMap<Integer, Integer>> beforeData,
                                           HashMap<Integer, HashMap<Integer, Integer>> afterData) {
        if (!beforeData.containsKey(id1) || !beforeData.containsKey(id2) || id1 == id2 ||
                !beforeData.get(id1).containsKey(id2)) {
            if (beforeData.equals(afterData)) {
                return 0;
            } else {
                return -1;
            }
        }
        /*if (!afterData.containsKey(id1) || !afterData.containsKey(id2) ||
                !afterData.get(id1).containsKey(id2)) {
            if (beforeData.equals(afterData)) {
                return 0;
            } else {
                return -1;
            }
        }*/
        int flag = 0;
        for (int i : beforeData.keySet()) {
            if (i != id1 && i != id2
                    && !Objects.equals(beforeData.get(i), afterData.get(i))) {
                flag = 1;
            }
        }
        if (beforeData.size() != afterData.size()) {
            return 1;
        } else if (!beforeData.keySet().equals(afterData.keySet())) {
            return 2;
        } else if (flag == 1) {
            return 3;
        } else if (beforeData.get(id1).get(id2) + value > 0) {
            return judgeLarge(id1, id2, value, beforeData, afterData);
        } else {
            return judgeLess(id1, id2, value, beforeData, afterData);
        }
    }

    public static int judgeLess(int id1, int id2, int value,
                                HashMap<Integer, HashMap<Integer, Integer>> beforeData
            , HashMap<Integer, HashMap<Integer, Integer>> afterData) {
        if (afterData.get(id1).containsKey(id2) || afterData.get(id2).containsKey(id1)) {
            return 15;
        } else if (beforeData.get(id1).size() != afterData.get(id1).size() + 1) {
            return 16;
        } else if (beforeData.get(id2).size() != afterData.get(id2).size() + 1) {
            return 17;
        } else {
            for (int i : afterData.get(id1).keySet()) {
                if (!Objects.equals(beforeData.get(id1).get(i), afterData.get(id1).get(i))) {
                    return 20;
                }
            }
            for (int i : afterData.get(id2).keySet()) {
                if (!Objects.equals(beforeData.get(id2).get(i), afterData.get(id2).get(i))) {
                    return 21;
                }
            }
            return 0;
        }
    }

    public static int judgeLarge(int id1, int id2, int value,
                                 HashMap<Integer, HashMap<Integer, Integer>> beforeData
            , HashMap<Integer, HashMap<Integer, Integer>> afterData) {
        if (!afterData.get(id1).containsKey(id2) || !afterData.get(id2).containsKey(id1)) {
            return 4;
        } else if (afterData.get(id1).get(id2) != (beforeData.get(id1).get(id2) + value)) {
            return 5;
        } else if (afterData.get(id2).get(id1) != (beforeData.get(id2).get(id1) + value)) {
            return 6;
        } else if (beforeData.get(id1).size() != afterData.get(id1).size()) {
            return 7;
        } else if (beforeData.get(id2).size() != afterData.get(id2).size()) {
            return 8;
        } else if (!beforeData.get(id1).keySet().equals(afterData.get(id1).keySet())) {
            return 9;
        } else if (!beforeData.get(id2).keySet().equals(afterData.get(id2).keySet())) {
            return 10;
        } else if (judgeEleTwi(id1, id2, beforeData,afterData) == 1) {
            return 11;
        } else if (judgeEleTwi(id2, id1, beforeData, afterData) == 1) {
            return 12;
        } else {
            return 0;
        }
    }

    public static int judgeEleTwi(int id1, int id2,
                                  HashMap<Integer, HashMap<Integer, Integer>> beforeData
            , HashMap<Integer, HashMap<Integer, Integer>> afterData) {
        int flag11 = 0;
        for (int i : beforeData.get(id1).keySet()) {
            if (i != id2) {
                if (!Objects.equals(beforeData.get(id1).get(i), afterData.get(id1).get(i))) {
                    flag11 = 1;
                }
            }
        }
        return flag11;
    }
}
