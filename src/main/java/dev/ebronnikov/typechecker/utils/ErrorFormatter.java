package dev.ebronnikov.typechecker.utils;

import dev.ebronnikov.antlr.stellaParser;
import dev.ebronnikov.typechecker.errors.Error;
import dev.ebronnikov.typechecker.errors.ErrorMessages;
import dev.ebronnikov.typechecker.types.Type;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

public class ErrorFormatter {
    public static String formatErrorToString(Error error, stellaParser parser) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("An error occurred during type checking!\n");
        stringBuilder.append("ERROR: ").append(error.errorType()).append("\n");

        Object[] formattedArgs = Arrays.stream(error.args())
                .map(arg -> {
                    if (arg instanceof ParserRuleContext context) {
                        Token start = context.getStart();
                        Token stop = context.getStop();
                        return parser.getTokenStream().getText(start, stop);
                    } else if (arg instanceof Type type) {
                        return type.getName();
                    } else {
                        return arg;
                    }
                }).toArray();

        String asString = String.format(ErrorMessages.messages.get(error.errorType()), formattedArgs);
        stringBuilder.append(asString);

        return stringBuilder.toString();
    }

    public static String formatErrorListToString(ArrayList<Error> errors, stellaParser parser) {
        return errors.stream()
                .map(error -> formatErrorToString(error, parser))
                .collect(Collectors.joining("\n"));
    }
}
