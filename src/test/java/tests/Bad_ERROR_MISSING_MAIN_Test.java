// NOTE: Auto-generated tests

package tests;

import org.junit.jupiter.api.Test;
import utils.Runner;

import dev.ebronnikov.typechecker.errors.ErrorType;

class Bad_ERROR_MISSING_MAIN_Test {
    @Test
    public void test_no_main() {
        Runner.runBadTest(ErrorType.ERROR_MISSING_MAIN, "no_main");
    }

}
