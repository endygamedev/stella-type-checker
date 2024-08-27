package dev.ebronnikov.typechecker.checker;

import dev.ebronnikov.antlr.stellaParserBaseVisitor;
import dev.ebronnikov.antlr.stellaParser;
import dev.ebronnikov.typechecker.errors.ErrorManager;
import dev.ebronnikov.typechecker.types.TypeContext;
import dev.ebronnikov.typechecker.types.TypeInferrer;
import dev.ebronnikov.typechecker.utils.AntlrUtils;
import org.antlr.v4.runtime.tree.ParseTree;

public class TypeCheckerVisitor extends stellaParserBaseVisitor<Void> {
    private final ErrorManager errorManager;
    private final TypeContext typeContext;

    public TypeCheckerVisitor(ErrorManager errorManager, TypeContext parentTypeContext) {
        this.errorManager = errorManager;
        this.typeContext = new TypeContext(parentTypeContext);
    }

    @Override
    public Void visitProgram(stellaParser.ProgramContext ctx) {
        TopLevelInfoCollector topLevelInfoCollector = new TopLevelInfoCollector(typeContext);
        topLevelInfoCollector.visitProgram(ctx);
        return super.visitProgram(ctx);
    }


    @Override
    public Void visitDeclFun(stellaParser.DeclFunContext ctx) {
        String functionName = AntlrUtils.getFunctionName(ctx);
        var functionTypeOptional = typeContext.resolveFunctionType(functionName);

        if (functionTypeOptional.isEmpty()) return null;
        var functionType = functionTypeOptional.get();
        var expectedFunctionRetType = functionType.getTo();

        TypeContext functionContext = new TypeContext(typeContext);
        functionContext.saveVariableType(AntlrUtils.getParamName(ctx.paramDecl), functionType.getFrom());

        TopLevelInfoCollector topLevelInfoCollector = new TopLevelInfoCollector(functionContext);
        ctx.children.forEach(topLevelInfoCollector::visit);

        TypeCheckerVisitor innerTypeCheckerVisitor = new TypeCheckerVisitor(errorManager, functionContext);
        ctx.children.forEach(innerTypeCheckerVisitor::visit);

        var returnExpr = ctx.returnExpr;
        TypeInferrer typeInferrer = new TypeInferrer(errorManager, functionContext);
        typeInferrer.visitExpression(returnExpr, expectedFunctionRetType);

        return null;
    }
}
