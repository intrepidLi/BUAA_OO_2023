package com.oocourse.spec2;

import com.oocourse.spec2.main.MyMessage;
import com.oocourse.spec2.main.MyPerson;
import com.oocourse.spec2.main.Runner;
import com.oocourse.spec2.main.MyNetwork;
import com.oocourse.spec2.main.MyGroup;

public class MainClass {
    public static void main(String[] args) throws Exception {
        Runner runner = new Runner(MyPerson.class, MyNetwork.class, MyGroup.class,
                MyMessage.class);
        runner.run();
    }
}

/* 测试数据
*  ap 1 2 3
*  ap 2 2 3
*  ap 3 2 3
*  ap 4 2 3
*  ap 5 2 3
*  ar 1 2 1
*  ar 2 3 1
*  ar 3 5 1
*  ar 5 4 1
*  ar 4 2 1
*  ar 5 2 1
*  ar 1 4 1
*  ar 3 4 1
*  qts
* */
/* 测试数据二
ap 1 2 3
ap 2 2 3
ap 3 2 3
ar 1 2 1
ar 2 3 1
ar 3 1 1
qts
* */

/*测试数据三
ap 1 2 3
ap 2 2 3
ap 3 2 3
ap 4 2 3
ar 1 2 1
ar 2 3 1
ar 3 1 1
ar 1 4 1
qts
* */

/* 测试数据五
ap 1 2 3
ap 2 2 3
ap 3 2 3
ar 1 2 1
ar 2 3 1
ar 1 3 1
qts

ap 1 2 3
ap 2 2 3
ap 3 2 3
ap 4 2 3
ar 1 2 1
ar 2 3 1
ar 1 3 1
ar 3 4 1
ar 1 4 1
qts
* */

/*
* ap 1 2 3
* ap 2 2 3
* ap 3 2 3
* ar 2 3 1
* ap 4 2 3
* ar 3 4 1
* ap 5 2 3
* ap 6 2 3
* ap 7 2 3
* ar 6 7 1
* ap 8 2 3
* ar 6 8 1
* ap 9 2 3
* ar 7 3 1
* qci 4 8
* */
// 暴力qts算法 O(n^3)
    /*int cnt = 0;
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
                int flag1 = 0;
                if (deg.get(i) > deg.get(person1.getId())) {
                    flag1 = 1;
                }
                else if (i > person1.getId() && Objects.equals(deg.get(i),
                        deg.get(person1.getId()))) {
                    flag1 = 1;
                }
                if (flag1 == 1) {
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
                        System.out.println("triple: " + v + " " + w + " " + i);
                    }
                }
            }
        }*/