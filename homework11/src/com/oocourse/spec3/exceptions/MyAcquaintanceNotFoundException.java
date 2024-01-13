package com.oocourse.spec3.exceptions;

public class MyAcquaintanceNotFoundException extends AcquaintanceNotFoundException {
    private static int cnt = 0;
    private static Counter counter = new Counter();
    private static int id;

    public MyAcquaintanceNotFoundException(int id) {
        MyAcquaintanceNotFoundException.id = id;
        cnt++;
        counter.addCnt(id);
    }

    @Override
    public void print() {
        System.out.println(String.format("anf-%d, %d-%d", cnt, id,
                counter.getAllCnt(id)));
    }
}
