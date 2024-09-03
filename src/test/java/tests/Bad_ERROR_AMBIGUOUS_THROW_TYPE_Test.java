// NOTE: Auto-generated tests

package tests;

import org.junit.jupiter.api.Test;
import utils.Runner;

import dev.ebronnikov.typechecker.errors.ErrorType;

class Bad_ERROR_AMBIGUOUS_THROW_TYPE_Test {
    @Test
    public void test_throw_or_function() {
        Runner.runBadTest(ErrorType.ERROR_AMBIGUOUS_THROW_TYPE, "throw_or_function");
    }

    @Test
    public void test_throw_inside_lambda() {
        Runner.runBadTest(ErrorType.ERROR_AMBIGUOUS_THROW_TYPE, "throw_inside_lambda");
    }

}
