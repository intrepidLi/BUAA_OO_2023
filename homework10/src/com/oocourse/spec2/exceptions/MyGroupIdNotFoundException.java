package com.oocourse.spec2.exceptions;

public class MyGroupIdNotFoundException extends GroupIdNotFoundException {
    private static int cnt = 0;
    private static Counter counter = new Counter();
    private static int id;

    public MyGroupIdNotFoundException(int id) {
        MyGroupIdNotFoundException.id = id;
        cnt++;
        counter.addCnt(id);
    }

    @Override
    public void print() {
        System.out.println(String.format("ginf-%d, %d-%d", cnt, id,
                counter.getAllCnt(id)));
    }
}
