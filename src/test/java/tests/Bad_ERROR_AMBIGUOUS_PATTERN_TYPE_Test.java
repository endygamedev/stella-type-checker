// NOTE: Auto-generated tests

package tests;

import org.junit.jupiter.api.Test;
import utils.Runner;

import dev.ebronnikov.typechecker.errors.ErrorType;

class Bad_ERROR_AMBIGUOUS_PATTERN_TYPE_Test {
    @Test
    public void test_simple_letrec() {
        Runner.runBadTest(ErrorType.ERROR_AMBIGUOUS_PATTERN_TYPE, "simple_letrec");
    }

}
