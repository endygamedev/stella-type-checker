package dev.ebronnikov.typechecker.types;

import org.antlr.v4.runtime.ParserRuleContext;

public final class TypeVar extends Type {
    private final int index;
    private static int counter = 0;

    private TypeVar(int index) {
        super(true);
        this.index = index;
    }

    @Override
    public String getName() {
        return "?T" + index;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;
        TypeVar typeVar = (TypeVar) other;
        return index == typeVar.index;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(index);
    }

    @Override
    public String toString() {
        return getName();
    }

    public static TypeVar newInstance() {
        return new TypeVar(counter++);
    }

    @Override
    public boolean isSubtypeOf(Type other, boolean subtypingEnabled) {
        return true;
    }

    public boolean containsIn(Type type, ParserRuleContext expression) {
        boolean result;
        if (type instanceof FunctionalType functionalType) {
            result = containsIn(functionalType.getFrom(), expression) || containsIn(functionalType.getTo(), expression);
        } else if (type instanceof ListType listType) {
            result = containsIn(listType.getType(), expression);
        } else if (type instanceof SumType sumType) {
            result = containsIn(sumType.getLeft(), expression) || containsIn(sumType.getRight(), expression);
        } else if (type instanceof TupleType tupleType) {
            result = tupleType.getTypes().stream().anyMatch(t -> {
                try {
                    return containsIn(t, expression);
                } catch (Exception e) {
                    return false;
                }
            });
        } else if (type instanceof TypeVar otherTypeVar) {
            result = this.equals(otherTypeVar);
        } else {
            result = false;
        }

        if (result) {
            throw new IllegalStateException();
        }

        return false;
    }

    @Override
    public Type replace(TypeVar what, Type to) {
        return this.equals(what) ? to : this;
    }
}
