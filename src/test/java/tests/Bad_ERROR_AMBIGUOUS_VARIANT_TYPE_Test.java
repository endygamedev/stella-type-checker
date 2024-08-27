// NOTE: Auto-generated tests

package tests;

import org.junit.jupiter.api.Test;
import utils.Runner;

import dev.ebronnikov.typechecker.errors.ErrorType;

class Bad_ERROR_AMBIGUOUS_VARIANT_TYPE_Test {
    @Test
    public void test_ambiguous_variant_type_1() {
        Runner.runBadTest(ErrorType.ERROR_AMBIGUOUS_VARIANT_TYPE, "ambiguous-variant-type-1");
    }

    @Test
    public void test_simple() {
        Runner.runBadTest(ErrorType.ERROR_AMBIGUOUS_VARIANT_TYPE, "simple");
    }

    @Test
    public void test_ambiguous_variant_type_3() {
        Runner.runBadTest(ErrorType.ERROR_AMBIGUOUS_VARIANT_TYPE, "ambiguous-variant-type-3");
    }

    @Test
    public void test_ambiguous_variant_type_2() {
        Runner.runBadTest(ErrorType.ERROR_AMBIGUOUS_VARIANT_TYPE, "ambiguous-variant-type-2");
    }

}
