package dev.ebronnikov.typechecker.types;

public final class UnknownType extends Type {
    public static final UnknownType INSTANCE = new UnknownType();

    private UnknownType() {
        super(false);
    }

    @Override
    public String getName() {
        return "Unknown";
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof Type;
    }
}
