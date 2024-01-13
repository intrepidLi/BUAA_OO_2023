package com.oocourse.spec3.exceptions;

public class MyPathNotFoundException extends PathNotFoundException {
    private static int cnt = 0;
    private static Counter counter = new Counter();
    private static int id;

    public MyPathNotFoundException(int id) {
        MyPathNotFoundException.id = id;
        cnt++;
        counter.addCnt(id);
    }

    @Override
    public void print() {
        System.out.println(String.format("pnf-%d, %d-%d", cnt, id,
                counter.getAllCnt(id)));
    }
}
