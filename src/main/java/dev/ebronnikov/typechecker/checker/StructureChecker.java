package dev.ebronnikov.typechecker.checker;

import dev.ebronnikov.antlr.stellaParser;
import dev.ebronnikov.typechecker.errors.ErrorManager;

public class StructureChecker implements Checker {
    private final MainFunctionCheckerVisitor visitor;

    public StructureChecker(ErrorManager errorManager) {
        this.visitor = new MainFunctionCheckerVisitor(errorManager);
    }

    @Override
    public void check(stellaParser.ProgramContext programContext) {
        visitor.visitProgram(programContext);
    }
}
