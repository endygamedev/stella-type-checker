// NOTE: Auto-generated tests

package tests;

import org.junit.jupiter.api.Test;
import utils.Runner;

import dev.ebronnikov.typechecker.errors.ErrorType;

class Bad_ERROR_NOT_A_TUPLE_Test {
    @Test
    public void test_simple_not_a_tuple() {
        Runner.runBadTest(ErrorType.ERROR_NOT_A_TUPLE, "simple_not_a_tuple");
    }

}
