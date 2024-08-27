// NOTE: Auto-generated tests

package tests;

import org.junit.jupiter.api.Test;
import utils.Runner;

import dev.ebronnikov.typechecker.errors.ErrorType;

class Bad_ERROR_NOT_A_RECORD_Test {
    @Test
    public void test_bool_is_not_a_record() {
        Runner.runBadTest(ErrorType.ERROR_NOT_A_RECORD, "bool_is_not_a_record");
    }

    @Test
    public void test_nat_is_not_a_record() {
        Runner.runBadTest(ErrorType.ERROR_NOT_A_RECORD, "nat_is_not_a_record");
    }

    @Test
    public void test_if_is_not_a_record() {
        Runner.runBadTest(ErrorType.ERROR_NOT_A_RECORD, "if_is_not_a_record");
    }

    @Test
    public void test_unit_is_not_a_record() {
        Runner.runBadTest(ErrorType.ERROR_NOT_A_RECORD, "unit_is_not_a_record");
    }

    @Test
    public void test_func_is_not_a_record() {
        Runner.runBadTest(ErrorType.ERROR_NOT_A_RECORD, "func_is_not_a_record");
    }

}
