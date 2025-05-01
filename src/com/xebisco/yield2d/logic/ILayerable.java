package com.xebisco.yield2d.logic;

public interface ILayerable extends Comparable<ILayerable> {

    short getLayer();

    @Override
    default int compareTo(ILayerable l) {
        return Short.compare(getLayer(), l.getLayer());
    }
}
