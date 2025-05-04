package com.xebisco.yield2d.async;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.stream.IntStream;

public class ParallelFor extends AWaitCall {
    public <T> ParallelFor(List<T> list, Consumer<T> consumer) {
        if (list.isEmpty())
            call();
        else {
            AtomicInteger r = new AtomicInteger(list.size());
            IntStream.range(0, list.size()).parallel().forEach(i -> {
                consumer.accept(list.get(i));
                if (r.decrementAndGet() == 0) {
                    call();
                }
            });
        }
    }

    public <T> ParallelFor(T[] arr, Consumer<T> consumer) {
        if (arr.length == 0)
            call();
        else {
            AtomicInteger r = new AtomicInteger(arr.length);
            IntStream.range(0, arr.length).parallel().forEach(i -> {
                consumer.accept(arr[i]);
                if (r.decrementAndGet() == 0) {
                    call();
                }
            });
        }
    }
}
