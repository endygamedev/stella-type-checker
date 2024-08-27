// NOTE: Auto-generated tests

package tests;

import org.junit.jupiter.api.Test;
import utils.Runner;

import dev.ebronnikov.typechecker.errors.ErrorType;

class Bad_ERROR_UNEXPECTED_RECORD_FIELDS_Test {
    @Test
    public void test_return_record_with_missing_fields() {
        Runner.runBadTest(ErrorType.ERROR_UNEXPECTED_RECORD_FIELDS, "return_record_with_missing_fields");
    }

}
