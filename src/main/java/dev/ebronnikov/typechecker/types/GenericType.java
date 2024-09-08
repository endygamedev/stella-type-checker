package dev.ebronnikov.typechecker.types;

public final class GenericType extends Type {
    private final String varName;
    private final String name;

    public GenericType(String varName) {
        super(true);
        this.varName = varName;
        this.name = "[" + varName + "]";
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;
        GenericType that = (GenericType) other;
        return varName.equals(that.varName);
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean isSubtypeOf(Type other, boolean subtypingEnabled) {
        return true;
    }
}
