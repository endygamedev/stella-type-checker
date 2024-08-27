// NOTE: Auto-generated tests

package tests;

import org.junit.jupiter.api.Test;
import utils.Runner;

import dev.ebronnikov.typechecker.errors.ErrorType;

class Bad_ERROR_AMBIGUOUS_SUM_TYPE_Test {
    @Test
    public void test_simple_inr() {
        Runner.runBadTest(ErrorType.ERROR_AMBIGUOUS_SUM_TYPE, "simple_inr");
    }

    @Test
    public void test_simple_inl() {
        Runner.runBadTest(ErrorType.ERROR_AMBIGUOUS_SUM_TYPE, "simple_inl");
    }

}
