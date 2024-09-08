package dev.ebronnikov.typechecker.checker;

import dev.ebronnikov.typechecker.types.Type;
import org.antlr.v4.runtime.ParserRuleContext;

import java.util.Objects;

public final class UnificationFailed extends UnificationResult {
    private final Type expectedType;
    private final Type actualType;
    private final ParserRuleContext expression;

    public UnificationFailed(Type expectedType, Type actualType, ParserRuleContext expression) {
        this.expectedType = expectedType;
        this.actualType = actualType;
        this.expression = expression;
    }

    public Type getExpectedType() {
        return expectedType;
    }

    public Type getActualType() {
        return actualType;
    }

    public ParserRuleContext getExpression() {
        return expression;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UnificationFailed that = (UnificationFailed) o;
        return Objects.equals(expectedType, that.expectedType) &&
                Objects.equals(actualType, that.actualType) &&
                Objects.equals(expression, that.expression);
    }

    @Override
    public int hashCode() {
        return Objects.hash(expectedType, actualType, expression);
    }

    @Override
    public String toString() {
        return "UnificationFailed{" +
                "expectedType=" + expectedType +
                ", actualType=" + actualType +
                ", expression=" + expression +
                '}';
    }
}
