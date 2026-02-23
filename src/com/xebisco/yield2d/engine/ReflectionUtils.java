package com.xebisco.yield2d.engine;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;

public class ReflectionUtils {
    public static ArrayList<Pair<Field, Object>> getAllFieldsWithAnnotation(Object o, Class<? extends Annotation> a) {
        ArrayList<Pair<Field, Object>> list = new ArrayList<>();
        for(Field f : o.getClass().getDeclaredFields()) {
            if(f.isAnnotationPresent(a)) {
                try {
                    f.setAccessible(true);
                    list.add(new Pair<>(f, f.get(o)));
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return list;
    }
}
