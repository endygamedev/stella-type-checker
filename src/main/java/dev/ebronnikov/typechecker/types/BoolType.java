package dev.ebronnikov.typechecker.types;

public final class BoolType extends Type {
    public static final BoolType INSTANCE = new BoolType();

    private BoolType() {
        super(true);
    }

    @Override
    public String getName() {
        return "Bool";
    }

    @Override
    public boolean equals(Object other) {
        if (!isKnownType() || other instanceof Type type && type.isKnownType()) {
            return true;
        }
        return other instanceof BoolType;
    }
}
