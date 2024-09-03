// NOTE: Auto-generated tests

package tests;

import org.junit.jupiter.api.Test;
import utils.Runner;

import dev.ebronnikov.typechecker.errors.ErrorType;

class Bad_ERROR_AMBIGUOUS_PANIC_TYPE_Test {
    @Test
    public void test_panic_inside_lambda() {
        Runner.runBadTest(ErrorType.ERROR_AMBIGUOUS_PANIC_TYPE, "panic_inside_lambda");
    }

    @Test
    public void test_panic_or_function() {
        Runner.runBadTest(ErrorType.ERROR_AMBIGUOUS_PANIC_TYPE, "panic_or_function");
    }

}
