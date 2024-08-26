package dev.ebronnikov.typechecker.checker;

import dev.ebronnikov.antlr.stellaParser;
import dev.ebronnikov.typechecker.errors.Error;
import dev.ebronnikov.typechecker.errors.ErrorManager;

import java.util.ArrayList;

public class StellaChecker {
    private final ErrorManager errorManager = new ErrorManager();
    private final ArrayList<Checker> checkers;

    public StellaChecker() {
        checkers = new ArrayList<>();
        checkers.add(new TypeChecker(errorManager));
        checkers.add(new StructureChecker(errorManager));
    }

    public ArrayList<Error> check(stellaParser.ProgramContext programContext) {
        checkers.forEach(checker -> checker.check(programContext));
        return errorManager.getErrors();
    }
}
