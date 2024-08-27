package dev.ebronnikov.typechecker.types;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public final class TupleType extends Type {
    private final List<Type> types;
    private final int arity;
    private final String name;

    public TupleType(List<Type> types, boolean isKnownType) {
        super(isKnownType);
        this.types = types;
        this.arity = types.size();
        this.name = isKnownType ? types.stream()
                .map(Type::getName)
                .collect(Collectors.joining(", ", "{", "}"))
                : "UnknownTuple";
    }

    public TupleType(List<Type> types) {
        this(types, true);
    }

    public List<Type> getTypes() {
        return types;
    }

    public int getArity() {
        return arity;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object other) {
        if (!isKnownType() || (other instanceof Type type && !type.isKnownType())) {
            return true;
        }
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;
        TupleType tupleType = (TupleType) other;
        return types.equals(tupleType.types);
    }
}
