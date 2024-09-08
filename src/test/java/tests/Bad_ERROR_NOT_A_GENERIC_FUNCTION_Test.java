// NOTE: Auto-generated tests

package tests;

import org.junit.jupiter.api.Test;
import utils.Runner;

import dev.ebronnikov.typechecker.errors.ErrorType;

class Bad_ERROR_NOT_A_GENERIC_FUNCTION_Test {
    @Test
    public void test_const2_not_generic() {
        Runner.runBadTest(ErrorType.ERROR_NOT_A_GENERIC_FUNCTION, "const2_not_generic");
    }

}
