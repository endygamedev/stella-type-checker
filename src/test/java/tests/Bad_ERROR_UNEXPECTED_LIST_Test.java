// NOTE: Auto-generated tests

package tests;

import org.junit.jupiter.api.Test;
import utils.Runner;

import dev.ebronnikov.typechecker.errors.ErrorType;

class Bad_ERROR_UNEXPECTED_LIST_Test {
    @Test
    public void test_simple() {
        Runner.runBadTest(ErrorType.ERROR_UNEXPECTED_LIST, "simple");
    }

    @Test
    public void test_cons() {
        Runner.runBadTest(ErrorType.ERROR_UNEXPECTED_LIST, "cons");
    }

    @Test
    public void test_infer_match() {
        Runner.runBadTest(ErrorType.ERROR_UNEXPECTED_LIST, "infer_match");
    }

}
