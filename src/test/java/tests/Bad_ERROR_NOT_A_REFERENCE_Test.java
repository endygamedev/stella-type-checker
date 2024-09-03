// NOTE: Auto-generated tests

package tests;

import org.junit.jupiter.api.Test;
import utils.Runner;

import dev.ebronnikov.typechecker.errors.ErrorType;

class Bad_ERROR_NOT_A_REFERENCE_Test {
    @Test
    public void test_assignment_to_non_ref_parameter() {
        Runner.runBadTest(ErrorType.ERROR_NOT_A_REFERENCE, "assignment_to_non_ref_parameter");
    }

    @Test
    public void test_deref_parameter() {
        Runner.runBadTest(ErrorType.ERROR_NOT_A_REFERENCE, "deref_parameter");
    }

}
