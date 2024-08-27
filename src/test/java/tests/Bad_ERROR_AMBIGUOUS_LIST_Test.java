// NOTE: Auto-generated tests

package tests;

import org.junit.jupiter.api.Test;
import utils.Runner;

import dev.ebronnikov.typechecker.errors.ErrorType;

class Bad_ERROR_AMBIGUOUS_LIST_Test {
    @Test
    public void test_head() {
        Runner.runBadTest(ErrorType.ERROR_AMBIGUOUS_LIST, "head");
    }

    @Test
    public void test_let() {
        Runner.runBadTest(ErrorType.ERROR_AMBIGUOUS_LIST, "let");
    }

    @Test
    public void test_infer_match() {
        Runner.runBadTest(ErrorType.ERROR_AMBIGUOUS_LIST, "infer_match");
    }

}
