package utils;

import dev.ebronnikov.antlr.stellaLexer;
import dev.ebronnikov.antlr.stellaParser;
import dev.ebronnikov.typechecker.checker.StellaChecker;
import dev.ebronnikov.typechecker.errors.Error;
import dev.ebronnikov.typechecker.errors.ErrorType;
import dev.ebronnikov.typechecker.utils.ErrorFormatter;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class Runner {
    public static void runOkTest(String testName) {
        Path testPath = Paths.get(Constants.OK_TEST_PATH, testName + Constants.FILE_EXTENSION);
        TypeCheckResult typeCheckResult = getTypeCheckResult(testPath);

        assertTrue(typeCheckResult.primaryError.isEmpty(), typeCheckResult.formattedError);
    }

    public static void runBadTest(ErrorType errorType, String testName) {
        Path testPath = Paths.get(Constants.BAD_TEST_PATH, errorType.toString(), testName + Constants.FILE_EXTENSION);
        TypeCheckResult typeCheckResult = getTypeCheckResult(testPath);

        assertFalse(typeCheckResult.primaryError.isEmpty());

        ErrorType actual = typeCheckResult.primaryError.get().errorType();
        assertEquals(errorType, actual, typeCheckResult.formattedError);
    }

    private static TypeCheckResult getTypeCheckResult(Path testPath) {
        File file = new File(String.valueOf(testPath));
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

            return new TypeCheckResult(primaryError, formattedError);
        } catch (Exception exception) {
            throw new RuntimeException(
                    String.format("Failed to read file %s", file.getAbsolutePath()),
                    exception
            );
        }
    }

    private record TypeCheckResult(Optional<Error> primaryError, String formattedError) {
    }
}
