// NOTE: Auto-generated tests

package tests;

import org.junit.jupiter.api.Test;
import utils.Runner;

import dev.ebronnikov.typechecker.errors.ErrorType;

class Bad_ERROR_INCORRECT_ARITY_OF_MAIN_Test {
    @Test
    public void test_main_with_zero_param() {
        Runner.runBadTest(ErrorType.ERROR_INCORRECT_ARITY_OF_MAIN, "main_with_zero_param");
    }

    @Test
    public void test_main_with_two_params() {
        Runner.runBadTest(ErrorType.ERROR_INCORRECT_ARITY_OF_MAIN, "main_with_two_params");
    }

}
