package utils.generator;

import utils.Constants;

import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        generateOkTests();
    }

    private static void generateOkTests() {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("package tests;").append("\n");
        String imports = """
                import org.junit.jupiter.api.Test;
                import utils.Runner;
                """;
        stringBuilder.append(imports).append("\n");
        stringBuilder.append("class OkTest {").append("\n");

        testList(Constants.OK_TEST_PATH).forEach(test -> {
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

    private static ArrayList<String> testList(String testsPath) {
        ArrayList<String> tests = new ArrayList<>();
        Path okTestsPath = Paths.get(testsPath);
        try {
            try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(okTestsPath)) {
                for (Path path : directoryStream) {
                    String filename = path.getFileName().toString();
                    tests.add(removeExtension(filename));
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


    private static String removeExtension(String filename) {
        int lastDotIndex = filename.lastIndexOf('.');
        if (lastDotIndex == -1) {
            return filename;
        }
        return filename.substring(0, lastDotIndex);
    }
}
