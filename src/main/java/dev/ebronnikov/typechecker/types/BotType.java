package dev.ebronnikov.typechecker.types;

public final class BotType extends Type {
    public static final  BotType INSTANCE = new BotType();

    private BotType() {
        super(true);
    }

    @Override
    public String getName() {
        return "Bot";
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof BotType;
    }

    @Override
    public boolean isSubtypeOf(Type other, boolean subtypingEnabled) {
        if (this.equals(other)) return true;
        if (!subtypingEnabled) return false;
        return other instanceof TopType || other instanceof BotType;
    }
}
