package dev.ebronnikov.typechecker;

import dev.ebronnikov.antlr.stellaLexer;
import dev.ebronnikov.antlr.stellaParser;
import dev.ebronnikov.typechecker.checker.StellaChecker;
import dev.ebronnikov.typechecker.errors.Error;
import dev.ebronnikov.typechecker.utils.ErrorFormatter;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;


public class Main {
    public static void main(String[] args) {
        String programText = readInputProgram();

        stellaLexer lexer = new stellaLexer(CharStreams.fromString(programText));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        stellaParser parser = new stellaParser(tokens);

        stellaParser.ProgramContext program = parser.program();

        StellaChecker checker = new StellaChecker();
        ArrayList<Error> errors = checker.check(program);
        Optional<Error> primaryError = errors.stream().findFirst();

        if (primaryError.isEmpty()) {
            System.out.println("OK");
            System.exit(0);
        }

        String formattedError = ErrorFormatter.formatErrorToString(primaryError.get(), parser);
        System.err.println(formattedError);
        System.exit(1);
    }

    private static String readInputProgram() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        return reader.lines().collect(Collectors.joining("\n"));
    }
}
