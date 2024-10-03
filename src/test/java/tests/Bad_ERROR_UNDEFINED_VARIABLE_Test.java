// NOTE: Auto-generated tests

package tests;

import org.junit.jupiter.api.Test;
import utils.Runner;

import dev.ebronnikov.typechecker.errors.ErrorType;

class Bad_ERROR_UNDEFINED_VARIABLE_Test {
    @Test
    public void test_simple_undefined_var() {
        Runner.runBadTest(ErrorType.ERROR_UNDEFINED_VARIABLE, "simple_undefined_var");
    }

    @Test
    public void test_undefined_var_in_other_fun() {
        Runner.runBadTest(ErrorType.ERROR_UNDEFINED_VARIABLE, "undefined_var_in_other_fun");
    }

    @Test
    public void test_in_let() {
        Runner.runBadTest(ErrorType.ERROR_UNDEFINED_VARIABLE, "in_let");
    }

}
