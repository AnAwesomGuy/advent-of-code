package net.anawesomguy.adventofcode;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.Supplier;

// 50% stolen from guava
public final class LazyCloseable<T extends AutoCloseable> implements Supplier<T>, AutoCloseable {
    private final transient Object lock = new Object();
    private volatile Supplier<T> delegate;
    private transient T value;

    LazyCloseable(@NotNull Supplier<T> delegate) {
        this.delegate = Objects.requireNonNull(delegate);
    }

    @Override
    public T get() {
        if (delegate != null)
            synchronized (lock) {
                if (delegate != null) {
                    T t = value = delegate.get();
                    delegate = null;
                    return t;
                }
            }
        return value;
    }

    @Override
    public void close() {
        T t = value;
        if (t != null)
            try {
                t.close();
            } catch (Exception e) {
                System.err.printf("Error while closing %s%n!", t);
                e.printStackTrace();
            }
    }
}
