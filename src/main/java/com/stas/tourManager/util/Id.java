package com.stas.tourManager.util;

import java.util.concurrent.atomic.AtomicLong;

public class Id {
    private static AtomicLong value = new AtomicLong(1);

    public static long getNext() {
        return value.getAndIncrement();
    }
}
