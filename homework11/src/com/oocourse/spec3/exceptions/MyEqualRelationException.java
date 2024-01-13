package com.oocourse.spec3.exceptions;

public class MyEqualRelationException extends EqualRelationException {
    private static int id1;
    private static int id2;
    private static Counter counter = new Counter();
    private static int cnt = 0;

    public MyEqualRelationException(int id1, int id2) {
        cnt++;
        if (id1 == id2) {
            counter.addCnt(id1);
        } else {
            counter.addDouble(id1, id2);
        }
        MyEqualRelationException.id1 = id1;
        MyEqualRelationException.id2 = id2;
    }

    @Override
    public void print() {
        int minId = Math.min(id1, id2);
        int maxId = Math.max(id1, id2);
        System.out.println(String.format("er-%d, %d-%d, %d-%d", cnt, minId,
                counter.getAllCnt(minId), maxId, counter.getAllCnt(maxId)));
    }
}
