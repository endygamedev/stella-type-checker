package dev.ebronnikov.typechecker.types;

public final class UnitType extends Type {
    public static final UnitType INSTANCE = new UnitType();

    private UnitType() {
        super(true);
    }

    @Override
    public String getName() {
        return "Unit";
    }

    @Override
    public boolean equals(Object other) {
        if (!isKnownType() || (other instanceof Type type && type.isKnownType())) {
            return true;
        }
        return other instanceof UnitType;
    }

    @Override
    public boolean isSubtypeOf(Type other, boolean subtypingEnabled) {
        return true;
    }
}
