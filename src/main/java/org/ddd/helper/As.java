package org.ddd.helper;

import java.util.function.Supplier;

public interface As<T> extends Supplier<T> {
    boolean isHere();
}
