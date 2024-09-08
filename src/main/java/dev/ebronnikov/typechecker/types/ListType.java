package dev.ebronnikov.typechecker.types;

import java.awt.*;

public final class ListType extends Type {
    private final Type type;
    private final String name;

    public ListType(Type type) {
        super(true);
        this.type = type;
        this.name = String.format("List[%s]", type.getName());
    }

    public Type getType() {
        return type;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;
        ListType listType = (ListType) other;
        return type.equals(listType.type);
    }

    @Override
    public boolean isSubtypeOf(Type other, boolean subtypingEnabled) {
        if (this.equals(other)) return true;
        if (!subtypingEnabled) return false;
        if (other instanceof ListType otherList) {
            return this.type.isSubtypeOf(otherList.type, subtypingEnabled);
        }
        return false;
    }

    @Override
    public Type replace(TypeVar what, Type to) {
        return new ListType(this.type.replace(what, to));
    }

    @Override
    public Type getFirstUnresolvedType() {
        return type.getFirstUnresolvedType();
    }
}
