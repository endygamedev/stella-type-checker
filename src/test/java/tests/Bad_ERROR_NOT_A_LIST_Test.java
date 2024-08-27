// NOTE: Auto-generated tests

package tests;

import org.junit.jupiter.api.Test;
import utils.Runner;

import dev.ebronnikov.typechecker.errors.ErrorType;

class Bad_ERROR_NOT_A_LIST_Test {
    @Test
    public void test_head() {
        Runner.runBadTest(ErrorType.ERROR_NOT_A_LIST, "head");
    }

    @Test
    public void test_is_empty() {
        Runner.runBadTest(ErrorType.ERROR_NOT_A_LIST, "is_empty");
    }

    @Test
    public void test_tail() {
        Runner.runBadTest(ErrorType.ERROR_NOT_A_LIST, "tail");
    }

}
