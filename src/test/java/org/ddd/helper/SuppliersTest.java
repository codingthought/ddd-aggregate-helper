package org.ddd.helper;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class SuppliersTest {
    @Nested
    class SelfSupplierTest {
        @Test
        void should_return_true_when_call_is_here() {
            As<Object> selfSupplier = Suppliers.self(new Object());

            assertTrue(selfSupplier.isHere());
        }

        @Test
        void should_return_self_when_get() {
            Object whatever = new Object();
            As<Object> selfSupplier = Suppliers.self(whatever);

            assertEquals(whatever, selfSupplier.get());
        }
    }

    @Nested
    class AwareMemoizeSupplierTest {
        @Test
        void should_return_false_when_call_is_here_if_not_get() {
            As<Object> awareMemoize = Suppliers.awareMemoize(Object::new);

            assertFalse(awareMemoize.isHere());
        }

        @Test
        void should_return_ture_when_call_is_here_after_get() {
            As<?> awareMemoize = Suppliers.awareMemoize(Object::new);
            awareMemoize.get();

            assertTrue(awareMemoize.isHere());
        }

        @Test
        void should_return_supplier_type_when_get() {
            As<?> awareMemoize = Suppliers.awareMemoize(LocalDateTime::now);

            assertTrue(awareMemoize.get() instanceof LocalDateTime);
        }

        @Test
        void should_return_the_same_object_when_get_2_times() {
            As<?> awareMemoize = Suppliers.awareMemoize(LocalDateTime::now);

            Object get1 = awareMemoize.get();
            Object get2 = awareMemoize.get();

            assertSame(get1, get2);
        }
    }
}