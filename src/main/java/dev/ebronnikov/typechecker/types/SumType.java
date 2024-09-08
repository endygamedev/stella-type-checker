package dev.ebronnikov.typechecker.types;

public final class SumType extends Type {
    private final Type left;
    private final Type right;

    public SumType(Type left, Type right) {
        super(true);
        this.left = left;
        this.right = right;
    }

    @Override
    public String getName() {
        return String.format("(%s + %s)", left.getName(), right.getName());
    }

    public Type getLeft() {
        return left;
    }

    public Type getRight() {
        return right;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;

        SumType sumType = (SumType) other;
        return left.equals(sumType.left) && right.equals(sumType.right);
    }

    @Override
    public boolean isSubtypeOf(Type other, boolean subtypingEnabled) {
        return true;
    }

    @Override
    public Type replace(TypeVar what, Type to) {
        return new SumType(this.left.replace(what, to), this.right.replace(what, to));
    }

    @Override
    public Type getFirstUnresolvedType() {
        Type leftFirstUnresolvedType = left.getFirstUnresolvedType();
        if (leftFirstUnresolvedType != null) {
            return leftFirstUnresolvedType;
        }
        return right.getFirstUnresolvedType();
    }
}
