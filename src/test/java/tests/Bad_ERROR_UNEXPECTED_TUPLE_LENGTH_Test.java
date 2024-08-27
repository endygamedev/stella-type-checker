// NOTE: Auto-generated tests

package tests;

import org.junit.jupiter.api.Test;
import utils.Runner;

import dev.ebronnikov.typechecker.errors.ErrorType;

class Bad_ERROR_UNEXPECTED_TUPLE_LENGTH_Test {
    @Test
    public void test_subtyping_tuple() {
        Runner.runBadTest(ErrorType.ERROR_UNEXPECTED_TUPLE_LENGTH, "subtyping_tuple");
    }

    @Test
    public void test_return_tuple_literal() {
        Runner.runBadTest(ErrorType.ERROR_UNEXPECTED_TUPLE_LENGTH, "return_tuple_literal");
    }

    @Test
    public void test_functional_type() {
        Runner.runBadTest(ErrorType.ERROR_UNEXPECTED_TUPLE_LENGTH, "functional_type");
    }

    @Test
    public void test_subtyping_tuple2() {
        Runner.runBadTest(ErrorType.ERROR_UNEXPECTED_TUPLE_LENGTH, "subtyping_tuple2");
    }

}
