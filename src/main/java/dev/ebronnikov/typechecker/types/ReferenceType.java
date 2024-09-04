package dev.ebronnikov.typechecker.types;

import java.util.Objects;

public final class ReferenceType extends Type {
    private final Type innerType;
    private final String name;

    public ReferenceType(Type innerType) {
        super(true);
        this.innerType = innerType;
        this.name = "&" + innerType.getName();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;
        ReferenceType that = (ReferenceType) other;
        return Objects.equals(innerType, that.innerType);
    }

    public Type getInnerType() {
        return innerType;
    }

    @Override
    public boolean isSubtypeOf(Type other, boolean subtypingEnabled) {
        if (this.equals(other)) return true;
        if (!subtypingEnabled) return false;
        if (other instanceof ReferenceType otherRef) {
            return this.innerType.isSubtypeOf(otherRef.innerType, subtypingEnabled);
        }
        return false;
    }
}
