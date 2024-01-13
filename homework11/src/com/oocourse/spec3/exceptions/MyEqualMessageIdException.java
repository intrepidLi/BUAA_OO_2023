package com.oocourse.spec3.exceptions;

public class MyEqualMessageIdException extends EqualMessageIdException {
    private static int cnt = 0;
    private static int id;
    private static Counter counter = new Counter();

    public MyEqualMessageIdException(int id) {
        MyEqualMessageIdException.id = id;
        cnt++;
        counter.addCnt(id);
    }

    @Override
    public void print() {
        System.out.println(String.format("emi-%d, %d-%d", cnt, id,
                counter.getAllCnt(id)));
    }
}
