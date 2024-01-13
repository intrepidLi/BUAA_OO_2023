package com.oocourse.spec1;

import com.oocourse.spec1.main.MyNetwork;
import com.oocourse.spec1.main.MyPerson;
import com.oocourse.spec1.main.Runner;

public class MainClass {
    public static void main(String[] args) throws Exception {
        Runner runner = new Runner(MyPerson.class, MyNetwork.class);
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

/* 测试数据四
qtsok 3 1 2 3 1 2 3 2 1 3 3 4 1 2 4 3 1 2 3 1 2 3 2 1 3 3 4 1 2 4 0
qtsok 4 1 2 3 4 3 2 1 3 1 4 1 2 1 1 3 1 3 1 1 2 1 4 1 2 1 1 3 1 4 1 2 3 4 3 2 1 3 1 4 1 2 1 1 3 1 3 1 1 2 1 4 1 2 1 1 3 1 2
qtsok 3 1 2 3 2 2 1 3 1 2 1 1 3 1 2 2 1 1 1 3 1 2 3 2 2 1 3 1 2 1 1 3 1 2 2 1 1 1 1
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