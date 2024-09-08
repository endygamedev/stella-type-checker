package dev.ebronnikov.typechecker.checker;

import dev.ebronnikov.antlr.stellaParserBaseVisitor;
import dev.ebronnikov.antlr.stellaParser;
import dev.ebronnikov.typechecker.errors.ErrorManager;
import dev.ebronnikov.typechecker.errors.ErrorType;
import dev.ebronnikov.typechecker.types.*;
import dev.ebronnikov.typechecker.utils.AntlrUtils;
import org.antlr.v4.runtime.tree.ParseTree;

import java.util.List;
import java.util.stream.Collectors;

public class TypeCheckerVisitor extends stellaParserBaseVisitor<Void> {
    private final ErrorManager errorManager;
    private final TypeContext typeContext;
    private final ExtensionManager extensionManager;
    private final UnifySolver unifySolver;

    public TypeCheckerVisitor(ErrorManager errorManager, ExtensionManager extensionManager, TypeContext parentTypeContext, UnifySolver unifySolver) {
        this.errorManager = errorManager;
        this.extensionManager = extensionManager;
        this.typeContext = new TypeContext(parentTypeContext);
        this.unifySolver = unifySolver;
    }

    @Override
    public Void visitProgram(stellaParser.ProgramContext ctx) {
        TopLevelInfoCollector topLevelInfoCollector = new TopLevelInfoCollector(typeContext);
        topLevelInfoCollector.visitProgram(ctx);
        for (stellaParser.DeclContext decl : ctx.decls) {
            visitDecl(decl);
        }
        
        UnificationResult unificationResult = unifySolver.solve();
        switch (unificationResult) {
            case UnificationFailed unificationFailed ->
                errorManager.registerError(
                        ErrorType.ERROR_UNEXPECTED_TYPE_FOR_EXPRESSION,
                        unificationFailed.getExpectedType(),
                        unificationFailed.getActualType(),
                        unificationFailed.getExpression()
                );
            case UnificationFailedInfiniteType unificationFailedInfiniteType ->
                errorManager.registerError(
                        ErrorType.ERROR_OCCURS_CHECK_INFINITE_TYPE,
                        unificationFailedInfiniteType.getExpression()
                );
            case UnificationOk ignored -> {}
            default -> throw new IllegalStateException("Unexpected value: " + unificationResult);
        }
        
        return null;
    }

    private void visitDecl(stellaParser.DeclContext ctx) {
        if (ctx instanceof stellaParser.DeclFunContext declFunContext) {
            visitDeclFun(declFunContext);
        } else if (ctx instanceof stellaParser.DeclExceptionTypeContext declExceptionTypeContext) {
            visitDeclExceptionType(declExceptionTypeContext);
        } else if (ctx instanceof stellaParser.DeclFunGenericContext declFunGenericContext) {
            visitDeclFunGeneric(declFunGenericContext);
        }
    }

    @Override
    public Void visitDeclExceptionType(stellaParser.DeclExceptionTypeContext ctx) {
        typeContext.setExceptionType(SyntaxTypeProcessor.getType(ctx.exceptionType));
        return null;
    }

    @Override
    public Void visitDeclFun(stellaParser.DeclFunContext ctx) {
        String functionName = AntlrUtils.getFunctionName(ctx);
        Type resolvedType = typeContext.resolveFunctionType(functionName);

        if (!(resolvedType instanceof FunctionalType functionalType)) {
            return null;
        }
        Type expectedFunctionRetType = functionalType.getTo();

        TypeContext functionContext = new TypeContext(typeContext);
        functionContext.saveVariableType(AntlrUtils.getParamName(ctx.paramDecl), functionalType.getFrom());

        TopLevelInfoCollector topLevelInfoCollector = new TopLevelInfoCollector(functionContext);
        ctx.children.forEach(topLevelInfoCollector::visit);

        TypeCheckerVisitor innerTypeCheckerVisitor = new TypeCheckerVisitor(errorManager, extensionManager, functionContext, unifySolver);
        ctx.children.forEach(innerTypeCheckerVisitor::visit);

        var returnExpr = ctx.returnExpr;
        TypeInferrer typeInferrer = new TypeInferrer(errorManager, extensionManager, functionContext, unifySolver);
        typeInferrer.visitExpression(returnExpr, expectedFunctionRetType);

        return null;
    }

    @Override
    public Void visitDeclFunGeneric(stellaParser.DeclFunGenericContext ctx) {
        String name = ctx.name.getText();

        List<stellaParser.ParamDeclContext> paramDecls = ctx.paramDecls;
        if (paramDecls == null || paramDecls.isEmpty()) {
            return null;
        }

        stellaParser.ParamDeclContext paramDecl = paramDecls.get(0);
        Type argType = SyntaxTypeProcessor.getType(paramDecl.paramType);
        Type returnType = SyntaxTypeProcessor.getType(ctx.returnType);

        List<GenericType> generics = ctx.generics.stream()
                .map(generic -> new GenericType(generic.getText()))
                .collect(Collectors.toList());

        FunctionalType functionalType = new FunctionalType(argType, returnType);
        UniversalWrapperType forallType = new UniversalWrapperType(generics, functionalType);

        typeContext.saveFunctionType(name, forallType);

        return null;
    }
}
