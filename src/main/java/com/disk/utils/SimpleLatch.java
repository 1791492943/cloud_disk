package com.disk.utils;

public class SimpleLatch {
    private Integer count;

    public SimpleLatch(int count) {
        this.count = count;
    }

    public synchronized void await() throws InterruptedException {
        while (count > 0) wait();
    }

    public synchronized void countDown() {
        count--;
        awaken();
    }

    private  void awaken() {
        if (count == 0) notify();
    }

    public synchronized void awakenNow() {
        count = 0;
        notify();
    }

    public synchronized void countUp() {
        count++;
        awaken();
    }
}