package com.xebisco.yield2d.engine;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface AddList {
    @FunctionalInterface
    interface AddListMethod {
        Object instance();
    }

    Class<? extends AddListMethod> value();
}
