package com.oocourse.spec3.exceptions;

public class MyMessageIdNotFoundException extends MessageIdNotFoundException {
    private static int cnt = 0;
    private static Counter counter = new Counter();
    private static int id;

    public MyMessageIdNotFoundException(int id) {
        MyMessageIdNotFoundException.id = id;
        cnt++;
        counter.addCnt(id);
    }

    @Override
    public void print() {
        System.out.println(String.format("minf-%d, %d-%d", cnt, id,
                counter.getAllCnt(id)));
    }
}
