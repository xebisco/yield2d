package com.xebisco.yield2d.impl;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

public abstract class Connection {

    private final HashMap<String, Method> methodCache = new HashMap<>();

    public final void callMethod(String s, Object... args) {
        Method m = methodCache.get(s);

        if (m == null)
            for (Method m1 : getClass().getMethods()) {
                if (m1.getName().hashCode() == s.hashCode() && m1.getName().equals(s)
                        && m1.isAnnotationPresent(ImplMethod.class)) {
                    m = m1;
                    if (m1.getAnnotation(ImplMethod.class).saveOnCache())
                        methodCache.put(s, m);
                    break;
                }
            }

        if (m == null)
            throw new RuntimeException("NO REACHABLE METHOD");

        try {
            m.invoke(this, args);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("NO REACHABLE METHOD");
        }
    }

    public final HashMap<String, Method> getMethodCache() {
        return methodCache;
    }
}
