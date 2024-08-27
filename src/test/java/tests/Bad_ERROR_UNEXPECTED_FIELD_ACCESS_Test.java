// NOTE: Auto-generated tests

package tests;

import org.junit.jupiter.api.Test;
import utils.Runner;

import dev.ebronnikov.typechecker.errors.ErrorType;

class Bad_ERROR_UNEXPECTED_FIELD_ACCESS_Test {
    @Test
    public void test_simple_field_access() {
        Runner.runBadTest(ErrorType.ERROR_UNEXPECTED_FIELD_ACCESS, "simple_field_access");
    }

}
