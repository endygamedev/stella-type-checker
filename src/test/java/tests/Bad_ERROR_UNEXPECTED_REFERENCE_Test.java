// NOTE: Auto-generated tests

package tests;

import org.junit.jupiter.api.Test;
import utils.Runner;

import dev.ebronnikov.typechecker.errors.ErrorType;

class Bad_ERROR_UNEXPECTED_REFERENCE_Test {
    @Test
    public void test_return_ref_from_non_reference_function() {
        Runner.runBadTest(ErrorType.ERROR_UNEXPECTED_REFERENCE, "return_ref_from_non_reference_function");
    }

    @Test
    public void test_return_ref_from_non_reference_function_complex() {
        Runner.runBadTest(ErrorType.ERROR_UNEXPECTED_REFERENCE, "return_ref_from_non_reference_function_complex");
    }

    @Test
    public void test_return_ref_from_non_reference_function_call_func() {
        Runner.runBadTest(ErrorType.ERROR_UNEXPECTED_REFERENCE, "return_ref_from_non_reference_function_call_func");
    }

}
