// NOTE: Auto-generated tests

package tests;

import org.junit.jupiter.api.Test;
import utils.Runner;

import dev.ebronnikov.typechecker.errors.ErrorType;

class Bad_ERROR_UNEXPECTED_LAMBDA_Test {
    @Test
    public void test_simple_unexpected_lambda() {
        Runner.runBadTest(ErrorType.ERROR_UNEXPECTED_LAMBDA, "simple_unexpected_lambda");
    }

}
