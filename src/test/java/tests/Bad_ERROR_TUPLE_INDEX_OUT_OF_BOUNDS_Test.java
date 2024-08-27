// NOTE: Auto-generated tests

package tests;

import org.junit.jupiter.api.Test;
import utils.Runner;

import dev.ebronnikov.typechecker.errors.ErrorType;

class Bad_ERROR_TUPLE_INDEX_OUT_OF_BOUNDS_Test {
    @Test
    public void test_tuple_from_function() {
        Runner.runBadTest(ErrorType.ERROR_TUPLE_INDEX_OUT_OF_BOUNDS, "tuple_from_function");
    }

    @Test
    public void test_tuple_literal() {
        Runner.runBadTest(ErrorType.ERROR_TUPLE_INDEX_OUT_OF_BOUNDS, "tuple_literal");
    }

}
