package dev.ebronnikov.typechecker.types;

public abstract sealed class Type permits NatType, BoolType, UnitType, UnknownType, SumType, RecordType, TupleType, FunctionalType, ListType, VariantType, BotType, TopType, ReferenceType {
    private final boolean isKnownType;

    public Type(boolean isKnownType) {
        this.isKnownType = isKnownType;
    }

    public boolean isKnownType() {
        return isKnownType;
    }

    public abstract String getName();

    abstract public boolean isSubtypeOf(Type other, boolean subtypingEnabled);

    public boolean isNotSubtypeOf(Type other, boolean subtypingEnabled) {
        return !isSubtypeOf(other, subtypingEnabled);
    }
}
