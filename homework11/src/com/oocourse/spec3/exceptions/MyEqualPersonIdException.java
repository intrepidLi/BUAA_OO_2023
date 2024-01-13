package com.oocourse.spec3.exceptions;

public class MyEqualPersonIdException extends EqualPersonIdException {
    private static int id;
    private static Counter counter = new Counter();
    private static int cnt = 0;

    @Override
    public void print() {
        System.out.println(String.format("epi-%d, %d-%d", cnt, id,
                counter.getAllCnt(id)));
    }

    public MyEqualPersonIdException(int id) {
        MyEqualPersonIdException.id = id;
        cnt++;
        counter.addCnt(id);
    }
}
