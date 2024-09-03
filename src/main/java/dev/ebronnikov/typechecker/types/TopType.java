package dev.ebronnikov.typechecker.types;

public final class TopType extends Type {
    public static final TopType INSTANCE = new TopType();

    public TopType() {
        super(true);
    }

    @Override
    public String getName() {
        return "Top";
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof TopType;
    }

    @Override
    public boolean isSubtypeOf(Type other, boolean subtypingEnabled) {
        return this.equals(other);
    }
}
