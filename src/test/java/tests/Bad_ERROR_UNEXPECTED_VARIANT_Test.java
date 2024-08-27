// NOTE: Auto-generated tests

package tests;

import org.junit.jupiter.api.Test;
import utils.Runner;

import dev.ebronnikov.typechecker.errors.ErrorType;

class Bad_ERROR_UNEXPECTED_VARIANT_Test {
    @Test
    public void test_unexpected_variant_3() {
        Runner.runBadTest(ErrorType.ERROR_UNEXPECTED_VARIANT, "unexpected-variant-3");
    }

    @Test
    public void test_simple_unexpected_variant() {
        Runner.runBadTest(ErrorType.ERROR_UNEXPECTED_VARIANT, "simple_unexpected_variant");
    }

}
