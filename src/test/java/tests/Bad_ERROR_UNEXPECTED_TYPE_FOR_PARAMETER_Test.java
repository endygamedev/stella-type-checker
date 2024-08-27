// NOTE: Auto-generated tests

package tests;

import org.junit.jupiter.api.Test;
import utils.Runner;

import dev.ebronnikov.typechecker.errors.ErrorType;

class Bad_ERROR_UNEXPECTED_TYPE_FOR_PARAMETER_Test {
    @Test
    public void test_return_lambda_with_wrong_second_argument() {
        Runner.runBadTest(ErrorType.ERROR_UNEXPECTED_TYPE_FOR_PARAMETER, "return_lambda_with_wrong_second_argument");
    }

    @Test
    public void test_return_lambda_with_wrong_argument() {
        Runner.runBadTest(ErrorType.ERROR_UNEXPECTED_TYPE_FOR_PARAMETER, "return_lambda_with_wrong_argument");
    }

    @Test
    public void test_recursion() {
        Runner.runBadTest(ErrorType.ERROR_UNEXPECTED_TYPE_FOR_PARAMETER, "recursion");
    }

}
