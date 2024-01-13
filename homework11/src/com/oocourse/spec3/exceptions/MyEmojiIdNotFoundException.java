package com.oocourse.spec3.exceptions;

public class MyEmojiIdNotFoundException extends EmojiIdNotFoundException {
    private static int cnt = 0;
    private static Counter counter = new Counter();
    private static int id;

    public MyEmojiIdNotFoundException(int id) {
        MyEmojiIdNotFoundException.id = id;
        cnt++;
        counter.addCnt(id);
    }

    @Override
    public void print() {
        System.out.println(String.format("einf-%d, %d-%d", cnt, id,
                counter.getAllCnt(id)));
    }
}
