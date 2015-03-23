package net.evlikat.hexatrix.entities;

import org.junit.Test;

import java.util.Arrays;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicInteger;

import static junit.framework.Assert.assertEquals;

public class CompositeIteratorTest {

    @Test
    public void shouldIterateProperly() {
        CompositeIterator<Integer> instance = new CompositeIterator<>(Arrays.asList(
                Arrays.asList(1, 2, 3),
                Arrays.asList(5, 6, 7)
        ).iterator());
        Iterator<Integer> expected = Arrays.asList(1, 2, 3, 5, 6, 7).iterator();
        while (instance.hasNext()) {
            assertEquals(expected.next(), instance.next());
        }
    }


    @Test
    public void shouldThrowEvent() {
        final AtomicInteger counter = new AtomicInteger();
        CompositeIterator<Integer> instance = new CompositeIterator<Integer>(Arrays.asList(
                Arrays.asList(1, 2, 3),
                Arrays.asList(5, 6, 7)
        ).iterator()) {
            @Override
            public void afterLine() {
                counter.incrementAndGet();
            }
        };
        while (instance.hasNext()) {
            instance.next();
        }
        assertEquals(1, counter.get());
    }

}