package dev.ebronnikov.typechecker;

import dev.ebronnikov.antlr.stellaLexer;
import dev.ebronnikov.antlr.stellaParser;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.stream.Collectors;


public class Main {
    public static void main(String[] args) {
        String program = readInputProgram();
        stellaLexer lexer = new stellaLexer(CharStreams.fromString(program));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        stellaParser parser = new stellaParser(tokens);
        stellaParser.ProgramContext parseTree = parser.program();
        System.out.println(parseTree.toStringTree(parser));
    }

    private static String readInputProgram() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        return reader.lines().collect(Collectors.joining("\n"));
    }
}
