package utils;

import dev.ebronnikov.antlr.stellaLexer;
import dev.ebronnikov.antlr.stellaParser;
import dev.ebronnikov.typechecker.checker.StellaChecker;
import dev.ebronnikov.typechecker.errors.Error;
import dev.ebronnikov.typechecker.utils.ErrorFormatter;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class Runner {
    public static void runOkTest(String testName) {
        File file = new File(Constants.OK_TEST_PATH + testName + Constants.FILE_EXTENSION);
        try (InputStream programTextStream = new FileInputStream(file)) {
            stellaLexer lexer = new stellaLexer(CharStreams.fromStream(programTextStream));
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            stellaParser parser = new stellaParser(tokens);
            stellaParser.ProgramContext programContext = parser.program();

            StellaChecker checker = new StellaChecker();
            ArrayList<Error> errors = checker.check(programContext);
            Optional<Error> primaryError = errors.stream().findFirst();

            String formattedError = primaryError
                    .map(error -> ErrorFormatter.formatErrorToString(error, parser))
                    .orElse("");

            assertTrue(primaryError.isEmpty(), formattedError);
        } catch (Exception exception) {
            throw new RuntimeException(
                    String.format("Failed to read file %s", file.getAbsolutePath()),
                    exception
            );
        }
    }
}
