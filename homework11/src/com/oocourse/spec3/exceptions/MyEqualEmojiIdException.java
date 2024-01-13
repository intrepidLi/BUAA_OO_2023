package com.oocourse.spec3.exceptions;

public class MyEqualEmojiIdException extends EqualEmojiIdException {
    private static int id;
    private static Counter counter = new Counter();
    private static int cnt = 0;

    public MyEqualEmojiIdException(int id) {
        MyEqualEmojiIdException.id = id;
        cnt++;
        counter.addCnt(id);
    }

    @Override
    public void print() {
        System.out.println(String.format("eei-%d, %d-%d", cnt, id,
                counter.getAllCnt(id)));
    }
}
