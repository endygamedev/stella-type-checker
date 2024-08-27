// NOTE: Auto-generated tests

package tests;

import org.junit.jupiter.api.Test;
import utils.Runner;

import dev.ebronnikov.typechecker.errors.ErrorType;

class Bad_ERROR_UNEXPECTED_PATTERN_FOR_TYPE_Test {
    @Test
    public void test_variant_asc_2() {
        Runner.runBadTest(ErrorType.ERROR_UNEXPECTED_PATTERN_FOR_TYPE, "variant_asc_2");
    }

    @Test
    public void test_unexpected_pattern_reconstruct() {
        Runner.runBadTest(ErrorType.ERROR_UNEXPECTED_PATTERN_FOR_TYPE, "unexpected_pattern_reconstruct");
    }

    @Test
    public void test_variant_unexpected_pattern() {
        Runner.runBadTest(ErrorType.ERROR_UNEXPECTED_PATTERN_FOR_TYPE, "variant_unexpected_pattern");
    }

    @Test
    public void test_unexpected_pattern() {
        Runner.runBadTest(ErrorType.ERROR_UNEXPECTED_PATTERN_FOR_TYPE, "unexpected_pattern");
    }

    @Test
    public void test_unexpected_variant_pattern() {
        Runner.runBadTest(ErrorType.ERROR_UNEXPECTED_PATTERN_FOR_TYPE, "unexpected_variant_pattern");
    }

    @Test
    public void test_try_cast_as() {
        Runner.runBadTest(ErrorType.ERROR_UNEXPECTED_PATTERN_FOR_TYPE, "try_cast_as");
    }

    @Test
    public void test_try_catch_variant() {
        Runner.runBadTest(ErrorType.ERROR_UNEXPECTED_PATTERN_FOR_TYPE, "try_catch_variant");
    }

    @Test
    public void test_bad_sum_types_13() {
        Runner.runBadTest(ErrorType.ERROR_UNEXPECTED_PATTERN_FOR_TYPE, "bad-sum-types-13");
    }

    @Test
    public void test_try_catch_unepected_pattern() {
        Runner.runBadTest(ErrorType.ERROR_UNEXPECTED_PATTERN_FOR_TYPE, "try_catch_unepected_pattern");
    }

    @Test
    public void test_let_as() {
        Runner.runBadTest(ErrorType.ERROR_UNEXPECTED_PATTERN_FOR_TYPE, "let_as");
    }

    @Test
    public void test_tuple_size() {
        Runner.runBadTest(ErrorType.ERROR_UNEXPECTED_PATTERN_FOR_TYPE, "tuple_size");
    }

    @Test
    public void test_letrec_asc() {
        Runner.runBadTest(ErrorType.ERROR_UNEXPECTED_PATTERN_FOR_TYPE, "letrec_asc");
    }

    @Test
    public void test_variant_asc() {
        Runner.runBadTest(ErrorType.ERROR_UNEXPECTED_PATTERN_FOR_TYPE, "variant_asc");
    }

    @Test
    public void test_unexpected_variant_pattern_label() {
        Runner.runBadTest(ErrorType.ERROR_UNEXPECTED_PATTERN_FOR_TYPE, "unexpected_variant_pattern_label");
    }

}
