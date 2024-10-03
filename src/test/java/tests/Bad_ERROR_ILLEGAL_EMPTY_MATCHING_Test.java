// NOTE: Auto-generated tests

package tests;

import org.junit.jupiter.api.Test;
import utils.Runner;

import dev.ebronnikov.typechecker.errors.ErrorType;

class Bad_ERROR_ILLEGAL_EMPTY_MATCHING_Test {
    @Test
    public void test_empty_match() {
        Runner.runBadTest(ErrorType.ERROR_ILLEGAL_EMPTY_MATCHING, "empty_match");
    }

}
