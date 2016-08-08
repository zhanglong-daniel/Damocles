package com.damocles.java.util;

/**
 * Created by zhanglong02 on 16/2/22.
 */
public final class TwoTuple<A, B> {

    public final A first;
    public final B second;

    public TwoTuple(A first, B second) {
        this.first = first;
        this.second = second;
    }

    @Override
    public String toString() {
        return "[TwoTuple]" + (first == null ? null
                                       : first.toString()) + " ; " + (second == null ? null : second.toString());
    }
}
