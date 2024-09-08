package dev.ebronnikov.typechecker.checker;

import dev.ebronnikov.antlr.stellaParser;
import dev.ebronnikov.antlr.stellaParserBaseVisitor;
import dev.ebronnikov.typechecker.types.*;
import dev.ebronnikov.typechecker.utils.AntlrUtils;
import org.antlr.v4.runtime.tree.RuleNode;

public class TopLevelInfoCollector extends stellaParserBaseVisitor<Void> {
    private final TypeContext typeContext;

    public TopLevelInfoCollector(TypeContext typeContext) {
        this.typeContext = typeContext;
    }

    @Override
    public Void visitDeclFun(stellaParser.DeclFunContext ctx) {
        String name = AntlrUtils.getFunctionName(ctx);

        var paramDecl = ctx.paramDecls.stream().findFirst().orElse(null);
        if (paramDecl == null) {
            return null;
        }

        var argType = SyntaxTypeProcessor.getType(paramDecl.paramType);
        var returnType = SyntaxTypeProcessor.getType(ctx.returnType);
        var functionType = new FunctionalType(argType, returnType);

        typeContext.saveFunctionType(name, functionType);

        return null;
    }

    @Override
    public Void visitChildren(RuleNode node) {
        for (int i = 0; i < node.getChildCount(); ++i) {
            var child = node.getChild(i);
            if (child instanceof stellaParser.DeclContext) {
                child.accept(this);
            }
        }
        return null;
    }
}
