// NOTE: Auto-generated tests

package tests;

import org.junit.jupiter.api.Test;
import utils.Runner;

import dev.ebronnikov.typechecker.errors.ErrorType;

class Bad_ERROR_INCORRECT_NUMBER_OF_TYPE_ARGUMENTS_Test {
    @Test
    public void test_const_many_vars() {
        Runner.runBadTest(ErrorType.ERROR_INCORRECT_NUMBER_OF_TYPE_ARGUMENTS, "const_many_vars");
    }

    @Test
    public void test_const_few_vars() {
        Runner.runBadTest(ErrorType.ERROR_INCORRECT_NUMBER_OF_TYPE_ARGUMENTS, "const_few_vars");
    }

}
