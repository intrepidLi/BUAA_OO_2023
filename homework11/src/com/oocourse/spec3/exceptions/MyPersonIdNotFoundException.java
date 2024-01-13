package com.oocourse.spec3.exceptions;

public class MyPersonIdNotFoundException extends PersonIdNotFoundException {
    private static int cnt = 0;
    private static Counter counter = new Counter();
    private static int id;

    public MyPersonIdNotFoundException(int id) {
        cnt++;
        counter.addCnt(id);
        MyPersonIdNotFoundException.id = id;
    }

    @Override
    public void print() {
        System.out.println(String.format("pinf-%d, %d-%d", cnt, id,
                counter.getAllCnt(id)));
    }
}
