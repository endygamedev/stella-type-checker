// NOTE: Auto-generated tests

package tests;

import org.junit.jupiter.api.Test;
import utils.Runner;

import dev.ebronnikov.typechecker.errors.ErrorType;

class Bad_ERROR_UNEXPECTED_TYPE_FOR_EXPRESSION_Test {
    @Test
    public void test_tuple_dot() {
        Runner.runBadTest(ErrorType.ERROR_UNEXPECTED_TYPE_FOR_EXPRESSION, "tuple_dot");
    }

    @Test
    public void test_let_list() {
        Runner.runBadTest(ErrorType.ERROR_UNEXPECTED_TYPE_FOR_EXPRESSION, "let_list");
    }

    @Test
    public void test_simple_ascription2() {
        Runner.runBadTest(ErrorType.ERROR_UNEXPECTED_TYPE_FOR_EXPRESSION, "simple_ascription2");
    }

    @Test
    public void test_return_lambda_with_wrong_return() {
        Runner.runBadTest(ErrorType.ERROR_UNEXPECTED_TYPE_FOR_EXPRESSION, "return_lambda_with_wrong_return");
    }

    @Test
    public void test_unexpected_tail() {
        Runner.runBadTest(ErrorType.ERROR_UNEXPECTED_TYPE_FOR_EXPRESSION, "unexpected_tail");
    }

    @Test
    public void test_simple_let() {
        Runner.runBadTest(ErrorType.ERROR_UNEXPECTED_TYPE_FOR_EXPRESSION, "simple_let");
    }

    @Test
    public void test_unexpected_zero_param() {
        Runner.runBadTest(ErrorType.ERROR_UNEXPECTED_TYPE_FOR_EXPRESSION, "unexpected_zero_param");
    }

    @Test
    public void test_unexpected_label_type() {
        Runner.runBadTest(ErrorType.ERROR_UNEXPECTED_TYPE_FOR_EXPRESSION, "unexpected_label_type");
    }

    @Test
    public void test_no_nat_rec() {
        Runner.runBadTest(ErrorType.ERROR_UNEXPECTED_TYPE_FOR_EXPRESSION, "no_nat_rec");
    }

    @Test
    public void test_function_return() {
        Runner.runBadTest(ErrorType.ERROR_UNEXPECTED_TYPE_FOR_EXPRESSION, "function_return");
    }

    @Test
    public void test_unexpected_unit() {
        Runner.runBadTest(ErrorType.ERROR_UNEXPECTED_TYPE_FOR_EXPRESSION, "unexpected_unit");
    }

    @Test
    public void test_succ_true() {
        Runner.runBadTest(ErrorType.ERROR_UNEXPECTED_TYPE_FOR_EXPRESSION, "succ_true");
    }

    @Test
    public void test_unexpected_s_rec() {
        Runner.runBadTest(ErrorType.ERROR_UNEXPECTED_TYPE_FOR_EXPRESSION, "unexpected_s_rec");
    }

    @Test
    public void test_is_zero_bool() {
        Runner.runBadTest(ErrorType.ERROR_UNEXPECTED_TYPE_FOR_EXPRESSION, "is_zero_bool");
    }

    @Test
    public void test_int_literal() {
        Runner.runBadTest(ErrorType.ERROR_UNEXPECTED_TYPE_FOR_EXPRESSION, "int_literal");
    }

    @Test
    public void test_if_nat() {
        Runner.runBadTest(ErrorType.ERROR_UNEXPECTED_TYPE_FOR_EXPRESSION, "if_nat");
    }

    @Test
    public void test_simple_ascription() {
        Runner.runBadTest(ErrorType.ERROR_UNEXPECTED_TYPE_FOR_EXPRESSION, "simple_ascription");
    }

    @Test
    public void test_record_dot() {
        Runner.runBadTest(ErrorType.ERROR_UNEXPECTED_TYPE_FOR_EXPRESSION, "record_dot");
    }

    @Test
    public void test_fix_from_arg() {
        Runner.runBadTest(ErrorType.ERROR_UNEXPECTED_TYPE_FOR_EXPRESSION, "fix_from_arg");
    }

    @Test
    public void test_unexpected_multiparam() {
        Runner.runBadTest(ErrorType.ERROR_UNEXPECTED_TYPE_FOR_EXPRESSION, "unexpected_multiparam");
    }

    @Test
    public void test_different_branches() {
        Runner.runBadTest(ErrorType.ERROR_UNEXPECTED_TYPE_FOR_EXPRESSION, "different_branches");
    }

    @Test
    public void test_nested_function_params_shadowing() {
        Runner.runBadTest(ErrorType.ERROR_UNEXPECTED_TYPE_FOR_EXPRESSION, "nested_function_params_shadowing");
    }

    @Test
    public void test_unexpected_application() {
        Runner.runBadTest(ErrorType.ERROR_UNEXPECTED_TYPE_FOR_EXPRESSION, "unexpected_application");
    }

    @Test
    public void test_fixpoint() {
        Runner.runBadTest(ErrorType.ERROR_UNEXPECTED_TYPE_FOR_EXPRESSION, "fixpoint");
    }

    @Test
    public void test_false_return() {
        Runner.runBadTest(ErrorType.ERROR_UNEXPECTED_TYPE_FOR_EXPRESSION, "false_return");
    }

    @Test
    public void test_unexpected_iszero() {
        Runner.runBadTest(ErrorType.ERROR_UNEXPECTED_TYPE_FOR_EXPRESSION, "unexpected_iszero");
    }

    @Test
    public void test_unexpected_isempty() {
        Runner.runBadTest(ErrorType.ERROR_UNEXPECTED_TYPE_FOR_EXPRESSION, "unexpected_isempty");
    }

}
