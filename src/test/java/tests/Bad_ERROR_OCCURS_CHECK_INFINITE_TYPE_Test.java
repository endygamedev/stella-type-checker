// NOTE: Auto-generated tests

package tests;

import org.junit.jupiter.api.Test;
import utils.Runner;

import dev.ebronnikov.typechecker.errors.ErrorType;

class Bad_ERROR_OCCURS_CHECK_INFINITE_TYPE_Test {
    @Test
    public void test_infinite_function_fix_type() {
        Runner.runBadTest(ErrorType.ERROR_OCCURS_CHECK_INFINITE_TYPE, "infinite_function_fix_type");
    }

    @Test
    public void test_infinite_function_type() {
        Runner.runBadTest(ErrorType.ERROR_OCCURS_CHECK_INFINITE_TYPE, "infinite_function_type");
    }

}
