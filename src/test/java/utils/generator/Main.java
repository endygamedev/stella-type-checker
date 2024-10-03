package utils.generator;

import dev.ebronnikov.typechecker.errors.ErrorType;
import utils.Constants;

import java.nio.file.*;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        generateOkTests();
        for (ErrorType errorType : ErrorType.values()) {
            try {
                generateBadTest(errorType);
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }

    private static void generateOkTests() {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("// NOTE: Auto-generated tests").append("\n\n");
        stringBuilder.append("package tests;").append("\n\n");
        String imports = """
                import org.junit.jupiter.api.Test;
                import utils.Runner;
                """;
        stringBuilder.append(imports).append("\n");
        stringBuilder.append("class OkTest {").append("\n");

        testList(Path.of(Constants.OK_TEST_PATH)).forEach(test -> {
            String testContent = String.format("""
                        @Test
                        public void test_%s() {
                            Runner.runOkTest("%s");
                        }
                    """, test, test);
            stringBuilder.append(testContent).append("\n");
        });

        stringBuilder.append("}").append("\n");

        try {
            Files.writeString(Path.of("src/test/java/tests/OkTest.java"), stringBuilder.toString(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (Exception exception) {
            throw new RuntimeException("Cannot write file", exception);
        }
    }

    private static void generateBadTest(ErrorType errorType) {
        String errorTypeClassName = errorType.toString();
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("// NOTE: Auto-generated tests").append("\n\n");
        stringBuilder.append("package tests;").append("\n\n");
        String imports = """
                import org.junit.jupiter.api.Test;
                import utils.Runner;
                
                import dev.ebronnikov.typechecker.errors.ErrorType;
                """;
        stringBuilder.append(imports).append("\n");
        stringBuilder.append(String.format("class Bad_%s_Test {", errorTypeClassName)).append("\n");

        testList(Paths.get(Constants.BAD_TEST_PATH, errorType.toString())).forEach(test -> {
            String testName = removeExtension(test, '/', 1)
                    .replaceAll("-", "_");
            String testContent = String.format("""
                        @Test
                        public void test_%s() {
                            Runner.runBadTest(ErrorType.%s, "%s");
                        }
                    """, testName, errorType, test);
            stringBuilder.append(testContent).append("\n");
        });

        stringBuilder.append("}").append("\n");

        try {
            Files.writeString(Path.of(String.format("src/test/java/tests/Bad_%s_Test.java", errorType)), stringBuilder.toString(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (Exception exception) {
            throw new RuntimeException("Cannot write file", exception);
        }
    }

    private static ArrayList<String> testList(Path testsPath) {
        ArrayList<String> tests = new ArrayList<>();
        try {
            try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(testsPath)) {
                for (Path path : directoryStream) {
                    String filename = path.getFileName().toString();
                    tests.add(removeExtension(filename, '.', 0));
                }
            }
        } catch (Exception exception) {
            throw new RuntimeException(
                    "Error occurred while listing files: " + Constants.OK_TEST_PATH,
                    exception
            );
        }
        return tests;
    }


    private static String removeExtension(String filename, Character delimiter, int position) {
        int lastDotIndex = filename.lastIndexOf(delimiter);
        if (lastDotIndex == -1) {
            return filename;
        }
        return filename.substring(position, lastDotIndex);
    }
}
