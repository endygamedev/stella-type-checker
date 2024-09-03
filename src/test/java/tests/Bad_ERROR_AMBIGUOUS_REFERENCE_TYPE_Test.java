// NOTE: Auto-generated tests

package tests;

import org.junit.jupiter.api.Test;
import utils.Runner;

import dev.ebronnikov.typechecker.errors.ErrorType;

class Bad_ERROR_AMBIGUOUS_REFERENCE_TYPE_Test {
    @Test
    public void test_deref_memory_from_lambda() {
        Runner.runBadTest(ErrorType.ERROR_AMBIGUOUS_REFERENCE_TYPE, "deref_memory_from_lambda");
    }

}
