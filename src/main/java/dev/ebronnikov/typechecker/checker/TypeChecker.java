package dev.ebronnikov.typechecker.checker;

import dev.ebronnikov.antlr.stellaParser;
import dev.ebronnikov.typechecker.errors.ErrorManager;

import java.util.List;

public class TypeChecker implements Checker {
    private final TypeCheckerVisitor checkerVisitor;
    private final ExtensionManager extensionManager;

    public TypeChecker(ErrorManager errorManager) {
        this.extensionManager = new ExtensionManager();
        this.checkerVisitor = new TypeCheckerVisitor(errorManager, extensionManager, null);
    }

    @Override
    public void check(stellaParser.ProgramContext programContext) {
        addExtensions(programContext.extensions);

        checkerVisitor.visitProgram(programContext);
    }

    private void addExtensions(List<stellaParser.ExtensionContext> ctxs) {
        List<Extension> extensions = ctxs.stream()
                .filter(ctx -> ctx instanceof stellaParser.AnExtensionContext)
                .map(ctx -> (stellaParser.AnExtensionContext) ctx)
                .flatMap(ctx -> ctx.extensionNames.stream())
                .map(name -> name.getText().replace("^#", ""))
                .map(Extension::fromString)
                .toList();

        extensionManager.enabledExtensions(extensions);
    }
}
