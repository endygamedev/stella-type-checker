package dev.ebronnikov.typechecker.checker;

import dev.ebronnikov.antlr.stellaParser.ProgramContext;

public interface Checker {
    void check(ProgramContext programContext);
}
