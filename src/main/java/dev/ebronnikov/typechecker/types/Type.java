package dev.ebronnikov.typechecker.types;

public abstract sealed class Type permits NatType, BoolType, UnitType, UnknownType, SumType, RecordType, TupleType, FunctionalType, ListType, VariantType {
    private final boolean isKnownType;

    public Type(boolean isKnownType) {
        this.isKnownType = isKnownType;
    }

    public boolean isKnownType() {
        return isKnownType;
    }

    public abstract String getName();
}
