package dev.ebronnikov.typechecker.errors;

public record Error(ErrorType errorType, Object[] args) {
}
