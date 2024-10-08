package dev.ebronnikov.typechecker.types;

import java.util.Map;
import java.util.stream.Collectors;

public final class FunctionalType extends Type {
    private final Type from;
    private final Type to;
    private final String name;

    public static final String UNKNOWN_FUNCTIONAL_TYPE_NAME = "functional type";

    public FunctionalType(Type from, Type to, boolean isKnownType) {
        super(isKnownType);
        this.from = from;
        this.to = to;
        this.name = isKnownType ? String.format("(%s) -> %s", from.getName(), to.getName()) : "(?) -> (?)";
    }

    public FunctionalType(Type from, Type to) {
        this(from, to, true);
    }

    public Type getFrom() {
        return from;
    }

    public Type getTo() {
        return to;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object other) {
        if (!isKnownType() || other instanceof Type type && !type.isKnownType()) {
            return true;
        }
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;
        FunctionalType functionalType = (FunctionalType) other;
        return from.equals(functionalType.from) && to.equals(functionalType.to);
    }

    @Override
    public boolean isSubtypeOf(Type other, boolean subtypingEnabled) {
        if (this.equals(other)) return true;
        if (!subtypingEnabled) return false;
        if (other instanceof FunctionalType otherFunc) {
            return otherFunc.from.isSubtypeOf(this.from, subtypingEnabled)
                    && this.to.isSubtypeOf(otherFunc.to, subtypingEnabled);
        }
        return false;
    }

    @Override
    public Type replace(TypeVar what, Type to) {
        return new FunctionalType(this.from.replace(what, to), this.to.replace(what, to));
    }

    public FunctionalType withSubstitution(Map<GenericType, Type> types) {
        Type newFrom = from;
        Type newTo = to;

        for (var entry : types.entrySet()) {
            GenericType gType = entry.getKey();
            Type type = entry.getValue();
            newFrom = substitute(newFrom, gType, type);
            newTo = substitute(newTo, gType, type);
        }

        return new FunctionalType(newFrom, newTo);
    }

    private Type substitute(Type original, GenericType generic, Type replacement) {
        if (original instanceof FunctionalType functionalType) {
            return new FunctionalType(
                    substitute(functionalType.getFrom(), generic, replacement),
                    substitute(functionalType.getTo(), generic, replacement)
            );
        } else if (original instanceof GenericType) {
            return original.equals(generic) ? replacement : original;
        } else if (original instanceof ListType listType) {
            return new ListType(substitute(listType.getType(), generic, replacement));
        } else if (original instanceof SumType sumType) {
            return new SumType(
                    substitute(sumType.getLeft(), generic, replacement),
                    substitute(sumType.getRight(), generic, replacement)
            );
        } else if (original instanceof TupleType tupleType) {
            return new TupleType(tupleType.getTypes().stream()
                    .map(t -> substitute(t, generic, replacement))
                    .collect(Collectors.toList()));
        } else if (original instanceof VariantType variantType) {
            return new VariantType(
                    variantType.getLabels(),
                    variantType.getTypes().stream()
                            .map(t -> substitute(t, generic, replacement))
                            .collect(Collectors.toList())
            );
        } else if (original instanceof UniversalWrapperType universalWrapperType) {
            return new UniversalWrapperType(
                    universalWrapperType.getTypeParams().stream()
                            .filter(tp -> !tp.equals(generic))
                            .collect(Collectors.toList()),
                    substitute(universalWrapperType.getInnerType(), generic, replacement)
            );
        } else {
            return original;
        }
    }

    @Override
    public Type getFirstUnresolvedType() {
        Type fromGetFirstUnresolvedType = getFirstUnresolvedType();
        if (fromGetFirstUnresolvedType != null) {
            return fromGetFirstUnresolvedType;
        }
        return to.getFirstUnresolvedType();
    }
}
