package org.ruleml.psoa.utils;

@FunctionalInterface
public interface FunctionWithThrowable<T, R, E extends Throwable> {
    R apply(final T input) throws E;
}