package dev.ebronnikov.typechecker.checker;

public final class UnificationOk extends UnificationResult {
    private UnificationOk() {}

    public static final UnificationOk INSTANCE = new UnificationOk();
}
