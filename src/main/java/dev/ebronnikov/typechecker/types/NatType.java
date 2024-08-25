package dev.ebronnikov.typechecker.types;

public final class NatType extends Type {
    public static final NatType INSTANCE = new NatType();

    private NatType() {
        super(true);
    }

    @Override
    public String getName() {
        return "Nat";
    }

    @Override
    public boolean equals(Object other) {
        if (!isKnownType() || (other instanceof Type type && type.isKnownType())) {
            return true;
        }
        return other instanceof NatType;
    }
}
