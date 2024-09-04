// NOTE: Auto-generated tests

package tests;

import org.junit.jupiter.api.Test;
import utils.Runner;

import dev.ebronnikov.typechecker.errors.ErrorType;

class Bad_ERROR_UNEXPECTED_SUBTYPE_Test {
    @Test
    public void test_record() {
        Runner.runBadTest(ErrorType.ERROR_UNEXPECTED_SUBTYPE, "record");
    }

    @Test
    public void test_error() {
        Runner.runBadTest(ErrorType.ERROR_UNEXPECTED_SUBTYPE, "error");
    }

    @Test
    public void test_ref() {
        Runner.runBadTest(ErrorType.ERROR_UNEXPECTED_SUBTYPE, "ref");
    }

    @Test
    public void test_ref2() {
        Runner.runBadTest(ErrorType.ERROR_UNEXPECTED_SUBTYPE, "ref2");
    }

    @Test
    public void test_tuple() {
        Runner.runBadTest(ErrorType.ERROR_UNEXPECTED_SUBTYPE, "tuple");
    }

    @Test
    public void test_tuple2() {
        Runner.runBadTest(ErrorType.ERROR_UNEXPECTED_SUBTYPE, "tuple2");
    }

    @Test
    public void test_variant() {
        Runner.runBadTest(ErrorType.ERROR_UNEXPECTED_SUBTYPE, "variant");
    }

    @Test
    public void test_func() {
        Runner.runBadTest(ErrorType.ERROR_UNEXPECTED_SUBTYPE, "func");
    }

    @Test
    public void test_bot() {
        Runner.runBadTest(ErrorType.ERROR_UNEXPECTED_SUBTYPE, "bot");
    }

    @Test
    public void test_list() {
        Runner.runBadTest(ErrorType.ERROR_UNEXPECTED_SUBTYPE, "list");
    }

    @Test
    public void test_sum() {
        Runner.runBadTest(ErrorType.ERROR_UNEXPECTED_SUBTYPE, "sum");
    }

    @Test
    public void test_func2() {
        Runner.runBadTest(ErrorType.ERROR_UNEXPECTED_SUBTYPE, "func2");
    }

}
