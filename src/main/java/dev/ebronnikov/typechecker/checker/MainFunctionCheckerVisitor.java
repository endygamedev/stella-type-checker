package dev.ebronnikov.typechecker.checker;

import dev.ebronnikov.antlr.stellaParser;
import dev.ebronnikov.antlr.stellaParserBaseVisitor;
import dev.ebronnikov.typechecker.errors.ErrorManager;
import dev.ebronnikov.typechecker.errors.ErrorType;
import org.antlr.v4.runtime.tree.ParseTree;

public final class MainFunctionCheckerVisitor extends stellaParserBaseVisitor<Void> {
    private final ErrorManager errorManager;
    private boolean isMainDiscovered = false;
    private static final String MAIN_FUNCTION_NAME = "main";

    public MainFunctionCheckerVisitor(ErrorManager errorManager) {
        this.errorManager = errorManager;
    }

    @Override
    public Void visitDeclFun(stellaParser.DeclFunContext ctx) {
        if (ctx != null && MAIN_FUNCTION_NAME.equals(ctx.name.getText())) {
            isMainDiscovered = true;
            if (ctx.paramDecls.size() != 1) {
                errorManager.registerError(
                        ErrorType.ERROR_INCORRECT_ARITY_OF_MAIN,
                        ctx.paramDecls.size()
                );
            }
        }
        return super.visitDeclFun(ctx);
    }

    @Override
    public Void visit(ParseTree tree) {
        if (isMainDiscovered) {
            return null;
        }
        return super.visit(tree);
    }

    @Override
    public Void visitProgram(stellaParser.ProgramContext ctx) {
        super.visitProgram(ctx);
        if (!isMainDiscovered) {
            errorManager.registerError(
                    ErrorType.ERROR_MISSING_MAIN
            );
        }
        return null;
    }
}
