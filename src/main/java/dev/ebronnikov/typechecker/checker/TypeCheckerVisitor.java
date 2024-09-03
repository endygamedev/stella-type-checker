package dev.ebronnikov.typechecker.checker;

import dev.ebronnikov.antlr.stellaParserBaseVisitor;
import dev.ebronnikov.antlr.stellaParser;
import dev.ebronnikov.typechecker.errors.ErrorManager;
import dev.ebronnikov.typechecker.types.SyntaxTypeProcessor;
import dev.ebronnikov.typechecker.types.TypeContext;
import dev.ebronnikov.typechecker.types.TypeInferrer;
import dev.ebronnikov.typechecker.utils.AntlrUtils;
import org.antlr.v4.runtime.tree.ParseTree;

import java.util.List;

public class TypeCheckerVisitor extends stellaParserBaseVisitor<Void> {
    private final ErrorManager errorManager;
    private final TypeContext typeContext;
    private final ExtensionManager extensionManager;

    public TypeCheckerVisitor(ErrorManager errorManager, ExtensionManager extensionManager, TypeContext parentTypeContext) {
        this.errorManager = errorManager;
        this.extensionManager = extensionManager;
        this.typeContext = new TypeContext(parentTypeContext);
    }

    @Override
    public Void visitProgram(stellaParser.ProgramContext ctx) {
        TopLevelInfoCollector topLevelInfoCollector = new TopLevelInfoCollector(typeContext);
        topLevelInfoCollector.visitProgram(ctx);
        for (stellaParser.DeclContext decl : ctx.decls) {
            visitDecl(decl);
        }
        return null;
    }

    private void visitDecl(stellaParser.DeclContext ctx) {
        if (ctx instanceof stellaParser.DeclFunContext declFunContext) {
            visitDeclFun(declFunContext);
        } else if (ctx instanceof stellaParser.DeclExceptionTypeContext declExceptionTypeContext) {
            visitDeclExceptionType(declExceptionTypeContext);
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
        var functionTypeOptional = typeContext.resolveFunctionType(functionName);

        if (functionTypeOptional.isEmpty()) return null;
        var functionType = functionTypeOptional.get();
        var expectedFunctionRetType = functionType.getTo();

        TypeContext functionContext = new TypeContext(typeContext);
        functionContext.saveVariableType(AntlrUtils.getParamName(ctx.paramDecl), functionType.getFrom());

        TopLevelInfoCollector topLevelInfoCollector = new TopLevelInfoCollector(functionContext);
        ctx.children.forEach(topLevelInfoCollector::visit);

        TypeCheckerVisitor innerTypeCheckerVisitor = new TypeCheckerVisitor(errorManager, extensionManager, functionContext);
        ctx.children.forEach(innerTypeCheckerVisitor::visit);

        var returnExpr = ctx.returnExpr;
        TypeInferrer typeInferrer = new TypeInferrer(errorManager, extensionManager, functionContext);
        typeInferrer.visitExpression(returnExpr, expectedFunctionRetType);

        return null;
    }
}
