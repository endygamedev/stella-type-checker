package dev.ebronnikov.typechecker.checker;

import dev.ebronnikov.antlr.stellaParser;
import dev.ebronnikov.typechecker.errors.ErrorManager;

public class TypeChecker implements Checker {
    private final TypeCheckerVisitor checkerVisitor;

    public TypeChecker(ErrorManager errorManager) {
        this.checkerVisitor = new TypeCheckerVisitor(errorManager, null);
    }

    @Override
    public void check(stellaParser.ProgramContext programContext) {
        checkerVisitor.visitProgram(programContext);
    }
}
