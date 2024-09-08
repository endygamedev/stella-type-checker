package dev.ebronnikov.typechecker.checker;

public sealed class UnificationResult permits UnificationOk, UnificationFailed, UnificationFailedInfiniteType {
}
