package dev.ebronnikov.typechecker.types;

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
}
