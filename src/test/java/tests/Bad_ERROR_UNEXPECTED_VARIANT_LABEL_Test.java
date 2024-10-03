// NOTE: Auto-generated tests

package tests;

import org.junit.jupiter.api.Test;
import utils.Runner;

import dev.ebronnikov.typechecker.errors.ErrorType;

class Bad_ERROR_UNEXPECTED_VARIANT_LABEL_Test {
    @Test
    public void test_unexpected_nullary_label() {
        Runner.runBadTest(ErrorType.ERROR_UNEXPECTED_VARIANT_LABEL, "unexpected_nullary_label");
    }

    @Test
    public void test_simple_unexpected_label() {
        Runner.runBadTest(ErrorType.ERROR_UNEXPECTED_VARIANT_LABEL, "simple_unexpected_label");
    }

}
