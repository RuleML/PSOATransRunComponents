package org.ruleml.psoa.util;

@FunctionalInterface
public interface FunctionWithThrowable<T, R, E extends Throwable> {
    R apply(final T input) throws E;
}