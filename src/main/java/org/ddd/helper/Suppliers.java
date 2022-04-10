package org.ddd.helper;

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

import static java.util.Objects.requireNonNull;

public class Suppliers {

    public static <T> As<T> awareMemoize(Supplier<T> supplier) {
        return new AwareMemoizeSupplier<>(supplier);
    }

    public static <T> As<T> self(T self) {
        return new SelfSupplier<>(self);
    }
    private static class AwareMemoizeSupplier<T> implements As<T> {
        private final AtomicReference<T> value = new AtomicReference<>();
        private final Supplier<T> supplier;
        private boolean callInnerGet = false;

        AwareMemoizeSupplier(Supplier<T> supplier) {
            requireNonNull(supplier);
            this.supplier = supplier;
        }

        @Override
        public boolean isHere() {
            return callInnerGet;
        }

        @Override
        public T get() {
            T val = value.get();
            if (val == null) {
                synchronized (value) {
                    val = value.get();
                    if (val == null) {
                        val = supplier.get();
                        callInnerGet = true;
                        value.set(val);
                    }
                }
            }
            return val;
        }
    }

    private static class SelfSupplier<T> implements As<T> {
        private final T self;

        SelfSupplier(T self) {
            requireNonNull(self);
            this.self = self;
        }

        @Override
        public boolean isHere() {
            return true;
        }

        @Override
        public T get() {
            return self;
        }
    }
}
