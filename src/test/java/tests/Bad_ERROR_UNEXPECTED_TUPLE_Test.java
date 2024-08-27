// NOTE: Auto-generated tests

package tests;

import org.junit.jupiter.api.Test;
import utils.Runner;

import dev.ebronnikov.typechecker.errors.ErrorType;

class Bad_ERROR_UNEXPECTED_TUPLE_Test {
    @Test
    public void test_return_tuple_from_function() {
        Runner.runBadTest(ErrorType.ERROR_UNEXPECTED_TUPLE, "return_tuple_from_function");
    }

    @Test
    public void test_succ_record() {
        Runner.runBadTest(ErrorType.ERROR_UNEXPECTED_TUPLE, "succ_record");
    }

    @Test
    public void test_application_record() {
        Runner.runBadTest(ErrorType.ERROR_UNEXPECTED_TUPLE, "application_record");
    }

}
