// NOTE: Auto-generated tests

package tests;

import org.junit.jupiter.api.Test;
import utils.Runner;

import dev.ebronnikov.typechecker.errors.ErrorType;

class Bad_ERROR_UNEXPECTED_PATTERN_FOR_TYPE_Test {
    @Test
    public void test_variant_unexpected_pattern() {
        Runner.runBadTest(ErrorType.ERROR_UNEXPECTED_PATTERN_FOR_TYPE, "variant_unexpected_pattern");
    }

    @Test
    public void test_unexpected_pattern() {
        Runner.runBadTest(ErrorType.ERROR_UNEXPECTED_PATTERN_FOR_TYPE, "unexpected_pattern");
    }

}
