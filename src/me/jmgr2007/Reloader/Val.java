package me.jmgr2007.Reloader;

public class Val {
    private int value = 0;

    public void setValue(int val) {
        value = val;
        return;
    }

    public int getValue() {
        return value;
    }

    public void increment() {
        value = value++;
        return;
    }

    public void add(int val) {
        value = value + val;
        return;
    }

    public void reset() {
        value = 0;
        return;
    }
}
