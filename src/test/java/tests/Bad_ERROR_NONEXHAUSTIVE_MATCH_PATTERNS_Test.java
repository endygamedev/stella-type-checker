// NOTE: Auto-generated tests

package tests;

import org.junit.jupiter.api.Test;
import utils.Runner;

import dev.ebronnikov.typechecker.errors.ErrorType;

class Bad_ERROR_NONEXHAUSTIVE_MATCH_PATTERNS_Test {
    @Test
    public void test_ne_nat_1() {
        Runner.runBadTest(ErrorType.ERROR_NONEXHAUSTIVE_MATCH_PATTERNS, "ne_nat_1");
    }

    @Test
    public void test_ne_list() {
        Runner.runBadTest(ErrorType.ERROR_NONEXHAUSTIVE_MATCH_PATTERNS, "ne_list");
    }

    @Test
    public void test_ne_sum_nat() {
        Runner.runBadTest(ErrorType.ERROR_NONEXHAUSTIVE_MATCH_PATTERNS, "ne_sum_nat");
    }

    @Test
    public void test_ne_tuple() {
        Runner.runBadTest(ErrorType.ERROR_NONEXHAUSTIVE_MATCH_PATTERNS, "ne_tuple");
    }

    @Test
    public void test_ne_empty_list() {
        Runner.runBadTest(ErrorType.ERROR_NONEXHAUSTIVE_MATCH_PATTERNS, "ne_empty_list");
    }

    @Test
    public void test_ne_sum_reconstruct() {
        Runner.runBadTest(ErrorType.ERROR_NONEXHAUSTIVE_MATCH_PATTERNS, "ne_sum_reconstruct");
    }

    @Test
    public void test_ne_record() {
        Runner.runBadTest(ErrorType.ERROR_NONEXHAUSTIVE_MATCH_PATTERNS, "ne_record");
    }

    @Test
    public void test_ne_nat_3() {
        Runner.runBadTest(ErrorType.ERROR_NONEXHAUSTIVE_MATCH_PATTERNS, "ne_nat_3");
    }

    @Test
    public void test_ne_nat_4() {
        Runner.runBadTest(ErrorType.ERROR_NONEXHAUSTIVE_MATCH_PATTERNS, "ne_nat_4");
    }

    @Test
    public void test_ne_nat_2() {
        Runner.runBadTest(ErrorType.ERROR_NONEXHAUSTIVE_MATCH_PATTERNS, "ne_nat_2");
    }

    @Test
    public void test_nonexhaustive_match_reconstruct() {
        Runner.runBadTest(ErrorType.ERROR_NONEXHAUSTIVE_MATCH_PATTERNS, "nonexhaustive_match_reconstruct");
    }

    @Test
    public void test_nonexhaustive_match() {
        Runner.runBadTest(ErrorType.ERROR_NONEXHAUSTIVE_MATCH_PATTERNS, "nonexhaustive_match");
    }

    @Test
    public void test_ne_sum() {
        Runner.runBadTest(ErrorType.ERROR_NONEXHAUSTIVE_MATCH_PATTERNS, "ne_sum");
    }

    @Test
    public void test_ne_bool() {
        Runner.runBadTest(ErrorType.ERROR_NONEXHAUSTIVE_MATCH_PATTERNS, "ne_bool");
    }

    @Test
    public void test_nonexhaustive_variant() {
        Runner.runBadTest(ErrorType.ERROR_NONEXHAUSTIVE_MATCH_PATTERNS, "nonexhaustive_variant");
    }

}
