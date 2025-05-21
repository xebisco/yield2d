package com.xebisco.yield2d.utils;

public interface IIndexable extends Comparable<IIndexable> {
    short getIndex();

    @Override
    default int compareTo(IIndexable l) {
        return Short.compare(getIndex(), l.getIndex());
    }
}
