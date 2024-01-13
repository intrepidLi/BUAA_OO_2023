package com.oocourse.spec2.exceptions;

public class MyEqualGroupIdException extends EqualGroupIdException {
    private static int cnt = 0;
    private static Counter counter = new Counter();
    private static int id;

    public MyEqualGroupIdException(int id) {
        MyEqualGroupIdException.id = id;
        cnt++;
        counter.addCnt(id);
    }

    @Override
    public void print() {
        System.out.println(String.format("egi-%d, %d-%d", cnt, id,
                counter.getAllCnt(id)));
    }
}
