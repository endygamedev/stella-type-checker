package dev.ebronnikov.typechecker.types;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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

    @Override
    public boolean isSubtypeOf(Type other, boolean subtypingEnabled) {
        if (this.equals(other)) return true;
        if (!subtypingEnabled) return false;
        if (other instanceof TupleType otherTuple) {
            if (this.types.size() != otherTuple.types.size()) return false;
            return IntStream.range(0, this.types.size())
                    .allMatch(i -> this.types.get(i).isSubtypeOf(otherTuple.types.get(i), subtypingEnabled));
        }
        return false;
    }

    @Override
    public Type replace(TypeVar what, Type to) {
        List<Type> newTypes = types.stream()
                .map(type -> type.replace(what, to))
                .collect(Collectors.toList());
        return new TupleType(newTypes);
    }

    @Override
    public Type getFirstUnresolvedType() {
        return types.stream()
                .map(Type::getFirstUnresolvedType)
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);
    }
}
