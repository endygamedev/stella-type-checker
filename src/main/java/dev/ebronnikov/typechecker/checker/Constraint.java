package dev.ebronnikov.typechecker.checker;

import dev.ebronnikov.typechecker.types.Type;
import dev.ebronnikov.typechecker.types.TypeVar;
import org.antlr.v4.runtime.ParserRuleContext;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Constraint {
    private final Type left;
    private final Type right;
    private final ParserRuleContext ruleContext;

    public Constraint(Type left, Type right, ParserRuleContext ruleContext) {
        this.left = left;
        this.right = right;
        this.ruleContext = ruleContext;
    }

    public Type getLeft() {
        return left;
    }

    public Type getRight() {
        return right;
    }

    public ParserRuleContext getRuleContext() {
        return ruleContext;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Constraint that = (Constraint) o;
        return Objects.equals(left, that.left) &&
                Objects.equals(right, that.right) &&
                Objects.equals(ruleContext, that.ruleContext);
    }

    @Override
    public int hashCode() {
        return Objects.hash(left, right, ruleContext);
    }

    @Override
    public String toString() {
        return "Constraint{" +
                "left=" + left +
                ", right=" + right +
                ", ruleContext=" + ruleContext +
                '}';
    }

    public Constraint replace(TypeVar what, Type to) {
        Type newLeft = left.replace(what, to);
        Type newRight = right.replace(what, to);
        return new Constraint(newLeft, newRight, ruleContext);
    }

    public static List<Constraint> replaceConstraints(List<Constraint> constraints, TypeVar what, Type to) {
        return constraints.stream()
                .map(constraint -> constraint.replace(what, to))
                .collect(Collectors.toList());
    }
}
