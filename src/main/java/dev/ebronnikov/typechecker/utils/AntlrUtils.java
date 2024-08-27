package dev.ebronnikov.typechecker.utils;

import dev.ebronnikov.antlr.stellaParser.DeclFunContext;
import dev.ebronnikov.antlr.stellaParser.ParamDeclContext;

public class AntlrUtils {
    public static String getFunctionName(DeclFunContext ctx) {
        return ctx.name.getText();
    }

    public static String getParamName(ParamDeclContext ctx) {
        return ctx.name.getText();
    }
}
