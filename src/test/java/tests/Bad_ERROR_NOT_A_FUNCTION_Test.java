// NOTE: Auto-generated tests

package tests;

import org.junit.jupiter.api.Test;
import utils.Runner;

import dev.ebronnikov.typechecker.errors.ErrorType;

class Bad_ERROR_NOT_A_FUNCTION_Test {
    @Test
    public void test_apply_tuple() {
        Runner.runBadTest(ErrorType.ERROR_NOT_A_FUNCTION, "apply_tuple");
    }

    @Test
    public void test_before_arg_type_check() {
        Runner.runBadTest(ErrorType.ERROR_NOT_A_FUNCTION, "before_arg_type_check");
    }

    @Test
    public void test_infer_fix_multiple_param() {
        Runner.runBadTest(ErrorType.ERROR_NOT_A_FUNCTION, "infer_fix_multiple_param");
    }

    @Test
    public void test_apply_record() {
        Runner.runBadTest(ErrorType.ERROR_NOT_A_FUNCTION, "apply_record");
    }

    @Test
    public void test_infer_fix() {
        Runner.runBadTest(ErrorType.ERROR_NOT_A_FUNCTION, "infer_fix");
    }

    @Test
    public void test_infer_fix_zero_param() {
        Runner.runBadTest(ErrorType.ERROR_NOT_A_FUNCTION, "infer_fix_zero_param");
    }

    @Test
    public void test_simple_no_function() {
        Runner.runBadTest(ErrorType.ERROR_NOT_A_FUNCTION, "simple_no_function");
    }

}
