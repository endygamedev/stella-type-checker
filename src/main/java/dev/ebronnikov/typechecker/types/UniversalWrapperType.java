package dev.ebronnikov.typechecker.types;

import java.util.List;

public final class UniversalWrapperType extends Type {
    private final List<GenericType> typeParams;
    private final Type innerType;
    private final String name;

    public UniversalWrapperType(List<GenericType> typeParams, Type innerType) {
        super(true);
        this.typeParams = typeParams;
        this.innerType = innerType;
        this.name = "[" + String.join(",", typeParams.toString()) + "]" + innerType.toString();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;
        UniversalWrapperType that = (UniversalWrapperType) other;
        return typeParams.equals(that.typeParams) && innerType.equals(that.innerType);
    }

    public Type getInnerType() {
        return innerType;
    }

    public List<GenericType> getTypeParams() {
        return typeParams;
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
