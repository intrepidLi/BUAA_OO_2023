package com.oocourse.spec3.exceptions;

public class MyRelationNotFoundException extends RelationNotFoundException {
    private static int id1;
    private static int id2;
    private static Counter counter = new Counter();
    private static int cnt = 0;

    public MyRelationNotFoundException(int id1, int id2) {
        cnt++;
        counter.addDouble(id1, id2);
        MyRelationNotFoundException.id1 = id1;
        MyRelationNotFoundException.id2 = id2;
    }

    @Override
    public void print() {
        int minId = Math.min(id1, id2);
        int maxId = Math.max(id1, id2);
        System.out.println(String.format("rnf-%d, %d-%d, %d-%d", cnt, minId,
                counter.getAllCnt(minId), maxId, counter.getAllCnt(maxId)));
    }
}
