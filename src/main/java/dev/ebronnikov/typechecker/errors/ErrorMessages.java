package dev.ebronnikov.typechecker.errors;

import java.util.Map;

public class ErrorMessages {
    public final Map<ErrorType, String> messages = Map.of(
            ErrorType.ERROR_MISSING_MAIN, "missing main function",
            ErrorType.ERROR_UNDEFINED_VARIABLE, "undefined variable %s",
            ErrorType.ERROR_UNEXPECTED_TYPE_FOR_EXPRESSION, "expected type %s but got %s for expression %s",
            ErrorType.ERROR_NOT_A_FUNCTION, "expected a function type but got %s for expression %s",
            ErrorType.ERROR_NOT_A_TUPLE, "expected an expression of tuple type but got expression of type %s in %s",
            ErrorType.ERROR_NOT_A_RECORD, "expected a record type but got %s for expression %s",
            ErrorType.ERROR_NOT_A_LIST, "expected a list type but got %s for expression %s"
    );
}
