// NOTE: Auto-generated tests

package tests;

import org.junit.jupiter.api.Test;
import utils.Runner;

import dev.ebronnikov.typechecker.errors.ErrorType;

class Bad_ERROR_MISSING_RECORD_FIELDS_Test {
    @Test
    public void test_record_in_record() {
        Runner.runBadTest(ErrorType.ERROR_MISSING_RECORD_FIELDS, "record_in_record");
    }

    @Test
    public void test_record_in_abstraction() {
        Runner.runBadTest(ErrorType.ERROR_MISSING_RECORD_FIELDS, "record_in_abstraction");
    }

    @Test
    public void test_subtyping_record() {
        Runner.runBadTest(ErrorType.ERROR_MISSING_RECORD_FIELDS, "subtyping_record");
    }

    @Test
    public void test_simple_missing_fields() {
        Runner.runBadTest(ErrorType.ERROR_MISSING_RECORD_FIELDS, "simple_missing_fields");
    }

    @Test
    public void test_call_function_with_missing_fields() {
        Runner.runBadTest(ErrorType.ERROR_MISSING_RECORD_FIELDS, "call_function_with_missing_fields");
    }

}
