// NOTE: Auto-generated tests

package tests;

import org.junit.jupiter.api.Test;
import utils.Runner;

import dev.ebronnikov.typechecker.errors.ErrorType;

class Bad_ERROR_UNEXPECTED_INJECTION_Test {
    @Test
    public void test_simple_unexpected_inr() {
        Runner.runBadTest(ErrorType.ERROR_UNEXPECTED_INJECTION, "simple_unexpected_inr");
    }

    @Test
    public void test_simple_unexpected_inl() {
        Runner.runBadTest(ErrorType.ERROR_UNEXPECTED_INJECTION, "simple_unexpected_inl");
    }

}
