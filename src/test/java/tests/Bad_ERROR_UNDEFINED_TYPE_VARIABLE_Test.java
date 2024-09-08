// NOTE: Auto-generated tests

package tests;

import org.junit.jupiter.api.Test;
import utils.Runner;

import dev.ebronnikov.typechecker.errors.ErrorType;

class Bad_ERROR_UNDEFINED_TYPE_VARIABLE_Test {
    @Test
    public void test_const_undefined_var() {
        Runner.runBadTest(ErrorType.ERROR_UNDEFINED_TYPE_VARIABLE, "const_undefined_var");
    }

}
