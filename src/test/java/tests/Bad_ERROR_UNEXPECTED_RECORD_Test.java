// NOTE: Auto-generated tests

package tests;

import org.junit.jupiter.api.Test;
import utils.Runner;

import dev.ebronnikov.typechecker.errors.ErrorType;

class Bad_ERROR_UNEXPECTED_RECORD_Test {
    @Test
    public void test_simple_unexpected_record() {
        Runner.runBadTest(ErrorType.ERROR_UNEXPECTED_RECORD, "simple_unexpected_record");
    }

    @Test
    public void test_succ_record() {
        Runner.runBadTest(ErrorType.ERROR_UNEXPECTED_RECORD, "succ_record");
    }

    @Test
    public void test_application_record() {
        Runner.runBadTest(ErrorType.ERROR_UNEXPECTED_RECORD, "application_record");
    }

}
