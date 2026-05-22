package com.muciociosan.theproject.shared.ids;

import java.util.Objects;

import static java.util.Objects.requireNonNull;

public abstract class ScalarValue<T> {
    protected final T value;

    protected ScalarValue(final T value) {
        this.value = requireNonNull(value, "value must not be null!");
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ScalarValue<?> that = (ScalarValue<?>) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }

    @Override
    public String toString() {
        return value.toString();
    }

    public T value() {
        return value;
    }
}
